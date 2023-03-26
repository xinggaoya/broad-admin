package com.broad.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.system.entity.SysPost;
import com.broad.system.mapper.SysPostMapper;
import com.broad.system.service.SysPostService;
import org.springframework.stereotype.Service;

/**
 * 岗位信息表(SysPost)表服务实现类
 *
 * @author XingGao
 * @since 2023-03-26 16:15:01
 */
@Service("sysPostService")
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPost> implements SysPostService {

}

