package com.broad.web.controller.job;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.broad.common.annotation.Log;
import com.broad.common.constant.Constants;
import com.broad.common.enums.BusinessType;
import com.broad.common.exception.job.TaskException;
import com.broad.common.utils.StringUtils;
import com.broad.common.utils.poi.ExcelUtil;
import com.broad.common.web.controller.BaseController;
import com.broad.common.web.entity.ResultData;
import com.broad.common.web.page.TableDataInfo;
import com.broad.job.entity.SysJob;
import com.broad.job.service.ISysJobService;
import com.broad.job.util.CronUtils;
import com.broad.job.util.ScheduleUtils;
import com.broad.web.controller.job.model.SysJobMetaVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 调度任务信息操作处理
 *
 * @author XingGao
 */
@RestController
@RequestMapping("/job")
@Tag(name = "定时任务管理")
@Validated
@RequiredArgsConstructor
public class SysJobController extends BaseController {

    private final ISysJobService jobService;

    /**
     * 查询定时任务列表
     *
     * @param sysJob the sys job
     * @return the table data info
     */
    @SaCheckPermission("job:list")
    @GetMapping("/list")
    @Operation(summary = "查询定时任务列表")
    public TableDataInfo list(SysJob sysJob) {
        return getDataTable(jobService.selectJobList(sysJob));
    }

    /**
     * 获取前端所需的元数据（例如触发策略）
     *
     * @return 元数据
     */
    @SaCheckPermission("job:list")
    @GetMapping("/meta")
    @Operation(summary = "获取定时任务表单元数据")
    public ResultData meta() {
        return success(SysJobMetaVO.defaultMeta());
    }

    /**
     * 导出定时任务列表
     *
     * @param response the response
     * @param sysJob   the sys job
     */
    @SaCheckPermission("job:export")
    @Log(title = "定时任务", description = "导出定时任务数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysJob sysJob) {
        List<SysJob> list = jobService.list(new LambdaQueryWrapper<>(sysJob));
        ExcelUtil<SysJob> util = new ExcelUtil<>(SysJob.class);
        util.exportExcel(response, list, "定时任务");
    }

    /**
     * 获取定时任务详细信息
     *
     * @param jobId the job id
     * @return the info
     */
    @SaCheckPermission("job:list")
    @GetMapping(value = "/{jobId}")
    @Operation(summary = "获取定时任务详情")
    public ResultData getInfo(@PathVariable("jobId") Long jobId) {
        return success(jobService.selectJobById(jobId));
    }

    /**
     * 新增定时任务
     *
     * @param job the job
     * @return the result data
     * @throws SchedulerException the scheduler exception
     * @throws TaskException      the task exception
     */
    @SaCheckPermission("job:add")
    @Log(title = "定时任务", description = "新增定时任务", businessType = BusinessType.INSERT)
    @PostMapping
    public ResultData add(@Valid @RequestBody SysJob job) throws SchedulerException, TaskException {
        String errorMessage = validateJob(job, "新增");
        if (StringUtils.isNotBlank(errorMessage)) {
            return error(errorMessage);
        }
        return toResult(jobService.insertJob(job));
    }

    /**
     * 修改定时任务
     *
     * @param job the job
     * @return the result data
     * @throws SchedulerException the scheduler exception
     * @throws TaskException      the task exception
     */
    @SaCheckPermission("job:update")
    @Log(title = "定时任务", description = "修改定时任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResultData edit(@Valid @RequestBody SysJob job) throws SchedulerException, TaskException {
        String errorMessage = validateJob(job, "修改");
        if (StringUtils.isNotBlank(errorMessage)) {
            return error(errorMessage);
        }
        return toResult(jobService.updateJob(job));
    }

    /**
     * 定时任务状态修改
     *
     * @param job the job
     * @return the result data
     * @throws SchedulerException the scheduler exception
     */
    @SaCheckPermission("job:update")
    @Log(title = "定时任务", description = "修改定时任务状态", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    @Operation(summary = "修改定时任务状态")
    public ResultData changeStatus(@RequestBody SysJob job) throws SchedulerException {
        SysJob newJob = jobService.selectJobById(job.getJobId());
        newJob.setStatus(job.getStatus());
        return toResult(jobService.changeStatus(newJob));
    }

    /**
     * 定时任务立即执行一次
     *
     * @param job the job
     * @return the result data
     * @throws SchedulerException the scheduler exception
     */
    @SaCheckPermission("job:update")
    @Log(title = "定时任务", description = "执行定时任务", businessType = BusinessType.UPDATE)
    @PutMapping("/run")
    @Operation(summary = "定时任务立即执行一次")
    public ResultData run(@RequestBody SysJob job) throws SchedulerException {
        jobService.run(job);
        return success();
    }

    /**
     * 删除定时任务
     *
     * @param jobIds the job ids
     * @return the result data
     * @throws SchedulerException the scheduler exception
     */
    @SaCheckPermission("job:delete")
    @Log(title = "定时任务", description = "删除定时任务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{jobIds}")
    @Operation(summary = "批量删除定时任务")
    public ResultData remove(@PathVariable Long[] jobIds) throws SchedulerException {
        jobService.deleteJobByIds(jobIds);
        return success();
    }

    /**
     * 统一校验任务配置
     *
     * @param job         任务
     * @param actionLabel 操作文案
     * @return 违规描述
     */
    private String validateJob(SysJob job, String actionLabel) {
        String prefix = actionLabel + "任务'" + job.getJobName() + "'失败，";
        if (!CronUtils.isValid(job.getCronExpression())) {
            return prefix + "Cron表达式不正确";
        }
        String invokeTarget = job.getInvokeTarget();
        if (StringUtils.containsIgnoreCase(invokeTarget, Constants.LOOKUP_RMI)) {
            return prefix + "目标字符串不允许'rmi'调用";
        }
        if (StringUtils.containsAnyIgnoreCase(invokeTarget, Constants.LOOKUP_LDAP, Constants.LOOKUP_LDAPS)) {
            return prefix + "目标字符串不允许'ldap(s)'调用";
        }
        if (StringUtils.containsAnyIgnoreCase(invokeTarget, Constants.HTTP, Constants.HTTPS)) {
            return prefix + "目标字符串不允许'http(s)'调用";
        }
        if (StringUtils.containsAnyIgnoreCase(invokeTarget, Constants.JOB_ERROR_STR)) {
            return prefix + "目标字符串存在违规";
        }
        if (!ScheduleUtils.whiteList(invokeTarget)) {
            return prefix + "目标字符串不在白名单内";
        }
        return null;
    }
}
