package com.broad.web.controller.system.model.user;

import java.util.List;

/**
 * 用户模块下拉依赖
 */
public class SysUserMetaVO {

    private List<DeptTreeNode> deptTree;

    private List<OptionItem> roleOptions;

    public List<DeptTreeNode> getDeptTree() {
        return deptTree;
    }

    public void setDeptTree(List<DeptTreeNode> deptTree) {
        this.deptTree = deptTree;
    }

    public List<OptionItem> getRoleOptions() {
        return roleOptions;
    }

    public void setRoleOptions(List<OptionItem> roleOptions) {
        this.roleOptions = roleOptions;
    }

    public static class DeptTreeNode {
        private Long id;
        private String label;
        private List<DeptTreeNode> children;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public List<DeptTreeNode> getChildren() {
            return children;
        }

        public void setChildren(List<DeptTreeNode> children) {
            this.children = children;
        }
    }

    public static class OptionItem {
        private Long value;
        private String label;
        private String status;

        public Long getValue() {
            return value;
        }

        public void setValue(Long value) {
            this.value = value;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
