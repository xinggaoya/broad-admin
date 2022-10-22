package com.broad.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.system.entity.SysUser;
import com.broad.system.entity.SysUserRole;
import com.broad.system.mapper.SysUserRoleMapper;
import com.broad.system.service.SysUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * (SysUserRole)表服务实现类
 *
 * @author XingGao
 * @since 2022-10-21 01:03:47
 */
@Service("sysUserRoleService")
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertUserRole(SysUser user) {
        this.remove(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, user.getId()));
        List<SysUserRole> userRoleList = user.getRoleIds().stream().map(roleId ->
                SysUserRole.builder().roleId(roleId).userId(user.getId()).build()).collect(Collectors.toList());
        this.saveBatch(userRoleList);
    }
}

