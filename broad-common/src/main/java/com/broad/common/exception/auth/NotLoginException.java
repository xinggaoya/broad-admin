package com.broad.common.exception.auth;

/**
 * 未能通过的登录认证异常
 *
 * @author XingGao
 */
public class NotLoginException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new Not login exception.
     *
     * @param message the message
     */
    public NotLoginException(String message) {
        super(message);
    }
}
