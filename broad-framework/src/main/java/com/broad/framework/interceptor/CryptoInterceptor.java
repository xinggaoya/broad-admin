package com.broad.framework.interceptor;

import cn.dev33.satoken.secure.SaSecureUtil;
import com.broad.common.annotation.Crypto;
import com.broad.common.config.BroadConfig;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Properties;

/**
 * @author: XingGao
 * @date: 2024/4/25
 * @description: 入库前加密数据
 */
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class CryptoInterceptor implements Interceptor {

    @Autowired
    private BroadConfig broadConfig;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        Object parameter = args[1];
        if (parameter == null) {
            return invocation.proceed();
        }

        // 处理集合类型的参数
        if (parameter instanceof Collection<?>) {
            Collection<?> collection = (Collection<?>) parameter;
            for (Object item : collection) {
                handleEncryption(item);
            }
        } else {
            handleEncryption(parameter);
        }
        return invocation.proceed();
    }

    private void handleEncryption(Object parameter) throws IllegalAccessException {
        if (parameter == null) {
            return;
        }
        Class<?> parameterClass = parameter.getClass();
        Field[] declaredFields = parameterClass.getDeclaredFields();
        for (Field field : declaredFields) {
            Crypto crypto = field.getAnnotation(Crypto.class);
            if (crypto != null) {
                field.setAccessible(true);
                Object value = field.get(parameter);
                if (value instanceof String) {
                    String strValue = (String) value;
                    if (strValue != null && !strValue.isEmpty()) {
                        // 在这里执行加密操作，然后设置回实体类
                        String encryptedValue = encrypt(strValue);
                        field.set(parameter, encryptedValue);
                    }
                }
            }
        }
    }

    private String encrypt(String value) {
        try {
            return SaSecureUtil.rsaEncryptByPublic(broadConfig.getPublicKey(), value);
        } catch (Exception e) {
            return value;
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
