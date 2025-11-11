package com.broad.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.broad.common.annotation.Log;
import com.broad.common.enums.BusinessType;
import com.broad.common.web.controller.BaseController;
import com.broad.common.web.entity.ResultData;
import com.broad.common.web.page.TableDataInfo;
import com.broad.system.entity.SysDept;
import com.broad.system.entity.SysRole;
import com.broad.system.entity.SysUser;
import com.broad.system.service.SysDeptService;
import com.broad.system.service.SysRoleService;
import com.broad.system.service.SysUserRoleService;
import com.broad.system.service.SysUserService;
import com.broad.web.controller.system.model.user.SysUserCreateRequest;
import com.broad.web.controller.system.model.user.SysUserMetaVO;
import com.broad.web.controller.system.model.user.SysUserPageRequest;
import com.broad.web.controller.system.model.user.SysUserPasswordRequest;
import com.broad.web.controller.system.model.user.SysUserStatusRequest;
import com.broad.web.controller.system.model.user.SysUserUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户管理
 */
@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 分页查询
     */
    @GetMapping("/page")
    @SaCheckPermission("sys:user:list")
    public TableDataInfo page(SysUserPageRequest request) {
        Page<SysUser> page = startPage();
        SysUser query = request == null ? new SysUser() : request.toEntity();
        return getDataTable(this.sysUserService.selectAll(query, page));
    }

    /**
     * 查询基础数据（部门、角色等）
     */
    @GetMapping("/meta")
    @SaCheckPermission("sys:user:list")
    public ResultData meta() {
        List<SysDept> deptTree = this.sysDeptService.selectAll(new SysDept());
        List<SysRole> roles = this.sysRoleService.list();
        SysUserMetaVO metaVO = new SysUserMetaVO();
        metaVO.setDeptTree(buildDeptNodes(deptTree));
        metaVO.setRoleOptions(buildRoleOptions(roles));
        return success(metaVO);
    }

    /**
     * 查询用户详情
     */
    @GetMapping("/{id}")
    @SaCheckPermission("sys:user:list")
    public ResultData detail(@PathVariable Long id) {
        SysUser user = this.sysUserService.getById(id);
        if (user == null) {
            return error("用户不存在或已被删除");
        }
        user.setPassword(null);
        user.setSalt(null);
        user.setRoleIds(this.sysUserRoleService.selectUserRoleIds(id));
        return success(user);
    }

    /**
     * 新增用户
     */
    @PostMapping
    @SaCheckPermission("sys:user:add")
    @Log(description = "新增系统用户", businessType = BusinessType.INSERT)
    public ResultData create(@RequestBody @Valid SysUserCreateRequest request) {
        return toResult(this.sysUserService.saveAdmin(request.toEntity()));
    }

    /**
     * 更新用户
     */
    @PutMapping
    @SaCheckPermission("sys:user:update")
    @Log(description = "编辑系统用户", businessType = BusinessType.UPDATE)
    public ResultData update(@RequestBody @Valid SysUserUpdateRequest request) {
        return toResult(this.sysUserService.updateAdmin(request.toEntity()));
    }

    /**
     * 更新用户状态
     */
    @PutMapping("/status")
    @SaCheckPermission("sys:user:update")
    @Log(description = "修改用户状态", businessType = BusinessType.UPDATE)
    public ResultData updateStatus(@RequestBody @Valid SysUserStatusRequest request) {
        SysUser user = new SysUser();
        user.setId(request.getId());
        user.setUserStatus(request.getUserStatus());
        return toResult(this.sysUserService.updateAdmin(user));
    }

    /**
     * 重置密码
     */
    @PutMapping("/password")
    @SaCheckPermission("sys:user:update")
    @Log(description = "重置用户密码", businessType = BusinessType.UPDATE)
    public ResultData resetPassword(@RequestBody @Valid SysUserPasswordRequest request) {
        this.sysUserService.resetPassword(request.getId(), request.getPassword());
        return success();
    }

    /**
     * 删除用户
     */
    @DeleteMapping
    @SaCheckPermission("sys:user:delete")
    @Log(description = "批量删除用户", businessType = BusinessType.DELETE)
    public ResultData delete(@RequestParam("ids") List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return error("请选择要删除的用户");
        }
        List<Integer> targetIds = ids.stream()
                .map(id -> Math.toIntExact(id))
                .collect(Collectors.toList());
        return toResult(this.sysUserService.removeByIds(targetIds));
    }

    private List<SysUserMetaVO.DeptTreeNode> buildDeptNodes(List<SysDept> source) {
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }
        return source.stream().map(item -> {
            SysUserMetaVO.DeptTreeNode node = new SysUserMetaVO.DeptTreeNode();
            node.setId(item.getDeptId());
            node.setLabel(item.getDeptName());
            node.setChildren(buildDeptNodes(item.getChildren()));
            return node;
        }).collect(Collectors.toList());
    }

    private List<SysUserMetaVO.OptionItem> buildRoleOptions(List<SysRole> roles) {
        if (roles == null || roles.isEmpty()) {
            return Collections.emptyList();
        }
        return roles.stream()
                .filter(role -> role.getId() != null)
                .map(role -> {
                    SysUserMetaVO.OptionItem item = new SysUserMetaVO.OptionItem();
                    item.setValue(role.getId().longValue());
                    item.setLabel(role.getName());
                    item.setStatus(role.getStatus());
                    return item;
                }).collect(Collectors.toList());
    }
}
