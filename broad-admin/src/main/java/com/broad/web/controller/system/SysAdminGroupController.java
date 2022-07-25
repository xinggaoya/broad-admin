package com.broad.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.broad.common.core.entity.ResultData;
import com.broad.common.enums.BusinessType;
import com.broad.framework.annotation.Log;
import com.broad.system.entity.SysAdminGroup;
import com.broad.system.service.SysAdminGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 管理分组表(SysAdminGroup)表控制层
 *
 * @author XingGao
 * @since 2022-07-15 11:21:45
 */
@RestController
@RequestMapping("sysAdminGroup")
public class SysAdminGroupController {
    /**
     * 服务对象
     */
    @Autowired
    private SysAdminGroupService sysAdminGroupService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param sysAdminGroup 查询实体
     * @return 所有数据
     */
    @GetMapping
    @SaCheckPermission("sys:adminGroup:list")
    public ResultData selectAll(Page<SysAdminGroup> page, SysAdminGroup sysAdminGroup) {
        return ResultData.data(this.sysAdminGroupService.selectAll(page, sysAdminGroup));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @SaCheckPermission("sys:adminGroup:info")
    public ResultData selectOne(@PathVariable Serializable id) {
        return ResultData.data(this.sysAdminGroupService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param sysAdminGroup 实体对象
     * @return 新增结果
     */
    @PostMapping
    @SaCheckPermission("sys:adminGroup:save")
    @Log(description = "新增管理分组", businessType = BusinessType.INSERT)
    public ResultData insert(@RequestBody SysAdminGroup sysAdminGroup) {
        return ResultData.data(this.sysAdminGroupService.save(sysAdminGroup));
    }

    /**
     * 修改数据
     *
     * @param sysAdminGroup 实体对象
     * @return 修改结果
     */
    @PutMapping
    @SaCheckPermission("sys:adminGroup:update")
    @Log(description = "修改管理分组", businessType = BusinessType.UPDATE)
    public ResultData update(@RequestBody SysAdminGroup sysAdminGroup) {
        return ResultData.data(this.sysAdminGroupService.updateById(sysAdminGroup));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @SaCheckPermission("sys:adminGroup:delete")
    @Log(description = "删除管理分组", businessType = BusinessType.DELETE)
    public ResultData delete(@RequestParam("idList") List<Long> idList) {
        return ResultData.data(this.sysAdminGroupService.removeByIds(idList));
    }
}

