package com.broad.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.common.constant.CacheConstants;
import com.broad.system.entity.SysMenu;
import com.broad.system.entity.SysRoleMenu;
import com.broad.system.mapper.SysMenuMapper;
import com.broad.system.service.SysMenuService;
import com.broad.system.service.SysRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * (SysMenu)表服务实现类
 *
 * @author XingGao
 * @since 2022-10-10 18:46:52
 */
@Service("sysMenuService")
@CacheConfig(cacheNames = CacheConstants.ROUTE_KEY, keyGenerator = CacheConstants.CACHE_PREFIX_GENERATION)
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysRoleMenuService roleMenuService;


    @Override
    @Cacheable(key = "#menuId", unless = "null == #result")
    public List<SysMenu> findMenuByRole(Integer menuId) {
        List<SysMenu> list = this.baseMapper.findMenuByRole(menuId);
        return buildTree(list);
    }

    @Override
    public List<SysMenu> selectAllByPage(SysMenu menu) {
        menu.setParentId(0);
        return this.baseMapper.selectChildListById(menu.getParentId());
    }

    @Override
    public List<SysMenu> selectChildListById(SysMenu menu) {
        return this.baseMapper.selectChildListById(menu.getMenuId());
    }

    @Override
    public List<SysMenu> menuTree(SysMenu menu) {
        return buildTree(this.baseMapper.selectMenuTree());
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public int saveMenu(SysMenu entity) {
        if (entity.getParentId() == null) {
            entity.setParentId(0);
        }
        return this.baseMapper.insert(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public int updateMenu(SysMenu entity) {
        return this.baseMapper.updateById(entity);
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
    private SysMenu buildChilTree(SysMenu pNode, List<SysMenu> menuList) {
        List<SysMenu> chilMenus = new ArrayList<>();
        for (SysMenu menuNode : menuList) {
            if (menuNode.getParentId().equals(pNode.getMenuId())) {
                SysMenu sysMenu = buildChilTree(menuNode, menuList);
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

    /**
     * 删除菜单和下级菜单
     */
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public int deleteMenu(List<Long> idList) {
        this.baseMapper.deleteBatchIds(idList);
        roleMenuService.remove(new LambdaUpdateWrapper<SysRoleMenu>().in(SysRoleMenu::getMenuId, idList));
        return this.baseMapper.deleteChildMenu(idList);
    }

}

