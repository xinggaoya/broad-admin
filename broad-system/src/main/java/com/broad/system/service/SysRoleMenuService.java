package com.broad.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.broad.system.entity.SysMenu;
import com.broad.system.entity.SysRoleMenu;

import java.util.List;

/**
 * 管理权限分组表(SysRoleMenu)表服务接口
 *
 * @author XingGao
 * @since 2022 -10-19 17:15:02
 */
public interface SysRoleMenuService extends IService<SysRoleMenu> {

    /**
     * 新增角色菜单
     *
     * @param roleMenu the role menu
     * @return int
     */
    int addRoleMenu(SysRoleMenu roleMenu);

    /**
     * 查询角色菜单
     *
     * @param roleId 查询实体
     * @return 所有数据 list
     */
    List<SysMenu> findRoleMenu(Integer roleId);
}

