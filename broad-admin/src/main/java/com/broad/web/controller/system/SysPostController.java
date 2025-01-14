package com.broad.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.broad.common.annotation.Log;
import com.broad.common.enums.BusinessType;
import com.broad.common.web.controller.BaseController;
import com.broad.common.web.entity.ResultData;
import com.broad.common.web.page.TableDataInfo;
import com.broad.system.entity.SysPost;
import com.broad.system.service.SysPostService;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 岗位信息表(SysPost)表控制层
 *
 * @author XingGao
 * @since 2023-03-26 16:15:00
 */
@RestController
@RequestMapping("sysPost")
public class SysPostController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private SysPostService sysPostService;

    /**
     * 分页查询所有数据
     *
     * @param sysPost 查询实体
     * @return 所有数据
     */
    @GetMapping
    @SaCheckPermission("sysPost:list")
    public TableDataInfo selectAll(SysPost sysPost) {
        Page<SysPost> page = startPage();
        return getDataTable(sysPostService.page(page, new QueryWrapper<>(sysPost)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @SaCheckPermission("sysPost:list")
    public ResultData selectOne(@PathVariable Serializable id) {
        return success(this.sysPostService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param sysPost 实体对象
     * @return 新增结果
     */
    @PostMapping
    @SaCheckPermission("sysPost:add")
    @Log(description = "岗位管理", businessType = BusinessType.INSERT)
    public ResultData insert(@RequestBody SysPost sysPost) {
        return toResult(this.sysPostService.save(sysPost));
    }

    /**
     * 修改数据
     *
     * @param sysPost 实体对象
     * @return 修改结果
     */
    @PutMapping
    @SaCheckPermission("sysPost:edit")
    @Log(description = "岗位管理", businessType = BusinessType.UPDATE)
    public ResultData update(@RequestBody SysPost sysPost) {
        return toResult(this.sysPostService.updateById(sysPost));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @SaCheckPermission("sysPost:delete")
    @Log(description = "岗位管理", businessType = BusinessType.DELETE)
    public ResultData delete(@RequestParam("idList") List<Long> idList) {
        return toResult(this.sysPostService.removeByIds(idList));
    }
}
