package com.broad.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.broad.system.entity.SysAdmin;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * 管理员表(SysAdmin)表服务接口
 *
 * @author XingGao
 * @since 2022-07-09 17:19:40
 */
public interface SysAdminService extends IService<SysAdmin> {

    Page<SysAdmin> selectAll(Page<SysAdmin> page, SysAdmin sysAdmin);

    int saveAdmin(SysAdmin sysAdmin);

    int updateAdmin(SysAdmin sysAdmin);

    Object administratorLogin(SysAdmin sysAdmin, HttpServletRequest request) throws IOException;

    void logout(SysAdmin admin);
}

