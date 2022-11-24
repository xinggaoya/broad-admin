package com.broad.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.broad.common.annotation.Log;
import com.broad.common.enums.BusinessType;
import com.broad.common.web.controller.BaseController;
import com.broad.common.web.entity.ResultData;
import com.broad.common.web.page.TableDataInfo;
import com.broad.system.entity.SysRole;
import com.broad.system.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * 角色表(SysRole)表控制层
 *
 * @author XingGao
 * @since 2022 -07-15 11:21:45
 */
@RestController
@RequestMapping("sysRole")
public class SysRoleController extends BaseController {
    /**
     * 服务对象
     */
    @Autowired
    private SysRoleService sysUserRoleService;

    /**
     * 分页查询所有数据
     *
     * @param sysUserRole 查询实体
     * @return 所有数据 table data info
     */
    @GetMapping
    @SaCheckPermission("sys:role:list")
    public TableDataInfo selectAll(SysRole sysUserRole) {
        startPage();
        return getDataTable(this.sysUserRoleService.selectAll(sysUserRole));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据 result data
     */
    @GetMapping("{id}")
    @SaCheckPermission("sys:role:list")
    public ResultData selectOne(@PathVariable Serializable id) {
        return ResultData.success(this.sysUserRoleService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param sysUserRole 实体对象
     * @return 新增结果 result data
     */
    @PostMapping
    @SaCheckPermission("sys:role:add")
    @Log(description = "新增角色", businessType = BusinessType.INSERT)
    public ResultData insert(@RequestBody SysRole sysUserRole) {
        return ResultData.success(this.sysUserRoleService.saveUserRole(sysUserRole));
    }

    /**
     * 修改数据
     *
     * @param sysUserRole 实体对象
     * @return 修改结果 result data
     */
    @PutMapping
    @SaCheckPermission("sys:role:update")
    @Log(description = "修改角色", businessType = BusinessType.UPDATE)
    public ResultData update(@RequestBody SysRole sysUserRole) {
        return ResultData.success(this.sysUserRoleService.updateById(sysUserRole));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果 result data
     */
    @DeleteMapping("{id}")
    @SaCheckPermission("sys:role:delete")
    @Log(description = "删除角色", businessType = BusinessType.DELETE)
    public ResultData delete(@PathVariable("id") Integer idList) {
        return ResultData.success(this.sysUserRoleService.removeById(idList));
    }
}

