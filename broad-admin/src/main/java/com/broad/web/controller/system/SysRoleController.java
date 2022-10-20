package com.broad.web.controller.system;

import com.broad.common.enums.BusinessType;
import com.broad.framework.annotation.Log;
import com.broad.framework.web.controller.BaseController;
import com.broad.framework.web.entity.ResultData;
import com.broad.framework.web.page.TableDataInfo;
import com.broad.system.entity.SysRole;
import com.broad.system.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 管理分组表(SysRole)表控制层
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
    @Log(description = "新增管理分组", businessType = BusinessType.INSERT)
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
    @Log(description = "修改管理分组", businessType = BusinessType.UPDATE)
    public ResultData update(@RequestBody SysRole sysUserRole) {
        return ResultData.success(this.sysUserRoleService.updateById(sysUserRole));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果 result data
     */
    @DeleteMapping
    @Log(description = "删除管理分组", businessType = BusinessType.DELETE)
    public ResultData delete(@RequestParam("idList") List<Long> idList) {
        return ResultData.success(this.sysUserRoleService.removeByIds(idList));
    }
}

