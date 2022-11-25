package com.broad.job.entity;


import com.broad.common.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 定时任务调度日志表 sys_job_log
 *
 * @author XingGao
 */
public class SysJobLog {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Excel(name = "日志序号")
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

    /**
     * Gets job log id.
     *
     * @return the job log id
     */
    public Long getJobLogId() {
        return jobLogId;
    }

    /**
     * Sets job log id.
     *
     * @param jobLogId the job log id
     */
    public void setJobLogId(Long jobLogId) {
        this.jobLogId = jobLogId;
    }

    /**
     * Gets create time.
     *
     * @return the create time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * Sets create time.
     *
     * @param createTime the create time
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * Gets job name.
     *
     * @return the job name
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * Sets job name.
     *
     * @param jobName the job name
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    /**
     * Gets job group.
     *
     * @return the job group
     */
    public String getJobGroup() {
        return jobGroup;
    }

    /**
     * Sets job group.
     *
     * @param jobGroup the job group
     */
    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    /**
     * Gets invoke target.
     *
     * @return the invoke target
     */
    public String getInvokeTarget() {
        return invokeTarget;
    }

    /**
     * Sets invoke target.
     *
     * @param invokeTarget the invoke target
     */
    public void setInvokeTarget(String invokeTarget) {
        this.invokeTarget = invokeTarget;
    }

    /**
     * Gets job message.
     *
     * @return the job message
     */
    public String getJobMessage() {
        return jobMessage;
    }

    /**
     * Sets job message.
     *
     * @param jobMessage the job message
     */
    public void setJobMessage(String jobMessage) {
        this.jobMessage = jobMessage;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets exception info.
     *
     * @return the exception info
     */
    public String getExceptionInfo() {
        return exceptionInfo;
    }

    /**
     * Sets exception info.
     *
     * @param exceptionInfo the exception info
     */
    public void setExceptionInfo(String exceptionInfo) {
        this.exceptionInfo = exceptionInfo;
    }

    /**
     * Gets start time.
     *
     * @return the start time
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * Sets start time.
     *
     * @param startTime the start time
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets stop time.
     *
     * @return the stop time
     */
    public Date getStopTime() {
        return stopTime;
    }

    /**
     * Sets stop time.
     *
     * @param stopTime the stop time
     */
    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("jobLogId", getJobLogId())
                .append("jobName", getJobName())
                .append("jobGroup", getJobGroup())
                .append("jobMessage", getJobMessage())
                .append("status", getStatus())
                .append("exceptionInfo", getExceptionInfo())
                .append("startTime", getStartTime())
                .append("stopTime", getStopTime())
                .toString();
    }
}
