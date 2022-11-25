package com.broad.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.broad.system.entity.SysMenu;
import com.broad.system.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 管理权限分组表(SysRoleMenu)表数据库访问层
 *
 * @author XingGao
 * @since 2022 -10-19 17:15:02
 */
@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    /**
     * 查询角色菜单
     *
     * @param roleId 查询实体
     * @return 所有数据 list
     */
    List<SysMenu> findRoleMenu(@Param("roleId") Integer roleId);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysRoleMenu> 实例对象列表
     * @return 影响行数 int
     */
    int insertBatch(@Param("entities") List<SysRoleMenu> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysRoleMenu> 实例对象列表
     * @return 影响行数 int
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<SysRoleMenu> entities);

}

