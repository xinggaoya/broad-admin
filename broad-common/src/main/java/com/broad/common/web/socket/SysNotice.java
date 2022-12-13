package com.broad.common.web.socket;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: XingGao
 * @date: 2022/12/9
 * @description:
 */
@Data
public class SysNotice implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息类型
     */
    private String type;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息描述
     */
    private String description;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息时间
     */
    private Date meta;

    /**
     * 消息来源
     */
    private Boolean confirm;
}
