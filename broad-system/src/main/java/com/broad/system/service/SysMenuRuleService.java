package com.broad.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.broad.system.entity.SysMenuRule;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单和权限规则表(SysMenuRule)表服务接口
 *
 * @author XingGao
 * @since 2022-07-13 09:36:33
 */
public interface SysMenuRuleService extends IService<SysMenuRule> {

    List<SysMenuRule> getRouteMenuByAdmin();


    List<SysMenuRule> getRouteMenuAll(SysMenuRule sysMenuRule);

    SysMenuRule getRouteById(Serializable id);

    List<SysMenuRule> getRouteByAdminList();
}

