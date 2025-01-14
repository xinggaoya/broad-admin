package com.broad.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

import lombok.Data;

/**
 * 用户与岗位关联表(SysUserPost)表实体类
 *
 * @author XingGao
 * @since 2023-03-26 16:37:28
 */
@Data
public class SysUserPost implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 岗位ID
     */
    private Long postId;
}
