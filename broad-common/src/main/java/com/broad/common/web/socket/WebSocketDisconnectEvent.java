package com.broad.common.web.socket;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * WebSocket断开事件
 * 在WebSocket连接关闭时发布
 *
 * @author XingGao
 */
@Getter
public class WebSocketDisconnectEvent extends ApplicationEvent {

    /**
     * 用户ID
     */
    private final String userId;

    /**
     * 构造函数
     *
     * @param source 事件源
     * @param userId 用户ID
     */
    public WebSocketDisconnectEvent(Object source, String userId) {
        super(source);
        this.userId = userId;
    }
}
