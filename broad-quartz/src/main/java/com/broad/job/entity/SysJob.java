package com.broad.job.entity;

import com.broad.common.annotation.Excel;
import com.broad.common.constant.ScheduleConstants;
import com.broad.common.utils.StringUtils;
import com.broad.job.util.CronUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * 定时任务调度表 sys_job
 *
 * @author XingGao
 */
public class SysJob implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    @Excel(name = "任务序号", cellType = Excel.ColumnType.NUMERIC)
    private Long jobId;

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
     * cron执行表达式
     */
    @Excel(name = "执行表达式 ")
    private String cronExpression;

    /**
     * cron计划策略
     */
    @Excel(name = "计划策略 ", readConverterExp = "0=默认,1=立即触发执行,2=触发一次执行,3=不触发立即执行")
    private String misfirePolicy = ScheduleConstants.MISFIRE_DEFAULT;

    /**
     * 是否并发执行（0允许 1禁止）
     */
    @Excel(name = "并发执行", readConverterExp = "0=允许,1=禁止")
    private String concurrent;

    /**
     * 任务状态（0正常 1暂停）
     */
    @Excel(name = "任务状态", readConverterExp = "0=正常,1=暂停")
    private String status;

    @Excel(name = "备注")
    private String remark;

    /**
     * Gets remark.
     *
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * Sets remark.
     *
     * @param remark the remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * Gets job id.
     *
     * @return the job id
     */
    public Long getJobId() {
        return jobId;
    }

    /**
     * Sets job id.
     *
     * @param jobId the job id
     */
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    /**
     * Gets job name.
     *
     * @return the job name
     */
    @NotBlank(message = "任务名称不能为空")
    @Size(min = 0, max = 64, message = "任务名称不能超过64个字符")
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
    @NotBlank(message = "调用目标字符串不能为空")
    @Size(min = 0, max = 500, message = "调用目标字符串长度不能超过500个字符")
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
     * Gets cron expression.
     *
     * @return the cron expression
     */
    @NotBlank(message = "Cron执行表达式不能为空")
    @Size(min = 0, max = 255, message = "Cron执行表达式不能超过255个字符")
    public String getCronExpression() {
        return cronExpression;
    }

    /**
     * Sets cron expression.
     *
     * @param cronExpression the cron expression
     */
    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    /**
     * Gets next valid time.
     *
     * @return the next valid time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getNextValidTime() {
        if (StringUtils.isNotEmpty(cronExpression)) {
            return CronUtils.getNextExecution(cronExpression);
        }
        return null;
    }

    /**
     * Gets misfire policy.
     *
     * @return the misfire policy
     */
    public String getMisfirePolicy() {
        return misfirePolicy;
    }

    /**
     * Sets misfire policy.
     *
     * @param misfirePolicy the misfire policy
     */
    public void setMisfirePolicy(String misfirePolicy) {
        this.misfirePolicy = misfirePolicy;
    }

    /**
     * Gets concurrent.
     *
     * @return the concurrent
     */
    public String getConcurrent() {
        return concurrent;
    }

    /**
     * Sets concurrent.
     *
     * @param concurrent the concurrent
     */
    public void setConcurrent(String concurrent) {
        this.concurrent = concurrent;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("jobId", getJobId())
                .append("jobName", getJobName())
                .append("jobGroup", getJobGroup())
                .append("cronExpression", getCronExpression())
                .append("nextValidTime", getNextValidTime())
                .append("misfirePolicy", getMisfirePolicy())
                .append("concurrent", getConcurrent())
                .append("status", getStatus())
                .toString();
    }
}
