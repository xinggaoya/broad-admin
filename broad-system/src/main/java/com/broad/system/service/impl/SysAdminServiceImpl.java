package com.broad.system.service.impl;

import cn.dev33.satoken.dao.SaTokenDaoRedisJackson;
import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.common.config.BroadConfig;
import com.broad.common.constant.Constants;
import com.broad.common.exception.ServiceException;
import com.broad.common.utils.ip.IpAddressUtils;
import com.broad.common.utils.ip.IpUtils;
import com.broad.system.entity.SysAdmin;
import com.broad.system.entity.SysAdminGroupAccess;
import com.broad.system.mapper.SysAdminMapper;
import com.broad.system.service.SysAdminGroupAccessService;
import com.broad.system.service.SysAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 管理员表(SysAdmin)表服务实现类
 *
 * @author XingGao
 * @since 2022 -07-09 17:19:40
 */
@Service
public class SysAdminServiceImpl extends ServiceImpl<SysAdminMapper, SysAdmin> implements SysAdminService {

    @Autowired
    private SaTokenDaoRedisJackson saTokenDaoRedisJackson;

    @Autowired
    private SysAdminGroupAccessService sysAdminGroupAccessService;

    @Autowired
    private BroadConfig broadConfig;

    @Override
    public List<SysAdmin> selectAll(SysAdmin sysAdmin) {
        return baseMapper.selectAll(sysAdmin);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveAdmin(SysAdmin sysAdmin) {
        String uid = java.util.UUID.randomUUID().toString();
        sysAdmin.setPassword(SaSecureUtil.md5BySalt(sysAdmin.getPassword(), uid));
        sysAdmin.setSalt(uid);
        boolean res = this.save(sysAdmin);
        // 设置管理员角色
        if (res && sysAdmin.getRoleId() != null) {
            SysAdmin admin = this.getOne(new LambdaQueryWrapper<SysAdmin>().eq(SysAdmin::getUserName, sysAdmin.getUserName()));

            SysAdminGroupAccess sysAdminGroupAccess = new SysAdminGroupAccess();
            sysAdminGroupAccess.setUid(admin.getId());
            sysAdminGroupAccess.setGroupId(sysAdmin.getRoleId());
            sysAdminGroupAccessService.save(sysAdminGroupAccess);
        }
        return res ? 1 : 0;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateAdmin(SysAdmin sysAdmin) {
        // 如果密码与数据库密码不一致，说明用户修改密码,则重新加密
        if (!sysAdmin.getPassword().equals(baseMapper.selectOne(new LambdaQueryWrapper<SysAdmin>().eq(SysAdmin::getId, sysAdmin.getId())).getPassword())) {
            String uid = java.util.UUID.randomUUID().toString();
            sysAdmin.setPassword(SaSecureUtil.md5BySalt(sysAdmin.getPassword(), uid));
            sysAdmin.setSalt(uid);
        }
        int res = baseMapper.updateById(sysAdmin);
        // 查询是否有角色
        if (res > 0 && sysAdmin.getRoleId() != null) {
            SysAdminGroupAccess sysAdminGroupAccess = sysAdminGroupAccessService.getOne(
                    new LambdaQueryWrapper<SysAdminGroupAccess>().eq(SysAdminGroupAccess::getUid,
                            sysAdmin.getId()).eq(SysAdminGroupAccess::getGroupId, sysAdmin.getRoleId()));
            // 如果没有角色，则添加角色
            if (sysAdminGroupAccess == null) {
                SysAdminGroupAccess adminGroupAccess = new SysAdminGroupAccess();
                adminGroupAccess.setUid(sysAdmin.getId());
                adminGroupAccess.setGroupId(sysAdmin.getRoleId());
                sysAdminGroupAccessService.save(adminGroupAccess);
            }
        }
        return res;
    }

    @Override
    public Object administratorLogin(SysAdmin sysAdmin, HttpServletRequest request) {

        if (sysAdmin.getUserName() == null || sysAdmin.getPassword() == null) {
            throw new ServiceException("用户名或密码不能为空");
        }
        SysAdmin admin = baseMapper.selectOne(new QueryWrapper<SysAdmin>().lambda().eq(SysAdmin::getUserName, sysAdmin.getUserName()));

        if (broadConfig.getCaptchaEnabled()) {
            if (sysAdmin.getCodeValue() == null || !sysAdmin.getCodeValue().equals(saTokenDaoRedisJackson.get(Constants.CAPTCHA_CODE_KEY + sysAdmin.getCodeId()))) {
                throw new ServiceException("请输入正确的验证码");
            }
        }
        if (admin == null) {
            throw new ServiceException("用户名不存在");
        }
        if (!admin.getPassword().equals(SaSecureUtil.md5BySalt(sysAdmin.getPassword(), admin.getSalt()))) {
            throw new ServiceException("密码错误");
        }
        admin.setLastLogintime(new Date());
        admin.setLastLoginip(IpAddressUtils.getHome(IpUtils.getIpAddr(request)));
        baseMapper.updateById(admin);
        // 标记登录状态
        StpUtil.login(admin.getId());
        admin.setTokenValue(StpUtil.getTokenValue());
        return admin;
    }

    /**
     * 退出登录
     *
     * @param admin
     */
    @Override
    public void logout(SysAdmin admin) {
        StpUtil.logout(admin.getId());
    }

}

