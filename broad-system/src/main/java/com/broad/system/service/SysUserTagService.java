package com.broad.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.broad.system.entity.SysUserTag;

import java.util.List;

/**
 * 用户标签(SysUserTag)表服务接口
 *
 * @author broad
 * @since 2024-03-06
 */
public interface SysUserTagService extends IService<SysUserTag> {

    /**
     * 根据用户ID查询标签
     *
     * @param userId 用户ID
     * @return 标签列表
     */
    List<SysUserTag> selectByUserId(String userId);
} 