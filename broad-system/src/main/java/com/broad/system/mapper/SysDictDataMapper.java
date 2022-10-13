package com.broad.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.broad.system.entity.SysDictData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典数据表(SysDictData)表数据库访问层
 *
 * @author XingGao
 * @since 2022-10-13 15:00:02
 */
public interface SysDictDataMapper extends BaseMapper<SysDictData> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysDictData> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SysDictData> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysDictData> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<SysDictData> entities);

}

