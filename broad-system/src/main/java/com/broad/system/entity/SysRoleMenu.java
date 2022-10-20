package com.broad.system.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 管理权限分组表(SysRoleMenu)表实体类
 *
 * @author XingGao
 * @since 2022-10-19 17:15:02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_role_menu")
public class SysRoleMenu implements Serializable {

    private static final long serialVersionUID = 139234182108616713L;


    /**
     * 角色ID
     */
    @TableField(value = "role_id")
    private Object roleId;
    /**
     * 菜单ID
     */
    @TableField(value = "menu_id")
    private Object menuId;

    @TableField(exist = false)
    private List<Integer> menuIds;

}

