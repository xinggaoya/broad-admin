package com.broad.system.service.impl;

import com.broad.common.socket.service.UserSocketServer;
import com.broad.common.web.entity.SysUser;
import com.broad.system.service.SysOnlineService;
import com.broad.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<SysUser> getOnlineList() {
        List<Long> ids = UserSocketServer.getLoginId();
        if (ids.size() == 0) {
            return new ArrayList<>();
        }
        List<SysUser> sysAdminList = sysAdminService.getAdminByIds(ids);
//        for (SysUser sysAdmin : sysAdminList) {
//            sysAdmin.setTokenValue(StpUtil.getTokenValueByLoginId(sysAdmin.getId()));
//        }
        return sysAdminList;
    }
}
