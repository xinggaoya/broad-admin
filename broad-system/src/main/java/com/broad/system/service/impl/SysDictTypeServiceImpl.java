package com.broad.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.system.entity.SysDictData;
import com.broad.system.entity.SysDictType;
import com.broad.system.mapper.SysDictTypeMapper;
import com.broad.system.service.SysDictDataService;
import com.broad.system.service.SysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 字典类型表(SysDictType)表服务实现类
 *
 * @author XingGao
 * @since 2022-10-13 15:00:03
 */
@Service("sysDictTypeService")
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements SysDictTypeService {

    @Autowired
    private SysDictDataService dictDataService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDictTypeById(Long id) {
        SysDictType dictType = this.getById(id);
        if (dictType != null) {
            this.removeById(dictType.getDictId());
            dictDataService.remove(new LambdaUpdateWrapper<SysDictData>().eq(SysDictData::getDictType, dictType.getDictType()));
            return true;
        }
        return false;
    }

}

