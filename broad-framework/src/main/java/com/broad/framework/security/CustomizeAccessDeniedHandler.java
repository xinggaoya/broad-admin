package com.broad.framework.security;


import com.alibaba.fastjson2.JSON;
import com.broad.common.constant.HttpStatus;
import com.broad.common.web.entity.ResultData;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: XingGao
 * @date: 2022/12/4
 * @description: 当访问接口没有权限时，自定义的返回结果
 */
@Component
public class CustomizeAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(ResultData.error("没有权限").setCode(HttpStatus.FORBIDDEN)));
    }
}