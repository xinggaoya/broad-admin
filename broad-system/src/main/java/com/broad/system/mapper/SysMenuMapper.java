package com.broad.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.broad.system.entity.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (SysMenu)表数据库访问层
 *
 * @author XingGao
 * @since 2022-10-10 18:46:52
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysMenu> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SysMenu> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysMenu> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<SysMenu> entities);

}

