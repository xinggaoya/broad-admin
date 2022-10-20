package com.broad.system.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.system.entity.SysUserRole;
import com.broad.system.mapper.SysUserRoleMapper;
import com.broad.system.service.SysUserRoleService;
import org.springframework.stereotype.Service;

/**
 * 管理权限分组表(SysUserRole)表服务实现类
 *
 * @author XingGao
 * @since 2022 -07-13 10:13:11
 */
@Service("sysUserRoleAccessService")
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

}

