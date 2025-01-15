package com.broad.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.common.utils.PageUtils;
import com.broad.common.utils.ip.IpUtils;
import com.broad.system.entity.SysLoginLog;
import com.broad.system.mapper.SysLoginLogMapper;
import com.broad.system.service.SysLoginLogService;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * (SysLoginLog)表服务实现类
 *
 * @author XingGao
 * @since 2022 -11-22 19:57:59
 */
@Service("sysLoginLogService")
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements SysLoginLogService {

    @Override
    public IPage<SysLoginLog> findLoginLogList(SysLoginLog loginLog) {
        Page<SysLoginLog> page = PageUtils.startPage();
        return baseMapper.findLoginLogList(page, loginLog);
    }

    /**
     * 保存登录日志
     *
     * @param request 请求体
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveLoginLog(HttpServletRequest request, SysLoginLog sysLoginLog) {
        // 获取用户代理对象
        String ip = IpUtils.getIpAddr(request);
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        sysLoginLog.setBrowser(userAgent.getBrowser().getName());
        sysLoginLog.setClientType(userAgent.getOperatingSystem().getDeviceType().getName());
        sysLoginLog.setOperatingSystem(userAgent.getOperatingSystem().getName());
        sysLoginLog.setLoginIp(ip);
        sysLoginLog.setLoginAddress(IpUtils.getIpAddress(ip));
        this.save(sysLoginLog);
    }

}
