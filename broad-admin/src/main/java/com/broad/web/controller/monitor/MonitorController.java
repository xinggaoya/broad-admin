package com.broad.web.controller.monitor;

import com.broad.common.web.controller.BaseController;
import com.broad.common.web.entity.ResultData;
import com.broad.system.service.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统监控控制器
 *
 * @author broad
 * @since 2024-03-06
 */
@RestController
@RequestMapping("monitor")
public class MonitorController extends BaseController {

    @Autowired
    private MonitorService monitorService;

    /**
     * 获取系统监控信息
     */
    @GetMapping("system")
    public ResultData getSystemInfo() {
        return success(monitorService.getSystemInfo());
    }

    /**
     * 获取服务状态信息
     */
    @GetMapping("service")
    public ResultData getServiceStatus() {
        return success(monitorService.getServiceStatus());
    }
} 