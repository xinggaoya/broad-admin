package com.broad.web.controller.system;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.broad.common.core.entity.ResultData;
import com.broad.system.entity.SysMenuRule;
import com.broad.system.service.SysMenuRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单和权限规则表(SysMenuRule)表控制层
 *
 * @author XingGao
 * @since 2022-07-13 11:18:52
 */
@RestController
@RequestMapping("sysMenuRule")
public class SysMenuRuleController {
    /**
     * 服务对象
     */
    @Autowired
    private SysMenuRuleService sysMenuRuleService;

    /**
     * 查询动态路由菜单
     *
     * @return 所有数据
     */
    @GetMapping
    @SaCheckLogin
    public ResultData selectAll() {
        return ResultData.success(this.sysMenuRuleService.getRouteMenu());
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @SaCheckLogin
    public ResultData selectOne(@PathVariable Serializable id) {
        return ResultData.success(this.sysMenuRuleService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param sysMenuRule 实体对象
     * @return 新增结果
     */
    @PostMapping
    @SaCheckPermission("sysMenuRule:add")
    public ResultData insert(@RequestBody SysMenuRule sysMenuRule) {
        return ResultData.success(this.sysMenuRuleService.save(sysMenuRule));
    }

    /**
     * 修改数据
     *
     * @param sysMenuRule 实体对象
     * @return 修改结果
     */
    @PutMapping
    @SaCheckPermission("sysMenuRule:edit")
    public ResultData update(@RequestBody SysMenuRule sysMenuRule) {
        return ResultData.success(this.sysMenuRuleService.updateById(sysMenuRule));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @SaCheckPermission("sysMenuRule:delete")
    public ResultData delete(@RequestParam("idList") List<Long> idList) {
        return ResultData.success(this.sysMenuRuleService.removeByIds(idList));
    }
}
