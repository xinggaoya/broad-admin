package com.broad.common.web.socket;

import jakarta.websocket.Session;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户会话封装类
 *
 * @author XingGao
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSession {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名称
     */
    private String username;

    /**
     * WebSocket会话
     */
    private Session session;

    /**
     * 会话建立时间
     */
    private LocalDateTime connectTime;

    /**
     * 最后心跳时间
     */
    private LocalDateTime lastHeartbeat;

    /**
     * 用户状态（ONLINE-在线，BUSY-忙碌，AWAY-离开）
     */
    private UserStatus status;

    /**
     * 用户IP地址
     */
    private String ipAddress;

    /**
     * User-Agent信息
     */
    private String userAgent;

    /**
     * 更新心跳时间
     */
    public void updateHeartbeat() {
        this.lastHeartbeat = LocalDateTime.now();
    }

    /**
     * 检查是否超时
     *
     * @param timeoutSeconds 超时时间（秒）
     * @return 是否超时
     */
    public boolean isTimeout(int timeoutSeconds) {
        if (lastHeartbeat == null) {
            return false;
        }
        return LocalDateTime.now().minusSeconds(timeoutSeconds).isAfter(lastHeartbeat);
    }

    /**
     * 检查会话是否有效
     *
     * @return 会话是否有效
     */
    public boolean isValid() {
        return session != null && session.isOpen();
    }

    /**
     * 用户状态枚举
     */
    public enum UserStatus {
        /**
         * 在线
         */
        ONLINE,

        /**
         * 忙碌
         */
        BUSY,

        /**
         * 离开
         */
        AWAY,

        /**
         * 离线
         */
        OFFLINE
    }
}
