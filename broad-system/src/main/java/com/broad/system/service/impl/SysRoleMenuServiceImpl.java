package com.broad.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.system.entity.SysMenu;
import com.broad.system.entity.SysRoleMenu;
import com.broad.system.mapper.SysRoleMenuMapper;
import com.broad.system.service.SysRoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理权限分组表(SysRoleMenu)表服务实现类
 *
 * @author XingGao
 * @since 2022-10-19 17:15:02
 */
@Service("sysRoleMenuService")
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {

    @Override
    public List<SysMenu> findRoleMenu(Integer roleId) {
        return this.baseMapper.findRoleMenu(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addRoleMenu(SysRoleMenu roleMenu) {
        List<SysRoleMenu> list = new ArrayList<>();
        // 删除角色与菜单关联
        this.remove(new LambdaUpdateWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleMenu.getRoleId()));
        if (roleMenu.getMenuIds().size() == 0) {
            return 1;
        }
        // 新增角色与菜单管理
        for (Integer menuId : roleMenu.getMenuIds()) {
            list.add(SysRoleMenu.builder().roleId(roleMenu.getRoleId()).menuId(menuId).build());
        }
        return list.size() > 0 ? baseMapper.insertBatch(list) : 1;
    }

}

