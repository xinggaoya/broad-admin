package com.broad.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.io.Serializable;

/**
 * 管理分组表(SysRole)实体类
 *
 * @author XingGao
 * @since 2022 -07-13 09:53:11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@TableName("sys_role")
public class SysRole extends Model<SysRole> implements Serializable {

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 组名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 半选中规则
     */
    @TableField(value = "half_rules")
    private String halfRules;

    /**
     * The Children.
     */
    @TableField(exist = false)
    public List<SysRole> children;

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
    private String status;
    /**
     * 备注
     */
    @TableField(value = "remarks")
    private String remarks;

}
