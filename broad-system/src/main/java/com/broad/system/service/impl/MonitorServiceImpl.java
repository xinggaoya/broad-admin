package com.broad.system.service.impl;

import com.broad.system.service.MonitorService;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 监控服务实现类
 *
 * @author broad
 * @since 2024-03-06
 */
@Service("monitorService")
public class MonitorServiceImpl implements MonitorService {

    private static final int OSHI_WAIT_SECOND = 1000;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取系统监控信息，包括服务器信息、CPU、内存、磁盘、JVM等
     *
     * @return 系统监控信息
     */
    @Override
    public Map<String, Object> getSystemInfo() {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            SystemInfo si = new SystemInfo();
            HardwareAbstractionLayer hal = si.getHardware();
            OperatingSystem os = si.getOperatingSystem();

            // 服务器信息
            resultMap.put("server", getServerInfo(os));
            
            // CPU信息
            resultMap.put("cpu", getCpuInfo(hal.getProcessor()));

            // 内存信息
            resultMap.put("memory", getMemoryInfo(hal.getMemory()));

            // 磁盘信息
            resultMap.put("disk", getDiskInfo(os));

            // JVM信息
            resultMap.put("jvm", getJvmInfo());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * 获取服务状态信息
     *
     * @return 服务状态列表
     */
    @Override
    public Object getServiceStatus() {
        // 模拟服务状态数据，后续可扩展为实际服务发现和健康检查
        List<Map<String, Object>> services = new ArrayList<>();
        
        // 添加一些示例服务
        services.add(createServiceInfo("用户认证服务", "localhost", 8080, "UP", new Date()));
        services.add(createServiceInfo("数据库服务", "localhost", 3306, "UP", new Date()));
        services.add(createServiceInfo("缓存服务", "localhost", 6379, "UP", new Date()));
        services.add(createServiceInfo("消息队列服务", "localhost", 5672, "UP", new Date()));
        services.add(createServiceInfo("文件存储服务", "localhost", 9000, "UP", new Date()));
        
        return services;
    }

    /**
     * 获取服务器信息
     */
    private Map<String, Object> getServerInfo(OperatingSystem os) {
        Map<String, Object> serverInfo = new HashMap<>();
        
        try {
            InetAddress addr = InetAddress.getLocalHost();
            serverInfo.put("computerName", addr.getHostName());
            serverInfo.put("hostIp", addr.getHostAddress());
        } catch (UnknownHostException e) {
            serverInfo.put("computerName", "未知");
            serverInfo.put("hostIp", "未知");
        }
        
        serverInfo.put("osName", os.getFamily() + " " + os.getVersionInfo().getVersion());
        serverInfo.put("osArch", System.getProperty("os.arch"));
        serverInfo.put("jdkVersion", System.getProperty("java.version"));
        serverInfo.put("timeZone", System.getProperty("user.timezone"));
        
        return serverInfo;
    }

    /**
     * 获取CPU信息
     */
    private Map<String, Object> getCpuInfo(CentralProcessor processor) {
        Map<String, Object> cpuInfo = new HashMap<>();
        
        // CPU核心数
        cpuInfo.put("cpuNum", processor.getLogicalProcessorCount());
        
        // CPU利用率
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(OSHI_WAIT_SECOND);
        long[] ticks = processor.getSystemCpuLoadTicks();
        
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long system = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        
        long total = user + nice + system + idle + iowait + irq + softirq + steal;
        
        cpuInfo.put("used", formatDouble((user + nice + system) * 100.0 / total));
        cpuInfo.put("sys", formatDouble(system * 100.0 / total));
        cpuInfo.put("free", formatDouble(idle * 100.0 / total));
        cpuInfo.put("usageRate", formatDouble((1.0 - (idle * 1.0 / total)) * 100));
        
        return cpuInfo;
    }

    /**
     * 获取内存信息
     */
    private Map<String, Object> getMemoryInfo(GlobalMemory memory) {
        Map<String, Object> memoryInfo = new HashMap<>();
        
        long total = memory.getTotal();
        long available = memory.getAvailable();
        
        memoryInfo.put("total", total);
        memoryInfo.put("used", total - available);
        memoryInfo.put("free", available);
        memoryInfo.put("usageRate", formatDouble((total - available) * 100.0 / total));
        
        return memoryInfo;
    }

    /**
     * 获取磁盘信息
     */
    private Map<String, Object> getDiskInfo(OperatingSystem os) {
        Map<String, Object> diskInfo = new HashMap<>();
        
        FileSystem fileSystem = os.getFileSystem();
        List<OSFileStore> fsArray = fileSystem.getFileStores();
        
        long total = 0L;
        long free = 0L;
        long used = 0L;
        
        List<Map<String, Object>> disksList = new ArrayList<>();
        
        for (OSFileStore fs : fsArray) {
            Map<String, Object> disk = new HashMap<>();
            disk.put("dirName", fs.getMount());
            disk.put("type", fs.getType());
            disk.put("total", fs.getTotalSpace());
            disk.put("free", fs.getUsableSpace());
            disk.put("used", fs.getTotalSpace() - fs.getUsableSpace());
            disk.put("usage", formatDouble((fs.getTotalSpace() - fs.getUsableSpace()) * 100.0 / fs.getTotalSpace()));
            disksList.add(disk);
            
            total += fs.getTotalSpace();
            free += fs.getUsableSpace();
            used += (fs.getTotalSpace() - fs.getUsableSpace());
        }
        
        diskInfo.put("total", total);
        diskInfo.put("free", free);
        diskInfo.put("used", used);
        diskInfo.put("usageRate", formatDouble(used * 100.0 / total));
        diskInfo.put("disks", disksList);
        
        return diskInfo;
    }

    /**
     * 获取JVM信息
     */
    private Map<String, Object> getJvmInfo() {
        Map<String, Object> jvmInfo = new HashMap<>();
        
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        
        long max = heapMemoryUsage.getMax();
        long total = heapMemoryUsage.getCommitted();
        long used = heapMemoryUsage.getUsed();
        long free = max - used;
        
        jvmInfo.put("max", max);
        jvmInfo.put("total", total);
        jvmInfo.put("used", used);
        jvmInfo.put("free", free);
        jvmInfo.put("version", System.getProperty("java.version"));
        jvmInfo.put("name", System.getProperty("java.vm.name"));
        jvmInfo.put("usageRate", formatDouble(used * 100.0 / max));
        
        // JVM启动时间
        long startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
        jvmInfo.put("startTime", sdf.format(new Date(startTime)));
        
        // JVM运行时间
        long runTime = System.currentTimeMillis() - startTime;
        long day = runTime / (1000 * 60 * 60 * 24);
        long hour = (runTime % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minute = (runTime % (1000 * 60 * 60)) / (1000 * 60);
        jvmInfo.put("runTime", day + "天" + hour + "小时" + minute + "分钟");
        
        return jvmInfo;
    }

    /**
     * 创建服务信息对象
     */
    private Map<String, Object> createServiceInfo(String name, String address, int port, String status, Date checkTime) {
        Map<String, Object> service = new HashMap<>();
        service.put("serviceName", name);
        service.put("serviceAddress", address);
        service.put("servicePort", port);
        service.put("status", status);
        service.put("lastCheckTime", sdf.format(checkTime));
        return service;
    }

    /**
     * 格式化双精度数字
     */
    private double formatDouble(double value) {
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(value));
    }
} 