package com.broad.common.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: JinJun
 * @Date: 2022/07/10 21:52
 * @Description:
 */

@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 全局异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler
    public SaResult handlerException(Exception e) {
        e.printStackTrace();
        return SaResult.error(e.getMessage());
    }

    /**
     * 未登录异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = NotLoginException.class)
    public SaResult handlerNotLoginException(NotLoginException e) {
        // 判断场景值，定制化异常信息
        String message = "";
        if (e.getType().equals(NotLoginException.NOT_TOKEN)) {
            message = "无权限，请登录后操作";
        } else if (e.getType().equals(NotLoginException.INVALID_TOKEN)) {
            message = "登录无效，请重新登录";
        } else if (e.getType().equals(NotLoginException.TOKEN_TIMEOUT)) {
            message = "登录已过期，请重新登录";
        } else if (e.getType().equals(NotLoginException.BE_REPLACED)) {
            message = "账户已在其他地方登录，请重新登录";
        } else if (e.getType().equals(NotLoginException.KICK_OUT)) {
            message = "账户已被踢下线，请联系管理员";
        } else {
            message = "当前会话未登录";
        }
        return SaResult.error(message);
    }

    /**
     * 服务异常处理
     *
     * @param e
     * @return
     */

    @ExceptionHandler(value = ServiceException.class)
    public SaResult handlerServiceException(ServiceException e) {
        return SaResult.error(e.getMessage());
    }
}
