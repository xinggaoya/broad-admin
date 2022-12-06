package com.broad.system.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.common.web.entity.SysUserLog;
import com.broad.system.mapper.SysUserLogMapper;
import com.broad.system.service.SysUserLogService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 管理员日志表(SysUserLog)表服务实现类
 *
 * @author XingGao
 * @since 2022 -07-11 23:20:17
 */
@Service("sysAdminLogService")
public class SysUserLogServiceImpl extends ServiceImpl<SysUserLogMapper, SysUserLog> implements SysUserLogService {

    @Override
    public List<SysUserLog> selectAll(SysUserLog sysAdminLog) {
        return baseMapper.selectAll(sysAdminLog);
    }

}

