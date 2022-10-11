package com.broad.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @Author: XingGao
 * @Date: 2022/10/10
 * @Description:
 */
@Getter
public enum BooleanEnum {
    ONE("1", false),
    TRUE("0", true);
    @EnumValue
    private final String code;
    @JsonValue
    private final Boolean desc;

    BooleanEnum(String code, Boolean desc) {
        this.code = code;
        this.desc = desc;
    }
}
