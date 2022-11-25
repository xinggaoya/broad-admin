package com.broad.common.exception;

/**
 * 工具类异常
 *
 * @author XingGao
 */
public class UtilException extends RuntimeException {
    private static final long serialVersionUID = 8247610319171014183L;

    /**
     * Instantiates a new Util exception.
     *
     * @param e the e
     */
    public UtilException(Throwable e) {
        super(e.getMessage(), e);
    }

    /**
     * Instantiates a new Util exception.
     *
     * @param message the message
     */
    public UtilException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Util exception.
     *
     * @param message   the message
     * @param throwable the throwable
     */
    public UtilException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
