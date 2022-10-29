package com.broad.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.broad.common.enums.BusinessType;
import com.broad.framework.annotation.Log;
import com.broad.framework.web.controller.BaseController;
import com.broad.framework.web.entity.ResultData;
import com.broad.framework.web.page.TableDataInfo;
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
     * @return 所有数据
     */
    @GetMapping
    @SaCheckPermission("sys:dict:list")
    public TableDataInfo selectAll(SysDictType sysDictType) {
        startPage();
        return getDataTable(this.sysDictTypeService.list(new QueryWrapper<>(sysDictType)));
    }

    @GetMapping("/list")
    @SaCheckPermission("sys:dict:list")
    public ResultData getDictTypeList(SysDictType sysDictType) {
        return ResultData.success(this.sysDictTypeService.list(new QueryWrapper<>(sysDictType)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @SaCheckPermission("sys:dict:list")
    public ResultData selectOne(@PathVariable Serializable id) {
        return ResultData.success(this.sysDictTypeService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param sysDictType 实体对象
     * @return 新增结果
     */
    @PostMapping
    @SaCheckPermission("sys:dict:add")
    @Log(description = "新增字典类型", businessType = BusinessType.INSERT)
    public ResultData insert(@RequestBody SysDictType sysDictType) {
        return ResultData.success(this.sysDictTypeService.save(sysDictType));
    }

    /**
     * 修改数据
     *
     * @param sysDictType 实体对象
     * @return 修改结果
     */
    @PutMapping
    @SaCheckPermission("sys:dict:update")
    @Log(description = "修改字典类型", businessType = BusinessType.UPDATE)
    public ResultData update(@RequestBody SysDictType sysDictType) {
        return ResultData.success(this.sysDictTypeService.updateById(sysDictType));
    }

    /**
     * 删除数据
     *
     * @param id 主键结合
     * @return 删除结果
     */
    @DeleteMapping("{id}")
    @SaCheckPermission("sys:dict:delete")
    @Log(description = "删除字典类型", businessType = BusinessType.DELETE)
    public ResultData delete(@PathVariable Long id) {
        return ResultData.success(this.sysDictTypeService.deleteDictTypeById(id));
    }
}

