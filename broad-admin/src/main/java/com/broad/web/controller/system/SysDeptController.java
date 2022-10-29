package com.broad.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.broad.common.enums.BusinessType;
import com.broad.framework.annotation.Log;
import com.broad.framework.web.controller.BaseController;
import com.broad.framework.web.entity.ResultData;
import com.broad.system.entity.SysDept;
import com.broad.system.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 部门表(SysDept)表控制层
 *
 * @author XingGao
 * @since 2022-10-02 19:54:52
 */
@RestController
@RequestMapping("sysDept")
public class SysDeptController extends BaseController {
    /**
     * 服务对象
     */
    @Autowired
    private SysDeptService sysDeptService;

    /**
     * 查询所有数据
     *
     * @param sysDept 查询实体
     * @return 所有数据
     */
    @GetMapping
    @SaCheckPermission("sys:dept:list")
    public ResultData selectAll(SysDept sysDept) {
        return ResultData.success(this.sysDeptService.selectAll(sysDept));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @SaCheckPermission("sys:dept:list")
    public ResultData selectOne(@PathVariable Serializable id) {
        return ResultData.success(this.sysDeptService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param sysDept 实体对象
     * @return 新增结果
     */
    @PostMapping
    @SaCheckPermission("sys:dept:add")
    @Log(description = "新增部门", businessType = BusinessType.INSERT)
    public ResultData insert(@RequestBody SysDept sysDept) {
        return ResultData.success(this.sysDeptService.insertDept(sysDept));
    }

    /**
     * 修改数据
     *
     * @param sysDept 实体对象
     * @return 修改结果
     */
    @PutMapping
    @SaCheckPermission("sys:dept:update")
    @Log(description = "修改部门", businessType = BusinessType.UPDATE)
    public ResultData update(@RequestBody SysDept sysDept) {
        return ResultData.success(this.sysDeptService.updateById(sysDept));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @SaCheckPermission("sys:dept:delete")
    @Log(description = "删除部门", businessType = BusinessType.DELETE)
    public ResultData delete(@RequestParam("idList") List<Long> idList) {
        this.sysDeptService.deleteDept(idList);
        return ResultData.ok();
    }
}

