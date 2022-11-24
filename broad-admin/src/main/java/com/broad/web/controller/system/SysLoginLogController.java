package com.broad.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.broad.common.web.controller.BaseController;
import com.broad.common.web.entity.ResultData;
import com.broad.common.web.page.TableDataInfo;
import com.broad.system.entity.SysLoginLog;
import com.broad.system.service.SysLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * (SysLoginLog)表控制层
 *
 * @author XingGao
 * @since 2022-11-22 19:57:59
 */
@RestController
@RequestMapping("sysLoginLog")
public class SysLoginLogController extends BaseController {
    /**
     * 服务对象
     */
    @Autowired
    private SysLoginLogService sysLoginLogService;

    /**
     * 分页查询所有数据
     *
     * @param sysLoginLog 查询实体
     * @return 所有数据
     */
    @GetMapping
    @SaCheckPermission("sys:loginLog:list")
    public TableDataInfo selectAll(SysLoginLog sysLoginLog) {
        startPage();
        return getDataTable(this.sysLoginLogService.findLoginLogList(sysLoginLog));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @SaCheckPermission("sys:loginLog:list")
    public ResultData selectOne(@PathVariable Serializable id) {
        return ResultData.success(this.sysLoginLogService.getById(id));
    }

}

