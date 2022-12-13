package com.broad.framework.exception;

import cn.dev33.satoken.exception.DisableServiceException;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import com.broad.common.constant.HttpStatus;
import com.broad.common.exception.ServiceException;
import com.broad.common.exception.user.UserException;
import com.broad.common.web.entity.ResultData;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: XingGao
 * @Date: 2022 /07/10 21:52
 * @Description:
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 全局异常处理
     *
     * @param e the e
     * @return result data
     */
    @ExceptionHandler
    public ResultData handlerException(Exception e) {
        e.printStackTrace();
        return ResultData.error("系统异常,请联系管理员");
    }

    /**
     * 未登录异常处理
     *
     * @param e the e
     * @return result data
     */
    @ExceptionHandler(value = NotLoginException.class)
    public ResultData handlerNotLoginException(NotLoginException e) {
        // 判断场景值，定制化异常信息
        String message;
        if (e.getType().equals(NotLoginException.NOT_TOKEN)) {
            message = "认证失败，请登录后操作";
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
        return new ResultData(HttpStatus.UNAUTHORIZED, message);
    }

    /**
     * 封禁异常处理
     *
     * @param e the e
     * @return result data
     */
    @ExceptionHandler(value = DisableServiceException.class)
    public ResultData handleSaTokenException(DisableServiceException e) {
        return new ResultData(HttpStatus.UNAUTHORIZED, "此账号已被禁用，请联系管理员");
    }

    /**
     * 用户异常处理
     *
     * @param e the e
     * @return result data
     */
    @ExceptionHandler(value = UserException.class)
    public ResultData handlerUserException(UserException e) {
        return new ResultData(HttpStatus.ERROR, e.getCode());
    }

    /**
     * 未授权异常处理
     *
     * @param e the e
     * @return result data
     */
    @ExceptionHandler(value = NotPermissionException.class)
    public ResultData handlerNotPermissionException(NotPermissionException e) {
        // 判断场景值，定制化异常信息
        return new ResultData(HttpStatus.FORBIDDEN, "无该操作权限，请联系管理员");
    }

    /**
     * 服务异常处理
     *
     * @param e the e
     * @return result data
     */
    @ExceptionHandler(value = ServiceException.class)
    public ResultData handlerServiceException(ServiceException e) {
        return ResultData.error(e.getMessage());
    }

    /**
     * 参数校验异常处理
     *
     * @param e the e
     * @return result data
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResultData handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResultData.error(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

}
