package com.broad.framework.security;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: XingGao
 * @Date: 2022 /07/10 18:03
 * @Description:
 */
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    /**
     * 注册Sa-Token的拦截器，打开注解式鉴权功能
     *
     * @param registry 拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handle -> {
                    // 对所有的路由进行登录鉴权
                    SaRouter.match("/**", r -> StpUtil.checkLogin());
                }))
                .excludePathPatterns(EXCLUDE_PATH)
                .addPathPatterns("/**");
    }


    /**
     * 排除不需要登录的接口
     */
    private final String[] EXCLUDE_PATH = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/favicon.ico",
            "/upload/**",
    };

}
