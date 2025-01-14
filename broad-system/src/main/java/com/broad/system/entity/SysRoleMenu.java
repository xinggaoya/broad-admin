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
import java.util.List;

/**
 * 管理权限分组表(SysRoleMenu)表实体类
 *
 * @author XingGao
 * @since 2022 -10-19 17:15:02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_role_menu")
public class SysRoleMenu implements Serializable {

    private static final long serialVersionUID = 139234182108616713L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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
