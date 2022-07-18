package com.broad.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.system.entity.SysAdminGroup;
import com.broad.system.entity.SysMenuRule;
import com.broad.system.entity.SysAdminGroupAccess;
import com.broad.system.mapper.SysAdminGroupAccessMapper;
import com.broad.system.mapper.SysAdminGroupMapper;
import com.broad.system.mapper.SysMenuRuleMapper;
import com.broad.system.service.SysMenuRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

/**
 * 菜单和权限规则表(SysMenuRule)表服务实现类
 *
 * @author XingGao
 * @since 2022-07-13 09:36:33
 */
@Service("sysMenuRuleService")
public class SysMenuRuleServiceImpl extends ServiceImpl<SysMenuRuleMapper, SysMenuRule> implements SysMenuRuleService {

    @Autowired
    private SysAdminGroupMapper sysAdminGroupMapper;

    @Autowired
    private SysAdminGroupAccessMapper sysAdminGroupAccessMapper;

    /**
     * 查询管理员拥有的路由菜单+创建树形结构
     *
     * @return 所有数据 result data
     */
    @Override
    public List<SysMenuRule> getRouteMenuByAdmin() {
        return buildTree(getRouteByAdminList());
    }


    /**
     * 查询全部路由菜单
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
    public List<SysMenuRule> getRouteByAdminList() {
        LambdaQueryWrapper<SysMenuRule> wrapper = new LambdaQueryWrapper<>();
        String[] menuIds;

        SysAdminGroupAccess access = sysAdminGroupAccessMapper.selectById(StpUtil.getLoginIdAsInt());
        SysAdminGroup group = sysAdminGroupMapper.selectById(access.getGroupId());
        if (!"*".equals(group.getRules())) {
            menuIds = group.getRules().split(",");
            List<String> menuIdList = new ArrayList<>(Arrays.asList(menuIds));
            wrapper.in(SysMenuRule::getId, menuIdList);
        }
        // 查询该角色的全部路由菜单
        return baseMapper.selectList(wrapper);
    }


    /**
     * 构建树形结构
     *
     * @param list
     * @return
     */
    public List<SysMenuRule> buildTree(List<SysMenuRule> list) {
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

}

