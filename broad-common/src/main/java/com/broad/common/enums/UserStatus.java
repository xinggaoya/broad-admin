package com.broad.common.enums;

/**
 * 用户状态
 *
 * @author XingGao
 */
public enum UserStatus {
    /**
     * Ok user status.
     */
    OK(0, "正常"),
    /**
     * Disable user status.
     */
    DISABLE(1, "停用"),
    /**
     * Deleted user status.
     */
    DELETED(2, "删除");

    private final int code;
    private final String info;

    UserStatus(int code, String info) {
        this.code = code;
        this.info = info;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public int getCode() {
        return code;
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
