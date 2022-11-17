package com.broad.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.broad.common.enums.BusinessType;
import com.broad.framework.annotation.Log;
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
     * 分页查询父级菜单
     *
     * @return 所有数据
     */
    @GetMapping
    @SaCheckPermission("sys:menu:list")
    public TableDataInfo selectAllByPage(SysMenu sysMenu) {
        startPage();
        return getDataTable(this.sysMenuService.selectAllByPage(sysMenu));
    }

    /**
     * 通过父级菜单id查询子菜单
     *
     * @param sysMenu 主键
     * @return 单条数据
     */
    @GetMapping("getMenuChild")
    @SaCheckPermission("sys:menu:list")
    public ResultData selectChildListById(SysMenu sysMenu) {
        return ResultData.success(this.sysMenuService.selectChildListById(sysMenu));
    }

    /**
     * 动态获取路由
     *
     * @return 角色路由
     */
    @GetMapping("getRouters")
    @SaCheckLogin
    public ResultData findMenuByRole() {
        return ResultData.success(this.sysMenuService.findMenuByRole(StpUtil.getLoginIdAsInt()));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @SaCheckPermission("sys:menu:list")
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
    @SaCheckPermission("sys:menu:add")
    @Log(description = "新增菜单数据", businessType = BusinessType.INSERT)
    public ResultData insert(@RequestBody SysMenu sysMenu) {
        return ResultData.success(this.sysMenuService.saveMenu(sysMenu));
    }

    /**
     * 获取树形角色菜单
     *
     * @param menu 实体对象
     * @return 新增结果
     */
    @GetMapping("tree")
    @SaCheckPermission("sys:menu:list")
    public ResultData tree(SysMenu menu) {
        return ResultData.success(this.sysMenuService.menuTree(menu));
    }


    /**
     * 修改数据
     *
     * @param sysMenu 实体对象
     * @return 修改结果
     */
    @PutMapping
    @SaCheckPermission("sys:menu:update")
    @Log(description = "修改菜单数据", businessType = BusinessType.UPDATE)
    public ResultData update(@RequestBody SysMenu sysMenu) {
        return ResultData.success(this.sysMenuService.updateMenu(sysMenu));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @SaCheckPermission("sys:menu:delete")
    @Log(description = "删除菜单", businessType = BusinessType.DELETE)
    public ResultData delete(@RequestParam("idList") List<Long> idList) {
        return ResultData.success(this.sysMenuService.deleteMenu(idList));
    }
}

