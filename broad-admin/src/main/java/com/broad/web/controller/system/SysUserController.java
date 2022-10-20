package com.broad.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.broad.common.enums.BusinessType;
import com.broad.framework.annotation.Log;
import com.broad.framework.web.controller.BaseController;
import com.broad.framework.web.entity.ResultData;
import com.broad.framework.web.page.TableDataInfo;
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
        return ResultData.success(this.sysAdminService.getById(id));
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
        return ResultData.success(this.sysAdminService.removeByIds(idList));
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
    public ResultData login(@RequestBody SysUser sysAdmin, HttpServletRequest request) throws IOException {
        return ResultData.success(this.sysAdminService.administratorLogin(sysAdmin, request)).setMsg("登录成功!");
    }

    /**
     * Logout result data.
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

