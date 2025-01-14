package com.broad.system.entity;

import lombok.Data;
import java.io.Serializable;

/**
 * @author: XingGao
 * @date: 2023/7/15
 * @description:
 */
@Data
public class SysMonitor implements Serializable {

    /**
     * 内存总量
     */
    private long totalMemory;

    /**
     * 剩余内存
     */
    private long freeMemory;

    /**
     * cpu核心数
     */
    private int availableProcessors;

    /**
     * 系统名称
     */
    private String osName;

    /**
     * 系统架构
     */
    private String osArch;

    /**
     * 系统版本
     */
    private String osVersion;

    /**
     * 系统用户
     */
    private String userName;

    /**
     * 系统主机名
     */
    private String hostName;

    /**
     * 系统ip
     */
    private String hostIp;
}
