package com.broad.common.web.socket;

/**
 * WebSocket事件类型枚举
 *
 * @author XingGao
 */
public enum WebSocketEvent {
    /**
     * 连接建立
     */
    CONNECT,

    /**
     * 连接断开
     */
    DISCONNECT,

    /**
     * 消息发送
     */
    MESSAGE_SEND,

    /**
     * 消息接收
     */
    MESSAGE_RECEIVE,

    /**
     * 心跳超时
     */
    HEARTBEAT_TIMEOUT,

    /**
     * 用户下线
     */
    USER_OFFLINE
}
