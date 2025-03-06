package com.broad.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 待办事项(SysTodo)实体类
 *
 * @author broad
 * @since 2024-03-06
 */
@Data
@TableName("sys_todo")
public class SysTodo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 待办事项ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 待办事项标题
     */
    private String title;

    /**
     * 状态（0未完成 1已完成）
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
} 