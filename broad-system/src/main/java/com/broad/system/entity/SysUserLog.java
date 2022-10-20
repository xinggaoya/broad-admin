package com.broad.system.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.util.Date;

/**
 * 管理员日志表(SysUserLog)实体类
 *
 * @author XingGao
 * @since 2022 -07-11 22:32:40
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("sys_user_log")
public class SysUserLog extends Model<SysUserLog> {
    /**
     * ID
     */
    @TableId(value = "log_id", type = IdType.AUTO)
    private Long logId;

    /**
     * 操作描述
     */
    @TableField(value = "log_description")
    private String logDescription;

    /**
     * 消耗时间
     */
    @TableField(value = "log_time_cost")
    private Double logTimeCost;

    /**
     * URL
     */
    @TableField(value = "log_url")
    private String logUrl;

    /**
     * 请求类型
     */
    @TableField(value = "log_http_method")
    private String logHttpMethod;

    /**
     * IP
     */
    @TableField(value = "log_ip")
    private String logIp;

    /**
     * IP地址
     */
    @TableField(value = "log_ip_address")
    private String logIpAddress;

    /**
     * 请求参数
     */
    @TableField(value = "log_params")
    private String logParams;

    /**
     * 返回参数
     */
    @TableField(value = "log_result")
    private String logResult;

    /**
     * 操作类型
     */
    @TableField(value = "log_method_type")
    private String logMethodType;

    /**
     * 操作时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 操作管理员
     */
    @TableField(value = "admin_id")
    private Integer adminId;

    @TableField(exist = false)
    private String adminName;

}

