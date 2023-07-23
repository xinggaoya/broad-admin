package com.broad.system.service;

import com.broad.system.entity.SysMonitor;

/**
 * @author: XingGao
 * @date: 2023/7/15
 * @description:
 */
public interface SysMonitorService {

    /**
     * 获取系统信息
     */
    SysMonitor getServer();
}
