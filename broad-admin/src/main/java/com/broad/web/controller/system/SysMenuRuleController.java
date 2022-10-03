package com.broad.web.controller.system;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.broad.common.enums.BusinessType;
import com.broad.framework.annotation.Log;
import com.broad.framework.web.controller.BaseController;
import com.broad.framework.web.entity.ResultData;
import com.broad.system.entity.SysMenuRule;
import com.broad.system.service.SysMenuRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * 菜单和权限规则表(SysMenuRule)表控制层
 *
 * @author XingGao
 * @since 2022 -07-13 11:18:52
 */
@RestController
@RequestMapping("sysMenuRule")
public class SysMenuRuleController extends BaseController {
    /**
     * 服务对象
     */
    @Autowired
    private SysMenuRuleService sysMenuRuleService;

    /**
     * 查询动态路由菜单
     *
     * @return 所有数据 result data
     */
    @GetMapping
    @SaCheckLogin
    public ResultData selectAllByAdmin() {
        return ResultData.success(this.sysMenuRuleService.getRouteMenuByAdmin());
    }

    /**
     * 分页查询全部数据
     *
     * @param sysMenuRule the sys menu rule
     * @return 所有数据 result data
     */
    @GetMapping("getRouteMenuAll")
    @SaCheckPermission("sysMenuRule:list")
    public ResultData getRouteMenuAll(SysMenuRule sysMenuRule) {
        return ResultData.ok().setData(this.sysMenuRuleService.getRouteMenuAll(sysMenuRule));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据 result data
     */
    @GetMapping("{id}")
    @SaCheckPermission("sysMenuRule:info")
    public ResultData selectOne(@PathVariable Serializable id) {
        return ResultData.success(this.sysMenuRuleService.getById(id));
    }

    /**
     * 通过管理员ID查询对应权限码
     *
     * @param adminId 管理员ID
     * @return result roleList
     */
    @GetMapping("getRoleMenu")
    @SaCheckLogin
    public List<String> getRoleByAdmin(@RequestParam Object adminId) {
        return this.sysMenuRuleService.getRoleByAdmin(adminId);
    }

    /**
     * 新增数据
     *
     * @param sysMenuRule 实体对象
     * @return 新增结果 result data
     */
    @PostMapping
    @SaCheckPermission("sysMenuRule:add")
    @Log(description = "新增菜单和权限规则表数据", businessType = BusinessType.INSERT)
    public ResultData insert(@RequestBody @Valid SysMenuRule sysMenuRule) {
        return ResultData.success(this.sysMenuRuleService.saveMenuRule(sysMenuRule));
    }

    /**
     * 修改数据
     *
     * @param sysMenuRule 实体对象
     * @return 修改结果 result data
     */
    @PutMapping
    @SaCheckPermission("sysMenuRule:edit")
    @Log(description = "修改菜单和权限规则表", businessType = BusinessType.UPDATE)
    public ResultData update(@RequestBody SysMenuRule sysMenuRule) {
        return ResultData.success(this.sysMenuRuleService.updateMenuRule(sysMenuRule));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果 result data
     */
    @DeleteMapping
    @SaCheckPermission("sysMenuRule:delete")
    @Log(description = "删除菜单和权限规则表", businessType = BusinessType.DELETE)
    public ResultData delete(@RequestParam("ids") List<Long> idList) {
        return ResultData.success(this.sysMenuRuleService.removeMenuRule(idList));
    }
}

