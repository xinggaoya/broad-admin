package com.broad.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.common.constant.CacheConstants;
import com.broad.common.utils.StringUtils;
import com.broad.system.entity.SysMenu;
import com.broad.system.mapper.SysMenuMapper;
import com.broad.system.service.SysMenuService;
import lombok.extern.slf4j.Slf4j;
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
 * @since 2022 -10-10 18:46:52
 */
@Service("sysMenuService")
@Slf4j
@CacheConfig(cacheNames = CacheConstants.ROUTE_KEY, keyGenerator = CacheConstants.CACHE_PREFIX_GENERATION)
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {


    @Override
    public List<SysMenu> findMenuByUserId(Integer userId) {
        return this.baseMapper.findMenuByUserId(userId);
    }

    @Override
    @Cacheable(key = "#userId", unless = "null == #result")
    public List<SysMenu> findTreeMenuByUserId(Integer userId) {
        List<SysMenu> list = findMenuByUserId(userId);
        return buildTreeList(list);
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
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(allEntries = true)
    public int saveMenu(SysMenu entity) {
        if (StringUtils.isNull(entity.getParentId())) {
            entity.setParentId(0);
        }
        return this.baseMapper.insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(allEntries = true)
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
            buildChildTree(menuNode, list);
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
    private SysMenu buildChildTree(SysMenu pNode, List<SysMenu> menuList) {
        List<SysMenu> childMenus = new ArrayList<>();
        for (SysMenu menuNode : menuList) {
            if (menuNode.getParentId().equals(pNode.getMenuId())) {
                // 将父节点的url拼接到子节点的url上
                String menuUrl;
                if (StringUtils.isNotBlank(menuNode.getMenuUrl())) {
                    menuUrl = pNode.getMenuUrl().concat(menuNode.getMenuUrl());
                    menuNode.setMenuUrl(menuUrl);
                }
                SysMenu sysMenu = buildChildTree(menuNode, menuList);
                childMenus.add(sysMenu);
            }
        }
        pNode.setChildren(childMenus);
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
     * 处理树形结构
     *
     * @param menuList
     */
    private List<SysMenu> buildTreeList(List<SysMenu> menuList) {
        return buildTree(menuList);
    }

    /**
     * 删除菜单和下级菜单
     */
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public int deleteMenu(List<Long> idList) {
        this.baseMapper.deleteBatchIds(idList);
        return this.baseMapper.deleteChildMenu(idList);
    }

}

