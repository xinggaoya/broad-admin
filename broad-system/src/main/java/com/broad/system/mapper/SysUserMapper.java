package com.broad.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.broad.system.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 管理员表(SysUser)表数据库访问层
 *
 * @author XingGao
 * @since 2022 -07-09 17:19:40
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysUser> 实例对象列表
     * @return 影响行数 int
     */
    int insertBatch(@Param("entities") List<SysUser> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysUser> 实例对象列表
     * @return 影响行数 int
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<SysUser> entities);

    /**
     * 查询所有.
     *
     * @param sysAdmin the sys admin
     * @return the page
     */
    List<SysUser> selectAll(SysUser sysAdmin);

    /**
     * 通过ids批量查询
     *
     * @return 单条数据 SysUser
     */
    List<SysUser> selectAllByIds(@Param("ids") List<Long> ids);

}

