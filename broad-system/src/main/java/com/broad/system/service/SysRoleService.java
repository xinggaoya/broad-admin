package com.broad.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.broad.system.entity.SysRole;

import java.util.List;

/**
 * 管理分组表(SysRole)表服务接口
 *
 * @author XingGao
 * @since 2022 -07-13 09:53:11
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 分页查询所有数据
     *
     * @param sysUserRole 查询实体
     * @return 所有数据 page
     */
    List<SysRole> selectAll(SysRole sysUserRole);

    /**
     * Save admin group int.
     *
     * @param sysUserRole the sys admin group
     * @return the int
     */
    int saveUserRole(SysRole sysUserRole);

    /**
     * Update admin group int.
     *
     * @param sysUserRole the sys admin group
     * @return the int
     */
    int updateUserRole(SysRole sysUserRole);

    /**
     * Delete admin group int.
     *
     * @param idList the id list
     * @return the int
     */
    int deleteUserRole(List<Long> idList);
}

