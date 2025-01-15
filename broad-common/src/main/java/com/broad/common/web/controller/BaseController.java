package com.broad.common.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.broad.common.utils.PageUtils;
import com.broad.common.web.page.TableDataInfo;
import com.broad.common.web.entity.ResultData;
import com.broad.common.utils.ServletUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;

/**
 * web层通用数据处理
 *
 * @author broad
 */
public class BaseController {

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(com.broad.common.utils.DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 设置请求分页数据
     */
    protected <T> Page<T> startPage() {
        return PageUtils.startPage();
    }

    /**
     * 设置请求分页数据
     */
    protected <T> Page<T> startPage(Integer pageNum, Integer pageSize) {
        return PageUtils.startPage(pageNum, pageSize);
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected TableDataInfo getDataTable(IPage<?> page) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(200);
        rspData.setRows(page.getRecords());
        rspData.setTotal(page.getTotal());
        return rspData;
    }

    /**
     * 响应请求分页数据
     */
    protected TableDataInfo getDataTable(List<?> list) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(200);
        rspData.setRows(list);
        rspData.setTotal(list.size());
        return rspData;
    }

    /**
     * 响应请求分页数据
     */
    protected TableDataInfo getDataTable(List<?> list, long total) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(200);
        rspData.setRows(list);
        rspData.setTotal(total);
        return rspData;
    }

    /**
     * 返回成功消息
     */
    protected ResultData success() {
        return ResultData.success();
    }

    /**
     * 返回成功消息
     */
    protected ResultData success(Object data) {
        return ResultData.success(data);
    }

    /**
     * 返回失败消息
     */
    protected ResultData error() {
        return ResultData.error();
    }

    /**
     * 返回失败消息
     */
    protected ResultData error(String message) {
        return ResultData.error(message);
    }

    /**
     * 返回警告消息
     */
    protected ResultData warn(String message) {
        return ResultData.warn(message);
    }

    /**
     * 返回结果
     */
    protected ResultData toResult(boolean result) {
        return result ? success() : error();
    }

    /**
     * 返回结果
     */
    protected ResultData toResult(int rows) {
        return rows > 0 ? success() : error();
    }
}
