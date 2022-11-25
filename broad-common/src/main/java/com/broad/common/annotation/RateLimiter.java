package com.broad.common.annotation;

import com.broad.common.constant.Constants;
import com.broad.common.enums.LimitType;

import java.lang.annotation.*;

/**
 * The interface Rate limiter.
 *
 * @Author: XingGao
 * @Date: 2022 /11/17
 * @Description: 限流注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface RateLimiter {
    /**
     * 限流key
     *
     * @return the string
     */
    String key() default Constants.RATE_LIMIT_KEY;

    /**
     * 限流时间,单位秒
     *
     * @return the int
     */
    int time() default 60;

    /**
     * 限流次数
     *
     * @return the int
     */
    int count() default 100;

    /**
     * 限流类型
     *
     * @return the limit type
     */
    LimitType limitType() default LimitType.DEFAULT;
}