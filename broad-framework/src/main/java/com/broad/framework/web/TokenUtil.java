package com.broad.framework.web;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.stp.StpUtil;

/**
 * @author: XingGao
 * @date: 2022/12/8
 * @description:
 */
public class TokenUtil {

    /**
     * token刷新时间(秒)
     */
    private static final int TOKEN_TIMEOUT = 86400;

    /**
     * 刷新token
     *
     * @return
     */
    public static void refreshToken() {
        long tokenSessionTimeout = StpUtil.getTokenTimeout();
        if (tokenSessionTimeout < TOKEN_TIMEOUT) {
            StpUtil.renewTimeout(SaManager.getConfig().getTimeout());
        }
    }
}