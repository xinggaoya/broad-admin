package com.broad.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.broad.common.annotation.Log;
import com.broad.common.enums.BusinessType;
import com.broad.common.web.controller.BaseController;
import com.broad.common.web.entity.ResultData;
import com.broad.system.entity.SysUserTag;
import com.broad.system.service.SysUserTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 用户标签(SysUserTag)表控制层
 *
 * @author broad
 * @since 2024-03-06
 */
@RestController
@RequestMapping("sysUserTag")
public class SysUserTagController extends BaseController {

    /**
     * 服务对象
     */
    @Autowired
    private SysUserTagService sysUserTagService;

    /**
     * 根据用户ID查询标签
     *
     * @param userId 用户ID
     * @return 标签列表
     */
    @GetMapping
    public ResultData selectByUserId(String userId) {
        return success(this.sysUserTagService.selectByUserId(userId));
    }

    /**
     * 新增数据
     *
     * @param sysUserTag 实体对象
     * @return 新增结果
     */
    @PostMapping
    @Log(description = "新增用户标签", businessType = BusinessType.INSERT)
    public ResultData insert(@RequestBody @Valid SysUserTag sysUserTag) {
        return toResult(this.sysUserTagService.save(sysUserTag));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @Log(description = "删除用户标签", businessType = BusinessType.DELETE)
    public ResultData delete(@RequestParam("idList") List<Long> idList) {
        return toResult(this.sysUserTagService.removeByIds(idList));
    }
} 