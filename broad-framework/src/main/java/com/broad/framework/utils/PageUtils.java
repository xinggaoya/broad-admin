package com.broad.framework.utils;

import com.broad.common.utils.sql.SqlUtil;
import com.broad.framework.web.page.PageDomain;
import com.broad.framework.web.page.TableSupport;
import com.github.pagehelper.PageHelper;

/**
 * 分页工具类
 *
 * @author XingGao
 */
public class PageUtils extends PageHelper {
    /**
     * 设置请求分页数据
     */
    public static void startPage() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
        Boolean reasonable = pageDomain.getReasonable();
        startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
    }

    /**
     * 清理分页的线程变量
     */
    public static void clearPage() {
        PageHelper.clearPage();
    }
}
