package com.broad.framework.aspect;

import com.broad.common.annotation.DataSource;
import com.broad.framework.db.DbContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

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

    @Around("dataSourcePointCut()")
    public Object doBefore(ProceedingJoinPoint point) {
        DataSource dataSource = getDataSource(point);
        // 切换数据源
        DbContextHolder.setDbType(dataSource.value());
        try {
            return point.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            DbContextHolder.clearDbType();
        }
    }

    /**
     * 获取需要切换的数据源
     */
    public DataSource getDataSource(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        DataSource dataSource = AnnotationUtils.findAnnotation(signature.getMethod(), DataSource.class);
        if (Objects.nonNull(dataSource)) {
            return dataSource;
        }

        return AnnotationUtils.findAnnotation(signature.getDeclaringType(), DataSource.class);
    }
}
