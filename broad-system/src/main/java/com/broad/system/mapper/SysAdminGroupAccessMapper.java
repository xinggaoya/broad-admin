package com.broad.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.broad.system.entity.SysAdminGroupAccess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 管理权限分组表(SysAdminGroupAccess)表数据库访问层
 *
 * @author XingGao
 * @since 2022 -07-13 10:13:11
 */
@Mapper
public interface SysAdminGroupAccessMapper extends BaseMapper<SysAdminGroupAccess> {

    /**
     * 通过ID查询数据
     *
     * @param adminId 主键
     * @return 实例对象 sys admin group access
     */
    SysAdminGroupAccess selectByAdminId(@Param("adminId") Object adminId);

}



