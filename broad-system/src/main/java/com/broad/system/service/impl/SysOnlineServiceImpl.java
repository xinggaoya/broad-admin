package com.broad.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.broad.system.entity.SysUser;
import com.broad.system.service.SysOnlineService;
import com.broad.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: XingGao
 * @Date: 2022/10/4
 * @Description:
 */
@Service("sysOnlineService")
public class SysOnlineServiceImpl implements SysOnlineService {

    @Autowired
    private SysUserService sysAdminService;

    @Override
    public List<SysUser> getOnlineList() {
        List<Long> ids = new ArrayList<>();
        List<String> sessionIdList = StpUtil.searchSessionId("", -1, -1, false);
        sessionIdList.forEach(item -> {
            List<String> names = Arrays.asList(item.split(":"));
            ids.add(Long.valueOf(names.get(3)));
        });
        List<SysUser> sysAdminList = sysAdminService.getAdminByIds(ids);
        for (SysUser sysAdmin : sysAdminList) {
            sysAdmin.setTokenValue(StpUtil.getTokenValueByLoginId(sysAdmin.getId()));
        }
        return sysAdminList;
    }
}
