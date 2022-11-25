package com.broad.common.exception.job;

/**
 * 计划策略异常
 *
 * @author XingGao
 */
public class TaskException extends Exception {
    private static final long serialVersionUID = 1L;

    private Code code;

    /**
     * Instantiates a new Task exception.
     *
     * @param msg  the msg
     * @param code the code
     */
    public TaskException(String msg, Code code) {
        this(msg, code, null);
    }

    /**
     * Instantiates a new Task exception.
     *
     * @param msg      the msg
     * @param code     the code
     * @param nestedEx the nested ex
     */
    public TaskException(String msg, Code code, Exception nestedEx) {
        super(msg, nestedEx);
        this.code = code;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public Code getCode() {
        return code;
    }

    /**
     * The enum Code.
     */
    public enum Code {
        /**
         * Task exists code.
         */
        TASK_EXISTS,
        /**
         * No task exists code.
         */
        NO_TASK_EXISTS,
        /**
         * Task already started code.
         */
        TASK_ALREADY_STARTED,
        /**
         * Unknown code.
         */
        UNKNOWN,
        /**
         * Config error code.
         */
        CONFIG_ERROR,
        /**
         * Task node not available code.
         */
        TASK_NODE_NOT_AVAILABLE
    }
}