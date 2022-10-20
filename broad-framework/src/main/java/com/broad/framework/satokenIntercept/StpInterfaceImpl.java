package com.broad.framework.satokenIntercept;

import cn.dev33.satoken.stp.StpInterface;
import com.broad.system.entity.SysMenu;
import com.broad.system.service.SysRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: XingGao
 * @Date: 2022/07/10 22:04
 * @return 是否有权限
 * @Description:自定义权限验证接口扩展
 */

@Component    // 保证此类被SpringBoot扫描，完成Sa-Token的自定义权限验证扩展
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    private SysRoleMenuService roleMenuService;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 本list仅做模拟，实际项目中要根据具体业务逻辑来查询权限
        List<String> list = new ArrayList<>();
        // 查询角色菜单
        List<SysMenu> menuList = roleMenuService.findRoleMenu(Integer.parseInt(loginId.toString()));
        for (SysMenu menu : menuList) {
            list.add(menu.getPerme());
        }
        return list;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 本list仅做模拟，实际项目中要根据具体业务逻辑来查询角色
        List<String> list = new ArrayList<>();
        return list;
    }

}
