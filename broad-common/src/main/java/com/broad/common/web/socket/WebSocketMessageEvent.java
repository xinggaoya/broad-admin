package com.broad.common.web.socket;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * WebSocket消息事件
 * 在WebSocket消息处理时发布
 *
 * @author XingGao
 */
@Getter
public class WebSocketMessageEvent extends ApplicationEvent {

    /**
     * 事件类型
     */
    private final WebSocketEvent event;

    /**
     * 用户ID
     */
    private final String userId;

    /**
     * 消息内容
     */
    private final WebSocketMessage<?> message;

    /**
     * 构造函数
     *
     * @param source 事件源
     * @param event 事件类型
     * @param userId 用户ID
     * @param message 消息内容
     */
    public WebSocketMessageEvent(Object source, WebSocketEvent event, String userId, WebSocketMessage<?> message) {
        super(source);
        this.event = event;
        this.userId = userId;
        this.message = message;
    }
}
