package com.broad.framework.web.service;

import com.broad.common.config.BroadConfig;
import com.broad.common.constant.Constants;
import com.broad.common.exception.ServiceException;
import com.broad.common.exception.user.UserPasswordNotMatchException;
import com.broad.common.service.RedisService;
import com.broad.common.utils.ServletUtils;
import com.broad.common.utils.ip.IpUtils;
import com.broad.common.web.entity.SysUser;
import com.broad.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 登录校验方法
 *
 * @author JinJun
 */
@Component
public class SysLoginService {
    @Resource
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisService redisService;
    @Autowired
    private BroadConfig broadConfig;
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private TokenService tokenService;

    /**
     * 登录验证
     *
     * @param sysUser 用户信息
     * @return 结果
     */
    public SysUser login(SysUser sysUser) {
        boolean captchaOnOff = broadConfig.getCaptchaEnabled();
        // 验证码开关
        if (captchaOnOff) {
            if (sysUser.getCodeValue() == null || !sysUser.getCodeValue().equals(redisService.getCacheObject(Constants.CAPTCHA_CODE_KEY + sysUser.getCodeId()))) {
                String msg = "验证码错误";
                throw new ServiceException(msg);
            }
        }
        Authentication authentication;
        // 用户验证
        try {
            authentication =
                    // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
                    authenticationManager
                            .authenticate(new UsernamePasswordAuthenticationToken(sysUser.getUsername(), sysUser.getPassword()));
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                throw new UserPasswordNotMatchException();
            } else {
                throw new ServiceException(e.getMessage());
            }
        }
        SysUser user = sysUserService.lambdaQuery().eq(SysUser::getUsername, sysUser.getUsername()).one();
        sysUser.setLastLogintime(new Date());
        String ip = IpUtils.getIp(ServletUtils.getRequest());
        sysUser.setLastIp(ip);
        sysUser.setLastLoginip(IpUtils.getIpAddress(ip));
        sysUserService.updateById(sysUser);

        user.setTokenValue(tokenService.createJwtToken(sysUser));

        tokenService.saveToken(user);
        // 生成token
        return user;
    }

}
