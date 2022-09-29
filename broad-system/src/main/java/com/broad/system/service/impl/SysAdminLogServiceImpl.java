package com.broad.system.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.broad.system.entity.SysAdminLog;
import com.broad.system.mapper.SysAdminLogMapper;
import com.broad.system.service.SysAdminLogService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 管理员日志表(SysAdminLog)表服务实现类
 *
 * @author XingGao
 * @since 2022 -07-11 23:20:17
 */
@Service("sysAdminLogService")
public class SysAdminLogServiceImpl extends ServiceImpl<SysAdminLogMapper, SysAdminLog> implements SysAdminLogService {

    @Override
    public List<SysAdminLog> selectAll(SysAdminLog sysAdminLog) {
        return baseMapper.selectAll(sysAdminLog);
    }

}

