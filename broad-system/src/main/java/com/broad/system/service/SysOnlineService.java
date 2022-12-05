package com.broad.system.service;

import com.broad.common.web.entity.SysUser;

import java.util.List;

/**
 * The interface Sys online service.
 *
 * @Author: XingGao
 * @Date: 2022 /10/4
 * @Description:
 */
public interface SysOnlineService {
    /**
     * Gets online list.
     *
     * @return the online list
     */
    List<SysUser> getOnlineList();
}
