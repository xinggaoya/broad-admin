package com.broad.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.broad.system.entity.SysTodo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 待办事项(SysTodo)表服务接口
 *
 * @author broad
 * @since 2024-03-06
 */
public interface SysTodoService extends IService<SysTodo> {

    /**
     * 根据用户ID查询待办事项
     *
     * @param userId 用户ID
     * @return 待办事项列表
     */
    List<SysTodo> selectByUserId(String userId);
} 