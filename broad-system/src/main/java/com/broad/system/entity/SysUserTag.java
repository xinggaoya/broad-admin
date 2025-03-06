package com.broad.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户标签(SysUserTag)实体类
 *
 * @author broad
 * @since 2024-03-06
 */
@Data
@TableName("sys_user_tag")
public class SysUserTag implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 标签ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 标签名称
     */
    private String name;

    /**
     * 标签类型（info, success, warning, error）
     */
    private String type;
} 