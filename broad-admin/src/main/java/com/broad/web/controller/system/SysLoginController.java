package com.broad.web.controller.system;

import com.broad.common.web.entity.ResultData;
import com.broad.common.web.entity.SysUser;
import com.broad.framework.web.service.SysLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Sys login controller.
 *
 * @author: XingGao
 * @date: 2022 /12/6
 * @description:
 */
@RestController
@ResponseBody
public class SysLoginController {

    @Autowired
    private SysLoginService sysLoginService;


    /**
     * Login sys user.
     *
     * @param sysUser the sys user
     * @return the sys user
     */
    @PostMapping("/admin/login")
    public ResultData login(@RequestBody SysUser sysUser) {
        return ResultData.success(sysLoginService.login(sysUser)).setMsg("登录成功");
    }
}
