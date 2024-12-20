package com.broad.job.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.broad.common.annotation.Excel;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

/**
 * 定时任务调度日志表 sys_job_log
 *
 * @author XingGao
 */
@Data
public class SysJobLog implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Excel(name = "日志序号")
    @TableId(value = "job_log_id", type = IdType.AUTO)
    private Long jobLogId;

    /**
     * 任务名称
     */
    @Excel(name = "任务名称")
    private String jobName;

    /**
     * 任务组名
     */
    @Excel(name = "任务组名")
    private String jobGroup;

    /**
     * 调用目标字符串
     */
    @Excel(name = "调用目标字符串")
    private String invokeTarget;

    /**
     * 日志信息
     */
    @Excel(name = "日志信息")
    private String jobMessage;

    /**
     * 执行状态（0正常 1失败）
     */
    @Excel(name = "执行状态", readConverterExp = "0=正常,1=失败")
    private String status;

    /**
     * 异常信息
     */
    @Excel(name = "异常信息")
    private String exceptionInfo;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 停止时间
     */
    private Date stopTime;
}
