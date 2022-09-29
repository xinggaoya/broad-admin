package com.broad.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.broad.system.entity.SysAdminLog;

import java.util.List;

/**
 * 管理员日志表(SysAdminLog)表服务接口
 *
 * @author XingGao
 * @since 2022 -07-11 23:20:17
 */
public interface SysAdminLogService extends IService<SysAdminLog> {

    /**
     * Select all page.
     *
     * @param sysAdminLog the sys admin log
     * @return the page
     */
    List<SysAdminLog> selectAll(SysAdminLog sysAdminLog);
}

