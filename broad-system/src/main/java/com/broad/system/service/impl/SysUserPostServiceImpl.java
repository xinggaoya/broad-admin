package com.broad.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.system.entity.SysUserPost;
import com.broad.system.mapper.SysUserPostMapper;
import com.broad.system.service.SysUserPostService;
import org.springframework.stereotype.Service;

/**
 * 用户与岗位关联表(SysUserPost)表服务实现类
 *
 * @author XingGao
 * @since 2023-03-26 16:15:01
 */
@Service("sysUserPostService")
public class SysUserPostServiceImpl extends ServiceImpl<SysUserPostMapper, SysUserPost> implements SysUserPostService {

}

