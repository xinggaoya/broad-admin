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
    /**
     * 0
     */
    FALSE("1", false),
    /**
     * 1
     */
    TRUE("0", true);
    /**
     * 数据库存储值
     */
    @EnumValue
    private final String value;
    /**
     * 描述
     */
    @JsonValue
    private final Boolean desc;

    BooleanEnum(String value, Boolean desc) {
        this.value = value;
        this.desc = desc;
    }
}
