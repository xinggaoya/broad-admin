package com.broad.framework.security;

import com.alibaba.fastjson2.JSON;
import com.broad.common.service.RedisService;
import com.broad.common.utils.SecurityUtils;
import com.broad.common.utils.ip.IpUtils;
import com.broad.common.web.entity.ResultData;
import com.broad.common.web.entity.SysUser;
import com.broad.framework.web.service.TokenService;
import com.broad.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author: XingGao
 * @date: 2022/12/4
 * @description: 登录成功返回结果
 */
@Component
public class CustomizeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        //更新用户表上次登录时间、更新人、更新时间等字段
        SysUser userDetails = SecurityUtils.getLoginUser();
        SysUser sysUser = sysUserService.lambdaQuery().eq(SysUser::getUsername, userDetails.getUsername()).one();
        sysUser.setLastLogintime(new Date());
        String ip = IpUtils.getIp(httpServletRequest);
        sysUser.setLastIp(ip);
        sysUser.setLastLoginip(IpUtils.getIpAddress(ip));
        sysUserService.updateById(sysUser);

        // 根据用户的id和account生成token并返回
        String uuid = UUID.randomUUID().toString();
        Map<String, String> results = new HashMap<>(1);
        results.put("token", uuid);

        //处理编码方式，防止中文乱码的情况
        httpServletResponse.setContentType("text/json;charset=utf-8");
        // 把Json数据放入HttpServletResponse中返回给前台
        httpServletResponse.getWriter().write(JSON.toJSONString(ResultData.success(sysUser).setMsg("登录成功123").setMap(results)));
    }
}