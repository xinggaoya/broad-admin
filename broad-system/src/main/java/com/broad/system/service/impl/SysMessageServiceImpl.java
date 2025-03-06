package com.broad.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.system.entity.SysMessage;
import com.broad.system.mapper.SysMessageMapper;
import com.broad.system.service.SysMessageService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统消息(SysMessage)表服务实现类
 *
 * @author broad
 * @since 2024-03-06
 */
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
} 