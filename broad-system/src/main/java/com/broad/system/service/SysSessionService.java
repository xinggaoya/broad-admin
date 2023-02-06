package com.broad.system.service;

import com.broad.system.entity.SysUser;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author: XingGao
 * @date: 2023/02/06 10:23
 * @description:
 */
public interface SysSessionService {

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
