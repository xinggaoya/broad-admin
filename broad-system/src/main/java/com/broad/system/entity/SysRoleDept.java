package com.broad.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

/**
 * (SysRoleDept)表实体类
 *
 * @author XingGao
 * @since 2022 -10-19 17:15:01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_role_dept")
public class SysRoleDept implements Serializable {

    private static final long serialVersionUID = 609672073321582750L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "role_id")
    private Integer roleId;

    @TableField(value = "dept_id")
    private Integer deptId;

}
