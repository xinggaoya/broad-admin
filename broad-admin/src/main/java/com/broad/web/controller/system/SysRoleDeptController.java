package com.broad.web.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.broad.framework.web.controller.BaseController;
import com.broad.framework.web.entity.ResultData;
import com.broad.framework.web.page.TableDataInfo;
import com.broad.system.entity.SysRoleDept;
import com.broad.system.service.SysRoleDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * (SysRoleDept)表控制层
 *
 * @author XingGao
 * @since 2022-10-19 17:14:52
 */
@RestController
@RequestMapping("sysRoleDept")
public class SysRoleDeptController extends BaseController {
    /**
     * 服务对象
     */
    @Autowired
    private SysRoleDeptService sysRoleDeptService;

    /**
     * 分页查询所有数据
     *
     * @param sysRoleDept 查询实体
     * @return 所有数据
     */
    @GetMapping
    public TableDataInfo selectAll(SysRoleDept sysRoleDept) {
        startPage();
        return getDataTable(this.sysRoleDeptService.list(new QueryWrapper<>(sysRoleDept)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResultData selectOne(@PathVariable Serializable id) {
        return ResultData.success(this.sysRoleDeptService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param sysRoleDept 实体对象
     * @return 新增结果
     */
    @PostMapping
    public ResultData insert(@RequestBody SysRoleDept sysRoleDept) {
        return ResultData.success(this.sysRoleDeptService.save(sysRoleDept));
    }

    /**
     * 修改数据
     *
     * @param sysRoleDept 实体对象
     * @return 修改结果
     */
    @PutMapping
    public ResultData update(@RequestBody SysRoleDept sysRoleDept) {
        return ResultData.success(this.sysRoleDeptService.updateById(sysRoleDept));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public ResultData delete(@RequestParam("idList") List<Long> idList) {
        return ResultData.success(this.sysRoleDeptService.removeByIds(idList));
    }
}

