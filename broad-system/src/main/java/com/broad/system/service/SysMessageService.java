package com.broad.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.broad.system.entity.SysMessage;

import java.util.List;

/**
 * 系统消息(SysMessage)表服务接口
 *
 * @author broad
 * @since 2024-03-06
 */
public interface SysMessageService extends IService<SysMessage> {

    /**
     * 根据用户ID查询消息
     *
     * @param userId 用户ID
     * @return 消息列表
     */
    List<SysMessage> selectByUserId(String userId);

    /**
     * 标记消息为已读
     *
     * @param id 消息ID
     * @return 操作结果
     */
    boolean markAsRead(Long id);

    /**
     * 获取未读消息数量
     *
     * @param userId 用户ID
     * @return 未读消息数量
     */
    int getUnreadCount(String userId);

    /**
     * 创建并发送通知（保存到数据库并通过WebSocket推送）
     *
     * @param message 消息对象
     * @return 是否创建成功
     */
    boolean createAndSendNotification(SysMessage message);

    /**
     * 发送广播通知（给所有在线用户）
     *
     * @param message 消息对象
     * @return 发送成功数量
     */
    int broadcastNotification(SysMessage message);
} 