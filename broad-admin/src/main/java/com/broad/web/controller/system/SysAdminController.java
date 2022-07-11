package com.broad.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.broad.common.core.entity.ResultData;
import com.broad.system.entity.SysAdmin;
import com.broad.system.service.SysAdminService;
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
    @SaCheckPermission("list")
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
    public ResultData delete(@RequestParam("idList") List<Long> idList) {
        return ResultData.success(this.sysAdminService.removeByIds(idList));
    }
}

