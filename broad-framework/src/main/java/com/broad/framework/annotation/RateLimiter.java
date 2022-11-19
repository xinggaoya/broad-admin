package com.broad.framework.annotation;

import com.broad.common.enums.LimitType;

import java.lang.annotation.*;

/**
 * @Author: XingGao
 * @Date: 2022/11/17
 * @Description: 限流注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface RateLimiter {
    /**
     * 限流的key
     *
     * @return key
     */
    String key() default "";

    /**
     * 限流的前缀
     *
     * @return 前缀
     */
    String prefix() default "";

    /**
     * 时间段，单位秒，默认60秒
     *
     * @return 时间段
     */
    int period() default 60;

    /**
     * 限制访问次数，默认值5
     *
     * @return 限制访问次数
     */
    int count() default 5;

    /**
     * 限流类型
     *
     * @return 限流类型
     */
    LimitType limitType() default LimitType.CUSTOMER;

    /**
     * 限流提示信息
     *
     * @return 限流提示信息
     */
    String msg() default "请求过于频繁，请稍后再试";
}