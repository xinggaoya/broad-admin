package com.broad.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.broad.common.core.entity.ResultData;
import com.broad.common.enums.BusinessType;
import com.broad.framework.annotation.Log;
import com.broad.system.entity.SysAdmin;
import com.broad.system.service.SysAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 管理员表(SysAdmin)表控制层
 *
 * @author XingGao
 * @since 2022-07-09 17:19:39
 */
@RestController
@RequestMapping("sysAdmin")
@Api(tags = "管理员表")
public class SysAdminController {

    /**
     * 服务对象
     */
    @Autowired
    private SysAdminService sysAdminService;


    /**
     * 分页查询所有数据
     *
     * @param page     分页对象
     * @param sysAdmin 查询实体
     * @return 所有数据
     */
    @GetMapping
    @ApiOperation("分页查询所有数据")
    public ResultData selectAll(Page<SysAdmin> page, SysAdmin sysAdmin) {
        return ResultData.success(this.sysAdminService.page(page, new QueryWrapper<>(sysAdmin)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation("通过主键查询单条数据")
    @SaCheckPermission("sysAdmin:info")
    public ResultData selectOne(@PathVariable Serializable id) {
        return ResultData.success(this.sysAdminService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param sysAdmin 实体对象
     * @return 新增结果
     */
    @PostMapping
    @SaCheckPermission("sysAdmin:save")
    @Log(description = "新增管理员表(SysAdmin)表", businessType = BusinessType.INSERT)
    public ResultData insert(@RequestBody SysAdmin sysAdmin) {
        return ResultData.success(this.sysAdminService.save(sysAdmin));
    }

    /**
     * 修改数据
     *
     * @param sysAdmin 实体对象
     * @return 修改结果
     */
    @PutMapping
    @SaCheckPermission("sysAdmin:update")
    @Log(description = "修改管理员表(SysAdmin)表", businessType = BusinessType.UPDATE)
    public ResultData update(@RequestBody SysAdmin sysAdmin) {
        return ResultData.success(this.sysAdminService.updateById(sysAdmin));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @SaCheckPermission("sysAdmin:delete")
    @Log(description = "删除管理员表(SysAdmin)表", businessType = BusinessType.DELETE)
    public ResultData delete(@RequestParam("idList") List<Long> idList) {
        return ResultData.success(this.sysAdminService.removeByIds(idList));
    }
    /**
     * 管理员登录
     *
     * @param sysAdmin
     * @return 删除结果
     */
    @PostMapping("/login")
    @ApiOperation("登录")
    public ResultData login(@RequestBody SysAdmin sysAdmin) {
        return ResultData.success("登录成功", this.sysAdminService.administratorLogin(sysAdmin));
    }

}

