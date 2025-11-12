package com.broad.common.web.socket;

/**
 * WebSocket消息类型枚举
 *
 * @author XingGao
 */
public enum MessageType {
    /**
     * 文本消息
     */
    TEXT,

    /**
     * 通知消息
     */
    NOTIFICATION,

    /**
     * 心跳请求
     */
    PING,

    /**
     * 心跳响应
     */
    PONG,

    /**
     * 消息确认
     */
    ACK,

    /**
     * 系统消息
     */
    SYSTEM,

    /**
     * 连接认证
     */
    AUTH,

    /**
     * 错误消息
     */
    ERROR
}
