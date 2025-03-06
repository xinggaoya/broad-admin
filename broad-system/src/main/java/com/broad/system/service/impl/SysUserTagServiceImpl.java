package com.broad.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.system.entity.SysUserTag;
import com.broad.system.mapper.SysUserTagMapper;
import com.broad.system.service.SysUserTagService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户标签(SysUserTag)表服务实现类
 *
 * @author broad
 * @since 2024-03-06
 */
@Service("sysUserTagService")
public class SysUserTagServiceImpl extends ServiceImpl<SysUserTagMapper, SysUserTag> implements SysUserTagService {

    /**
     * 根据用户ID查询标签
     *
     * @param userId 用户ID
     * @return 标签列表
     */
    @Override
    public List<SysUserTag> selectByUserId(String userId) {
        LambdaQueryWrapper<SysUserTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserTag::getUserId, userId);
        return this.list(queryWrapper);
    }
} 