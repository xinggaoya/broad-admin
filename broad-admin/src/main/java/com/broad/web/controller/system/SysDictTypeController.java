package com.broad.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.broad.common.annotation.Log;
import com.broad.common.enums.BusinessType;
import com.broad.common.web.controller.BaseController;
import com.broad.common.web.entity.ResultData;
import com.broad.common.web.page.TableDataInfo;
import com.broad.system.entity.SysDictType;
import com.broad.system.service.SysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * 字典类型表(SysDictType)表控制层
 *
 * @author XingGao
 * @since 2022-10-13 15:00:02
 */
@RestController
@RequestMapping("sysDictType")
public class SysDictTypeController extends BaseController {
    /**
     * 服务对象
     */
    @Autowired
    private SysDictTypeService sysDictTypeService;

    /**
     * 分页查询所有数据
     *
     * @param sysDictType 查询实体
     * @return 所有数据 table data info
     */
    @GetMapping
    @SaCheckPermission("sys:dict:list")
    public TableDataInfo selectAll(SysDictType sysDictType) {
        Page<SysDictType> page = startPage();
        return getDataTable(sysDictTypeService.page(page, new LambdaQueryWrapper<>(sysDictType)));
    }

    /**
     * Gets dict type list.
     *
     * @param sysDictType the sys dict type
     * @return the dict type list
     */
    @GetMapping("/list")
    @SaCheckPermission("sys:dict:list")
    public ResultData getDictTypeList(SysDictType sysDictType) {
        return success(this.sysDictTypeService.list(new LambdaQueryWrapper<>(sysDictType)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据 result data
     */
    @GetMapping("{id}")
    @SaCheckPermission("sys:dict:list")
    public ResultData selectOne(@PathVariable Serializable id) {
        return success(this.sysDictTypeService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param sysDictType 实体对象
     * @return 新增结果 result data
     */
    @PostMapping
    @SaCheckPermission("sys:dict:add")
    @Log(description = "新增字典类型", businessType = BusinessType.INSERT)
    public ResultData insert(@RequestBody SysDictType sysDictType) {
        return toResult(this.sysDictTypeService.save(sysDictType));
    }

    /**
     * 修改数据
     *
     * @param sysDictType 实体对象
     * @return 修改结果 result data
     */
    @PutMapping
    @SaCheckPermission("sys:dict:update")
    @Log(description = "修改字典类型", businessType = BusinessType.UPDATE)
    public ResultData update(@RequestBody SysDictType sysDictType) {
        return toResult(this.sysDictTypeService.updateById(sysDictType));
    }

    /**
     * 删除数据
     *
     * @param id 主键结合
     * @return 删除结果 result data
     */
    @DeleteMapping("{id}")
    @SaCheckPermission("sys:dict:delete")
    @Log(description = "删除字典类型", businessType = BusinessType.DELETE)
    public ResultData delete(@PathVariable Long id) {
        return toResult(this.sysDictTypeService.deleteDictTypeById(id));
    }
}
