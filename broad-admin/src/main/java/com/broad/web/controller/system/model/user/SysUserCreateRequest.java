package com.broad.web.controller.system.model.user;

import com.broad.system.entity.SysUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * 新增用户请求
 */
public class SysUserCreateRequest {

    @NotBlank(message = "用户名不能为空")
    private String userName;

    @NotBlank(message = "昵称不能为空")
    private String nickName;

    @NotNull(message = "部门不能为空")
    private Integer deptId;

    @NotEmpty(message = "至少选择一个角色")
    private List<Integer> roleIds;

    @Email(message = "邮箱格式不正确")
    @NotBlank(message = "邮箱不能为空")
    private String email;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @NotBlank(message = "手机号不能为空")
    private String mobile;

    @NotBlank(message = "用户状态不能为空")
    private String userStatus;

    @Size(min = 6, max = 32, message = "密码长度需在6-32位之间")
    @NotBlank(message = "初始密码不能为空")
    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 构建实体
     */
    public SysUser toEntity() {
        SysUser user = new SysUser();
        user.setUserName(this.userName);
        user.setNickName(this.nickName);
        user.setDeptId(this.deptId);
        user.setRoleIds(this.roleIds);
        user.setEmail(this.email);
        user.setMobile(this.mobile);
        user.setUserStatus(this.userStatus);
        user.setPassword(this.password);
        return user;
    }
}
