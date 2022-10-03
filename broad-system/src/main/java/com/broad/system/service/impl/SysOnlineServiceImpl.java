package com.broad.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.broad.system.entity.SysAdmin;
import com.broad.system.service.SysAdminService;
import com.broad.system.service.SysOnlineService;
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
    private SysAdminService sysAdminService;

    @Override
    public List<SysAdmin> getOnlineList() {
        List<Long> ids = new ArrayList<>();
        List<String> sessionIdList = StpUtil.searchSessionId("", -1, -1, false);
        sessionIdList.forEach(item -> {
            List<String> names = Arrays.asList(item.split(":"));
            ids.add(Long.valueOf(names.get(3)));
        });
        return sysAdminService.getAdminByIds(ids);
    }
}
