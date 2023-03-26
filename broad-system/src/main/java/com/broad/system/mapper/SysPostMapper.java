package com.broad.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.broad.system.entity.SysPost;

/**
 * 岗位信息表(SysPost)表数据库访问层
 *
 * @author XingGao
 * @since 2023-03-26 16:15:01
 */
public interface SysPostMapper extends BaseMapper<SysPost> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysPost> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SysPost> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysPost> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<SysPost> entities);

}

