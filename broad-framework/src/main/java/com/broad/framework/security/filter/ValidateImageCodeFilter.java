package com.broad.framework.security.filter;

import com.broad.common.constant.Constants;
import com.broad.common.exception.CaptchaException;
import com.broad.common.service.RedisService;
import com.broad.common.utils.StringUtils;
import com.broad.framework.security.CustomizeAuthenticationFailureHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author: XingGao
 * @date: 2022/12/4
 * @description:
 */
@Slf4j
@Component
public class ValidateImageCodeFilter extends OncePerRequestFilter {

    @Autowired
    //自定义验证失败处理器
    private CustomizeAuthenticationFailureHandler customizeAuthenticationFailureHandler;

    @Autowired
    private RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //请求路径中是否包含login这个关键词 && 发送的请求必须是post
        if (StringUtils.contains(request.getRequestURI(), "login")
                && StringUtils.equalsIgnoreCase(request.getMethod(), "post")) {
            try {
                //开始验证
                validateCode(request);
            } catch (CaptchaException e) {
                //如果验证失败，就使用自定义验证处理器
                customizeAuthenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void validateCode(HttpServletRequest request) throws CaptchaException {
        // 获取请求中的验证码
        String codeId = request.getParameter("codeId");
        String codeValue = request.getParameter("codeValue");
        //验证码判空
        if (StringUtils.isBlank(codeValue)) {
            throw new CaptchaException("验证码不能为空 ");
        }
        String key = Constants.CAPTCHA_CODE_KEY + codeId;
        String cacheCode = redisService.getCacheObject(key);
        if (StringUtils.isBlank(codeValue)) {
            throw new CaptchaException("验证码不存在或已过期");
        }
        if (!Objects.equals(cacheCode, codeValue)) {
            throw new CaptchaException("验证码不正确");
        }
        redisService.deleteObject(key);
    }

}