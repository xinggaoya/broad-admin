package com.broad.common.web.socket;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import jakarta.websocket.Session;

/**
 * WebSocket连接事件
 * 在WebSocket连接建立时发布
 *
 * @author XingGao
 */
@Getter
public class WebSocketConnectEvent extends ApplicationEvent {

    /**
     * 用户ID
     */
    private final String userId;

    /**
     * WebSocket会话
     */
    private final Session session;

    /**
     * 构造函数
     *
     * @param source 事件源
     * @param userId 用户ID
     * @param session WebSocket会话
     */
    public WebSocketConnectEvent(Object source, String userId, Session session) {
        super(source);
        this.userId = userId;
        this.session = session;
    }
}
