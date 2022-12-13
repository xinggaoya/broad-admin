package com.broad.system.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.common.config.BroadConfig;
import com.broad.common.constant.Constants;
import com.broad.common.enums.UserStatus;
import com.broad.common.exception.ServiceException;
import com.broad.common.exception.user.UserPasswordNotMatchException;
import com.broad.common.service.RedisService;
import com.broad.common.utils.StringUtils;
import com.broad.common.utils.ip.IpUtils;
import com.broad.system.entity.SysLoginLog;
import com.broad.system.entity.SysUser;
import com.broad.system.mapper.SysUserMapper;
import com.broad.system.service.SysLoginLogService;
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

    @Autowired
    private SysLoginLogService loginLogService;


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
        if (StringUtils.isNotNull(sysAdmin.getPassword())) {
            String uid = java.util.UUID.randomUUID().toString();
            sysAdmin.setPassword(SaSecureUtil.md5BySalt(sysAdmin.getPassword(), uid));
            sysAdmin.setSalt(uid);
        }
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
        if (sysAdmin.getRoleIds() != null && sysAdmin.getRoleIds().size() > 0) {
            userRoleService.insertUserRole(sysAdmin);
        }
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object administratorLogin(SysUser sysAdmin, HttpServletRequest request) {
        SysLoginLog sysLoginLog = new SysLoginLog();
        if (sysAdmin.getUserName() == null || sysAdmin.getPassword() == null) {
            String msg = "用户名或密码不能为空";
            throw new ServiceException(msg);
        }
        SysUser admin = this.lambdaQuery().eq(SysUser::getUserName, sysAdmin.getUserName()).one();
        try {
            if (broadConfig.getCaptchaEnabled()) {
                String code = redisService.getCacheObject(Constants.CAPTCHA_CODE_KEY + sysAdmin.getCodeId());
                if (code == null) {
                    throw new ServiceException("验证码已失效");
                }
                if (sysAdmin.getCodeValue() == null || !sysAdmin.getCodeValue().equals(code)) {
                    String msg = "验证码错误";
                    throw new ServiceException(msg);
                }
            }
            if (admin == null) {
                throw new ServiceException("用户名不存在");
            }
            if (!admin.getPassword().equals(SaSecureUtil.md5BySalt(sysAdmin.getPassword(), admin.getSalt()))) {
                throw new UserPasswordNotMatchException();
            }
            // 效验是否被封禁
            if (StpUtil.isDisable(admin.getId())) {
                throw new ServiceException("账号已被封禁,请联系管理员");
            }
            String ip = IpUtils.getIp(request);
            admin.setLastLogintime(new Date());
            admin.setLastLoginip(IpUtils.getIpAddress(ip));
            admin.setLastIp(ip);
            baseMapper.updateById(admin);
            // 标记登录状态
            StpUtil.login(admin.getId());
            admin.setTokenValue(StpUtil.getTokenValue());

            sysLoginLog.setMessage("登录成功");
            sysLoginLog.setUserId(admin.getId());
            loginLogService.saveLoginLog(request, sysLoginLog);
            return admin;
        } catch (ServiceException e) {
            String msg = e.getMessage();
            sysLoginLog.setLoginStatus("1");
            sysLoginLog.setMessage(msg);
            sysLoginLog.setUserId(admin.getId());
            loginLogService.saveLoginLog(request, sysLoginLog);
            throw new ServiceException(msg);
        }
    }

    /**
     * 退出登录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logout() {
        StpUtil.logout();
    }

    private void forceLogout(Long id) {
        StpUtil.kickout(id);
        StpUtil.disable(id, 60 * 60 * 1000);
    }

    private void unban(Long id) {
        StpUtil.untieDisable(id);
    }

}

