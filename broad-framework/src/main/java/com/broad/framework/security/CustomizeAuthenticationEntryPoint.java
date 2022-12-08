package com.broad.framework.security;

import com.broad.common.constant.HttpStatus;
import com.broad.common.utils.ServletUtils;
import com.broad.common.web.entity.ResultData;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: XingGao
 * @date: 2022/12/4
 * @description: 未登录或token失效时访问接口时，自定义的返回结果
 */
@Component
public class CustomizeAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        httpServletResponse.setContentType("text/json;charset=utf-8");
        ServletUtils.renderJsonString(httpServletResponse, ResultData.error("系统认证失败，无法访问系统资源").setCode(HttpStatus.UNAUTHORIZED));

    }
}
