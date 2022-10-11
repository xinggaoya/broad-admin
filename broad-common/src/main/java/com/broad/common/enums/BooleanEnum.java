package com.broad.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @Author: XingGao
 * @Date: 2022/10/10
 * @Description:
 */
public enum BooleanEnum implements IEnum<String> {
    ONE("1", "false"),
    TRUE("0", "true");
    @JsonValue
    private String code;
    private String desc;

    BooleanEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getValue() {
        return this.code;
    }
}
