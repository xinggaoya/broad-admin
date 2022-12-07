package com.broad.framework.web.entity;

import lombok.Data;

import java.util.List;

/**
 * @author: XingGao
 * @date: 2022/12/7
 * @description:
 */
@Data
public class TokenEntity {

    private String id;

    private Long createTime;

    private List<String> tokenSignList;
}


