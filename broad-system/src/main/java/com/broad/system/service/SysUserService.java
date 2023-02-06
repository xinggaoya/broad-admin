package com.broad.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.broad.system.entity.SysUser;

import java.util.List;

/**
 * 管理员表(SysUser)表服务接口
 *
 * @author XingGao
 * @since 2022 -07-09 17:19:40
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * Select all page.
     *
     * @param sysAdmin the sys admin
     * @return the page
     */
    List<SysUser> selectAll(SysUser sysAdmin);

    /**
     * Save admin int.
     *
     * @param sysAdmin the sys admin
     * @return the int
     */
    int saveAdmin(SysUser sysAdmin);

    /**
     * Gets admin by ids.
     *
     * @param ids the ids
     * @return the admin by ids
     */
    List<SysUser> getAdminByIds(List<Long> ids);

    /**
     * Update admin int.
     *
     * @param sysAdmin the sys admin
     * @return the int
     */
    int updateAdmin(SysUser sysAdmin);
}

