package com.broad.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.broad.system.entity.SysUser;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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

    List<SysUser> getAdminByIds(List<Long> ids);

    /**
     * Update admin int.
     *
     * @param sysAdmin the sys admin
     * @return the int
     */
    int updateAdmin(SysUser sysAdmin);

    /**
     * Administrator login object.
     *
     * @param sysAdmin the sys admin
     * @param request  the request
     * @return the object
     * @throws IOException the io exception
     */
    Object administratorLogin(SysUser sysAdmin, HttpServletRequest request) throws IOException;

    /**
     * Logout.
     *
     * @param admin the admin
     */
    void logout();
}

