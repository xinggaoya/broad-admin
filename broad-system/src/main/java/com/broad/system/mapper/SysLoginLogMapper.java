package com.broad.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.broad.system.entity.SysLoginLog;

import java.util.List;

/**
 * (SysLoginLog)表数据库访问层
 *
 * @author XingGao
 * @since 2022 -11-22 19:57:59
 */
public interface SysLoginLogMapper extends BaseMapper<SysLoginLog> {

    /**
     * Find login log list list.
     *
     * @param loginLog the login log
     * @return the list
     */
    IPage<SysLoginLog> findLoginLogList(Page<SysLoginLog> page, SysLoginLog loginLog);

}

