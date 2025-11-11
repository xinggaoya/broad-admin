package com.broad.web.controller.system;

import com.broad.common.annotation.Log;
import com.broad.common.enums.BusinessType;
import com.broad.common.web.controller.BaseController;
import com.broad.common.web.entity.ResultData;
import com.broad.system.entity.SysTodo;
import com.broad.system.service.SysTodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 待办事项(SysTodo)表控制层
 *
 * @author broad
 * @since 2024-03-06
 */
@RestController
@RequestMapping("sysTodo")
public class SysTodoController extends BaseController {

    /**
     * 服务对象
     */
    @Autowired
    private SysTodoService sysTodoService;

    /**
     * 根据用户ID查询待办事项
     *
     * @param userId 用户ID
     * @return 待办事项列表
     */
    @GetMapping
    public ResultData selectByUserId(String userId) {
        return success(this.sysTodoService.selectByUserId(userId));
    }

    /**
     * 新增数据
     *
     * @param sysTodo 实体对象
     * @return 新增结果
     */
    @PostMapping
    @Log(description = "新增待办事项", businessType = BusinessType.INSERT)
    public ResultData insert(@RequestBody @Valid SysTodo sysTodo) {
        boolean result = this.sysTodoService.save(sysTodo);
        return result ? success(sysTodo.getId()) : error("新增失败");
    }

    /**
     * 修改数据
     *
     * @param sysTodo 实体对象
     * @return 修改结果
     */
    @PutMapping
    @Log(description = "修改待办事项", businessType = BusinessType.UPDATE)
    public ResultData update(@RequestBody SysTodo sysTodo) {
        return toResult(this.sysTodoService.updateById(sysTodo));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @Log(description = "删除待办事项", businessType = BusinessType.DELETE)
    public ResultData delete(@RequestParam("idList") List<Long> idList) {
        return toResult(this.sysTodoService.removeByIds(idList));
    }
} 