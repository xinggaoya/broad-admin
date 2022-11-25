package com.broad.common.web.page;


import com.broad.common.utils.StringUtils;

/**
 * 分页数据
 *
 * @author XingGao
 */
public class PageDomain {
    /**
     * 当前记录起始索引
     */
    private Integer pageNum;

    /**
     * 每页显示记录数
     */
    private Integer pageSize;

    /**
     * 排序列
     */
    private String orderByColumn;

    /**
     * 排序的方向desc或者asc
     */
    private String isAsc = "asc";

    /**
     * 分页参数合理化
     */
    private Boolean reasonable = true;

    /**
     * Gets order by.
     *
     * @return the order by
     */
    public String getOrderBy() {
        if (StringUtils.isEmpty(orderByColumn)) {
            return "";
        }
        return StringUtils.toUnderScoreCase(orderByColumn) + " " + isAsc;
    }

    /**
     * Gets page num.
     *
     * @return the page num
     */
    public Integer getPageNum() {
        return pageNum;
    }

    /**
     * Sets page num.
     *
     * @param pageNum the page num
     */
    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    /**
     * Gets page size.
     *
     * @return the page size
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * Sets page size.
     *
     * @param pageSize the page size
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * Gets order by column.
     *
     * @return the order by column
     */
    public String getOrderByColumn() {
        return orderByColumn;
    }

    /**
     * Sets order by column.
     *
     * @param orderByColumn the order by column
     */
    public void setOrderByColumn(String orderByColumn) {
        this.orderByColumn = orderByColumn;
    }

    /**
     * Gets is asc.
     *
     * @return the is asc
     */
    public String getIsAsc() {
        return isAsc;
    }

    /**
     * Sets is asc.
     *
     * @param isAsc the is asc
     */
    public void setIsAsc(String isAsc) {
        if (StringUtils.isNotEmpty(isAsc)) {
            // 兼容前端排序类型
            if ("ascending".equals(isAsc)) {
                isAsc = "asc";
            } else if ("descending".equals(isAsc)) {
                isAsc = "desc";
            }
            this.isAsc = isAsc;
        }
    }

    /**
     * Gets reasonable.
     *
     * @return the reasonable
     */
    public Boolean getReasonable() {
        if (StringUtils.isNull(reasonable)) {
            return Boolean.TRUE;
        }
        return reasonable;
    }

    /**
     * Sets reasonable.
     *
     * @param reasonable the reasonable
     */
    public void setReasonable(Boolean reasonable) {
        this.reasonable = reasonable;
    }
}
