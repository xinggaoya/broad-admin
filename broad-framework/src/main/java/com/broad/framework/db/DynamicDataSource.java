package com.broad.framework.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.Nullable;

/**
 * 动态数据源实现
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Nullable
    @Override
    protected Object determineCurrentLookupKey() {
        return DbContextHolder.getDbType();
    }

    @Override
    public void afterPropertiesSet() {
        // 必须调用父类的afterPropertiesSet()方法，否则会导致数据源初始化失败
        super.afterPropertiesSet();
    }
}
