package com.broad.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.broad.system.entity.SysAdminLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 管理员日志表(SysAdminLog)表数据库访问层
 *
 * @author XingGao
 * @since 2022-07-11 23:20:16
 */
@Mapper
public interface SysAdminLogMapper extends BaseMapper<SysAdminLog> {

    Page<SysAdminLog> selectAll(Page<SysAdminLog> page, SysAdminLog sysAdminLog);
}

