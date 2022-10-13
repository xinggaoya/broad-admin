package com.broad.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.system.entity.SysDictType;
import com.broad.system.mapper.SysDictTypeMapper;
import com.broad.system.service.SysDictTypeService;
import org.springframework.stereotype.Service;

/**
 * 字典类型表(SysDictType)表服务实现类
 *
 * @author XingGao
 * @since 2022-10-13 15:00:03
 */
@Service("sysDictTypeService")
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements SysDictTypeService {

}

