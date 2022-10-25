package com.broad.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.common.constant.CacheConstants;
import com.broad.common.constant.Constants;
import com.broad.common.service.RedisService;
import com.broad.system.entity.SysDictData;
import com.broad.system.mapper.SysDictDataMapper;
import com.broad.system.service.SysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典数据表(SysDictData)表服务实现类
 *
 * @author XingGao
 * @since 2022-10-13 15:00:02
 */
@Service("sysDictDataService")
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData> implements SysDictDataService {

    @Autowired
    private RedisService redisService;

    @Override
    public List<SysDictData> selectDictList(SysDictData dictData) {
        if (redisService.hasKey(Constants.SYS_DICT_KEY + dictData.getDictType())) {
            return redisService.getCacheObject(Constants.SYS_DICT_KEY + dictData.getDictType());
        } else {
            List<SysDictData> dictDataList = this.list(new LambdaQueryWrapper<>(dictData));
            redisService.setCacheObject(Constants.SYS_DICT_KEY + dictData.getDictType(), dictDataList, CacheConstants.EXPIRATION);
            return dictDataList;
        }
    }
}

