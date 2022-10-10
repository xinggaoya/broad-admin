package com.broad.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.system.entity.SysMenu;
import com.broad.system.mapper.SysMenuMapper;
import com.broad.system.service.SysMenuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * (SysMenu)表服务实现类
 *
 * @author XingGao
 * @since 2022-10-10 18:46:52
 */
@Service("sysMenuService")
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Override
    public List<SysMenu> selectAll() {
        List<SysMenu> list = this.baseMapper.selectList(null);
        return buildTree(list);
    }

    /**
     * 构建树形结构
     *
     * @param list the list
     * @return list
     */
    private List<SysMenu> buildTree(List<SysMenu> list) {
        List<SysMenu> treeMenus = new ArrayList<>();
        for (SysMenu menuNode : getRootNode(list)) {
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
    private SysMenu buildChilTree(SysMenu pNode, List<SysMenu> menuList) {
        List<SysMenu> chilMenus = new ArrayList<>();
        for (SysMenu menuNode : menuList) {
            if (menuNode.getParentId().equals(pNode.getMenuId())) {
                SysMenu sysMenu = buildChilTree(menuNode, menuList);
                sysMenu.setParentPath(pNode.getMenuUrl());
                chilMenus.add(sysMenu);
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
    private List<SysMenu> getRootNode(List<SysMenu> menuList) {
        List<SysMenu> rootMenuLists = new ArrayList<>();
        for (SysMenu menuNode : menuList) {
            if (menuNode.getParentId() == 0) {
                rootMenuLists.add(menuNode);
            }
        }
        return rootMenuLists;
    }

}

