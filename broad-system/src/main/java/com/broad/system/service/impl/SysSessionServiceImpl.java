package com.broad.system.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.broad.common.config.BroadConfig;
import com.broad.common.constant.Constants;
import com.broad.common.exception.ServiceException;
import com.broad.common.exception.user.UserPasswordNotMatchException;
import com.broad.common.service.RedisService;
import com.broad.common.utils.ip.IpUtils;
import com.broad.system.entity.SysLoginLog;
import com.broad.system.entity.SysUser;
import com.broad.system.mapper.SysUserMapper;
import com.broad.system.service.SysLoginLogService;
import com.broad.system.service.SysSessionService;
import com.broad.system.service.SysMenuService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

/**
 * @author: XingGao
 * @date: 2023/02/06 10:24
 * @description:
 */
@Service
public class SysSessionServiceImpl implements SysSessionService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysLoginLogService loginLogService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private SysMenuService sysMenuService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object administratorLogin(SysUser sysAdmin, HttpServletRequest request) {
        SysLoginLog sysLoginLog = new SysLoginLog();
        if (StringUtils.isAnyBlank(sysAdmin.getUserName(), sysAdmin.getPassword())) {
            throw new ServiceException("用户名或密码不能为空");
        }
        if (StringUtils.isBlank(sysAdmin.getCodeValue())) {
            throw new ServiceException("验证码不能为空");
        }
        SysUser admin = this.sysUserMapper
                .selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserName, sysAdmin.getUserName()));
        if (ObjectUtils.isEmpty(admin)) {
            throw new ServiceException("用户名不存在");
        }
        try {
            // 效验验证码
            String code = redisService.getCacheObject(Constants.CAPTCHA_CODE_KEY + sysAdmin.getCodeId());
            if (StringUtils.isBlank(code)) {
                throw new ServiceException("验证码已失效");
            }
            if (!sysAdmin.getCodeValue().toLowerCase().equals(code)) {
                throw new ServiceException("验证码错误");
            }
            if (!admin.getPassword().equals(SaSecureUtil.md5BySalt(sysAdmin.getPassword(), admin.getSalt()))) {
                throw new ServiceException("密码错误");
            }
            // 效验是否被封禁
            if (StpUtil.isDisable(admin.getId())) {
                throw new ServiceException("账号已被封禁,请联系管理员");
            }
            String ip = IpUtils.getIpAddr(request);
            admin.setLastLogintime(new Date());
            admin.setLastLoginip(IpUtils.getIpAddress(ip));
            admin.setLastIp(ip);
            this.sysUserMapper.updateById(admin);
            // 标记登录状态
            StpUtil.login(admin.getId());
            admin.setTokenValue(StpUtil.getTokenValue());

            sysLoginLog.setMessage("登录成功");
            sysLoginLog.setUserId(admin.getId());
            loginLogService.saveLoginLog(request, sysLoginLog);
            
            // 获取用户路由信息并添加到返回数据中
            Map<String, Object> result = new HashMap<>();
            result.put("userInfo", admin);
            result.put("routes", this.sysMenuService.findTreeMenuByUserId(admin.getId()));
            
            return result;
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
}
