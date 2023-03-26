package com.broad.web.controller.system;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.broad.common.annotation.Log;
import com.broad.common.enums.BusinessType;
import com.broad.common.web.controller.BaseController;
import com.broad.common.web.entity.ResultData;
import com.broad.system.entity.SysUserPost;
import com.broad.system.service.SysUserPostService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 用户与岗位关联表(SysUserPost)表控制层
 *
 * @author XingGao
 * @since 2023-03-26 16:15:01
 */
@RestController
@RequestMapping("sysUserPost")
public class SysUserPostController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private SysUserPostService sysUserPostService;

    /**
     * 分页查询所有数据
     *
     * @param page        分页对象
     * @param sysUserPost 查询实体
     * @return 所有数据
     */
    @GetMapping
    @SaCheckPermission("sysUserPost:list")
    public ResultData selectAll(Page<SysUserPost> page, SysUserPost sysUserPost) {
        return ResultData.success(this.sysUserPostService.page(page, new QueryWrapper<>(sysUserPost)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @SaCheckPermission("sysUserPost:list")
    public ResultData selectOne(@PathVariable Serializable id) {
        return ResultData.success(this.sysUserPostService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param sysUserPost 实体对象
     * @return 新增结果
     */
    @PostMapping
    @SaCheckPermission("sysUserPost:add")
    @Log(description = "用户与岗位关联表", businessType = BusinessType.INSERT)
    public ResultData insert(@RequestBody SysUserPost sysUserPost) {
        return ResultData.success(this.sysUserPostService.save(sysUserPost));
    }

    /**
     * 修改数据
     *
     * @param sysUserPost 实体对象
     * @return 修改结果
     */
    @PutMapping
    @SaCheckPermission("sysUserPost:edit")
    @Log(description = "用户与岗位关联表", businessType = BusinessType.UPDATE)
    public ResultData update(@RequestBody SysUserPost sysUserPost) {
        return ResultData.success(this.sysUserPostService.updateById(sysUserPost));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @SaCheckPermission("sysUserPost:delete")
    @Log(description = "用户与岗位关联表", businessType = BusinessType.DELETE)
    public ResultData delete(@RequestParam("idList") List<Long> idList) {
        return ResultData.success(this.sysUserPostService.removeByIds(idList));
    }
}

