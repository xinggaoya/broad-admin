package com.broad.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.broad.system.entity.SysAdminLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 管理员日志表(SysAdminLog)表数据库访问层
 *
 * @author XingGao
 * @since 2022 -07-11 23:20:16
 */
@Mapper
public interface SysAdminLogMapper extends BaseMapper<SysAdminLog> {

    /**
     * Select all page.
     *
     * @param sysAdminLog the sys admin log
     * @return the page
     */
    List<SysAdminLog> selectAll(SysAdminLog sysAdminLog);
}

