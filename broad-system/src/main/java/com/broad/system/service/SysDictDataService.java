package com.broad.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.broad.system.entity.SysDictData;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 字典数据表(SysDictData)表服务接口
 *
 * @author XingGao
 * @since 2022 -10-13 15:00:02
 */
public interface SysDictDataService extends IService<SysDictData> {

    /**
     * 查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息 做了缓存
     */
    List<SysDictData> selectDictList(SysDictData dictData);

    /**
     * Insert dict data.
     *
     * @param dictData the dict data
     */
    @Transactional(rollbackFor = Exception.class)
    void insertDictData(SysDictData dictData);

    /**
     * Update dict data.
     *
     * @param dictData the dict data
     */
    @Transactional(rollbackFor = Exception.class)
    void updateDictData(SysDictData dictData);

    /**
     * Delete dict data by id.
     *
     * @param dictData the dict data
     */
    @Transactional(rollbackFor = Exception.class)
    void deleteDictDataById(SysDictData dictData);
}

