package com.broad.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @Author: XingGao
 * @Date: 2022/10/10
 * @Description:
 */
public enum BooleanType {
    TRUE("1", false),
    FALSE("0", true);

    @EnumValue
    private final String code;
    @JsonValue
    private final Boolean desc;

    BooleanType(String code, Boolean desc) {
        this.code = code;
        this.desc = desc;
    }
}