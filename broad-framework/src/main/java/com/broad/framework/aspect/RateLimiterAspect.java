package com.broad.framework.aspect;

import com.broad.common.exception.ServiceException;
import com.broad.common.utils.ServletUtils;
import com.broad.common.utils.StringUtils;
import com.broad.common.utils.ip.IpUtils;
import com.broad.framework.annotation.RateLimiter;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: XingGao
 * @Date: 2022/11/17
 * @Description:
 */
@Aspect
@Component
@Slf4j
public class RateLimiterAspect {

    /**
     * 不同的接口，不同的流量控制
     * map的key为 Limiter.key
     */
    private final Map<String, com.google.common.util.concurrent.RateLimiter> limitMap = Maps.newConcurrentMap();

    @Around("@annotation(com.broad.framework.annotation.RateLimiter)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //拿limit的注解
        RateLimiter limit = method.getAnnotation(RateLimiter.class);
        if (limit != null) {
            // key作用：不同的接口，不同的流量控制
            String key = limit.key();
            // 如果key为空，则使用方法名
            if (StringUtils.isBlank(key)) {
                key = method.getName();
            }
            if (StringUtils.isNotBlank(limit.prefix())) {
                key = limit.prefix().concat(key);
            }
            com.google.common.util.concurrent.RateLimiter rateLimiter;
            // 验证缓存是否有命中key
            if (!limitMap.containsKey(key)) {
                // 创建令牌桶
                rateLimiter = com.google.common.util.concurrent.RateLimiter.create(limit.count());
                limitMap.put(key, rateLimiter);
            }
            rateLimiter = limitMap.get(key);
            boolean acquire = rateLimiter.tryAcquire(limit.period(), TimeUnit.SECONDS);
            if (!acquire) {
                log.warn("{} 异常连接，异常信息：{}"
                        , IpUtils.getIp(ServletUtils.getRequest())
                        , limit.msg());
                throw new ServiceException(limit.msg());
            }
        }
        return joinPoint.proceed();
    }

}
