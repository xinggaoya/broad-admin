package com.broad.common.web.socket;

import cn.hutool.core.util.StrUtil;
import com.broad.common.web.entity.ResultData;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * WebSocket消息处理器
 * 负责处理接收到的消息，进行路由和分发
 *
 * @author XingGao
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketMessageHandler {

    /**
     * 会话管理器
     */
    private final WebSocketSessionManager sessionManager;

    /**
     * 消息发送器
     */
    private final WebSocketMessageSender messageSender;

    /**
     * 事件发布器
     */
    private final ApplicationEventPublisher eventPublisher;

    /**
     * JSON对象映射器
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 处理收到的消息
     *
     * @param session WebSocket会话
     * @param message 消息文本
     * @param userId 用户ID
     */
    public void handleMessage(Session session, String message, String userId) {
        if (StrUtil.isBlank(message)) {
            log.warn("收到空消息，来自用户 {}", userId);
            return;
        }

        try {
            // 解析消息
            WebSocketMessage<?> webSocketMessage = parseMessage(message);
            if (webSocketMessage == null) {
                log.error("无法解析消息格式: {}", message);
                sendError(userId, "消息格式错误");
                return;
            }

            log.info("收到用户 {} 的消息: type={}, payload={}",
                    userId, webSocketMessage.getType(), webSocketMessage.getPayload());

            // 根据消息类型分发处理
            switch (webSocketMessage.getType()) {
                case PING:
                    handlePing(userId);
                    break;
                case PONG:
                    handlePong(userId);
                    break;
                case TEXT:
                    handleTextMessage(userId, webSocketMessage);
                    break;
                case ACK:
                    handleAck(webSocketMessage.getId());
                    break;
                default:
                    handleUnknownMessage(userId, webSocketMessage);
                    break;
            }

            // 发布消息事件
            publishMessageEvent(WebSocketEvent.MESSAGE_RECEIVE, userId, webSocketMessage);

        } catch (Exception e) {
            log.error("处理用户 {} 的消息失败: {}", userId, e.getMessage(), e);
            sendError(userId, "处理消息失败: " + e.getMessage());
        }
    }

    /**
     * 处理心跳请求
     *
     * @param userId 用户ID
     */
    private void handlePing(String userId) {
        log.debug("收到用户 {} 的心跳请求", userId);
        sessionManager.updateHeartbeat(userId);
        messageSender.sendToUser(WebSocketMessage.createPongMessage());
    }

    /**
     * 处理心跳响应
     *
     * @param userId 用户ID
     */
    private void handlePong(String userId) {
        log.debug("收到用户 {} 的心跳响应", userId);
        sessionManager.updateHeartbeat(userId);
    }

    /**
     * 处理文本消息
     *
     * @param userId 用户ID
     * @param message 消息
     */
    private void handleTextMessage(String userId, WebSocketMessage<?> message) {
        log.info("处理文本消息: 发送者={}, 接收者={}, 内容={}",
                userId, message.getReceiver(), message.getPayload());

        // 如果是广播消息
        if (StrUtil.isBlank(message.getReceiver())) {
            // 创建新消息，添加发送者信息
            WebSocketMessage<?> broadcastMsg = WebSocketMessage.builder()
                    .id(message.getId())
                    .type(message.getType())
                    .sender(userId)
                    .receiver(message.getReceiver())
                    .payload(message.getPayload())
                    .timestamp(message.getTimestamp())
                    .build();

            messageSender.broadcast(broadcastMsg);
            log.info("用户 {} 发送广播消息: {}", userId, message.getPayload());
        } else {
            // 点对点消息
            if (!messageSender.sendToUser(message)) {
                sendError(userId, "用户 " + message.getReceiver() + " 不在线");
            } else {
                // 发送成功确认
                messageSender.sendSuccess(userId, "消息已发送");
            }
        }
    }

    /**
     * 处理消息确认
     *
     * @param messageId 消息ID
     */
    private void handleAck(String messageId) {
        log.info("收到消息确认: messageId={}", messageId);
        // TODO: 更新消息状态为已接收，可以对接消息持久化服务
    }

    /**
     * 处理未知类型消息
     *
     * @param userId 用户ID
     * @param message 消息
     */
    private void handleUnknownMessage(String userId, WebSocketMessage<?> message) {
        log.warn("收到未知类型消息: userId={}, type={}", userId, message.getType());
        sendError(userId, "不支持的消息类型: " + message.getType());
    }

    /**
     * 解析消息
     *
     * @param message 消息文本
     * @return 解析后的消息对象
     * @throws Exception 解析异常
     */
    private WebSocketMessage<?> parseMessage(String message) throws Exception {
        return objectMapper.readValue(message, WebSocketMessage.class);
    }

    /**
     * 发布消息事件
     *
     * @param event 事件类型
     * @param userId 用户ID
     * @param message 消息
     */
    private void publishMessageEvent(WebSocketEvent event, String userId, WebSocketMessage<?> message) {
        try {
            WebSocketMessageEvent messageEvent = new WebSocketMessageEvent(this, event, userId, message);
            eventPublisher.publishEvent(messageEvent);
        } catch (Exception e) {
            log.error("发布消息事件失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 发送错误消息
     *
     * @param userId 用户ID
     * @param errorMessage 错误消息
     */
    private void sendError(String userId, String errorMessage) {
        messageSender.sendError(userId, errorMessage);
    }

    /**
     * 处理连接建立
     *
     * @param session WebSocket会话
     * @param userId 用户ID
     */
    public void handleConnect(Session session, String userId) {
        log.info("用户 {} 连接建立", userId);
        eventPublisher.publishEvent(new WebSocketConnectEvent(this, userId, session));
    }

    /**
     * 处理连接关闭
     *
     * @param userId 用户ID
     */
    public void handleDisconnect(String userId) {
        log.info("用户 {} 连接关闭", userId);
        eventPublisher.publishEvent(new WebSocketDisconnectEvent(this, userId));
    }

    /**
     * 处理错误
     *
     * @param userId 用户ID
     * @param error 错误信息
     */
    public void handleError(String userId, Throwable error) {
        log.error("用户 {} 发生错误: {}", userId, error.getMessage(), error);
        if (userId != null) {
            sendError(userId, "系统错误: " + error.getMessage());
        }
    }
}
