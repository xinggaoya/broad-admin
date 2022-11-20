package com.broad.common.enums;

/**
 * @Author: XingGao
 * @Date: 2022/11/17
 * @Description:
 */
public enum LimitType {

    /**
     * 默认策略全局限流
     */
    DEFAULT,

    /**
     * 根据请求者IP进行限流
     */
    IP

}
