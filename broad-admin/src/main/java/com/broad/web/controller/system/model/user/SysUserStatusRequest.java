package com.broad.web.controller.system.model.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 用户状态变更请求
 */
public class SysUserStatusRequest {

    @NotNull(message = "用户ID不能为空")
    private Integer id;

    @NotBlank(message = "状态不能为空")
    private String userStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }
}
