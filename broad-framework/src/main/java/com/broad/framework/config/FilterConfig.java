package com.broad.framework.config;

import com.broad.common.utils.StringUtils;
import com.broad.framework.xss.XssFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: XingGao
 * @date: 2022/12/21 15:27
 * @description: 过滤器配置
 */
@Configuration
@Order(-1)
public class FilterConfig {

    /**
     * 排除的url
     */
    @Value("${xss.excludes}")
    public List<String> excludes = new ArrayList<>();
    /**
     * 是否开启xss过滤
     */
    @Value("${xss.enabled}")
    private boolean enabled;
    /**
     * 是否过滤富文本内容
     */
    @Value("${xss.isIncludeRichText}")
    private boolean IS_INCLUDE_RICH_TEXT;

    /**
     * xss过滤拦截器
     */
    @Bean
    public FilterRegistrationBean xssFilterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new XssFilter());
        filterRegistrationBean.setOrder(Integer.MAX_VALUE - 1);
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.addUrlPatterns("/*");
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("enabled", String.valueOf(enabled));
        //excludes用于配置不需要参数过滤的请求url
        initParameters.put("excludes", StringUtils.join(excludes, ","));
        //isIncludeRichText主要用于设置富文本内容是否需要过滤
        initParameters.put("isIncludeRichText", "true");
        filterRegistrationBean.setInitParameters(initParameters);
        return filterRegistrationBean;
    }
}
