package com.broad.web.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.broad.common.core.entity.ResultData;
import com.broad.system.entity.SysAdminGroup;
import com.broad.system.service.SysAdminGroupService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.Serializable;
import java.util.List;

/**
 * 管理分组表(SysAdminGroup)表控制层
 *
 * @author XingGao
 * @since 2022-07-15 11:21:45
 */
@RestController
@RequestMapping("sysAdminGroup")
public class SysAdminGroupController {
    /**
     * 服务对象
     */
    @Autowired
    private SysAdminGroupService sysAdminGroupService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param sysAdminGroup 查询实体
     * @return 所有数据
     */
    @GetMapping
    public ResultData selectAll(Page<SysAdminGroup> page, SysAdminGroup sysAdminGroup) {
        return ResultData.success(this.sysAdminGroupService.selectAll(page, sysAdminGroup));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResultData selectOne(@PathVariable Serializable id) {
        return ResultData.success(this.sysAdminGroupService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param sysAdminGroup 实体对象
     * @return 新增结果
     */
    @PostMapping
    public ResultData insert(@RequestBody SysAdminGroup sysAdminGroup) {
        return ResultData.success(this.sysAdminGroupService.save(sysAdminGroup));
    }

    /**
     * 修改数据
     *
     * @param sysAdminGroup 实体对象
     * @return 修改结果
     */
    @PutMapping
    public ResultData update(@RequestBody SysAdminGroup sysAdminGroup) {
        return ResultData.success(this.sysAdminGroupService.updateById(sysAdminGroup));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public ResultData delete(@RequestParam("idList") List<Long> idList) {
        return ResultData.success(this.sysAdminGroupService.removeByIds(idList));
    }
}

