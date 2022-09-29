package com.broad.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.broad.system.entity.SysMenuRule;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单和权限规则表(SysMenuRule)表服务接口
 *
 * @author XingGao
 * @since 2022 -07-13 09:36:33
 */
public interface SysMenuRuleService extends IService<SysMenuRule> {

    /**
     * 查询管理员拥有的路由菜单+创建树形结构
     *
     * @return 所有数据 result data
     */
    List<SysMenuRule> getRouteMenuByAdmin();

    /**
     * 处理菜单数据 筛选权限标识
     *
     * @param adminId the admin id
     * @return 所有数据 result data
     */
    List<String> getRoleByAdmin(Object adminId);

    /**
     * 分页查询全部数据
     *
     * @param sysMenuRule the sys menu rule
     * @return 所有数据 result data
     */
    List<SysMenuRule> getRouteMenuAll(SysMenuRule sysMenuRule);

    /**
     * 查询管理员拥有的路由菜单
     *
     * @param id the id
     * @return 所有数据 result data
     */
    SysMenuRule getRouteById(Serializable id);

    /**
     * 查询管理员拥有的路由菜单
     *
     * @param adminId the admin id
     * @return 所有数据 result data
     */
    List<SysMenuRule> getRouteByAdminList(Object adminId);

    /**
     * Save menu rule boolean.
     *
     * @param menuRule the menu rule
     * @return the boolean
     */
    boolean saveMenuRule(SysMenuRule menuRule);

    /**
     * Update menu rule boolean.
     *
     * @param menuRule the menu rule
     * @return the boolean
     */
    boolean updateMenuRule(SysMenuRule menuRule);

    /**
     * Remove menu rule boolean.
     *
     * @param idList the id list
     * @return the boolean
     */
    boolean removeMenuRule(List<Long> idList);
}

