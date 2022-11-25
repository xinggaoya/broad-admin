package com.broad.common.web.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Tree基类
 *
 * @author XingGao
 */
public class TreeEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 父菜单名称
     */
    private String parentName;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 祖级列表
     */
    private String ancestors;

    /**
     * 子部门
     */
    private List<?> children = new ArrayList<>();

    /**
     * Gets parent name.
     *
     * @return the parent name
     */
    public String getParentName() {
        return parentName;
    }

    /**
     * Sets parent name.
     *
     * @param parentName the parent name
     */
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    /**
     * Gets parent id.
     *
     * @return the parent id
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * Sets parent id.
     *
     * @param parentId the parent id
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * Gets order num.
     *
     * @return the order num
     */
    public Integer getOrderNum() {
        return orderNum;
    }

    /**
     * Sets order num.
     *
     * @param orderNum the order num
     */
    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    /**
     * Gets ancestors.
     *
     * @return the ancestors
     */
    public String getAncestors() {
        return ancestors;
    }

    /**
     * Sets ancestors.
     *
     * @param ancestors the ancestors
     */
    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    /**
     * Gets children.
     *
     * @return the children
     */
    public List<?> getChildren() {
        return children;
    }

    /**
     * Sets children.
     *
     * @param children the children
     */
    public void setChildren(List<?> children) {
        this.children = children;
    }
}
