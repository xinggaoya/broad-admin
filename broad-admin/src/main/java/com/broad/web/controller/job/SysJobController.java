package com.broad.web.controller.job;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 调度任务信息操作处理
 *
 * @author XingGao
 */
@RestController
@RequestMapping("/job")
@Tag(name = "定时任务管理")
public class SysJobController extends BaseController {

    @Autowired
    private ISysJobService jobService;

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
        Page<SysJob> page = startPage();
        List<SysJob> list = jobService.selectJobList(sysJob);
        page.setRecords(list);
        page.setTotal(list.size());
        return getDataTable(page);
    }

    /**
     * 导出定时任务列表
     *
     * @param response the response
     * @param sysJob   the sys job
     */
    @SaCheckPermission("job:export")
    @Log(description = "导出定时任务数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysJob sysJob) {
        List<SysJob> list = jobService.selectJobList(sysJob);
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
    @Log(description = "新增定时任务", businessType = BusinessType.INSERT)
    @PostMapping
    public ResultData add(@RequestBody SysJob job) throws SchedulerException, TaskException {
        if (!CronUtils.isValid(job.getCronExpression())) {
            return error("新增任务'" + job.getJobName() + "'失败，Cron表达式不正确");
        } else if (StringUtils.containsIgnoreCase(job.getInvokeTarget(), Constants.LOOKUP_RMI)) {
            return error("新增任务'" + job.getJobName() + "'失败，目标字符串不允许'rmi'调用");
        } else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(),
                new String[] { Constants.LOOKUP_LDAP, Constants.LOOKUP_LDAPS })) {
            return error("新增任务'" + job.getJobName() + "'失败，目标字符串不允许'ldap(s)'调用");
        } else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(),
                new String[] { Constants.HTTP, Constants.HTTPS })) {
            return error("新增任务'" + job.getJobName() + "'失败，目标字符串不允许'http(s)'调用");
        } else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), Constants.JOB_ERROR_STR)) {
            return error("新增任务'" + job.getJobName() + "'失败，目标字符串存在违规");
        } else if (!ScheduleUtils.whiteList(job.getInvokeTarget())) {
            return error("新增任务'" + job.getJobName() + "'失败，目标字符串不在白名单内");
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
    @Log(description = "修改定时任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResultData edit(@RequestBody SysJob job) throws SchedulerException, TaskException {
        if (!CronUtils.isValid(job.getCronExpression())) {
            return error("修改任务'" + job.getJobName() + "'失败，Cron表达式不正确");
        } else if (StringUtils.containsIgnoreCase(job.getInvokeTarget(), Constants.LOOKUP_RMI)) {
            return error("修改任务'" + job.getJobName() + "'失败，目标字符串不允许'rmi'调用");
        } else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(),
                new String[] { Constants.LOOKUP_LDAP, Constants.LOOKUP_LDAPS })) {
            return error("修改任务'" + job.getJobName() + "'失败，目标字符串不允许'ldap(s)'调用");
        } else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(),
                new String[] { Constants.HTTP, Constants.HTTPS })) {
            return error("修改任务'" + job.getJobName() + "'失败，目标字符串不允许'http(s)'调用");
        } else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), Constants.JOB_ERROR_STR)) {
            return error("修改任务'" + job.getJobName() + "'失败，目标字符串存在违规");
        } else if (!ScheduleUtils.whiteList(job.getInvokeTarget())) {
            return error("修改任务'" + job.getJobName() + "'失败，目标字符串不在白名单内");
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
    @Log(description = "修改定时任务状态", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
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
    @Log(description = "执行定时任务", businessType = BusinessType.UPDATE)
    @PutMapping("/run")
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
    @Log(description = "删除定时任务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{jobIds}")
    public ResultData remove(@PathVariable Long[] jobIds) throws SchedulerException {
        jobService.deleteJobByIds(jobIds);
        return success();
    }
}
