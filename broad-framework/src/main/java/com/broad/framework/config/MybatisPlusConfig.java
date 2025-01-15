package com.broad.framework.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.broad.framework.interceptor.CryptoInterceptor;
import com.broad.framework.interceptor.DecryptInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import javax.sql.DataSource;
import org.apache.ibatis.plugin.Interceptor;

/**
 * MyBatis Plus 配置类
 *
 * @Author: XingGao
 * @Date: 2022/07/15 13:51
 * @Description: 配置 MyBatis Plus 的各种拦截器，包括分页、乐观锁、防全表更新删除等
 */
@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        // 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // 防止全表更新与删除插件
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return interceptor;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/**/*Mapper.xml"));
        sqlSessionFactory.setPlugins(mybatisPlusInterceptor());
        sqlSessionFactory.setPlugins(new Interceptor[] {
                mybatisPlusInterceptor(),
                cryptoInterceptor(),
                decryptInterceptor()
        });
        return sqlSessionFactory.getObject();
    }

    @Bean
    public CryptoInterceptor cryptoInterceptor() {
        return new CryptoInterceptor();
    }

    @Bean
    public DecryptInterceptor decryptInterceptor() {
        return new DecryptInterceptor();
    }

}
