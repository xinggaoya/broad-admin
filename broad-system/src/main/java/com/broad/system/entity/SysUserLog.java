package com.broad.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.broad.common.annotation.Excel;
import lombok.*;

import java.util.Date;
import java.util.List;

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
    @Excel(name = "操作描述")
    private String logDescription;

    /**
     * 方法名
     */
    @TableField(value = "log_method")
    @Excel(name = "方法名")
    private String logMethod;

    /**
     * URL
     */
    @TableField(value = "log_url")
    @Excel(name = "请求URL")
    private String logUrl;

    /**
     * 请求类型
     */
    @TableField(value = "log_http_method")
    @Excel(name = "请求类型")
    private String logHttpMethod;

    /**
     * IP
     */
    @TableField(value = "log_ip")
    @Excel(name = "IP")
    private String logIp;

    /**
     * IP地址
     */
    @TableField(value = "log_ip_address")
    @Excel(name = "IP地址")
    private String logIpAddress;
    /**
     * 状态
     */
    @TableField(value = "log_status")
    @Excel(name = "状态")
    private String logStatus;

    /**
     * 异常消息
     */
    @TableField(value = "exception_info")
    @Excel(name = "异常消息")
    private String exceptionInfo;

    /**
     * 请求参数
     */
    @TableField(value = "log_params")
    @Excel(name = "请求参数")
    private String logParams;

    /**
     * 返回参数
     */
    @TableField(value = "log_result")
    @Excel(name = "返回参数")
    private String logResult;

    /**
     * 操作类型
     */
    @TableField(value = "log_method_type")
    @Excel(name = "操作类型")
    private String logMethodType;

    /**
     * 操作时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @Excel(name = "操作时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 操作管理员
     */
    @TableField(value = "admin_id")
    private Integer adminId;

    @TableField(exist = false)
    @Excel(name = "操作管理员")
    private String adminName;

    @TableField(exist = false)
    private String operatingTime;

    @TableField(exist = false)
    private List<Date> operatingTimeList;

}

