package com.broad.system.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.broad.common.web.entity.BaseEntity;
import lombok.Data;

/**
 * 岗位信息表(SysPost)表实体类
 *
 * @author XingGao
 * @since 2023-03-26 16:37:26
 */
@Data
@TableName("sys_post")
public class SysPost extends BaseEntity {
    /**
     * 岗位ID
     */
    @TableId(value = "post_id",type = IdType.AUTO)
    private Long postId;
    /**
     * 岗位编码
     */
    @TableField("post_code")
    private String postCode;
    /**
     * 岗位名称
     */
    @TableField("post_name")
    private String postName;
    /**
     * 显示顺序
     */
    @TableField("post_sort")
    private Integer postSort;
    /**
     * 状态（0正常 1停用）
     */
    @TableField("status")
    private String status;
}

