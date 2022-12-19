package com.broad.framework.aspect;

import com.broad.common.annotation.DataSource;
import com.broad.framework.db.DbContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author: XingGao
 * @date: 2022/12/19 10:56
 * @description: 数据源切换
 */
@Aspect
@Component
@Slf4j
@Order(-100)
public class DataSourceAspect {


    @Pointcut("@annotation(com.broad.common.annotation.DataSource)")
    public void dataSourcePointCut() {
    }

    @Before("dataSourcePointCut()")
    public void doBefore(JoinPoint point) {
        log.info("切换数据源");
        DataSource dataSource = getDataSource(point);
        // 切换数据源
        DbContextHolder.setDbType(dataSource.value());
    }


//    @After("dataSourcePointCut()")
//    public void doAfter(JoinPoint point) {
//        log.info("清除数据源");
//        DbContextHolder.clearDbType();
//    }

    private DataSource getDataSource(JoinPoint point) {
        MethodSignature sign = (MethodSignature) point.getSignature();
        Method method = sign.getMethod();
        DataSource dataSource = method.getAnnotation(DataSource.class);
        if (dataSource == null) {
            dataSource = point.getTarget().getClass().getSuperclass().getAnnotation(DataSource.class);
        }
        if (dataSource == null) {
            dataSource = point.getTarget().getClass().getAnnotation(DataSource.class);
        }
        return dataSource;
    }
}
