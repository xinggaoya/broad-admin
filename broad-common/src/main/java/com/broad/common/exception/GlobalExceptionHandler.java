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
        return SaResult.error("无权限,请先登录");
    }
}
