package com.broad.system.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (SysUserRole)表实体类
 *
 * @author XingGao
 * @since 2022 -10-21 01:03:46
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user_role")
public class SysUserRole implements Serializable {

    private static final long serialVersionUID = -93280302663195627L;


    @TableField(value = "user_id")
    private Integer userId;
    @TableField(value = "role_id")
    private Integer roleId;

}

