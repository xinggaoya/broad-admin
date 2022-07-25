package com.broad.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.broad.system.entity.SysAdminLog;

import java.util.List;

/**
 * 管理员日志表(SysAdminLog)表服务接口
 *
 * @author XingGao
 * @since 2022-07-11 23:20:17
 */
public interface SysAdminLogService extends IService<SysAdminLog> {

    Page<SysAdminLog> selectAll(Page<SysAdminLog> page, SysAdminLog sysAdminLog);
}

