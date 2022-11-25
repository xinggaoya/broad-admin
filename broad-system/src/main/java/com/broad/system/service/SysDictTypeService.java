package com.broad.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.broad.system.entity.SysDictType;

/**
 * 字典类型表(SysDictType)表服务接口
 *
 * @author XingGao
 * @since 2022 -10-13 15:00:03
 */
public interface SysDictTypeService extends IService<SysDictType> {

    /**
     * 根据id删除字典类型和下级字典数据
     *
     * @param id the id
     * @return boolean
     */
    boolean deleteDictTypeById(Long id);
}

