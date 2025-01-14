package com.broad.web.controller.job;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.broad.common.annotation.Log;
import com.broad.common.enums.BusinessType;
import com.broad.common.utils.poi.ExcelUtil;
import com.broad.common.web.controller.BaseController;
import com.broad.common.web.entity.ResultData;
import com.broad.common.web.page.TableDataInfo;
import com.broad.job.entity.SysJobLog;
import com.broad.job.service.ISysJobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 调度日志操作处理
 *
 * @author XingGao
 */
@RestController
@RequestMapping("/job/log")
public class SysJobLogController extends BaseController {
    @Autowired
    private ISysJobLogService jobLogService;

    /**
     * 查询定时任务调度日志列表
     *
     * @param sysJobLog the sys job log
     * @return the table data info
     */
    @SaCheckPermission("monitor:job:list")
    @GetMapping("/list")
    public TableDataInfo list(SysJobLog sysJobLog) {
        startPage();
        List<SysJobLog> list = jobLogService.selectJobLogList(sysJobLog);
        return getDataTable(list);
    }

    /**
     * 导出定时任务调度日志列表
     *
     * @param response  the response
     * @param sysJobLog the sys job log
     */
    @SaCheckPermission("monitor:job:export")
    @Log(description = "任务调度日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysJobLog sysJobLog) {
        List<SysJobLog> list = jobLogService.selectJobLogList(sysJobLog);
        ExcelUtil<SysJobLog> util = new ExcelUtil<SysJobLog>(SysJobLog.class);
        util.exportExcel(response, list, "调度日志");
    }

    /**
     * 根据调度编号获取详细信息
     *
     * @param jobLogId the job log id
     * @return the info
     */
    @SaCheckPermission("monitor:job:query")
    @GetMapping(value = "/{configId}")
    public ResultData getInfo(@PathVariable Long jobLogId) {
        return ResultData.ok().setData(jobLogService.selectJobLogById(jobLogId));
    }

    /**
     * 删除定时任务调度日志
     *
     * @param jobLogIds the job log ids
     * @return the result data
     */
    @SaCheckPermission("monitor:job:remove")
    @Log(description = "定时任务调度日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{jobLogIds}")
    public ResultData remove(@PathVariable Long[] jobLogIds) {
        return toResult(jobLogService.deleteJobLogByIds(jobLogIds));
    }

    /**
     * 清空定时任务调度日志
     *
     * @return the result data
     */
    @SaCheckPermission("monitor:job:remove")
    @Log(description = "调度日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public ResultData clean() {
        jobLogService.cleanJobLog();
        return ResultData.ok();
    }
}
