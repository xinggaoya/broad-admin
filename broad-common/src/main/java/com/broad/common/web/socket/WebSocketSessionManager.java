package com.broad.common.web.socket;

import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * WebSocket会话管理器
 * 管理所有在线用户的会话，提供线程安全的会话操作
 *
 * @author XingGao
 */
@Slf4j
@Component
public class WebSocketSessionManager {

    /**
     * 用户会话映射表：userId -> UserSession
     * 使用ConcurrentHashMap保证线程安全
     */
    private final Map<String, UserSession> userSessions = new ConcurrentHashMap<>();

    /**
     * 添加用户会话
     *
     * @param userId 用户ID
     * @param session 用户会话
     * @return 是否添加成功
     */
    public boolean addSession(String userId, UserSession session) {
        if (userId == null || session == null) {
            return false;
        }

        // 如果用户已存在，先移除旧会话
        if (userSessions.containsKey(userId)) {
            log.warn("用户 {} 已存在会话，正在替换旧会话", userId);
            removeSession(userId);
        }

        userSessions.put(userId, session);
        log.info("用户 {} 会话已添加，当前在线用户数: {}", userId, getOnlineCount());
        return true;
    }

    /**
     * 移除用户会话
     *
     * @param userId 用户ID
     * @return 是否移除成功
     */
    public boolean removeSession(String userId) {
        UserSession removed = userSessions.remove(userId);
        if (removed != null) {
            try {
                if (removed.isValid()) {
                    removed.getSession().close();
                }
            } catch (Exception e) {
                log.error("关闭用户 {} 的WebSocket会话时发生错误", userId, e);
            }
            log.info("用户 {} 会话已移除，当前在线用户数: {}", userId, getOnlineCount());
            return true;
        }
        return false;
    }

    /**
     * 获取用户会话
     *
     * @param userId 用户ID
     * @return 用户会话，不存在返回null
     */
    public UserSession getSession(String userId) {
        return userSessions.get(userId);
    }

    /**
     * 获取WebSocket会话
     *
     * @param userId 用户ID
     * @return WebSocket会话，不存在返回null
     */
    public Session getWebSocketSession(String userId) {
        UserSession userSession = userSessions.get(userId);
        return userSession != null ? userSession.getSession() : null;
    }

    /**
     * 检查用户是否在线
     *
     * @param userId 用户ID
     * @return 是否在线
     */
    public boolean isOnline(String userId) {
        UserSession session = userSessions.get(userId);
        return session != null && session.isValid();
    }

    /**
     * 获取所有在线用户ID
     *
     * @return 在线用户ID列表
     */
    public List<String> getOnlineUsers() {
        return new ArrayList<>(userSessions.keySet());
    }

    /**
     * 获取所有在线用户会话
     *
     * @return 在线用户会话列表
     */
    public List<UserSession> getAllSessions() {
        return new ArrayList<>(userSessions.values());
    }

    /**
     * 获取在线用户数量
     *
     * @return 在线用户数量
     */
    public int getOnlineCount() {
        return userSessions.size();
    }

    /**
     * 更新用户心跳时间
     *
     * @param userId 用户ID
     */
    public void updateHeartbeat(String userId) {
        UserSession session = userSessions.get(userId);
        if (session != null) {
            session.updateHeartbeat();
        }
    }

    /**
     * 获取超时用户列表
     *
     * @param timeoutSeconds 超时时间（秒）
     * @return 超时用户ID列表
     */
    public List<String> getTimeoutUsers(int timeoutSeconds) {
        return userSessions.entrySet().stream()
                .filter(entry -> entry.getValue().isTimeout(timeoutSeconds))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * 批量移除用户会话
     *
     * @param userIds 用户ID列表
     */
    public void removeSessions(List<String> userIds) {
        userIds.forEach(this::removeSession);
    }

    /**
     * 清空所有会话
     */
    public void clearAllSessions() {
        List<String> userIds = new ArrayList<>(userSessions.keySet());
        userIds.forEach(this::removeSession);
        log.info("所有WebSocket会话已清空");
    }

    /**
     * 获取指定用户集合的会话
     *
     * @param userIds 用户ID集合
     * @return 用户会话列表
     */
    public List<UserSession> getSessionsByUserIds(List<String> userIds) {
        return userIds.stream()
                .map(userSessions::get)
                .filter(session -> session != null && session.isValid())
                .collect(Collectors.toList());
    }
}
