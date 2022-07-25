package com.broad.common.exception;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: XingGao
 * @Date: 2022/07/11 22:03
 * @Description:
 */
public class ServiceException extends RuntimeException {

    public ServiceException(String message) {
        super(message);
    }

}
