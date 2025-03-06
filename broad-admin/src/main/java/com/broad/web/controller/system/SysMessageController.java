package com.broad.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.broad.common.annotation.Log;
import com.broad.common.enums.BusinessType;
import com.broad.common.web.controller.BaseController;
import com.broad.common.web.entity.ResultData;
import com.broad.system.entity.SysMessage;
import com.broad.system.service.SysMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 系统消息(SysMessage)表控制层
 *
 * @author broad
 * @since 2024-03-06
 */
@RestController
@RequestMapping("sysMessage")
public class SysMessageController extends BaseController {

    /**
     * 服务对象
     */
    @Autowired
    private SysMessageService sysMessageService;

    /**
     * 根据用户ID查询消息
     *
     * @param userId 用户ID
     * @return 消息列表
     */
    @GetMapping
    public ResultData selectByUserId(String userId) {
        return success(this.sysMessageService.selectByUserId(userId));
    }

    /**
     * 标记消息为已读
     *
     * @param id 消息ID
     * @return 操作结果
     */
    @PutMapping("/read")
    @Log(description = "标记消息已读", businessType = BusinessType.UPDATE)
    public ResultData read(Long id) {
        return toResult(this.sysMessageService.markAsRead(id));
    }

    /**
     * 获取未读消息数量
     *
     * @param userId 用户ID
     * @return 未读消息数量
     */
    @GetMapping("/unread")
    public ResultData getUnreadCount(String userId) {
        return success(this.sysMessageService.getUnreadCount(userId));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @Log(description = "删除消息", businessType = BusinessType.DELETE)
    public ResultData delete(@RequestParam("idList") List<Long> idList) {
        return toResult(this.sysMessageService.removeByIds(idList));
    }
} 