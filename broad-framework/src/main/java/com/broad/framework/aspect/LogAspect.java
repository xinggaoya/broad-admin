package com.broad.framework.aspect;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson2.JSON;
import com.broad.common.utils.sign.IpAddressUtil;
import com.broad.framework.annotation.Log;
import com.broad.system.entity.SysAdminLog;
import com.broad.system.service.SysAdminLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * 定义切面
 *
 * @Author: XingGao
 * @Date: 2022/07/11 22:46
 * @Description:
 */

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Autowired
    private SysAdminLogService sysAdminLogService;

    /**
     * web层切点
     * 1. @Pointcut("execution(public * com.hyh.web.*.*(..))")  web层的所有方法
     * 2. @Pointcut("@annotation(com.hyh.annotation.Log)")      Log注解标注的方法
     */
    @Pointcut("@annotation(com.broad.framework.annotation.Log)")
    public void webLog() {
    }

    /**
     * 环绕通知
     */
    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取请求对象
        HttpServletRequest request = getRequest();
        Object result;
        try {
            long start = System.currentTimeMillis();
            result = joinPoint.proceed();
            long timeCost = System.currentTimeMillis() - start;
            // 获取Log注解
            Log logAnnotation = getAnnotation(joinPoint);
            // 写入数据库
            this.setLog(request, logAnnotation, timeCost, result, joinPoint);
        } catch (Throwable e) {
            throw new Throwable(e);
        }
        return result;
    }

    /**
     * 获取方法上的注解
     */
    private Log getAnnotation(ProceedingJoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        return method.getAnnotation(Log.class);
    }


    @Async("asyncServiceExecutor")
    public void setLog(HttpServletRequest request, Log logAnnotation, long timeCost, Object result, ProceedingJoinPoint joinPoint) {
        // TODO Auto-generated method stub
        SysAdminLog sysAdminLog = new SysAdminLog();
        sysAdminLog.setLogDescription(logAnnotation.description());
        sysAdminLog.setLogTimeCost((double) timeCost);
        sysAdminLog.setLogIpAddress(IpAddressUtil.getIpAddress(request));
        sysAdminLog.setLogHttpMethod(request.getMethod());
        sysAdminLog.setLogParams(JSON.toJSONString(getParams(joinPoint)));
        sysAdminLog.setLogResult(JSON.toJSONString(result));
        sysAdminLog.setLogUrl(request.getRequestURL().toString());
        sysAdminLog.setLogMethodType(logAnnotation.businessType().name());
        sysAdminLog.setAdminId(StpUtil.getLoginIdAsInt());
        sysAdminLogService.save(sysAdminLog);
    }


    /**
     * 获取参数 params:{"name":"天乔巴夏"}
     */
    private Object getParams(ProceedingJoinPoint joinPoint) {
        // 参数名
        String[] paramNames = getMethodSignature(joinPoint).getParameterNames();
        // 参数值
        Object[] paramValues = joinPoint.getArgs();
        // 存储参数
        Map<String, Object> params = new LinkedHashMap<>();
        for (int i = 0; i < paramNames.length; i++) {
            Object value = paramValues[i];
            // MultipartFile对象以文件名作为参数值
            if (value instanceof MultipartFile) {
                MultipartFile file = (MultipartFile) value;
                value = file.getOriginalFilename();
            }
            params.put(paramNames[i], value);
        }
        return params;
    }

    private MethodSignature getMethodSignature(ProceedingJoinPoint joinPoint) {
        return (MethodSignature) joinPoint.getSignature();
    }


    private HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }

}
