package com.broad.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.broad.framework.web.controller.BaseController;
import com.broad.framework.web.entity.ResultData;
import com.broad.framework.web.page.TableDataInfo;
import com.broad.system.entity.SysAdmin;
import com.broad.system.service.SysOnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统监控(SysMonitor)表控制层
 *
 * @Author: XingGao
 * @Date: 2022 /07/12 14:34
 * @Description:
 */
@RestController
@RequestMapping("/monitor/online")
public class SysOnlineController extends BaseController {

    @Autowired
    private SysOnlineService sysOnlineService;

    @GetMapping
    @SaCheckPermission("online:list")
    public TableDataInfo getOnlineList() {
        startPage();
        return getDataTable(sysOnlineService.getOnlineList());
    }

    @PutMapping("/forceLogout")
    @SaCheckPermission("online:delete")
    public ResultData forceLogout(SysAdmin sysAdmin) {
        StpUtil.kickout(sysAdmin.getId());
        return ResultData.ok();
    }
}
