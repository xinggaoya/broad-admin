package com.broad.common.enums;

import lombok.Getter;

/**
 * The enum Db type.
 *
 * @author: XingGao
 * @date: 2022 /12/19 10:47
 * @description: 数据源类型
 */
@Getter
public enum DbType {
    /**
     * Master db type.
     */
    MASTER("master"),
    /**
     * Slave db type.
     */
    SLAVE("slave");

    private final String value;

    DbType(String value) {
        this.value = value;
    }
}
