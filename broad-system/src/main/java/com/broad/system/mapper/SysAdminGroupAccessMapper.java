package com.broad.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.broad.system.entity.SysAdminGroupAccess;

/**
 * 管理权限分组表(SysAdminGroupAccess)表数据库访问层
 *
 * @author XingGao
 * @since 2022-07-13 10:13:11
 */
@Mapper
public interface SysAdminGroupAccessMapper extends BaseMapper<SysAdminGroupAccess> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysAdminGroupAccess> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SysAdminGroupAccess> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysAdminGroupAccess> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<SysAdminGroupAccess> entities);

}

