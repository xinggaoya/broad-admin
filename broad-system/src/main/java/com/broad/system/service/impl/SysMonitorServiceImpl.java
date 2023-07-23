package com.broad.system.service.impl;

import com.broad.system.entity.SysMonitor;
import com.broad.system.service.SysMonitorService;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author: XingGao
 * @date: 2023/7/15
 * @description:
 */
@Service
public class SysMonitorServiceImpl implements SysMonitorService {
    /**
     * 获取系统信息
     */
    @Override
    public SysMonitor getServer() {
        // 获取内存
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        // 获取cpu核心数
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        // 获取系统信息
        String osName = System.getProperty("os.name");
        // 获取系统架构
        String osArch = System.getProperty("os.arch");
        // 获取系统版本
        String osVersion = System.getProperty("os.version");
        // 获取系统用户
        String userName = System.getProperty("user.name");


        SysMonitor sysMonitor = new SysMonitor();
        sysMonitor.setTotalMemory(totalMemory);
        sysMonitor.setFreeMemory(freeMemory);
        sysMonitor.setAvailableProcessors(availableProcessors);
        sysMonitor.setOsName(osName);
        sysMonitor.setOsArch(osArch);
        sysMonitor.setOsVersion(osVersion);
        sysMonitor.setUserName(userName);

        try {
            InetAddress addr = InetAddress.getLocalHost();
            // 获取系统主机名
            sysMonitor.setHostName(addr.getHostName());
            // 获取系统ip
            sysMonitor.setHostIp(addr.getHostAddress());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        return sysMonitor;

    }
}
