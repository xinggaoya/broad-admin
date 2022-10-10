package com.broad.web.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.broad.framework.web.controller.BaseController;
import com.broad.framework.web.entity.ResultData;
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
     * 动态获取路由
     *
     * @return 所有数据
     */
    @GetMapping
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
    public ResultData insert(@RequestBody List<SysMenu> sysMenu) {
        sysMenu.forEach(item -> {
            item.setParentId(0);
        });
        this.sysMenuService.saveBatch(sysMenu);
        sysMenu.forEach(item -> {
            if (item.getChildren() != null && item.getChildren().size() > 0) {
                Integer parentId = this.sysMenuService.getOne(
                        new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getMenuName, item.getMenuName())).getMenuId();
                item.getChildren().forEach(child -> {
                    child.setParentId(parentId);
                });
                this.sysMenuService.saveBatch(item.getChildren());
                item.getChildren().forEach(child -> {
                    if (child.getChildren() != null && child.getChildren().size() > 0) {
                        Integer childParentId = this.sysMenuService.getOne(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getMenuName, child.getMenuName())).getMenuId();
                        child.getChildren().forEach(childChild -> {
                            childChild.setParentId(childParentId);
                        });
                        this.sysMenuService.saveBatch(child.getChildren());
                        child.getChildren().forEach(childChild -> {
                            if (childChild.getChildren() != null && childChild.getChildren().size() > 0) {
                                Integer childChildParentId = this.sysMenuService.getOne(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getMenuName, childChild.getMenuName())).getMenuId();
                                childChild.getChildren().forEach(childChildChild -> {
                                    childChildChild.setParentId(childChildParentId);
                                });
                                this.sysMenuService.saveBatch(childChild.getChildren());
                                childChild.getChildren().forEach(childChildChild -> {
                                    if (childChildChild.getChildren() != null && childChildChild.getChildren().size() > 0) {
                                        Integer childChildChildParentId = this.sysMenuService.getOne(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getMenuName, childChildChild.getMenuName())).getMenuId();
                                        childChildChild.getChildren().forEach(childChildChildChild -> {
                                            childChildChildChild.setParentId(childChildChildParentId);
                                        });
                                        this.sysMenuService.saveBatch(childChildChild.getChildren());
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
        return ResultData.success(this.sysMenuService.saveBatch(sysMenu));
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

