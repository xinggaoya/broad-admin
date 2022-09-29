package com.broad.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.common.constant.CacheConstants;
import com.broad.common.constant.SecurityConstants;
import com.broad.common.service.RedisService;
import com.broad.system.entity.SysAdminGroupAccess;
import com.broad.system.entity.SysMenuRule;
import com.broad.system.mapper.SysAdminGroupAccessMapper;
import com.broad.system.mapper.SysAdminGroupMapper;
import com.broad.system.mapper.SysMenuRuleMapper;
import com.broad.system.service.SysMenuRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 菜单和权限规则表(SysMenuRule)表服务实现类
 *
 * @author XingGao
 * @since 2022 -07-13 09:36:33
 */
@Service("sysMenuRuleService")
public class SysMenuRuleServiceImpl extends ServiceImpl<SysMenuRuleMapper, SysMenuRule> implements SysMenuRuleService {

    @Autowired
    private SysAdminGroupMapper sysAdminGroupMapper;
    @Autowired
    private SysAdminGroupAccessMapper sysAdminGroupAccessMapper;
    @Autowired
    private RedisService redisService;

    /**
     * 查询管理员拥有的路由菜单+创建树形结构
     *
     * @return 所有数据 result data
     */
    @Override
    public List<SysMenuRule> getRouteMenuByAdmin() {
        return buildTree(getRouteByAdminList(StpUtil.getLoginIdAsInt()));
    }


    /**
     * 处理菜单数据 筛选权限标识
     *
     * @return 所有数据 result data
     */

    @Override
    public List<String> getRoleByAdmin(Object adminId) {
        List<String> roleList = new ArrayList<>();
        // 查询管理员所属组
        List<SysMenuRule> sysMenuRules = getRouteByAdminList(adminId);
        sysMenuRules.forEach(menuRule -> {
            roleList.add(menuRule.getName());
        });
        return roleList;
    }


    /**
     * 分页查询全部数据
     *
     * @return 所有数据 result data
     */
    @Override
    public List<SysMenuRule> getRouteMenuAll(SysMenuRule sysMenuRule) {
        List<SysMenuRule> list = baseMapper.selectList(new LambdaQueryWrapper<>(sysMenuRule));
        return buildTree(list);
    }

    /**
     * 查询菜单详情和上级菜单
     *
     * @return 所有数据 result data
     */
    @Override
    public SysMenuRule getRouteById(Serializable id) {
        SysMenuRule menu = baseMapper.selectById(id);
        if (menu.getPid() != null) {
            SysMenuRule parentMenu = baseMapper.selectById(menu.getPid());
            parentMenu.getChildren().add(menu);
            return parentMenu;
        }
        return menu;
    }

    /**
     * 查询管理员拥有的路由菜单
     *
     * @return 树形结构 result data
     */
    @Override
    public List<SysMenuRule> getRouteByAdminList(Object adminId) {
        List<SysMenuRule> sysMenuRulesList = new ArrayList<>();
        List<SysMenuRule> redisServiceCacheList = redisService.getCacheList(CacheConstants.ROUTE_KEY);
        List<String> menuIds;

        SysAdminGroupAccess groupAccess = sysAdminGroupAccessMapper.selectByAdminId(adminId);

        // 检查缓存
        if (redisServiceCacheList.isEmpty()) {
            redisServiceCacheList = baseMapper.selectList(null);
            redisService.setCacheList(CacheConstants.ROUTE_KEY, redisServiceCacheList, CacheConstants.ROUTE_TIME);
        }
        // 判断是否是超级管理员
        if (!SecurityConstants.ADMIN_KEY.equals(groupAccess.getRules())) {
            menuIds = Arrays.asList(groupAccess.getRules().split(","));
            for (SysMenuRule sysMenuRule : redisServiceCacheList) {
                for (String menuId : menuIds) {
                    if (sysMenuRule.getId().equals(Integer.valueOf(menuId))) {
                        sysMenuRulesList.add(sysMenuRule);
                    }
                }
            }
        } else {
            sysMenuRulesList = redisServiceCacheList;
        }


        return sysMenuRulesList;
    }


    /**
     * 构建树形结构
     *
     * @param list the list
     * @return list
     */
    private List<SysMenuRule> buildTree(List<SysMenuRule> list) {
        List<SysMenuRule> treeMenus = new ArrayList<>();
        for (SysMenuRule menuNode : getRootNode(list)) {
            menuNode = buildChilTree(menuNode, list);
            treeMenus.add(menuNode);
        }
        return treeMenus;
    }

    /**
     * 构建子树形结构
     *
     * @param pNode
     * @param menuList
     * @return
     */
    private SysMenuRule buildChilTree(SysMenuRule pNode, List<SysMenuRule> menuList) {
        List<SysMenuRule> chilMenus = new ArrayList<>();
        for (SysMenuRule menuNode : menuList) {
            if (menuNode.getPid().equals(pNode.getId())) {
                chilMenus.add(buildChilTree(menuNode, menuList));
            }
        }
        pNode.setChildren(chilMenus);
        return pNode;
    }

    /**
     * 获取根节点
     *
     * @param menuList
     * @return
     */
    private List<SysMenuRule> getRootNode(List<SysMenuRule> menuList) {
        List<SysMenuRule> rootMenuLists = new ArrayList<>();
        for (SysMenuRule menuNode : menuList) {
            if (menuNode.getPid() == 0) {
                rootMenuLists.add(menuNode);
            }
        }
        return rootMenuLists;
    }

    /**
     * 新增菜单
     *
     * @param menuRule SysMenuRule
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveMenuRule(SysMenuRule menuRule) {
        clearPermissionCache();
        return this.save(menuRule);
    }

    /**
     * 更新菜单信息
     *
     * @param menuRule SysMenuRule
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateMenuRule(SysMenuRule menuRule) {
        clearPermissionCache();
        return this.updateById(menuRule);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeMenuRule(List<Long> idList) {
        List<Integer> pidList = new ArrayList<>();
        List<SysMenuRule> menuRuleList = baseMapper.selectList(new LambdaQueryWrapper<SysMenuRule>().in(SysMenuRule::getId, idList));
        // 删除子菜单
        menuRuleList.forEach(menuRule -> {
            if (menuRule.getPid() == 0) {
                pidList.add(menuRule.getId());
            }
        });
        // 删除菜单
        this.baseMapper.delete(new LambdaUpdateWrapper<SysMenuRule>().in(SysMenuRule::getPid, pidList));
        this.baseMapper.deleteBatchIds(idList);
        return clearPermissionCache();
    }


    private boolean clearPermissionCache() {
        return redisService.deleteObject(CacheConstants.ROUTE_KEY);
    }

}

