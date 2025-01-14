package com.broad.common.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 分页工具类
 * 
 * @author broad
 */
public class PageUtils {

    /**
     * 设置请求分页数据
     */
    public static <T> Page<T> startPage() {
        Integer pageNum = ServletUtils.getParameterToInt("pageNum");
        Integer pageSize = ServletUtils.getParameterToInt("pageSize");
        if (pageNum == null) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        return new Page<>(pageNum, pageSize);
    }

    /**
     * 设置请求分页数据
     */
    public static <T> Page<T> startPage(Integer pageNum, Integer pageSize) {
        if (pageNum == null) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        return new Page<>(pageNum, pageSize);
    }
}
