package com.broad.common.utils;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.StringWriter;

/**
 * 错误信息处理类。
 *
 * @author XingGao
 */
public class ExceptionUtil {
    /**
     * 获取exception的详细错误信息。
     *
     * @param e the e
     * @return the exception message
     */
    public static String getExceptionMessage(Throwable e) {
        StringWriter sw = new StringWriter();
        return sw.toString();
    }

    /**
     * Gets root error message.
     *
     * @param e the e
     * @return the root error message
     */
    public static String getRootErrorMessage(Exception e) {
        Throwable root = ExceptionUtils.getRootCause(e);
        root = (root == null ? e : root);
        if (root == null) {
            return "";
        }
        String msg = root.getMessage();
        if (msg == null) {
            return "null";
        }
        return StringUtils.defaultString(msg);
    }
}
