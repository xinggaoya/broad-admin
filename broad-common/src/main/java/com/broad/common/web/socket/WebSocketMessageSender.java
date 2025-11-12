package com.broad.common.web.socket;

import cn.hutool.core.util.StrUtil;
import com.broad.common.web.entity.ResultData;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * WebSocket消息发送器
 * 负责消息的发送和广播
 *
 * @author XingGao
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketMessageSender {

    /**
     * 会话管理器
     */
    private final WebSocketSessionManager sessionManager;

    /**
     * JSON对象映射器
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 发送消息给指定用户
     *
     * @param message 消息
     * @return 是否发送成功
     */
    public boolean sendToUser(WebSocketMessage<?> message) {
        if (message == null || StrUtil.isBlank(message.getReceiver())) {
            log.error("消息或接收者为空，发送失败");
            return false;
        }

        UserSession userSession = sessionManager.getSession(message.getReceiver());
        if (userSession == null || !userSession.isValid()) {
            log.warn("用户 {} 不在线或会话无效，消息发送失败", message.getReceiver());
            return false;
        }

        try {
            String json = objectMapper.writeValueAsString(message);
            sendTextMessage(userSession.getSession(), json);
            log.debug("消息已发送给用户 {}: {}", message.getReceiver(), message.getType());
            return true;
        } catch (Exception e) {
            log.error("发送消息给用户 {} 失败: {}", message.getReceiver(), e.getMessage());
            sessionManager.removeSession(message.getReceiver());
            return false;
        }
    }

    /**
     * 批量发送消息给多个用户
     *
     * @param userIds 用户ID列表
     * @param message 消息
     * @return 成功发送的用户数
     */
    public int sendToUsers(List<String> userIds, WebSocketMessage<?> message) {
        if (userIds == null || userIds.isEmpty()) {
            log.warn("用户ID列表为空");
            return 0;
        }

        int successCount = 0;
        for (String userId : userIds) {
            WebSocketMessage<?> msg = WebSocketMessage.builder()
                    .id(message.getId())
                    .type(message.getType())
                    .sender(message.getSender())
                    .receiver(userId)
                    .payload(message.getPayload())
                    .timestamp(message.getTimestamp())
                    .build();

            if (sendToUser(msg)) {
                successCount++;
            }
        }

        log.info("批量发送消息给 {} 个用户，成功 {} 个", userIds.size(), successCount);
        return successCount;
    }

    /**
     * 广播消息给所有在线用户
     *
     * @param message 消息
     * @return 成功发送的用户数
     */
    public int broadcast(WebSocketMessage<?> message) {
        List<String> onlineUsers = sessionManager.getOnlineUsers();
        log.info("广播消息给所有在线用户，当前在线用户数: {}", onlineUsers.size());

        return sendToUsers(onlineUsers, message);
    }

    /**
     * 发送系统通知
     *
     * @param receiver 接收者（为发送给特定用户，null为广播给所有人）
     * @param content 通知内容
     * @return 是否发送成功
     */
    public boolean sendNotification(String receiver, Object content) {
        WebSocketMessage<Object> message;
        if (receiver == null) {
            // 广播
            message = WebSocketMessage.createSystemMessage(null, content);
            int count = broadcast(message);
            return count > 0;
        } else {
            // 单发
            message = WebSocketMessage.createNotificationMessage("SYSTEM", receiver, content);
            return sendToUser(message);
        }
    }

    /**
     * 发送错误消息
     *
     * @param receiver 接收者
     * @param errorMessage 错误消息
     * @return 是否发送成功
     */
    public boolean sendError(String receiver, String errorMessage) {
        WebSocketMessage<String> message = WebSocketMessage.createErrorMessage(receiver, errorMessage);
        return sendToUser(message);
    }

    /**
     * 发送成功响应
     *
     * @param receiver 接收者
     * @param data 响应数据
     * @return 是否发送成功
     */
    public boolean sendSuccess(String receiver, Object data) {
        ResultData result = ResultData.success(data);
        WebSocketMessage<ResultData> message = WebSocketMessage
                .createSystemMessage(receiver, result);
        return sendToUser(message);
    }

    /**
     * 发送文本消息
     *
     * @param session WebSocket会话
     * @param message 消息文本
     * @throws IOException IO异常
     */
    private void sendTextMessage(Session session, String message) throws IOException {
        session.getBasicRemote().sendText(message);
    }
}
