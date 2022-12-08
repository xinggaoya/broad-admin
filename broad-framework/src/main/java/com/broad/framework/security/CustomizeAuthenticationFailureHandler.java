package com.broad.framework.security;

import com.alibaba.fastjson2.JSON;
import com.broad.common.exception.CaptchaException;
import com.broad.common.web.entity.ResultData;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: XingGao
 * @date: 2022/12/4
 * @description: 登录失败返回结果
 */
@Component
public class CustomizeAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        //返回json数据
        ResultData result;
        if (e instanceof AccountExpiredException) {
            //账号过期
            result = ResultData.error("账号过期");
        } else if (e instanceof BadCredentialsException) {
            //密码错误
            result = ResultData.error("密码错误");
        } else if (e instanceof CredentialsExpiredException) {
            //密码过期
            result = ResultData.error("密码过期");
        } else if (e instanceof DisabledException) {
            //账号不可用
            result = ResultData.error("账号不可用");
        } else if (e instanceof LockedException) {
            //账号锁定
            result = ResultData.error("账号锁定");
        } else if (e instanceof InternalAuthenticationServiceException) {
            //用户不存在
            result = ResultData.error("用户不存在");
        } else if (e instanceof CaptchaException) {
            //验证码错误
            result = ResultData.error(e.getMessage());
        } else {
            //其他错误
            result = ResultData.error("认证失败，请联系管理员");
        }
        //处理编码方式，防止中文乱码的情况
        httpServletResponse.setContentType("text/json;charset=utf-8");
        // 把Json数据放入到HttpServletResponse中返回给前台
        httpServletResponse.getWriter().write(JSON.toJSONString(result));
    }
}
