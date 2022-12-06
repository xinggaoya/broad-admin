package com.broad.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.broad.common.web.entity.SysUserLog;

import java.util.List;

/**
 * 管理员日志表(SysUserLog)表服务接口
 *
 * @author XingGao
 * @since 2022 -07-11 23:20:17
 */
public interface SysUserLogService extends IService<SysUserLog> {

    /**
     * Select all page.
     *
     * @param sysAdminLog the sys admin log
     * @return the page
     */
    List<SysUserLog> selectAll(SysUserLog sysAdminLog);
}

