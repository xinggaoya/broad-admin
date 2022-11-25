package com.broad.common.web.page;

import java.io.Serializable;
import java.util.List;

/**
 * 表格分页数据对象
 *
 * @author XingGao
 */
public class TableDataInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 列表数据
     */
    private List<?> rows;

    /**
     * 消息状态码
     */
    private int code;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 表格数据对象
     */
    public TableDataInfo() {
    }

    /**
     * 分页
     *
     * @param list  列表数据
     * @param total 总记录数
     */
    public TableDataInfo(List<?> list, int total) {
        this.rows = list;
        this.total = total;
    }

    /**
     * Gets total.
     *
     * @return the total
     */
    public long getTotal() {
        return total;
    }

    /**
     * Sets total.
     *
     * @param total the total
     */
    public void setTotal(long total) {
        this.total = total;
    }

    /**
     * Gets rows.
     *
     * @return the rows
     */
    public List<?> getRows() {
        return rows;
    }

    /**
     * Sets rows.
     *
     * @param rows the rows
     */
    public void setRows(List<?> rows) {
        this.rows = rows;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}