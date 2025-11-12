package com.broad.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.common.utils.TreeUtils;
import com.broad.system.entity.SysDept;
import com.broad.system.mapper.SysDeptMapper;
import com.broad.system.service.SysDeptService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 部门表(SysDept)表服务实现类
 *
 * @author XingGao
 * @since 2022 -10-02 19:54:53
 */
@Service("sysDeptService")
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    @Override
    public List<SysDept> selectAll(SysDept sysDept) {
        List<SysDept> list = this.baseMapper.selectList(new LambdaQueryWrapper<>(sysDept));
        return TreeUtils.build(list, SysDept::getDeptId, SysDept::getParentId, SysDept::setChildren, 0L);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertDept(SysDept sysDept) {
        if (sysDept.getParentId() == null) {
            sysDept.setParentId(0L);
        }
        return this.save(sysDept);
    }


    


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDept(List<Long> deptIds) {
        // 删除下级部门
        this.baseMapper.delete(new LambdaUpdateWrapper<SysDept>().in(SysDept::getParentId, deptIds));
        // 删除当前部门
        this.baseMapper.deleteBatchIds(deptIds);
    }
}

