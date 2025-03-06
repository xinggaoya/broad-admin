package com.broad.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.system.entity.SysTodo;
import com.broad.system.mapper.SysTodoMapper;
import com.broad.system.service.SysTodoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 待办事项(SysTodo)表服务实现类
 *
 * @author broad
 * @since 2024-03-06
 */
@Service("sysTodoService")
public class SysTodoServiceImpl extends ServiceImpl<SysTodoMapper, SysTodo> implements SysTodoService {

    /**
     * 根据用户ID查询待办事项
     *
     * @param userId 用户ID
     * @return 待办事项列表
     */
    @Override
    public List<SysTodo> selectByUserId(String userId) {
        LambdaQueryWrapper<SysTodo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysTodo::getUserId, userId)
                .orderByDesc(SysTodo::getCreateTime);
        return this.list(queryWrapper);
    }
} 