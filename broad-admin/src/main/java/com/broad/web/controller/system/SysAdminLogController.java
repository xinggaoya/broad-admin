package com.broad.web.controller.system;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.broad.common.core.entity.ResultData;
import com.broad.common.enums.BusinessType;
import com.broad.framework.annotation.Log;
import com.broad.system.entity.SysAdminLog;
import com.broad.system.service.SysAdminLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 管理员日志表(SysAdminLog)表控制层
 *
 * @author XingGao
 * @since 2022-07-16 21:10:59
 */
@RestController
@RequestMapping("sysAdminLog")
public class SysAdminLogController {

    /**
     * 服务对象
     */
    @Autowired
    private SysAdminLogService sysAdminLogService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param sysAdminLog 查询实体
     * @return 所有数据
     */
    @GetMapping
    @SaCheckPermission("sys:adminLog:list")
    public ResultData selectAll(Page<SysAdminLog> page, SysAdminLog sysAdminLog) {
        return ResultData.data(this.sysAdminLogService.selectAll(page, sysAdminLog));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @SaCheckPermission("sys:adminLog:info")
    public ResultData selectOne(@PathVariable Serializable id) {
        return ResultData.data(this.sysAdminLogService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param sysAdminLog 实体对象
     * @return 新增结果
     */
    @PostMapping
    @SaCheckPermission("sys:adminLog:save")
    @Log(description = "新增管理员日志", businessType = BusinessType.INSERT)
    public ResultData insert(@RequestBody SysAdminLog sysAdminLog) {
        return ResultData.data(this.sysAdminLogService.save(sysAdminLog));
    }

    /**
     * 修改数据
     *
     * @param sysAdminLog 实体对象
     * @return 修改结果
     */
    @PutMapping
    @SaCheckPermission("sys:adminLog:update")
    @Log(description = "修改管理员日志", businessType = BusinessType.UPDATE)
    public ResultData update(@RequestBody SysAdminLog sysAdminLog) {
        return ResultData.data(this.sysAdminLogService.updateById(sysAdminLog));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @SaCheckPermission("sys:adminLog:delete")
    @Log(description = "删除管理员日志", businessType = BusinessType.DELETE)
    public ResultData delete(@RequestParam("idList") List<Long> idList) {
        return ResultData.data(this.sysAdminLogService.removeByIds(idList));
    }
}

