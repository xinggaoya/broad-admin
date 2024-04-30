package com.broad.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.broad.common.constant.ThreadPoolConstant;
import com.broad.system.entity.SysLoginLog;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * (SysLoginLog)表服务接口
 *
 * @author XingGao
 * @since 2022 -11-22 19:57:59
 */
public interface SysLoginLogService extends IService<SysLoginLog> {

    /**
     * 分页查询登录日志
     *
     * @param loginLog the login log
     * @return the list
     */
    List<SysLoginLog> findLoginLogList(SysLoginLog loginLog);

    /**
     * 保存登录日志
     *
     * @param request     请求体
     * @param sysLoginLog the sys login log
     */
    @Async(ThreadPoolConstant.SERVICE_EXECUTOR)
    void saveLoginLog(HttpServletRequest request, SysLoginLog sysLoginLog);
}

