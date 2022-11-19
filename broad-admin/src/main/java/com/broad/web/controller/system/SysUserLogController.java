package com.broad.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.broad.common.enums.BusinessType;
import com.broad.common.utils.poi.ExcelUtil;
import com.broad.framework.annotation.Log;
import com.broad.framework.web.controller.BaseController;
import com.broad.framework.web.entity.ResultData;
import com.broad.framework.web.page.TableDataInfo;
import com.broad.system.entity.SysUserLog;
import com.broad.system.service.SysUserLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;

/**
 * 管理员日志表(SysUserLog)表控制层
 *
 * @author XingGao
 * @since 2022 -07-16 21:10:59
 */
@RestController
@RequestMapping("sysAdminLog")
public class SysUserLogController extends BaseController {

    /**
     * 服务对象
     */
    @Autowired
    private SysUserLogService sysAdminLogService;

    /**
     * 分页查询所有数据
     *
     * @param sysAdminLog 查询实体
     * @return 所有数据 table data info
     */
    @GetMapping
    @SaCheckPermission("monitor:log:list")
    public TableDataInfo selectAll(SysUserLog sysAdminLog) {
        startPage();
        return getDataTable(this.sysAdminLogService.selectAll(sysAdminLog));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据 result data
     */
    @GetMapping("{id}")
    @SaCheckPermission("monitor:log:list")
    public ResultData selectOne(@PathVariable Serializable id) {
        return ResultData.success(this.sysAdminLogService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param sysAdminLog 实体对象
     * @return 新增结果 result data
     */
    @PostMapping
    @SaCheckLogin
    public void insert(@RequestBody SysUserLog sysAdminLog) {
        this.sysAdminLogService.save(sysAdminLog);
    }

    /**
     * 修改数据
     *
     * @param sysAdminLog 实体对象
     * @return 修改结果 result data
     */
    @PutMapping
    @SaCheckPermission("monitor:log:update")
    @Log(description = "修改管理员日志", businessType = BusinessType.UPDATE)
    public ResultData update(@RequestBody SysUserLog sysAdminLog) {
        return ResultData.success(this.sysAdminLogService.updateById(sysAdminLog));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果 result data
     */
    @DeleteMapping
    @SaCheckPermission("monitor:log:delete")
    @Log(description = "删除管理员日志", businessType = BusinessType.DELETE)
    public ResultData delete(@RequestParam("idList") List<Long> idList) {
        return ResultData.success(this.sysAdminLogService.removeByIds(idList));
    }

    @PostMapping("export")
    @Log(description = "导出管理员日志", businessType = BusinessType.EXPORT)
    public void export(@RequestBody SysUserLog sysAdminLog, HttpServletResponse response) {
        List<SysUserLog> list = this.sysAdminLogService.selectAll(sysAdminLog);
        ExcelUtil<SysUserLog> util = new ExcelUtil<>(SysUserLog.class);
        util.exportExcel(response, list, "管理员日志");
    }
}

