package com.broad.system.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 管理分组表(SysAdminGroup)实体类
 *
 * @author XingGao
 * @since 2022-07-13 09:53:11
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("sys_admin_group")
public class SysAdminGroup extends Model<SysAdminGroup> {

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 上级分组
     */
    @TableField(value = "pid")
    private Integer pid;

    /**
     * 组名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 权限规则ID
     */
    @TableField(value = "rules")
    private String rules;

    /**
     * 半选中规则
     */
    @TableField(value = "half_rules")
    private String halfRules;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 状态:0=禁用,1=启用
     */
    @TableField(value = "status")
    private Integer status;

    @TableField(exist = false)
    public List<SysAdminGroup> children;


}

