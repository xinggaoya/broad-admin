package com.broad.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.broad.system.entity.SysMenu;

import java.util.List;

/**
 * (SysMenu)表服务接口
 *
 * @author XingGao
 * @since 2022-10-10 18:46:52
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 根据角色获取对应菜单
     *
     * @return 菜单列表
     */
    List<SysMenu> findMenuByRole(Integer menuId);

    /**
     * 分页查询父级菜单
     *
     * @param menu 菜单
     * @return 菜单列表
     */
    List<SysMenu> selectAllByPage(SysMenu menu);


    /**
     * 根据父级id查询子菜单
     *
     * @param menu 菜单
     * @return 菜单列表
     */
    List<SysMenu> selectChildListById(SysMenu menu);

    List<SysMenu> menuTree(SysMenu menu);

    int saveMenu(SysMenu entity);

    /**
     * 删除菜单
     *
     * @param idList 菜单id
     * @return 结果
     */
    int deleteMenu(List<Long> idList);
}

