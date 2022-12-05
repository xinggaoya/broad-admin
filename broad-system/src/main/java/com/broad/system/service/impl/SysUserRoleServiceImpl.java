package com.broad.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.common.constant.CacheConstants;
import com.broad.common.web.entity.SysUser;
import com.broad.system.entity.SysRole;
import com.broad.system.entity.SysUserRole;
import com.broad.system.mapper.SysUserRoleMapper;
import com.broad.system.service.SysRoleService;
import com.broad.system.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (SysUserRole)表服务实现类
 *
 * @author XingGao
 * @since 2022 -10-21 01:03:47
 */
@Service("sysUserRoleService")
@CacheConfig(cacheNames = CacheConstants.ROLE_KEY, keyGenerator = CacheConstants.CACHE_PREFIX_GENERATION)
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

    @Autowired
    private SysRoleService roleService;

    @Override
    public List<Integer> selectUserRoleIds(Long userId) {
        return this.list(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, userId)).stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(allEntries = true)
    public void insertUserRole(SysUser user) {
        this.remove(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, user.getId()));
        List<SysUserRole> userRoleList = user.getRoleIds().stream().map(roleId ->
                SysUserRole.builder().roleId(roleId).userId(user.getId()).build()).collect(Collectors.toList());
        this.saveBatch(userRoleList);
    }

    /**
     * 查询用户拥有角色码
     *
     * @param userId 用户ID
     */
    @Override
    @Cacheable(key = "#userId + '_role_codes'")
    public List<String> selectUserRoleCodes(Long userId) {
        List<String> roleCodes = new ArrayList<>();
        List<Integer> roleIds = this.selectUserRoleIds(userId);
        if (roleIds != null && roleIds.size() > 0) {
            roleCodes = this.roleService.lambdaQuery().in(SysRole::getId, roleIds).list().stream().map(SysRole::getHalfRules).collect(Collectors.toList());
        }
        return roleCodes;
    }
}

