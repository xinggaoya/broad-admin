package com.broad.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.broad.system.entity.SysDictData;

import java.util.List;

/**
 * 字典数据表(SysDictData)表服务接口
 *
 * @author XingGao
 * @since 2022-10-13 15:00:02
 */
public interface SysDictDataService extends IService<SysDictData> {

    /**
     * 查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    List<SysDictData> selectDictList(SysDictData dictData);
}

