package com.broad.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.system.entity.SysAdminGroup;
import com.broad.system.entity.SysAdminGroupAccess;
import com.broad.system.mapper.SysAdminGroupAccessMapper;
import com.broad.system.mapper.SysAdminGroupMapper;
import com.broad.system.mapper.SysMenuRuleMapper;
import com.broad.system.entity.SysMenuRule;
import com.broad.system.service.SysMenuRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Override
    public List<SysMenuRule> getRouteMenu() {
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
        List<SysMenuRule> list = baseMapper.selectList(wrapper);
        // 创建树形结构
        return getTreeMenu(list);
    }

    /**
     * 创建树形结构
     *
     * @param sysMenuRuleList
     * @return
     */
    public List<SysMenuRule> getTreeMenu(List<SysMenuRule> sysMenuRuleList) {
        List<SysMenuRule> list = sysMenuRuleList;
        List<SysMenuRule> treeList = new ArrayList<>();
        for (SysMenuRule menu : list) {
            if (menu.getPid() == 0) {
                treeList.add(menu);
            }
        }
        for (SysMenuRule menu : list) {
            for (SysMenuRule tree : treeList) {
                if (menu.getPid().equals(tree.getId())) {
                    tree.addChild(menu);
                }
            }
        }
        return treeList;
    }

}

