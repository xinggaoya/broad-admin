package com.broad.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.broad.common.web.entity.SysUser;
import com.broad.system.entity.SysUserRole;

import java.util.List;

/**
 * (SysUserRole)表服务接口
 *
 * @author XingGao
 * @since 2022 -10-21 01:03:47
 */
public interface SysUserRoleService extends IService<SysUserRole> {

    /**
     * 根据用户ID查询角色ID列表
     *
     * @param userId 用户ID
     * @return 角色ID列表 list
     */
    List<Integer> selectUserRoleIds(Long userId);

    /**
     * 新增用户角色
     *
     * @param user 用户
     */
    void insertUserRole(SysUser user);

    /**
     * 查询用户拥有角色码
     *
     * @param userId 用户ID
     */
    List<String> selectUserRoleCodes(Long userId);
}

