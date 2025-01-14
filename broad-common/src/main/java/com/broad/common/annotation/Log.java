package com.broad.common.annotation;

import com.broad.common.enums.BusinessType;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: XingGao
 * @Date: 2022 /07/11 22:35
 * @Description:
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    /**
     * 模块标题
     *
     * @return the string
     */
    String title() default "";

    /**
     * 描述
     *
     * @return the string
     */
    String description() default "";

    /**
     * 方法类型 INSERT DELETE UPDATE OTHER
     *
     * @return the business type
     */
    BusinessType businessType() default BusinessType.OTHER;

    /**
     * 操作类型
     *
     * @return the int
     */
    int operatorType() default 0;

    /**
     * 是否保存请求的参数
     *
     * @return the boolean
     */
    boolean isSaveRequestData() default true;

    /**
     * 是否保存响应的参数
     *
     * @return the boolean
     */
    boolean isSaveResponseData() default true;
}
