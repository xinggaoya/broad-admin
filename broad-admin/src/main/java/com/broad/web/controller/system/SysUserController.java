package com.broad.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckDisable;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaIgnore;
import com.broad.common.annotation.Log;
import com.broad.common.annotation.RateLimiter;
import com.broad.common.enums.BusinessType;
import com.broad.common.web.controller.BaseController;
import com.broad.common.web.entity.ResultData;
import com.broad.common.web.page.TableDataInfo;
import com.broad.system.entity.SysUser;
import com.broad.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * 管理员表(SysUser)表控制层
 *
 * @author XingGao
 * @since 2022 -07-09 17:19:39
 */
@RestController
@RequestMapping("sysAdmin")
public class SysUserController extends BaseController {

    /**
     * 服务对象
     */
    @Autowired
    private SysUserService sysAdminService;


    /**
     * 分页查询所有数据
     *
     * @param sysAdmin 查询实体
     * @return 所有数据 result data
     */
    @GetMapping
    @SaCheckPermission("sys:user:list")
    public TableDataInfo selectAll(SysUser sysAdmin) {
        startPage();
        return getDataTable(this.sysAdminService.selectAll(sysAdmin));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据 result data
     */
    @GetMapping("{id}")
    @SaCheckPermission("sys:user:list")
    public ResultData selectOne(@PathVariable Serializable id) {
        SysUser sysAdmin = this.sysAdminService.getById(id);
        sysAdmin.setPassword(null);
        return ResultData.success(sysAdmin);
    }

    /**
     * 新增数据
     *
     * @param sysAdmin 实体对象
     * @return 新增结果 result data
     */
    @PostMapping
    @SaCheckPermission("sys:user:add")
    @Log(description = "新增管理员表", businessType = BusinessType.INSERT)
    public ResultData insert(@RequestBody @Valid SysUser sysAdmin) {
        return ResultData.success(this.sysAdminService.saveAdmin(sysAdmin));
    }

    /**
     * 修改数据
     *
     * @param sysAdmin 实体对象
     * @return 修改结果 result data
     */
    @PutMapping
    @SaCheckPermission("sys:user:update")
    @Log(description = "修改管理员表", businessType = BusinessType.UPDATE)
    public ResultData update(@RequestBody SysUser sysAdmin) {
        return ResultData.success(this.sysAdminService.updateAdmin(sysAdmin));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果 result data
     */
    @DeleteMapping
    @SaCheckPermission("sys:user:delete")
    @Log(description = "删除管理员", businessType = BusinessType.DELETE)
    public ResultData delete(@RequestParam("idList") List<Long> idList) {
        return toResult(this.sysAdminService.removeByIds(idList));
    }

    /**
     * 管理员登录
     *
     * @param sysAdmin the sys admin
     * @param request  the request
     * @return 删除结果 result data
     * @throws IOException the io exception
     */
    @PostMapping("/login")
    @RateLimiter(key = "ADMIN_LOGIN", count = 5, time = 1)
    @SaIgnore
    public ResultData login(@RequestBody SysUser sysAdmin, HttpServletRequest request) throws IOException {
        return ResultData.success(this.sysAdminService.administratorLogin(sysAdmin, request)).setMsg("登录成功!");
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
        this.sysAdminService.logout();
        return ResultData.ok();
    }

}

