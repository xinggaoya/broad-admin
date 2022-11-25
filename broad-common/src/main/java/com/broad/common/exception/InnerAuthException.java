package com.broad.common.exception;

/**
 * 内部认证异常
 *
 * @author XingGao
 */
public class InnerAuthException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new Inner auth exception.
     *
     * @param message the message
     */
    public InnerAuthException(String message) {
        super(message);
    }
}
