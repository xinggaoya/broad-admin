package com.broad.framework.interceptor;

import cn.dev33.satoken.secure.SaSecureUtil;
import com.broad.common.annotation.Crypto;
import com.broad.common.config.BroadConfig;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

/**
 * 出库数据解密拦截器
 */
@Intercepts({
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})
})
public class DecryptInterceptor implements Interceptor {

    @Autowired
    private BroadConfig broadConfig;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result = invocation.proceed();
        if (result instanceof List) {
            List resultList = (List) result;
            for (Object obj : resultList) {
                if (obj != null) {
                    Class<?> objClass = obj.getClass();
                    Field[] declaredFields = objClass.getDeclaredFields();
                    for (Field field : declaredFields) {
                        Crypto crypto = field.getAnnotation(Crypto.class);
                        if (crypto != null) {
                            field.setAccessible(true);
                            String encryptedValue = (String) field.get(obj);
                            if (encryptedValue != null) {
                                // 在这里执行解密操作，然后设置回实体类
                                String decryptedValue = decrypt(encryptedValue);
                                field.set(obj, decryptedValue);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private String decrypt(String encryptedValue) {
        // 加密数据长度小于16位，则不进行解密 节省性能
        if (encryptedValue.length() < 16) {
            return encryptedValue;
        }
        // 尝试解密
        try {
            return SaSecureUtil.rsaDecryptByPrivate(broadConfig.getPrivateKey(), encryptedValue);
        } catch (Exception e) {
            return encryptedValue;
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