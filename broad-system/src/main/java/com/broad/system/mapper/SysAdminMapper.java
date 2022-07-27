package com.broad.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.broad.system.entity.SysAdmin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 管理员表(SysAdmin)表数据库访问层
 *
 * @author XingGao
 * @since 2022-07-09 17:19:40
 */
@Mapper
public interface SysAdminMapper extends BaseMapper<SysAdmin> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysAdmin> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SysAdmin> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysAdmin> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<SysAdmin> entities);

    Page<SysAdmin> selectAll(Page<SysAdmin> page, SysAdmin sysAdmin);

}

