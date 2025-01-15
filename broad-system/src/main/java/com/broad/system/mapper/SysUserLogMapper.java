package com.broad.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.broad.system.entity.SysUserLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 管理员日志表(SysUserLog)表数据库访问层
 *
 * @author XingGao
 * @since 2022 -07-11 23:20:16
 */
@Mapper
public interface SysUserLogMapper extends BaseMapper<SysUserLog> {

    /**
     * Select all page.
     *
     * @param sysAdminLog the sys admin log
     * @return the page
     */
    IPage<SysUserLog> selectAll(Page<SysUserLog> page, SysUserLog sysAdminLog);
}

