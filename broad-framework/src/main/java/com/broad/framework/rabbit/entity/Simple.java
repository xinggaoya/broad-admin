package com.broad.framework.rabbit.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: XingGao
 * @Date: 2022/09/30 10:51
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Simple implements Serializable {

    private String name;

    private String no;

    private int age;

    private String phone;

    private Date createTime;

}