package com.broad.system.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.common.config.BroadConfig;
import com.broad.common.constant.Constants;
import com.broad.common.exception.ServiceException;
import com.broad.common.service.RedisService;
import com.broad.common.utils.ip.IpUtils;
import com.broad.system.entity.SysUser;
import com.broad.system.mapper.SysUserMapper;
import com.broad.system.service.SysUserRoleService;
import com.broad.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 管理员表(SysUser)表服务实现类
 *
 * @author XingGao
 * @since 2022 -07-09 17:19:40
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private RedisService redisService;
    @Autowired
    private SysUserRoleService userRoleService;
    @Autowired
    private BroadConfig broadConfig;

    @Override
    public List<SysUser> selectAll(SysUser sysAdmin) {
        return baseMapper.selectAll(sysAdmin);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveAdmin(SysUser sysAdmin) {
        String uid = java.util.UUID.randomUUID().toString();
        sysAdmin.setPassword(SaSecureUtil.md5BySalt(sysAdmin.getPassword(), uid));
        sysAdmin.setSalt(uid);
        boolean res = this.save(sysAdmin);
        // 设置管理员角色
        if (res && sysAdmin.getRoleIds().size() > 0) {
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
        // 如果密码与数据库密码不一致，说明用户修改密码,则重新加密
        if (sysAdmin.getPassword() != null && !sysAdmin.getPassword().equals(baseMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getId, sysAdmin.getId())).getPassword())) {
            String uid = java.util.UUID.randomUUID().toString();
            sysAdmin.setPassword(SaSecureUtil.md5BySalt(sysAdmin.getPassword(), uid));
            sysAdmin.setSalt(uid);
        }
        int res = baseMapper.updateById(sysAdmin);
        // 查询是否有角色
        if (sysAdmin.getRoleIds() != null && sysAdmin.getRoleIds().size() > 0) {
            userRoleService.insertUserRole(sysAdmin);
        }
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object administratorLogin(SysUser sysAdmin, HttpServletRequest request) {

        if (sysAdmin.getUserName() == null || sysAdmin.getPassword() == null) {
            throw new ServiceException("用户名或密码不能为空");
        }
        SysUser admin = baseMapper.selectOne(new QueryWrapper<SysUser>().lambda().eq(SysUser::getUserName, sysAdmin.getUserName()));

        if (broadConfig.getCaptchaEnabled()) {
            if (sysAdmin.getCodeValue() == null || !sysAdmin.getCodeValue().equals(redisService.getCacheObject(Constants.CAPTCHA_CODE_KEY + sysAdmin.getCodeId()))) {
                throw new ServiceException("请输入正确的验证码");
            }
        }
        if (admin == null) {
            throw new ServiceException("用户名不存在");
        }
        if (!admin.getPassword().equals(SaSecureUtil.md5BySalt(sysAdmin.getPassword(), admin.getSalt()))) {
            throw new ServiceException("密码错误");
        }
        String ip = IpUtils.getIp(request);
        admin.setLastLogintime(new Date());
        admin.setLastLoginip(IpUtils.getIpAddress(ip));
        admin.setLastIp(ip);
        baseMapper.updateById(admin);
        // 标记登录状态
        StpUtil.login(admin.getId());
        admin.setTokenValue(StpUtil.getTokenValue());
        return admin;
    }

    /**
     * 退出登录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logout() {
        StpUtil.logout();
    }

}

