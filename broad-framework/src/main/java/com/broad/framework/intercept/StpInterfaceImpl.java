//package com.broad.framework.intercept;
//
//import cn.dev33.satoken.stp.StpInterface;
//import com.broad.system.service.SysRoleMenuService;
//import com.broad.system.service.SysUserRoleService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
///**
// * Created with IntelliJ IDEA.
// *
// * @author: XingGao
// * @date: 2022 /07/10 22:04
// * @description: 自定义权限验证接口扩展
// */
//@Component
//public class StpInterfaceImpl implements StpInterface {
//
//    @Autowired
//    private SysRoleMenuService roleMenuService;
//    @Autowired
//    private SysUserRoleService userRoleService;
//
//    /**
//     * 返回一个账号所拥有的权限码集合
//     *
//     * @param loginId 账号id
//     * @return 权限码集合
//     */
//    @Override
//    public List<String> getPermissionList(Object loginId, String loginType) {
//        // 查询角色菜单
//        return roleMenuService.findRoleMenuCodeByUserId(Integer.parseInt(loginId.toString()));
//    }
//
//    /**
//     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
//     *
//     * @param loginId 账号id
//     * @return 角色标识集合
//     */
//    @Override
//    public List<String> getRoleList(Object loginId, String loginType) {
//        return userRoleService.selectUserRoleCodes(Long.parseLong(loginId.toString()));
//    }
//
//}
