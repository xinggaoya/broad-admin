package com.broad.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @Author: XingGao
 * @Date: 2022/10/10
 * @Description:
 */
@Getter
public enum BooleanEnum implements IEnum<String> {
    ONE("1", false),
    TRUE("0", true);
    private String value;
    @JsonValue
    private Boolean desc;

    BooleanEnum(final String value, final Boolean desc) {
        this.value = value;
        this.desc = desc;
    }
}
