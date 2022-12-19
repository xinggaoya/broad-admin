package com.broad.framework.db;

import com.broad.common.enums.DbType;

/**
 * @author: XingGao
 * @date: 2022/12/19 10:46
 * @description:
 */
public class DbContextHolder {

    private static final ThreadLocal<Object> contextHolder = new ThreadLocal<>();

    /**
     * 取得当前数据源
     *
     * @return
     */
    public static String getDbType() {
        return (String) contextHolder.get();
    }

    /**
     * 设置数据源
     *
     * @param dbTypeEnum
     */
    public static void setDbType(DbType dbTypeEnum) {
        contextHolder.set(dbTypeEnum.getValue());
    }

    /**
     * 清除上下文数据
     */
    public static void clearDbType() {
        contextHolder.remove();
    }
}