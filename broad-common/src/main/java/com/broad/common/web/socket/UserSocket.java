package com.broad.common.web.socket;

import com.broad.common.constant.CacheConstants;
import com.broad.common.service.RedisService;
import com.broad.common.utils.SpringUtils;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * WebSocket用户端点
 * 处理用户WebSocket连接、消息收发和断开
 *
 * @author XingGao
 * @Date: 2022/11/22
 */
@Component
@Slf4j
@ServerEndpoint(value = "/ws/session/{userId}", configurator = WebSocketConfig.class)
public class UserSocket {

    /**
     * 消息处理器（通过WebSocketConfig注入）
     */
    private WebSocketMessageHandler messageHandler;

    /**
     * 会话管理器（通过WebSocketConfig注入）
     */
    private WebSocketSessionManager sessionManager;

    /**
     * WebSocket会话
     */
    private Session session;

    /**
     * 当前连接的用户ID
     */
    private String userId;

    /**
     * 连接建立成功调用的方法
     *
     * @param session WebSocket会话
     * @param userId 用户ID
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;

        log.info("WebSocket连接建立: userId={}", userId);

        try {
            // 从握手配置获取验证结果
            Boolean isValid = (Boolean) session.getUserProperties().get("isValid");

            // 验证是否通过握手认证
            if (isValid == null || !isValid) {
                log.warn("用户 {} WebSocket握手认证失败，拒绝连接", userId);
                session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "认证失败：无效的Token"));
                return;
            }

            // 获取用户信息
            String username = (String) session.getUserProperties().get("username");
            if (username == null) {
                username = "用户" + userId;
            }

            String userAgent = (String) session.getUserProperties().get("user-agent");
            String ipAddress = getRemoteAddress(session);

            log.info("WebSocket认证通过: userId={}, username={}", userId, username);

            // 初始化依赖
            initDependencies();

            // 如果会话管理器已初始化，则添加会话
            if (sessionManager != null) {
                // 创建用户会话
                UserSession userSession = UserSession.builder()
                        .userId(userId)
                        .username(username)
                        .session(session)
                        .connectTime(LocalDateTime.now())
                        .lastHeartbeat(LocalDateTime.now())
                        .status(UserSession.UserStatus.ONLINE)
                        .ipAddress(ipAddress != null ? ipAddress : "unknown")
                        .userAgent(userAgent != null ? userAgent : "unknown")
                        .build();

                // 添加用户会话
                sessionManager.addSession(userId, userSession);
                log.info("用户 {} 连接成功，当前在线用户数: {}", userId, sessionManager.getOnlineCount());
            } else {
                log.warn("会话管理器未初始化，仅建立连接");
            }

        } catch (Exception e) {
            log.error("用户 {} 连接失败: {}", userId, e.getMessage(), e);
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, "服务器错误"));
            } catch (IOException ioException) {
                log.error("关闭会话失败: {}", ioException.getMessage());
            }
        }
    }

    /**
     * 初始化依赖（WebSocket端点不是Spring单例，需要手动获取Bean）
     */
    private void initDependencies() {
        try {
            if (this.messageHandler == null) {
                this.messageHandler = SpringUtils.getBean(WebSocketMessageHandler.class);
            }
            if (this.sessionManager == null) {
                this.sessionManager = SpringUtils.getBean(WebSocketSessionManager.class);
            }
        } catch (Exception e) {
            log.error("初始化WebSocket依赖失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        try {
            if (userId != null && sessionManager != null) {
                sessionManager.removeSession(userId);
                messageHandler.handleDisconnect(userId);
                log.info("用户 {} 连接关闭，当前在线用户数: {}", userId, sessionManager.getOnlineCount());
            }
        } catch (Exception e) {
            log.error("处理用户 {} 连接关闭时发生错误: {}", userId, e.getMessage(), e);
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送的消息
     * @param session WebSocket会话
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        if (userId == null) {
            log.warn("收到消息时用户ID为空，消息: {}", message);
            return;
        }

        log.debug("收到用户 {} 的消息: {}", userId, message);
        messageHandler.handleMessage(session, message, userId);
    }

    /**
     * 发生错误时调用的方法
     *
     * @param session WebSocket会话
     * @param error 错误信息
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户 {} 的WebSocket连接发生错误: {}", userId, error.getMessage(), error);
        if (messageHandler != null) {
            messageHandler.handleError(userId, error);
        }
    }

    /**
     * 获取远程客户端IP地址
     *
     * @param session WebSocket会话
     * @return IP地址
     */
    private String getRemoteAddress(Session session) {
        try {
            Object remoteAddress = session.getUserProperties().get("remoteAddress");
            return remoteAddress != null ? remoteAddress.toString() : "unknown";
        } catch (Exception e) {
            return "unknown";
        }
    }

    /**
     * 发送错误消息并关闭连接
     *
     * @param errorMessage 错误信息
     */
    private void sendError(String errorMessage) {
        try {
            if (session != null && session.isOpen()) {
                session.getBasicRemote().sendText(errorMessage);
            }
        } catch (IOException e) {
            log.error("发送错误消息失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 自检查：检查未登录用户并推送重新登录通知（静态方法，兼容旧代码）
     *
     * @param userId 用户ID
     */
    public static void sendSelfCheck(String userId) {
        try {
            // 使用Redis防止短时间内重复推送
            RedisService redisService = SpringUtils.getBean(RedisService.class);
            String cacheKey = CacheConstants.SELF_CHECK + userId;

            if (redisService.getCacheObject(cacheKey) == null) {
                // 使用WebSocketMessageSender发送通知
                WebSocketMessageSender sender = SpringUtils.getBean(WebSocketMessageSender.class);
                sender.sendNotification(userId, "会话已过期，请重新登录");

                redisService.setCacheObject(cacheKey, System.currentTimeMillis(), 5L);
                log.info("向用户 {} 发送自检查通知", userId);
            }
        } catch (Exception e) {
            log.error("发送自检查通知失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 关闭WebSocket会话
     */
    private void closeSession() {
        try {
            if (session != null && session.isOpen()) {
                session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "认证失败"));
            }
        } catch (IOException e) {
            log.error("关闭会话失败: {}", e.getMessage(), e);
        }
    }
}
