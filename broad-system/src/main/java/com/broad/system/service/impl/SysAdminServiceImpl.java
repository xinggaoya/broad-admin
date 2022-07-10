package com.broad.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.system.entity.SysAdmin;
import com.broad.system.mapper.SysAdminMapper;
import com.broad.system.service.SysAdminService;
import org.springframework.stereotype.Service;

/**
 * 管理员表(SysAdmin)表服务实现类
 *
 * @author XingGao
 * @since 2022-07-09 17:19:40
 */
@Service
public class SysAdminServiceImpl extends ServiceImpl<SysAdminMapper, SysAdmin> implements SysAdminService {

}

