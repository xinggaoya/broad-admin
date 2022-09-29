package com.broad.system.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.broad.system.entity.SysAdminGroupAccess;
import com.broad.system.mapper.SysAdminGroupAccessMapper;
import com.broad.system.service.SysAdminGroupAccessService;
import org.springframework.stereotype.Service;

/**
 * 管理权限分组表(SysAdminGroupAccess)表服务实现类
 *
 * @author XingGao
 * @since 2022 -07-13 10:13:11
 */
@Service("sysAdminGroupAccessService")
public class SysAdminGroupAccessServiceImpl extends ServiceImpl<SysAdminGroupAccessMapper, SysAdminGroupAccess> implements SysAdminGroupAccessService {

}

