package com.broad.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.broad.system.entity.SysMenu;

import java.util.List;

/**
 * (SysMenu)表服务接口
 *
 * @author XingGao
 * @since 2022-10-10 18:46:52
 */
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> selectAll();
}

