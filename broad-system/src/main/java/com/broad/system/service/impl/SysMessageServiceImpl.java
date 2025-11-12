package com.broad.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.common.utils.SpringUtils;
import com.broad.common.web.socket.WebSocketMessage;
import com.broad.common.web.socket.WebSocketMessageSender;
import com.broad.system.entity.SysMessage;
import com.broad.system.mapper.SysMessageMapper;
import com.broad.system.service.SysMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统消息(SysMessage)表服务实现类
 *
 * @author broad
 * @since 2024-03-06
 */
@Slf4j
@Service("sysMessageService")
public class SysMessageServiceImpl extends ServiceImpl<SysMessageMapper, SysMessage> implements SysMessageService {

    /**
     * 根据用户ID查询消息
     *
     * @param userId 用户ID
     * @return 消息列表
     */
    @Override
    public List<SysMessage> selectByUserId(String userId) {
        LambdaQueryWrapper<SysMessage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysMessage::getUserId, userId)
                .orderByDesc(SysMessage::getCreateTime);
        return this.list(queryWrapper);
    }

    /**
     * 标记消息为已读
     *
     * @param id 消息ID
     * @return 操作结果
     */
    @Override
    public boolean markAsRead(Long id) {
        SysMessage message = this.getById(id);
        if (message != null && message.getStatus() == 0) {
            message.setStatus(1);
            return this.updateById(message);
        }
        return false;
    }

    /**
     * 获取未读消息数量
     *
     * @param userId 用户ID
     * @return 未读消息数量
     */
    @Override
    public int getUnreadCount(String userId) {
        LambdaQueryWrapper<SysMessage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysMessage::getUserId, userId)
                .eq(SysMessage::getStatus, 0);
        return (int) this.count(queryWrapper);
    }

    /**
     * 创建并发送通知（保存到数据库并通过WebSocket推送）
     *
     * @param message 消息对象
     * @return 是否创建成功
     */
    @Override
    public boolean createAndSendNotification(SysMessage message) {
        if (message == null || message.getUserId() == null) {
            return false;
        }

        // 设置默认值
        if (message.getStatus() == null) {
            message.setStatus(0); // 0-未读，1-已读
        }
        if (message.getCreateTime() == null) {
            message.setCreateTime(LocalDateTime.now());
        }

        // 保存到数据库
        boolean saved = this.save(message);

        if (saved) {
            // 通过WebSocket推送通知
            sendWebSocketNotification(message);
        }

        return saved;
    }

    /**
     * 发送WebSocket通知
     *
     * @param message 消息对象
     */
    private void sendWebSocketNotification(SysMessage message) {
        try {
            WebSocketMessageSender sender = SpringUtils.getBean(WebSocketMessageSender.class);

            // 创建通知消息
            WebSocketMessage<SysMessage> websocketMessage = WebSocketMessage.createNotificationMessage(
                    "SYSTEM",
                    message.getUserId().toString(),
                    message
            );

            // 发送通知
            sender.sendToUser(websocketMessage);

        } catch (Exception e) {
            log.error("发送WebSocket通知失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 发送广播通知（给所有在线用户）
     *
     * @param message 消息对象
     * @return 发送成功数量
     */
    @Override
    public int broadcastNotification(SysMessage message) {
        int successCount = 0;
        try {
            // 获取所有在线用户
            WebSocketMessageSender sender = SpringUtils.getBean(WebSocketMessageSender.class);
            com.broad.common.web.socket.WebSocketSessionManager sessionManager = SpringUtils.getBean(com.broad.common.web.socket.WebSocketSessionManager.class);
            List<String> onlineUsers = sessionManager.getOnlineUsers();

            // 给每个在线用户发送通知
            for (String userId : onlineUsers) {
                try {
                    Long userIdLong = Long.parseLong(userId);
                    SysMessage userMessage = new SysMessage();
                    userMessage.setUserId(userIdLong);
                    userMessage.setTitle(message.getTitle());
                    userMessage.setContent(message.getContent());
                    userMessage.setType(message.getType());
                    userMessage.setStatus(0);
                    userMessage.setCreateTime(LocalDateTime.now());

                    // 保存并发送
                    if (this.save(userMessage)) {
                        sendWebSocketNotification(userMessage);
                        successCount++;
                    }
                } catch (NumberFormatException e) {
                    log.warn("用户ID {} 无法转换为Long类型", userId);
                }
            }
        } catch (Exception e) {
            log.error("广播通知失败: {}", e.getMessage(), e);
        }
        return successCount;
    }
} 