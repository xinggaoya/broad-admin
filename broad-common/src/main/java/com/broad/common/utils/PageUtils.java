package com.broad.common.utils;

import com.broad.common.utils.sql.SqlUtil;
import com.broad.common.web.page.PageDomain;
import com.broad.common.web.page.TableSupport;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.page.PageMethod;

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
        PageMethod.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
    }

    /**
     * 清理分页的线程变量
     */
    public static void clearPage() {
        PageHelper.clearPage();
    }
}
