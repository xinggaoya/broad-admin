package com.broad.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.broad.system.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 管理员表(SysUser)表数据库访问层
 *
 * @author broad
 * @since 2022-07-09 17:19:40
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
     * @param sysAdmin 查询条件
     * @param page     分页对象
     * @return 分页结果
     */
    IPage<SysUser> selectAll(@Param("sysAdmin") SysUser sysAdmin, @Param("page") Page<SysUser> page);

    /**
     * 通过ids批量查询
     *
     * @param ids the ids
     * @return 单条数据 SysUser
     */
    List<SysUser> selectAllByIds(@Param("ids") List<Long> ids);

}
