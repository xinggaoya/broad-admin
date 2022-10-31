package com.broad.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.broad.common.enums.BusinessType;
import com.broad.framework.annotation.Log;
import com.broad.framework.web.controller.BaseController;
import com.broad.framework.web.entity.ResultData;
import com.broad.framework.web.page.TableDataInfo;
import com.broad.system.entity.SysDictData;
import com.broad.system.service.SysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 字典数据表(SysDictData)表控制层
 *
 * @author XingGao
 * @since 2022-10-13 15:00:01
 */
@RestController
@RequestMapping("sysDictData")
public class SysDictDataController extends BaseController {
    /**
     * 服务对象
     */
    @Autowired
    private SysDictDataService sysDictDataService;

    /**
     * 分页查询所有数据
     *
     * @param sysDictData 查询实体
     * @return 所有数据
     */
    @GetMapping
    @SaCheckPermission("sys:dict:list")
    public TableDataInfo selectAll(SysDictData sysDictData) {
        startPage();
        return getDataTable(this.sysDictDataService.list(new LambdaQueryWrapper<>(sysDictData)));
    }

    @GetMapping("getDictDataByType")
    @SaCheckPermission("sys:dict:list")
    public ResultData getDictDataByType(SysDictData sysDictData) {
        return ResultData.success(this.sysDictDataService.selectDictList(sysDictData));
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
        return ResultData.success(this.sysDictDataService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param sysDictData 实体对象
     * @return 新增结果
     */
    @PostMapping
    @SaCheckPermission("sys:dict:add")
    @Log(description = "新增字典数据", businessType = BusinessType.INSERT)
    public ResultData insert(@RequestBody SysDictData sysDictData) {
        this.sysDictDataService.insertDictData(sysDictData);
        return ResultData.ok();
    }

    /**
     * 修改数据
     *
     * @param sysDictData 实体对象
     * @return 修改结果
     */
    @PutMapping
    @SaCheckPermission("sys:dict:update")
    @Log(description = "修改字典数据", businessType = BusinessType.UPDATE)
    public ResultData update(@RequestBody SysDictData sysDictData) {
        this.sysDictDataService.updateDictData(sysDictData);
        return ResultData.ok();
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @SaCheckPermission("sys:dict:delete")
    @Log(description = "删除字典数据", businessType = BusinessType.DELETE)
    public ResultData delete(@RequestParam("idList") List<Long> idList) {
        this.sysDictDataService.deleteDictDataById(SysDictData.builder().dictCode(idList.get(0)).build());
        return ResultData.ok();
    }
}

