package com.broad.web.controller.system.model.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 重置密码请求
 */
public class SysUserPasswordRequest {

    @NotNull(message = "用户ID不能为空")
    private Integer id;

    @Size(min = 6, max = 32, message = "密码长度需在6-32位之间")
    @NotBlank(message = "密码不能为空")
    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
