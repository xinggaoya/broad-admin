package com.broad.web.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.broad.common.enums.BusinessType;
import com.broad.framework.annotation.Log;
import com.broad.framework.web.controller.BaseController;
import com.broad.framework.web.entity.ResultData;
import com.broad.system.entity.SysRoleMenu;
import com.broad.system.service.SysRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * 管理权限分组表(SysRoleMenu)表控制层
 *
 * @author XingGao
 * @since 2022-10-19 17:15:02
 */
@RestController
@RequestMapping("sysRoleMenu")
public class SysRoleMenuController extends BaseController {
    /**
     * 服务对象
     */
    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    /**
     * 查询所有数据
     *
     * @param sysRoleMenu 查询实体
     * @return 所有数据
     */
    @GetMapping
    public ResultData selectAll(SysRoleMenu sysRoleMenu) {
        return ResultData.success(this.sysRoleMenuService.list(new QueryWrapper<>(sysRoleMenu)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResultData selectOne(@PathVariable Serializable id) {
        return ResultData.success(this.sysRoleMenuService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param sysRoleMenu 实体对象
     * @return 新增结果
     */
    @PostMapping
    @Log(description = "新增角色菜单", businessType = BusinessType.INSERT)
    public ResultData insert(@RequestBody SysRoleMenu sysRoleMenu) {
        return toResult(this.sysRoleMenuService.addRoleMenu(sysRoleMenu));
    }

    /**
     * 修改数据
     *
     * @param sysRoleMenu 实体对象
     * @return 修改结果
     */
    @PutMapping
    @Log(description = "修改角色菜单", businessType = BusinessType.UPDATE)
    public ResultData update(@RequestBody SysRoleMenu sysRoleMenu) {
        return ResultData.success(this.sysRoleMenuService.updateById(sysRoleMenu));
    }
}

