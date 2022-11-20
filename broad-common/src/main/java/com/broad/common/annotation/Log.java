package com.broad.common.annotation;

import com.broad.common.enums.BusinessType;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: XingGao
 * @Date: 2022/07/11 22:35
 * @Description:
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    /**
     * 描述
     */
    String description() default "";

    /**
     * 方法类型 INSERT DELETE UPDATE OTHER
     */
    BusinessType businessType() default BusinessType.OTHER;
}

