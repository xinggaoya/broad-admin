package com.broad.framework.security.service.impl;

import com.broad.common.utils.StringUtils;
import com.broad.common.web.entity.SysUser;
import com.broad.framework.security.service.SecurityUserService;
import com.broad.system.service.SysRoleMenuService;
import com.broad.system.service.SysUserRoleService;
import com.broad.system.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

/**
 * @author: XingGao
 * @date: 2022/12/4
 * @description: 用户权限处理
 */
@Service
@Slf4j
public class SecurityUserServiceImpl implements SecurityUserService {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    /**
     * 根据用户名查找数据库，判断是否存在这个用户
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 用户名必须是唯一的，不允许重复
        SysUser sysUser = sysUserService.lambdaQuery().eq(SysUser::getUsername, username).one();

        if (StringUtils.isNull(sysUser)) {
            throw new UsernameNotFoundException("用户名不存在");
        }

        // 获取用户角色
        List<String> roleCodes = sysUserRoleService.selectUserRoleCodes(Long.valueOf(sysUser.getId()));
        // 获取用户权限
        List<String> sysPermissions = sysRoleMenuService.findRoleMenuCodeByUserId(sysUser.getId());
        sysUser.setPermissions(new HashSet<>(sysPermissions));
        sysUser.setRoleCode(new HashSet<>(roleCodes));

        return sysUser;
    }
}