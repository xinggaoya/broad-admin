package com.broad.framework.intercept;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: XingGao
 * @Date: 2022/07/10 18:03
 * @Description:
 */
//@Configuration
//public class SaTokenConfigure implements WebMvcConfigurer {
//
//    /**
//     * 注册Sa-Token的注解拦截器，打开注解式鉴权功能
//     *
//     * @param registry 拦截器
//     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        // 注册拦截器，并排除不需要注解鉴权的接口地址
//        registry.addInterceptor(new SaInterceptor(handle -> {
//                    // 对所有的路由进行登录鉴权
//
//                    SaRouter.match("/**").check(StpUtil::checkLogin);
//                }))
//                .addPathPatterns("/**")
//                .excludePathPatterns("/favicon.ico", "/**/*.js", "/**/*.css",
//                        "/swagger-resources", "/sysAdmin/login", "/captchaImage", "/doc.html", "/test/**"
//                        , "/druid/**");
//    }
//
//}
