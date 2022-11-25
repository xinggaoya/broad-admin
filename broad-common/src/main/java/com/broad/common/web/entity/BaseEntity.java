package com.broad.common.web.entity;


import com.alibaba.fastjson2.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Entity基类
 *
 * @author XingGao
 */
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 搜索值
     */
    private String searchValue;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 请求参数
     */
    private Map<String, Object> params;

    /**
     * Gets search value.
     *
     * @return the search value
     */
    public String getSearchValue() {
        return searchValue;
    }

    /**
     * Sets search value.
     *
     * @param searchValue the search value
     */
    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    /**
     * Gets create by.
     *
     * @return the create by
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * Sets create by.
     *
     * @param createBy the create by
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /**
     * Gets create time.
     *
     * @return the create time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * Sets create time.
     *
     * @param createTime the create time
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * Gets update by.
     *
     * @return the update by
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * Sets update by.
     *
     * @param updateBy the update by
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    /**
     * Gets update time.
     *
     * @return the update time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * Sets update time.
     *
     * @param updateTime the update time
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * Gets remark.
     *
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * Sets remark.
     *
     * @param remark the remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * Gets params.
     *
     * @return the params
     */
    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    /**
     * Sets params.
     *
     * @param params the params
     */
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
