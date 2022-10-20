package com.broad.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.broad.system.entity.SysRoleDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (SysRoleDept)表数据库访问层
 *
 * @author XingGao
 * @since 2022-10-19 17:15:02
 */
public interface SysRoleDeptMapper extends BaseMapper<SysRoleDept> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysRoleDept> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SysRoleDept> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysRoleDept> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<SysRoleDept> entities);

}

