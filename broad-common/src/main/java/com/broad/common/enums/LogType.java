package com.broad.common.enums;

/**
 * The enum Log type.
 *
 * @Author: XingGao
 * @Date: 2022 /11/19
 * @Description:
 */
public enum LogType {
    /**
     * 成功
     */
    SUCCESS("0"),

    /**
     * 失败
     */
    FAIL("1");

    private final String info;

    LogType(String info) {
        this.info = info;
    }

    /**
     * Gets info.
     *
     * @return the info
     */
    public String getInfo() {
        return info;
    }
}
