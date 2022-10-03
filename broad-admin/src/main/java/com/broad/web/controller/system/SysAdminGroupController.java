package com.broad.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.broad.common.enums.BusinessType;
import com.broad.framework.annotation.Log;
import com.broad.framework.web.controller.BaseController;
import com.broad.framework.web.entity.ResultData;
import com.broad.framework.web.page.TableDataInfo;
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
 * @since 2022 -07-15 11:21:45
 */
@RestController
@RequestMapping("sysAdminGroup")
public class SysAdminGroupController extends BaseController {
    /**
     * 服务对象
     */
    @Autowired
    private SysAdminGroupService sysAdminGroupService;

    /**
     * 分页查询所有数据
     *
     * @param sysAdminGroup 查询实体
     * @return 所有数据 table data info
     */
    @GetMapping
    @SaCheckPermission("sys:adminGroup:list")
    public TableDataInfo selectAll(SysAdminGroup sysAdminGroup) {
        startPage();
        return getDataTable(this.sysAdminGroupService.selectAll(sysAdminGroup));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据 result data
     */
    @GetMapping("{id}")
    @SaCheckPermission("sys:adminGroup:info")
    public ResultData selectOne(@PathVariable Serializable id) {
        return ResultData.success(this.sysAdminGroupService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param sysAdminGroup 实体对象
     * @return 新增结果 result data
     */
    @PostMapping
    @SaCheckPermission("sys:adminGroup:save")
    @Log(description = "新增管理分组", businessType = BusinessType.INSERT)
    public ResultData insert(@RequestBody SysAdminGroup sysAdminGroup) {
        return ResultData.success(this.sysAdminGroupService.saveAdminGroup(sysAdminGroup));
    }

    /**
     * 修改数据
     *
     * @param sysAdminGroup 实体对象
     * @return 修改结果 result data
     */
    @PutMapping
    @SaCheckPermission("sys:adminGroup:update")
    @Log(description = "修改管理分组", businessType = BusinessType.UPDATE)
    public ResultData update(@RequestBody SysAdminGroup sysAdminGroup) {
        return ResultData.success(this.sysAdminGroupService.updateById(sysAdminGroup));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果 result data
     */
    @DeleteMapping
    @SaCheckPermission("sys:adminGroup:delete")
    @Log(description = "删除管理分组", businessType = BusinessType.DELETE)
    public ResultData delete(@RequestParam("idList") List<Long> idList) {
        return ResultData.success(this.sysAdminGroupService.removeByIds(idList));
    }
}

