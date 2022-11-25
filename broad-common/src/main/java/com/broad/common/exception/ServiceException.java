package com.broad.common.exception;

/**
 * 业务异常
 *
 * @author XingGao
 */
public final class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 错误明细，内部调试错误
     * <p>
     * 和 {@link CommonResult#getDetailMessage()} 一致的设计
     */
    private String detailMessage;

    /**
     * 空构造方法，避免反序列化问题
     */
    public ServiceException() {
    }

    /**
     * Instantiates a new Service exception.
     *
     * @param message the message
     */
    public ServiceException(String message) {
        this.message = message;
    }

    /**
     * Instantiates a new Service exception.
     *
     * @param message the message
     * @param code    the code
     */
    public ServiceException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    /**
     * Gets detail message.
     *
     * @return the detail message
     */
    public String getDetailMessage() {
        return detailMessage;
    }

    @Override
    public String getMessage() {
        return message;
    }

    /**
     * Sets detail message.
     *
     * @param detailMessage the detail message
     * @return the detail message
     */
    public ServiceException setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
        return this;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * Sets message.
     *
     * @param message the message
     * @return the message
     */
    public ServiceException setMessage(String message) {
        this.message = message;
        return this;
    }
}