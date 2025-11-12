package com.broad.common.web.socket;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * WebSocket消息模型
 *
 * @author XingGao
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketMessage<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息ID，用于消息追踪和确认
     */
    private String id;

    /**
     * 消息类型
     */
    private MessageType type;

    /**
     * 消息负载数据
     */
    private T payload;

    /**
     * 发送者ID
     */
    private String sender;

    /**
     * 接收者ID（为空表示广播）
     */
    private String receiver;

    /**
     * 时间戳
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    /**
     * 创建文本消息
     */
    public static <T> WebSocketMessage<T> createTextMessage(String sender, String receiver, T payload) {
        return createMessage(MessageType.TEXT, sender, receiver, payload);
    }

    /**
     * 创建通知消息
     */
    public static <T> WebSocketMessage<T> createNotificationMessage(String sender, String receiver, T payload) {
        return createMessage(MessageType.NOTIFICATION, sender, receiver, payload);
    }

    /**
     * 创建系统消息
     */
    public static <T> WebSocketMessage<T> createSystemMessage(String receiver, T payload) {
        return createMessage(MessageType.SYSTEM, "SYSTEM", receiver, payload);
    }

    /**
     * 创建心跳消息
     */
    public static WebSocketMessage<String> createPingMessage() {
        return createMessage(MessageType.PING, "SERVER", null, "PING");
    }

    /**
     * 创建心跳响应消息
     */
    public static WebSocketMessage<String> createPongMessage() {
        return createMessage(MessageType.PONG, null, "SERVER", "PONG");
    }

    /**
     * 创建确认消息
     */
    public static WebSocketMessage<String> createAckMessage(String messageId) {
        WebSocketMessage<String> message = createMessage(MessageType.ACK, null, null, "ACK");
        message.setId(messageId);
        return message;
    }

    /**
     * 创建错误消息
     */
    public static <T> WebSocketMessage<T> createErrorMessage(String receiver, String errorMessage) {
        return createMessage(MessageType.ERROR, "SYSTEM", receiver, (T) errorMessage);
    }

    /**
     * 创建基础消息
     */
    private static <T> WebSocketMessage<T> createMessage(MessageType type, String sender, String receiver, T payload) {
        return WebSocketMessage.<T>builder()
                .id(java.util.UUID.randomUUID().toString())
                .type(type)
                .sender(sender)
                .receiver(receiver)
                .payload(payload)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
