package com.broad.system.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.common.enums.UserStatus;
import com.broad.common.exception.ServiceException;
import com.broad.common.utils.StringUtils;
import com.broad.system.entity.SysUser;
import com.broad.system.mapper.SysUserMapper;
import com.broad.system.service.SysUserRoleService;
import com.broad.system.service.SysUserService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 管理员表(SysUser)表服务实现类
 *
 * @author broad
 * @since 2022-07-09 17:19:40
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserRoleService userRoleService;

    @Override
    public IPage<SysUser> selectAll(SysUser sysAdmin, Page<SysUser> page) {
        return baseMapper.selectAll(sysAdmin, page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveAdmin(SysUser sysAdmin) {
        String uid = java.util.UUID.randomUUID().toString();
        sysAdmin.setPassword(SaSecureUtil.md5BySalt(sysAdmin.getPassword(), uid));
        sysAdmin.setSalt(uid);
        boolean res = super.save(sysAdmin);
        // 设置管理员角色
        if (res && !sysAdmin.getRoleIds().isEmpty()) {
            userRoleService.insertUserRole(sysAdmin);
        }
        return res ? 1 : 0;
    }

    @Override
    public List<SysUser> getAdminByIds(List<Long> ids) {
        return this.baseMapper.selectAllByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateAdmin(SysUser sysAdmin) {
        // 密码不允许修改
        sysAdmin.setPassword(null);
        // 用户名不允许修改
        sysAdmin.setUserName(null);

        if (ObjectUtils.isEmpty(sysAdmin)) {
            throw new ServiceException("请选择需要修改的用户");
        }
        // 处理用户状态
        if (sysAdmin.getUserStatus() != null) {
            if (UserStatus.OK.getCode().equals(sysAdmin.getUserStatus())) {
                unban(Long.valueOf(sysAdmin.getId()));
            }
            if (UserStatus.DISABLE.getCode().equals(sysAdmin.getUserStatus())) {
                forceLogout(Long.valueOf(sysAdmin.getId()));
            }
        }
        int res = baseMapper.updateById(sysAdmin);
        // 查询是否有角色
        if (sysAdmin.getRoleIds() != null && !sysAdmin.getRoleIds().isEmpty()) {
            userRoleService.insertUserRole(sysAdmin);
        }
        return res;
    }

    private void forceLogout(Long id) {
        StpUtil.kickout(id);
        StpUtil.disable(id, 60 * 60 * 1000);
    }

    private void unban(Long id) {
        StpUtil.untieDisable(id);
    }
}
