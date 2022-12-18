package com.broad.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.broad.system.entity.SysMenu;

import java.util.List;

/**
 * (SysMenu)表服务接口
 *
 * @author XingGao
 * @since 2022 -10-10 18:46:52
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 根据用户ID获取对应所有菜单
     *
     * @param userId the user id
     * @return 菜单列表 list
     */
    List<SysMenu> findMenuByUserId(Integer userId);

    /**
     * 根据用户ID获取对应所有菜单
     *
     * @param userId the user id
     * @return 菜单列表 list
     */
    List<SysMenu> findTreeMenuByUserId(Integer userId);

    /**
     * 分页查询父级菜单
     *
     * @param menu 菜单
     * @return 菜单列表 list
     */
    List<SysMenu> selectAllByPage(SysMenu menu);


    /**
     * 根据父级id查询子菜单
     *
     * @param menu 菜单
     * @return 菜单列表 list
     */
    List<SysMenu> selectChildListById(SysMenu menu);

    /**
     * Menu tree list.
     *
     * @param menu the menu
     * @return the list
     */
    List<SysMenu> menuTree(SysMenu menu);

    /**
     * Save menu int.
     *
     * @param entity the entity
     * @return the int
     */
    int saveMenu(SysMenu entity);

    /**
     * Update menu int.
     *
     * @param entity the entity
     * @return the int
     */
    int updateMenu(SysMenu entity);

    /**
     * 删除菜单
     *
     * @param idList 菜单id
     * @return 结果 int
     */
    int deleteMenu(List<Long> idList);
}

