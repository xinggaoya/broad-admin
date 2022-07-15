package com.broad.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.broad.system.entity.SysAdminGroup;

import java.util.List;

/**
 * 管理分组表(SysAdminGroup)表服务接口
 *
 * @author XingGao
 * @since 2022-07-13 09:53:11
 */
public interface SysAdminGroupService extends IService<SysAdminGroup> {

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param sysAdminGroup 查询实体
     * @return 所有数据
     */
    Page<SysAdminGroup> selectAll(Page<SysAdminGroup> page, SysAdminGroup sysAdminGroup);
}

