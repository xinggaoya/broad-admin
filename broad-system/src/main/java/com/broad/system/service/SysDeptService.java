package com.broad.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.broad.system.entity.SysDept;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 部门表(SysDept)表服务接口
 *
 * @author XingGao
 * @since 2022 -10-02 19:54:53
 */
public interface SysDeptService extends IService<SysDept> {

    /**
     * 查询所有数据
     *
     * @param sysDept 查询实体
     * @return 所有数据 list
     */
    List<SysDept> selectAll(SysDept sysDept);

    /**
     * Insert dept boolean.
     *
     * @param sysDept the sys dept
     * @return the boolean
     */
    @Transactional(rollbackFor = Exception.class)
    boolean insertDept(SysDept sysDept);

    /**
     * Delete dept.
     *
     * @param deptIds the dept ids
     */
    @Transactional(rollbackFor = Exception.class)
    void deleteDept(List<Long> deptIds);
}

