package com.broad.common.exception;

/**
 * 全局异常
 *
 * @author XingGao
 */
public class GlobalException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 错误明细，内部调试错误
     * <p>
     *
     */
    private String detailMessage;

    /**
     * 空构造方法，避免反序列化问题
     */
    public GlobalException() {
    }

    /**
     * Instantiates a new Global exception.
     *
     * @param message the message
     */
    public GlobalException(String message) {
        this.message = message;
    }

    /**
     * Gets detail message.
     *
     * @return the detail message
     */
    public String getDetailMessage() {
        return detailMessage;
    }

    /**
     * Sets detail message.
     *
     * @param detailMessage the detail message
     * @return the detail message
     */
    public GlobalException setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
        return this;
    }

    @Override
    public String getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     * @return the message
     */
    public GlobalException setMessage(String message) {
        this.message = message;
        return this;
    }
}
