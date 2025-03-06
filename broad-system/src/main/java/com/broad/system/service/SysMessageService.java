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
} 