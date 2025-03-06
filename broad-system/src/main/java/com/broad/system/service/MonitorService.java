package com.broad.system.service;

import java.util.Map;

/**
 * 监控服务接口
 *
 * @author broad
 * @since 2024-03-06
 */
public interface MonitorService {

    /**
     * 获取系统监控信息，包括服务器信息、CPU、内存、磁盘、JVM等
     *
     * @return 系统监控信息
     */
    Map<String, Object> getSystemInfo();

    /**
     * 获取服务状态信息
     *
     * @return 服务状态列表
     */
    Object getServiceStatus();
} 