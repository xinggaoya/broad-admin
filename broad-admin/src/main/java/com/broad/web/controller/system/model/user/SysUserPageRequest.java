package com.broad.web.controller.system.model.user;

import com.broad.system.entity.SysUser;

/**
 * 用户分页查询入参
 */
public class SysUserPageRequest {

    private String userName;
    private String nickName;
    private String mobile;
    private String email;
    private String userStatus;
    private Integer deptId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    /**
     * 转为实体用于 Mapper 查询
     */
    public SysUser toEntity() {
        SysUser user = new SysUser();
        user.setUserName(this.userName);
        user.setNickName(this.nickName);
        user.setMobile(this.mobile);
        user.setEmail(this.email);
        user.setUserStatus(this.userStatus);
        user.setDeptId(this.deptId);
        return user;
    }
}
