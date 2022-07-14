package com.broad.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.system.mapper.SysAdminGroupMapper;
import com.broad.system.entity.SysAdminGroup;
import com.broad.system.service.SysAdminGroupService;
import org.springframework.stereotype.Service;

/**
 * 管理分组表(SysAdminGroup)表服务实现类
 *
 * @author XingGao
 * @since 2022-07-13 09:53:11
 */
@Service("sysAdminGroupService")
public class SysAdminGroupServiceImpl extends ServiceImpl<SysAdminGroupMapper, SysAdminGroup> implements SysAdminGroupService {

}

