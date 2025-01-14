package com.broad.framework.db;

import com.broad.common.enums.DbType;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 数据源上下文持有者
 */
public class DbContextHolder {
    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 获取当前数据源类型
     */
    @Nullable
    public static String getDbType() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * 设置数据源类型
     */
    public static void setDbType(@NonNull DbType dbType) {
        CONTEXT_HOLDER.set(dbType.getValue());
    }

    /**
     * 清除当前数据源类型
     */
    public static void clearDbType() {
        CONTEXT_HOLDER.remove();
    }
}