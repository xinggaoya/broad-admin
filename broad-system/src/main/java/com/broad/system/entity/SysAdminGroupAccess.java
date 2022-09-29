package com.broad.system.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

/**
 * 管理权限分组表(SysAdminGroupAccess)实体类
 *
 * @author XingGao
 * @since 2022 -07-13 10:13:11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("sys_admin_group_access")
public class SysAdminGroupAccess extends Model<SysAdminGroupAccess> {

    /**
     * 管理员ID
     */
    @TableId(value = "uid", type = IdType.AUTO)
    private Integer uid;

    /**
     * 分组ID
     */
    @TableField(value = "group_id")
    private Integer groupId;

    /**
     * 权限字符
     */
    @TableField(exist = false)
    private String rules;


}

