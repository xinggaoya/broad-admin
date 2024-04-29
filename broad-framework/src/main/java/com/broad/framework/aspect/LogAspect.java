package com.broad.framework.aspect;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.broad.common.annotation.Log;
import com.broad.common.constant.ThreadPoolConstant;
import com.broad.common.enums.LogType;
import com.broad.common.utils.ServletUtils;
import com.broad.common.utils.StringUtils;
import com.broad.common.utils.ip.IpUtils;
import com.broad.system.entity.SysUserLog;
import com.broad.system.service.SysUserLogService;
import io.swagger.models.HttpMethod;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * 定义切面
 *
 * @Author: XingGao
 * @Date: 2022 /07/11 22:46
 * @Description:
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    @Autowired
    private SysUserLogService userLogService;
    /**
     * 处理完请求后执行
     *
     * @param joinPoint     切点
     * @param controllerLog the controller log
     * @param jsonResult    the json result
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    /**
     * 异常发生后执行
     *
     * @param joinPoint     切点
     * @param controllerLog the controller log
     * @param e             异常
     */
    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        handleLog(joinPoint, controllerLog, e, null);
    }

    /**
     * Handle log.
     *
     * @param joinPoint     the join point
     * @param controllerLog the controller log
     * @param e             the e
     * @param jsonResult    the json result
     */
    @Async(value = ThreadPoolConstant.SERVICE_EXECUTOR)
    protected void handleLog(final JoinPoint joinPoint, Log controllerLog, final Exception e, Object jsonResult) {
        try {

            HttpServletRequest response = ServletUtils.getRequest();
            // *========数据库日志=========*//
            SysUserLog operLog = new SysUserLog();
            // 请求的地址
            String ip = IpUtils.getIp(response);
            operLog.setLogIp(ip);
            operLog.setLogIpAddress(IpUtils.getIpAddress(ip));
            if (response != null) {
                operLog.setLogUrl(response.getRequestURI());
            }
            operLog.setAdminId(StpUtil.getLoginIdAsInt());

            if (e != null) {
                operLog.setLogStatus(LogType.FAILURE.getInfo());
                operLog.setExceptionInfo(StringUtils.substring(e.getMessage(), 0, 1000));
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLog.setLogMethod(className + "." + methodName + "()");
            // 设置请求方式
            operLog.setLogHttpMethod(response.getMethod());
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, controllerLog, operLog, jsonResult);
            // 保存数据库
            userLogService.save(operLog);
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("==异常日志 前置通知异常==");
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint  the join point
     * @param log        日志
     * @param operLog    操作日志
     * @param jsonResult the json result
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, Log log, SysUserLog operLog, Object jsonResult) {
        // 设置action动作
        operLog.setLogMethodType(log.businessType().toString());
        // 设置标题
        operLog.setLogDescription(log.description());
        // 保存request，参数和值
        // 获取参数的信息，传入到数据库中。
        setRequestValue(joinPoint, operLog);
        // 是否需要保存response，参数和值
        if (StringUtils.isNotNull(jsonResult)) {
            operLog.setLogResult(StringUtils.substring(JSON.toJSONString(jsonResult), 0, 2000));
        }
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param operLog 操作日志
     */
    private void setRequestValue(JoinPoint joinPoint, SysUserLog operLog) {
        String requestMethod = operLog.getLogHttpMethod();
        if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)) {
            String params = argsArrayToString(joinPoint.getArgs());
            operLog.setLogParams(StringUtils.substring(params, 0, 2000));
        } else {
            // 获取request url中的参数
            Map<?, ?> paramsMap = getRequestParam(ServletUtils.getRequest());
            if (StringUtils.isNotNull(paramsMap)) {
                operLog.setLogParams(StringUtils.substring(JSON.toJSONString(paramsMap), 0, 2000));
            }
        }
    }

    /**
     * 获取request url中的参数
     *
     * @param request request
     * @return 参数map
     */
    private Map<String, String> getRequestParam(HttpServletRequest request) {
        Map<String, String> map = new LinkedHashMap<>();
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String key = entry.getKey();
            String[] values = entry.getValue();
            String value = "";
            if (values != null && values.length > 0) {
                value = values[0];
            }
            map.put(key, value);
        }
        return map;
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray) {
        StringBuilder params = new StringBuilder();
        if (paramsArray != null) {
            for (Object o : paramsArray) {
                if (StringUtils.isNotNull(o) && !isFilterObject(o)) {
                    try {
                        String jsonObj = JSONObject.toJSONString(o);
                        params.append(jsonObj).append(" ");
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        return params.toString().trim();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象 ，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }


}
