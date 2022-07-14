package com.broad.system.service.impl;

import cn.dev33.satoken.dao.SaTokenDaoRedisJackson;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broad.common.config.BroadConfig;
import com.broad.common.exception.ServiceException;
import com.broad.system.entity.SysAdmin;
import com.broad.system.mapper.SysAdminMapper;
import com.broad.system.service.SysAdminService;
import com.broad.system.service.SysMenuRuleService;
import com.google.code.kaptcha.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 管理员表(SysAdmin)表服务实现类
 *
 * @author XingGao
 * @since 2022-07-09 17:19:40
 */
@Service
public class SysAdminServiceImpl extends ServiceImpl<SysAdminMapper, SysAdmin> implements SysAdminService {

    @Autowired
    private SaTokenDaoRedisJackson saTokenDaoRedisJackson;
    @Autowired
    private SysMenuRuleService menuRuleService;

    @Autowired
    private BroadConfig broadConfig;

    @Override
    public Object administratorLogin(SysAdmin sysAdmin) {
        SysAdmin admin = baseMapper.selectOne(new QueryWrapper<SysAdmin>().lambda().eq(SysAdmin::getUserName, sysAdmin.getUserName()));

        if ("true".equals(broadConfig.getCaptchaEnabled())) {
            if (sysAdmin.getCodeValue() == null || !sysAdmin.getCodeValue().equals(saTokenDaoRedisJackson.get(Constants.KAPTCHA_SESSION_KEY + sysAdmin.getCodeId()))) {
                throw new ServiceException("请输入正确的验证码");
            }
        }
        if (admin == null) {
            throw new ServiceException("用户名不存在");
        }
        if (!admin.getPassword().equals(sysAdmin.getPassword())) {
            throw new ServiceException("密码错误");
        }
        admin.setLastLogintime(new Date());
        // 标记登录状态
        StpUtil.login(admin.getId());
        admin.setTokenValue(StpUtil.getTokenValue());
        return admin;
    }

}

