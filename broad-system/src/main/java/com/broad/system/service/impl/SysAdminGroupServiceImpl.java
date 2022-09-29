package com.broad.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.broad.common.exception.ServiceException;
import com.broad.system.entity.SysAdminGroup;
import com.broad.system.entity.SysAdminGroupAccess;
import com.broad.system.mapper.SysAdminGroupAccessMapper;
import com.broad.system.mapper.SysAdminGroupMapper;
import com.broad.system.service.SysAdminGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理分组表(SysAdminGroup)表服务实现类
 *
 * @author XingGao
 * @since 2022 -07-13 09:53:11
 */
@Service("sysAdminGroupService")
public class SysAdminGroupServiceImpl extends ServiceImpl<SysAdminGroupMapper, SysAdminGroup> implements SysAdminGroupService {

    @Autowired
    private SysAdminGroupAccessMapper sysAdminGroupAccessMapper;

    @Override
    public List<SysAdminGroup> selectAll( SysAdminGroup sysAdminGroup) {
        List<SysAdminGroup> pageList = baseMapper.selectList(new QueryWrapper<>(sysAdminGroup));
        // 构建树形结构数据
//        pageList.setRecords(buildTree(pageList.getRecords()));
        return buildTree(pageList);
    }

    @Override
    public int saveAdminGroup(SysAdminGroup sysAdminGroup) {
        // 如果上级组为空，则设置为根组
        if (sysAdminGroup.getPid() == null) {
            sysAdminGroup.setPid(0);
        }
        return baseMapper.insert(sysAdminGroup);
    }

    @Override
    public int updateAdminGroup(SysAdminGroup sysAdminGroup) {
        return baseMapper.updateById(sysAdminGroup);
    }


    @Override
    public int deleteAdminGroup(List<Long> idList) {
        // 检查该角色下是否有管理员，如果有则不能删除
        List<SysAdminGroupAccess> adminGroupAccesses = sysAdminGroupAccessMapper.selectList(new LambdaQueryWrapper<SysAdminGroupAccess>()
                .in(SysAdminGroupAccess::getGroupId, idList));
        if (adminGroupAccesses.size() > 0) {
            throw new ServiceException("该角色下有管理员绑定，不能删除");
        }
        return baseMapper.deleteBatchIds(idList);
    }

    /**
     * 构建树形结构
     *
     * @param list the list
     * @return list list
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
