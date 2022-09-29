package com.broad.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.broad.system.entity.SysAdminGroup;

import java.util.List;

/**
 * 管理分组表(SysAdminGroup)表服务接口
 *
 * @author XingGao
 * @since 2022 -07-13 09:53:11
 */
public interface SysAdminGroupService extends IService<SysAdminGroup> {

    /**
     * 分页查询所有数据
     *
     * @param sysAdminGroup 查询实体
     * @return 所有数据 page
     */
    List<SysAdminGroup> selectAll(SysAdminGroup sysAdminGroup);

    /**
     * Save admin group int.
     *
     * @param sysAdminGroup the sys admin group
     * @return the int
     */
    int saveAdminGroup(SysAdminGroup sysAdminGroup);

    /**
     * Update admin group int.
     *
     * @param sysAdminGroup the sys admin group
     * @return the int
     */
    int updateAdminGroup(SysAdminGroup sysAdminGroup);

    /**
     * Delete admin group int.
     *
     * @param idList the id list
     * @return the int
     */
    int deleteAdminGroup(List<Long> idList);
}

