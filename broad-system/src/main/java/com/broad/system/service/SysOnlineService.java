package com.broad.system.service;

import com.broad.system.entity.SysAdmin;

import java.util.List;

/**
 * @Author: XingGao
 * @Date: 2022/10/4
 * @Description:
 */
public interface SysOnlineService {
    List<SysAdmin> getOnlineList();
}
