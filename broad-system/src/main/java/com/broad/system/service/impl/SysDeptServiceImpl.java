package com.broad.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.system.entity.SysDept;
import com.broad.system.mapper.SysDeptMapper;
import com.broad.system.service.SysDeptService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 部门表(SysDept)表服务实现类
 *
 * @author XingGao
 * @since 2022-10-02 19:54:53
 */
@Service("sysDeptService")
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    @Override
    public List<SysDept> selectAll(SysDept sysDept) {
        return buildTree(this.baseMapper.selectList(new LambdaQueryWrapper<>(sysDept)));
    }


    /**
     * 构建树形结构
     *
     * @param list the list
     * @return list
     */
    private List<SysDept> buildTree(List<SysDept> list) {
        List<SysDept> treeMenus = new ArrayList<>();
        for (SysDept menuNode : getRootNode(list)) {
            buildChilTree(menuNode, list);
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
    private SysDept buildChilTree(SysDept pNode, List<SysDept> menuList) {
        List<SysDept> chilMenus = new ArrayList<>();
        for (SysDept menuNode : menuList) {
            if (menuNode.getParentId().equals(pNode.getDeptId())) {
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
    private List<SysDept> getRootNode(List<SysDept> menuList) {
        List<SysDept> rootMenuLists = new ArrayList<>();
        for (SysDept menuNode : menuList) {
            if (menuNode.getParentId() == 0) {
                rootMenuLists.add(menuNode);
            }
        }
        return rootMenuLists;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDept(List<Long> deptIds) {
        // 删除下级部门
        this.baseMapper.delete(new LambdaUpdateWrapper<SysDept>().in(SysDept::getParentId, deptIds));
        // 删除当前部门
        this.baseMapper.deleteBatchIds(deptIds);
    }
}

