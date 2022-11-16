package com.broad.common.constant;

/**
 * 缓存的key 常量
 *
 * @author XingGao
 */
public class CacheConstants {
    /**
     * 缓存有效期，默认1440 * 7（分钟）
     */
    public final static long EXPIRATION = 1440 * 7;

    /**
     * 缓存刷新时间，默认120（分钟）
     */
    public final static long REFRESH_TIME = 120;

    /**
     * 权限缓存前缀
     */
    public final static String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 路由缓存前缀
     */
    public final static String ROUTE_KEY = "router_menu";

    /**
     * 路由缓存时间 （分钟）
     */
    public final static long ROUTE_TIME = 60 * 24;

    /**
     * 缓存前缀生成
     */
    public final static String CACHE_PREFIX_GENERATION = "selfKeyGenerator";

}
