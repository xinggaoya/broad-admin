package com.broad.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.common.constant.CacheConstants;
import com.broad.common.constant.Constants;
import com.broad.system.entity.SysDictData;
import com.broad.system.mapper.SysDictDataMapper;
import com.broad.system.service.SysDictDataService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 字典数据表(SysDictData)表服务实现类
 *
 * @author XingGao
 * @since 2022-10-13 15:00:02
 */
@Service("sysDictDataService")
@CacheConfig(cacheNames = Constants.SYS_DICT_KEY, keyGenerator = CacheConstants.CACHE_PREFIX_GENERATION)
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData> implements SysDictDataService {

    @Override
    @Cacheable(key = "#dictData.dictType", unless = "null == #result")
    public List<SysDictData> selectDictList(SysDictData dictData) {
        List<SysDictData> dictDataList = this.list(new LambdaQueryWrapper<>(dictData));
        return dictDataList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CachePut(key = "#dictData.dictType", unless = "null == #result")
    public void insertDictData(SysDictData dictData) {
        this.save(dictData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(key = "#dictData.dictType")
    public void updateDictData(SysDictData dictData) {
        this.updateById(dictData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(key = "#dictData.dictType")
    public void deleteDictDataById(SysDictData dictData) {
        this.removeById(dictData.getDictCode());
    }
}

