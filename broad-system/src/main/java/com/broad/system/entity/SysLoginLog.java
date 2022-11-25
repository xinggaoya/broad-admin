package com.broad.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * (SysLoginLog)表实体类
 *
 * @author XingGao
 * @since 2022 -11-22 20:00:01
 */
@Data
@TableName("sys_login_log")
public class SysLoginLog implements Serializable {

    private static final long serialVersionUID = -94255216124708998L;

    /**
     * ID
     */
    @TableId(value = "login_id", type = IdType.AUTO)
    private Integer loginId;

    /**
     * 操作系统
     */
    @TableField(value = "operating_system")
    private String operatingSystem;
    /**
     * 登录平台
     */
    @TableField(value = "browser")
    private String browser;
    /**
     * 平台版本
     */
    @TableField(value = "client_type")
    private String clientType;
    /**
     * 登录IP
     */
    @TableField(value = "login_ip")
    private String loginIp;

    /**
     * 登录状态
     */
    @TableField(value = "login_status")
    private String loginStatus;

    /**
     * 信息
     */
    @TableField(value = "message")
    private String message;
    /**
     * 登录地址
     */
    @TableField(value = "login_address")
    private String loginAddress;
    /**
     * 登录用户
     */
    @TableField(value = "user_id")
    private Integer userId;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

}

