package com.broad.system.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.system.entity.SysUserLog;
import com.broad.system.mapper.SysUserLogMapper;
import com.broad.system.service.SysUserLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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
        if (StringUtils.isNotBlank(sysAdminLog.getOperatingTime())){
            // 使用%分割
            List<Date> dates = new ArrayList<>();
            String[] split = sysAdminLog.getOperatingTime().split(",");
            // 将时间戳转时间
             dates.add(new Date(Long.parseLong(split[0])));
             dates.add(new Date(Long.parseLong(split[1])));
             sysAdminLog.setOperatingTimeList(dates);
        }
        return baseMapper.selectAll(sysAdminLog);
    }

}

