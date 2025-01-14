package com.broad.framework.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.broad.common.enums.DbType;
import com.broad.framework.db.DynamicDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 多数据源配置
 * 使用Druid数据源
 */
@Configuration(proxyBeanMethods = false)
public class DruidConfig {

    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource.druid.master")
    public DataSourceProperties masterDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.slave")
    public DataSourceProperties slaveDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "masterDataSource")
    public DataSource masterDataSource(DataSourceProperties masterDataSourceProperties) {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        dataSource.setUrl(masterDataSourceProperties.getUrl());
        dataSource.setUsername(masterDataSourceProperties.getUsername());
        dataSource.setPassword(masterDataSourceProperties.getPassword());
        dataSource.setDriverClassName(masterDataSourceProperties.getDriverClassName());
        return dataSource;
    }

    @Bean(name = "slaveDataSource")
    public DataSource slaveDataSource(DataSourceProperties slaveDataSourceProperties) {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        dataSource.setUrl(slaveDataSourceProperties.getUrl());
        dataSource.setUsername(slaveDataSourceProperties.getUsername());
        dataSource.setPassword(slaveDataSourceProperties.getPassword());
        dataSource.setDriverClassName(slaveDataSourceProperties.getDriverClassName());
        return dataSource;
    }

    @Bean(name = "dynamicDataSource")
    @Primary
    public DynamicDataSource dynamicDataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
            @Qualifier("slaveDataSource") DataSource slaveDataSource) {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>(2);
        targetDataSources.put(DbType.MASTER.getValue(), masterDataSource);
        targetDataSources.put(DbType.SLAVE.getValue(), slaveDataSource);
        dynamicDataSource.setDefaultTargetDataSource(masterDataSource);
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.afterPropertiesSet();
        return dynamicDataSource;
    }
}
