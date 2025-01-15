package com.broad.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.common.exception.ServiceException;
import com.broad.common.utils.PageUtils;
import com.broad.system.entity.SysRole;
import com.broad.system.entity.SysUserRole;
import com.broad.system.mapper.SysRoleMapper;
import com.broad.system.mapper.SysUserRoleMapper;
import com.broad.system.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 管理分组表(SysRole)表服务实现类
 *
 * @author XingGao
 * @since 2022 -07-13 09:53:11
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Override
    public List<SysRole> selectAll(SysRole sysUserRole) {
        Page<SysRole> page = PageUtils.startPage();
        List<SysRole> pageList = baseMapper.selectList(page, new QueryWrapper<>(sysUserRole));
        // 构建树形结构数据
//        pageList.setRecords(buildTree(pageList.getRecords()));
        return pageList;
    }

    @Override
    public int saveUserRole(SysRole sysUserRole) {
        return baseMapper.insert(sysUserRole);
    }

    @Override
    public int updateUserRole(SysRole sysUserRole) {
        return baseMapper.updateById(sysUserRole);
    }


    @Override
    public int deleteUserRole(List<Long> idList) {
        // 检查该角色下是否有管理员，如果有则不能删除
        List<SysUserRole> adminGroupAccesses = userRoleMapper.selectList(new LambdaQueryWrapper<SysUserRole>()
                .in(SysUserRole::getRoleId, idList));
        if (adminGroupAccesses.size() > 0) {
            throw new ServiceException("该角色下有管理员绑定，不能删除");
        }
        return baseMapper.deleteBatchIds(idList);
    }


}
