package com.broad.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.broad.common.socket.service.UserSocketServer;
import com.broad.common.web.controller.BaseController;
import com.broad.common.web.entity.ResultData;
import com.broad.common.web.page.TableDataInfo;
import com.broad.system.entity.SysUser;
import com.broad.system.service.SysOnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    /**
     * 分页查询在线用户
     *
     * @return the online list
     */
    @GetMapping
    @SaCheckPermission("online:list")
    public TableDataInfo getOnlineList() {
        startPage();
        return getDataTable(sysOnlineService.getOnlineList());
    }

    /**
     * 强退用户
     *
     * @param sysAdmin the sys admin
     * @return the result data
     */
    @DeleteMapping("/forceLogout")
    @SaCheckPermission("online:delete")
    public ResultData forceLogout(SysUser sysAdmin) {
        StpUtil.kickout(sysAdmin.getId());
        UserSocketServer.sendInfo(ResultData.error(), sysAdmin.getId().toString());
        return ResultData.ok();
    }

    /**
     * 封禁用户
     */
    @DeleteMapping("/ban")
    @SaCheckPermission("online:delete")
    public ResultData ban(SysUser sysAdmin) {
        StpUtil.kickout(sysAdmin.getId());
        StpUtil.disable(sysAdmin.getId(), 60 * 60 * 1000);
        UserSocketServer.sendInfo(ResultData.error(), sysAdmin.getId().toString());
        return ResultData.ok();
    }
}
