package com.broad.web.controller.system.model.user;

import com.broad.system.entity.SysUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

/**
 * 更新用户请求
 */
public class SysUserUpdateRequest {

    @NotNull(message = "用户ID不能为空")
    private Integer id;
    private String nickName;
    private Integer deptId;
    private List<Integer> roleIds;
    @Email(message = "邮箱格式不正确")
    private String email;
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String mobile;
    private String userStatus;
    private String avatar;
    private String motto;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public List<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    /**
     * 构建实体
     */
    public SysUser toEntity() {
        SysUser user = new SysUser();
        user.setId(this.id);
        user.setNickName(this.nickName);
        user.setDeptId(this.deptId);
        user.setRoleIds(this.roleIds);
        user.setEmail(this.email);
        user.setMobile(this.mobile);
        user.setUserStatus(this.userStatus);
        user.setAvatar(this.avatar);
        user.setMotto(this.motto);
        return user;
    }
}
