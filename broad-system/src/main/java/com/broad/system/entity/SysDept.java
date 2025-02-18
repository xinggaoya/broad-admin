package com.broad.system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 部门表(SysDept)表实体类
 *
 * @author XingGao
 * @since 2022 -10-02 19:56:02
 */
@Data
public class SysDept implements Serializable {

    private static final long serialVersionUID = 398394317634427417L;

    /**
     * 部门id
     */
    @TableId(value = "dept_id", type = IdType.AUTO)
    private Long deptId;

    /**
     * 父部门id
     */
    @TableField(value = "parent_id")
    private Long parentId;
    /**
     * 祖级列表
     */
    @TableField(value = "ancestors")
    private String ancestors;
    /**
     * 部门名称
     */
    @TableField(value = "dept_name")
    private String deptName;
    /**
     * 显示顺序
     */
    @TableField(value = "order_num")
    private Integer orderNum;
    /**
     * 负责人
     */
    @TableField(value = "leader")
    private String leader;
    /**
     * 联系电话
     */
    @TableField(value = "phone")
    private String phone;
    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;
    /**
     * 部门状态（0正常 1停用）
     */
    @TableField(value = "status")
    private String status;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableField(value = "del_flag")
    private String delFlag;
    /**
     * 创建者
     */
    @TableField(value = "create_by")
    private String createBy;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 更新者
     */
    @TableField(value = "update_by")
    private String updateBy;
    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(exist = false)
    private List<SysDept> children;

}
