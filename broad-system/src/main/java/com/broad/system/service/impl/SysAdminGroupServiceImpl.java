package com.broad.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.system.mapper.SysAdminGroupMapper;
import com.broad.system.entity.SysAdminGroup;
import com.broad.system.service.SysAdminGroupService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理分组表(SysAdminGroup)表服务实现类
 *
 * @author XingGao
 * @since 2022-07-13 09:53:11
 */

@Service("sysAdminGroupService")
public class SysAdminGroupServiceImpl extends ServiceImpl<SysAdminGroupMapper, SysAdminGroup> implements SysAdminGroupService {

    @Override
    public Page<SysAdminGroup> selectAll(Page<SysAdminGroup> page, SysAdminGroup sysAdminGroup) {
        Page<SysAdminGroup> pageList = baseMapper.selectPage(page, new QueryWrapper<>(sysAdminGroup));
        pageList.setRecords(buildTree(pageList.getRecords()));
        return pageList;
    }

    /**
     * 构建树形结构
     *
     * @param list
     * @return
     */
    public List<SysAdminGroup> buildTree(List<SysAdminGroup> list) {
        List<SysAdminGroup> treeMenus = new ArrayList<>();
        for (SysAdminGroup menuNode : getRootNode(list)) {
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
    private SysAdminGroup buildChilTree(SysAdminGroup pNode, List<SysAdminGroup> menuList) {
        List<SysAdminGroup> chilMenus = new ArrayList<>();
        for (SysAdminGroup menuNode : menuList) {
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
    private List<SysAdminGroup> getRootNode(List<SysAdminGroup> menuList) {
        List<SysAdminGroup> rootMenuLists = new ArrayList<>();
        for (SysAdminGroup menuNode : menuList) {
            if (menuNode.getPid() == 0) {
                rootMenuLists.add(menuNode);
            }
        }
        return rootMenuLists;
    }

}
