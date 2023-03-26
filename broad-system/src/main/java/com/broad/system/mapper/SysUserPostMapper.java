package com.broad.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.broad.system.entity.SysUserPost;

/**
 * 用户与岗位关联表(SysUserPost)表数据库访问层
 *
 * @author XingGao
 * @since 2023-03-26 16:15:01
 */
public interface SysUserPostMapper extends BaseMapper<SysUserPost> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysUserPost> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SysUserPost> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysUserPost> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<SysUserPost> entities);

}

