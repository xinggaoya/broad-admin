package com.broad.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.broad.framework.web.controller.BaseController;
import com.broad.framework.web.entity.ResultData;
import com.broad.framework.web.page.TableDataInfo;
import com.broad.system.entity.SysMenu;
import com.broad.system.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * (SysMenu)表控制层
 *
 * @author XingGao
 * @since 2022-10-10 18:46:51
 */
@RestController
@RequestMapping("sysMenu")
public class SysMenuController extends BaseController {
    /**
     * 服务对象
     */
    @Autowired
    private SysMenuService sysMenuService;


    /**
     * 分页查询所有数据
     *
     * @return 所有数据
     */
    @GetMapping
    public TableDataInfo selectAllByPage(SysMenu sysMenu) {
        return getDataTable(this.sysMenuService.selectAllByPage(sysMenu));
    }

    /**
     * 动态获取路由
     *
     * @return 角色路由
     */
    @GetMapping("getRouters")
    @SaCheckLogin
    public ResultData selectAll() {
        return ResultData.success(this.sysMenuService.selectAll());
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResultData selectOne(@PathVariable Serializable id) {
        return ResultData.success(this.sysMenuService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param sysMenu 实体对象
     * @return 新增结果
     */
    @PostMapping
    public ResultData insert(@RequestBody SysMenu sysMenu) {
        return ResultData.success(this.sysMenuService.saveMenu(sysMenu));
    }


    /**
     * 修改数据
     *
     * @param sysMenu 实体对象
     * @return 修改结果
     */
    @PutMapping
    public ResultData update(@RequestBody SysMenu sysMenu) {
        return ResultData.success(this.sysMenuService.updateById(sysMenu));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public ResultData delete(@RequestParam("idList") List<Long> idList) {
        return ResultData.success(this.sysMenuService.removeByIds(idList));
    }
}

