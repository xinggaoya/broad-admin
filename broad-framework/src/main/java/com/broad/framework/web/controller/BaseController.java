package com.broad.framework.web.controller;

import com.broad.common.constant.HttpStatus;
import com.broad.common.utils.DateUtils;
import com.broad.framework.utils.PageUtils;
import com.broad.framework.web.entity.ResultData;
import com.broad.framework.web.page.TableDataInfo;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;

/**
 * web层通用数据处理
 *
 * @author XingGao
 */
@Slf4j
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
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage() {
        PageUtils.startPage();
    }

    /**
     * 清理分页的线程变量
     */
    protected void clearPage() {
        PageUtils.clearPage();
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected TableDataInfo getDataTable(List<?> list) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setRows(list);
        rspData.setMessage("查询成功");
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected ResultData toResult(int rows) {
        return rows > 0 ? ResultData.ok() : ResultData.error();
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    protected ResultData toResult(boolean result) {
        return result ? success() : error();
    }

    /**
     * 返回成功
     */
    public ResultData success() {
        return ResultData.ok();
    }

    /**
     * 返回失败消息
     */
    public ResultData error() {
        return ResultData.error();
    }

    /**
     * 返回成功消息
     */
    public ResultData success(String message) {
        return ResultData.ok(message);
    }

    /**
     * 返回失败消息
     */
    public ResultData error(String message) {
        return ResultData.error(message);
    }
}
