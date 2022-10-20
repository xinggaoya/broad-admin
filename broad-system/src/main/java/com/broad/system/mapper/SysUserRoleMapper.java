package com.broad.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.broad.system.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 管理权限分组表(SysUserRole)表数据库访问层
 *
 * @author XingGao
 * @since 2022 -07-13 10:13:11
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    /**
     * 通过ID查询数据
     *
     * @param adminId 主键
     * @return 实例对象 sys admin group access
     */
    SysUserRole selectByAdminId(@Param("adminId") Object adminId);

}



