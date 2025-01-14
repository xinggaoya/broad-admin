package com.broad.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.broad.system.entity.SysUser;

import java.util.List;

/**
 * 管理员表(SysUser)表服务接口
 *
 * @author broad
 * @since 2022-07-09 17:19:40
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 查询所有数据
     *
     * @param sysAdmin 查询实体
     * @param page     分页对象
     * @return 所有数据
     */
    IPage<SysUser> selectAll(SysUser sysAdmin, Page<SysUser> page);

    /**
     * 新增数据
     *
     * @param sysAdmin 实体对象
     * @return 新增结果
     */
    int saveAdmin(SysUser sysAdmin);

    /**
     * 通过id批量查询
     *
     * @param ids id集合
     * @return 查询结果
     */
    List<SysUser> getAdminByIds(List<Long> ids);

    /**
     * 修改数据
     *
     * @param sysAdmin 实体对象
     * @return 修改结果
     */
    int updateAdmin(SysUser sysAdmin);

}
