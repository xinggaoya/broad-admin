package com.broad.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (SysRoleDept)表实体类
 *
 * @author XingGao
 * @since 2022-10-19 17:15:01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_role_dept")
public class SysRoleDept implements Serializable {

    private static final long serialVersionUID = 609672073321582750L;


    @TableField(value = "role_id")
    private Integer roleId;

    @TableField(value = "dept_id")
    private Integer deptId;

}

