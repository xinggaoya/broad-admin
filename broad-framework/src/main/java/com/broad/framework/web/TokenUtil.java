package com.broad.framework.web;

import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import com.broad.common.utils.SpringUtils;
import com.broad.system.entity.SysUser;
import com.broad.system.service.SysUserService;

/**
 * @author: XingGao
 * @date: 2022/12/8
 * @description:
 */
public class TokenUtil {

    private static StpLogic getStpLogic() {
        return StpUtil.stpLogic;
    }

    /**
     * 获取当前登录Token
     *
     * @return token
     */
    public static String getTokenValue() {
        return getStpLogic().getTokenValue();
    }

    /**
     * 获取当前登录ID
     *
     * @return id
     */
    public static Long getLoginIdAsLong() {
        return getStpLogic().getLoginIdAsLong();
    }

    /**
     * 获取当前登录用户信息
     *
     * @return sys admin
     */
    public static SysUser getLoginUser() {
        return SpringUtils.getBean(SysUserService.class).getById(getLoginIdAsLong());
    }
}
