package com.broad.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.broad.system.entity.SysLoginLog;

import java.util.List;

/**
 * (SysLoginLog)表数据库访问层
 *
 * @author XingGao
 * @since 2022-11-22 19:57:59
 */
public interface SysLoginLogMapper extends BaseMapper<SysLoginLog> {

   List<SysLoginLog> findLoginLogList(SysLoginLog loginLog);

}

