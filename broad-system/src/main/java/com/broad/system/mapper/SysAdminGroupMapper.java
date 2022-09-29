package com.broad.system.mapper;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.broad.system.entity.SysAdminGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 管理分组表(SysAdminGroup)表数据库访问层
 *
 * @author XingGao
 * @since 2022 -07-13 09:53:10
 */
@Mapper
public interface SysAdminGroupMapper extends BaseMapper<SysAdminGroup> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysAdminGroup> 实例对象列表
     * @return 影响行数 int
     */
    int insertBatch(@Param("entities") List<SysAdminGroup> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysAdminGroup> 实例对象列表
     * @return 影响行数 int
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<SysAdminGroup> entities);

}

