package com.broad.common.annotation;

import com.broad.common.enums.DbType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author: XingGao
 * @date: 2022/12/19 10:58
 * @description:
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource {
    DbType value() default DbType.MASTER;
}
