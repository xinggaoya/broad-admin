package com.broad.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckDisable;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.broad.common.annotation.RateLimiter;
import com.broad.common.web.entity.ResultData;
import com.broad.system.entity.SysUser;
import com.broad.system.service.SysSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author: XingGao
 * @date: 2023/02/06 10:20
 * @description:
 */
@RestController
@RequestMapping("sysAdmin")
public class SysSessionController {

    @Autowired
    private SysSessionService sysSessionService;


    /**
     * 管理员登录
     *
     * @param sysAdmin the sys admin
     * @param request  the request
     * @return 删除结果 result data
     */
    @PostMapping("/login")
    @RateLimiter(key = "ADMIN_LOGIN")
    @SaIgnore
    public ResultData login(@RequestBody SysUser sysAdmin, HttpServletRequest request) throws IOException {
        return ResultData.success(this.sysSessionService.administratorLogin(sysAdmin, request)).setMsg("登录成功!");
    }

    /**
     * 验证用户状态
     *
     * @return result data
     */
    @GetMapping("/checkLogin")
    @SaCheckLogin
    @SaCheckDisable
    public ResultData checkLogin() {
        return ResultData.ok();
    }

    /**
     * 退出登录.
     *
     * @return the result data
     */
    @GetMapping("/logout")
    @SaCheckLogin
    public ResultData logout() {
        this.sysSessionService.logout();
        return ResultData.ok();
    }
}
