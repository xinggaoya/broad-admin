package com.broad.system.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

/**
 * 管理权限分组表(SysUserRole)实体类
 *
 * @author XingGao
 * @since 2022 -07-13 10:13:11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("sys_user_role")
public class SysUserRole extends Model<SysUserRole> {

    /**
     * 管理员ID
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    /**
     * 分组ID
     */
    @TableField(value = "role_id")
    private Integer roleId;


}

