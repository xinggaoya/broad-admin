package com.broad.common.exception;

/**
 * 检查异常
 *
 * @author XingGao
 */
public class CheckedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new Checked exception.
     *
     * @param message the message
     */
    public CheckedException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Checked exception.
     *
     * @param cause the cause
     */
    public CheckedException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new Checked exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public CheckedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Checked exception.
     *
     * @param message            the message
     * @param cause              the cause
     * @param enableSuppression  the enable suppression
     * @param writableStackTrace the writable stack trace
     */
    public CheckedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
