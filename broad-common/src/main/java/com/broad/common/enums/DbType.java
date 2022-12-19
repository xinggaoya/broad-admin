package com.broad.common.enums;

/**
 * @author: XingGao
 * @date: 2022/12/19 10:47
 * @description:
 */
public enum DbType {
    MASTER("master"),
    SLAVE("slave");

    private String value;

    DbType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
