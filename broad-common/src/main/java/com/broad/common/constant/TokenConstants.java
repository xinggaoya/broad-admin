package com.broad.common.constant;

/**
 * Token的Key常量
 *
 * @author XingGao
 */
public class TokenConstants {
    /**
     * 登录用户ID
     */
    public static final String LOGIN_KEY = ":login:session:";

    /**
     * 登录用户token
     */
    public static final String TOKEN_KEY = ":login:token:";


    /**
     * 账户被顶下线
     */
    public static final int LOGIN_DOWN_LINE = -5;

    /**
     * 登录无效
     */
    public static final int LOGIN_INVALID = -4;

    /**
     * 登录过期
     */
    public static final int LOGIN_EXPIRE = -3;

}
