package com.broad.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.broad.common.web.socket.WebSocketSessionManager;
import com.broad.system.entity.SysUser;
import com.broad.system.service.SysOnlineService;
import com.broad.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Sys online service.
 *
 * @Author: XingGao
 * @Date: 2022 /10/4
 * @Description:
 */
@Service("sysOnlineService")
public class SysOnlineServiceImpl implements SysOnlineService {

    @Autowired
    private SysUserService sysAdminService;

    @Autowired
    private WebSocketSessionManager webSocketSessionManager;

    @Override
    public List<SysUser> getOnlineList() {
        List<String> onlineUserIds = webSocketSessionManager.getOnlineUsers();
        if (onlineUserIds.isEmpty()) {
            return new ArrayList<>();
        }

        // 转换为Long类型列表
        List<Long> ids = onlineUserIds.stream()
                .filter(id -> id.matches("\\d+"))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        if (ids.isEmpty()) {
            return new ArrayList<>();
        }

        List<SysUser> sysAdminList = sysAdminService.getAdminByIds(ids);
        for (SysUser sysAdmin : sysAdminList) {
            sysAdmin.setTokenValue(StpUtil.getTokenValueByLoginId(sysAdmin.getId()));
        }
        return sysAdminList;
    }
}
