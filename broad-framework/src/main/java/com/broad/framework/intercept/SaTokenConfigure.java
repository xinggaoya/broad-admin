package com.broad.framework.intercept;//package com.broad.framework.intercept;
//
//import cn.dev33.satoken.interceptor.SaInterceptor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
///**
// * Created with IntelliJ IDEA.
// *
// * @Author: XingGao
// * @Date: 2022 /07/10 18:03
// * @Description:
// */
//@Configuration
//public class SaTokenConfigure implements WebMvcConfigurer {
//
//    /**
//     * 注册Sa-Token的拦截器，打开注解式鉴权功能
//     *
//     * @param registry 拦截器
//     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        // 注册拦截器，并排除不需要注解鉴权的接口地址
//        registry.addInterceptor(new SaInterceptor(handle -> {
//                    // 对所有的路由进行登录鉴权
////                    SaRouter.match("/**", r -> StpUtil.checkLogin());
//                }))
//                .addPathPatterns("/**")
//                .excludePathPatterns("/**/*.js", "/**/*.css", "/**/*.gif", "/**/*.png", "/**/*.jpg", "/upload/**",
//                        "/**/*.jpeg", "/**/*.html", "/**/*.svg", "/**/*.woff", "/**/*.ttf", "/**/*.woff2", "/**/*.ico",
//                        "/swagger-resources", "/sysAdmin/login", "/captchaImage", "/doc.html", "/test/**"
//                        , "/druid/**");
//    }
//
//}


import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.util.SaResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * [Sa-Token 权限认证] 配置类
 */
@Configuration
public class SaTokenConfigure {

    /**
     * 注册 [Sa-Token全局过滤器]
     */
    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()

                // 指定 拦截路由 与 放行路由
                .addInclude("/test").addExclude("/favicon.ico")    /* 排除掉 /favicon.ico */

                // 认证函数: 每次请求执行
                .setAuth(obj -> {
                    // 更多拦截处理方式，请参考“路由拦截式鉴权”章节 */
                })

                // 异常处理函数：每次认证函数发生异常时执行此函数
                .setError(e -> {
                    System.out.println("---------- 进入Sa-Token异常处理 -----------");
                    return SaResult.error(e.getMessage());
                })

                // 前置函数：在每次认证函数之前执行
                .setBeforeAuth(r -> {
                    // ---------- 设置一些安全响应头 ----------
                    SaHolder.getResponse()
                            // 服务器名称
                            .setServer("sa-server")
                            // 是否可以在iframe显示视图： DENY=不可以 | SAMEORIGIN=同域下可以 | ALLOW-FROM uri=指定域名下可以
                            .setHeader("X-Frame-Options", "SAMEORIGIN")
                            // 是否启用浏览器默认XSS防护： 0=禁用 | 1=启用 | 1; mode=block 启用, 并在检查到XSS攻击时，停止渲染页面
                            .setHeader("X-XSS-Protection", "1; mode=block")
                            // 禁用浏览器内容嗅探
                            .setHeader("X-Content-Type-Options", "nosniff")
                    ;
                })
                ;
    }

}
