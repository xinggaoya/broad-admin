package com.broad.web.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.broad.framework.web.controller.BaseController;
import com.broad.framework.web.entity.ResultData;
import com.broad.framework.web.page.TableDataInfo;
import com.broad.system.entity.SysUserRole;
import com.broad.system.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * (SysUserRole)表控制层
 *
 * @author XingGao
 * @since 2022-10-21 01:03:46
 */
@RestController
@RequestMapping("sysUserRole")
public class SysUserRoleController extends BaseController {
    /**
     * 服务对象
     */
    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 分页查询所有数据
     *
     * @param sysUserRole 查询实体
     * @return 所有数据
     */
    @GetMapping
    public TableDataInfo selectAll(SysUserRole sysUserRole) {
        startPage();
        return getDataTable(this.sysUserRoleService.list(new QueryWrapper<>(sysUserRole)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResultData selectOne(@PathVariable Serializable id) {
        return ResultData.success(this.sysUserRoleService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param sysUserRole 实体对象
     * @return 新增结果
     */
    @PostMapping
    public ResultData insert(@RequestBody SysUserRole sysUserRole) {
        return ResultData.success(this.sysUserRoleService.save(sysUserRole));
    }

    /**
     * 修改数据
     *
     * @param sysUserRole 实体对象
     * @return 修改结果
     */
    @PutMapping
    public ResultData update(@RequestBody SysUserRole sysUserRole) {
        return ResultData.success(this.sysUserRoleService.updateById(sysUserRole));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public ResultData delete(@RequestParam("idList") List<Long> idList) {
        return ResultData.success(this.sysUserRoleService.removeByIds(idList));
    }
}

