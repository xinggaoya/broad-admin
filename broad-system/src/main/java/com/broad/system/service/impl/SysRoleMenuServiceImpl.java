package com.broad.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.common.constant.CacheConstants;
import com.broad.common.exception.ServiceException;
import com.broad.common.utils.StringUtils;
import com.broad.system.entity.SysMenu;
import com.broad.system.entity.SysRoleMenu;
import com.broad.system.mapper.SysRoleMenuMapper;
import com.broad.system.service.SysMenuService;
import com.broad.system.service.SysRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理权限分组表(SysRoleMenu)表服务实现类
 *
 * @author XingGao
 * @since 2022 -10-19 17:15:02
 */
@Service("sysRoleMenuService")
@CacheConfig(cacheNames = CacheConstants.ROUTE_KEY, keyGenerator = CacheConstants.CACHE_PREFIX_GENERATION)
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {

    @Autowired
    private SysMenuService menuService;

    /**
     * 查询权限码
     *
     * @param userId 查询实体
     * @return 所有数据 list
     */
    @Override
    public List<String> findRoleMenuCodeByUserId(Integer userId) {
        return menuService.findMenuByUserId(userId)
                .stream()
                .map(SysMenu::getPerme)
                // 过滤空权限码
                .filter(StringUtils::isNotEmpty)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(allEntries = true)
    public int addRoleMenu(SysRoleMenu roleMenu) {
        List<SysRoleMenu> list = new ArrayList<>();
        // 删除角色与菜单关联
        this.lambdaUpdate().eq(SysRoleMenu::getRoleId, roleMenu.getRoleId()).remove();
        if (roleMenu.getMenuIds().size() == 0) {
            throw new ServiceException("请选择菜单");
        }
        // 新增角色与菜单管理
        for (Integer menuId : roleMenu.getMenuIds()) {
            list.add(SysRoleMenu.builder()
                    .roleId(roleMenu.getRoleId())
                    .menuId(menuId)
                    .build());
        }
        return baseMapper.insertBatch(list);
    }

}

