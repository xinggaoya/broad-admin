package com.broad.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.broad.system.entity.SysMenu;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * (SysMenu)表服务接口
 *
 * @author XingGao
 * @since 2022-10-10 18:46:52
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 根据角色获取对应菜单
     *
     * @return 菜单列表
     */
    List<SysMenu> selectAll();

    List<SysMenu> selectAllByPage(SysMenu menu);

    int saveMenu(SysMenu entity);
}

