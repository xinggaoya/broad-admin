package com.broad.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.broad.system.entity.SysUser;
import com.broad.system.entity.SysUserRole;

/**
 * (SysUserRole)表服务接口
 *
 * @author XingGao
 * @since 2022-10-21 01:03:47
 */
public interface SysUserRoleService extends IService<SysUserRole> {

    void insertUserRole(SysUser user);
}

