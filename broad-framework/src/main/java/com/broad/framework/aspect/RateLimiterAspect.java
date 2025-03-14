package com.broad.framework.aspect;

import com.broad.common.annotation.RateLimiter;
import com.broad.common.enums.LimitType;
import com.broad.common.exception.ServiceException;
import com.broad.common.utils.ServletUtils;
import com.broad.common.utils.StringUtils;
import com.broad.common.utils.ip.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * 限流切面
 *
 * @Author: XingGao
 * @Date: 2022/11/17
 * @Description:
 */
@Aspect
@Component
@Slf4j
public class RateLimiterAspect {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisScript<Long> limitScript;

    /**
     * Do before.
     *
     * @param point       the point
     * @param rateLimiter the rate limiter
     */
    @Before("@annotation(rateLimiter)")
    public void doBefore(JoinPoint point, RateLimiter rateLimiter) {
        String key = rateLimiter.key();
        int time = rateLimiter.time();
        int count = rateLimiter.count();

        String combineKey = getCombineKey(rateLimiter, point);
        List<String> keys = Collections.singletonList(combineKey);
        try {
            Long number = redisTemplate.execute(limitScript, keys, count, time);
            if (StringUtils.isNull(number) || number.intValue() > count) {
                throw new ServiceException("访问过于频繁，请稍候再试");
            }
            log.info("限制请求'{}',当前请求'{}',缓存key'{}'", count, number.intValue(), key);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("服务器限流异常，请稍候再试");
        }
    }

    /**
     * Gets combine key.
     *
     * @param rateLimiter the rate limiter
     * @param point       the point
     * @return the combine key
     */
    public String getCombineKey(RateLimiter rateLimiter, JoinPoint point) {
        StringBuilder stringBuffer = new StringBuilder(rateLimiter.key());
        if (rateLimiter.limitType() == LimitType.IP) {
            stringBuffer.append(IpUtils.getIpAddr(ServletUtils.getRequest())).append("-");
        }
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Class<?> targetClass = method.getDeclaringClass();
        stringBuffer.append(targetClass.getName()).append("-").append(method.getName());
        return stringBuffer.toString();
    }
}
