package com.broad.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.broad.common.enums.BooleanEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * (SysMenu)表实体类
 *
 * @author XingGao
 * @since 2022-10-10 18:59:10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_menu")
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 679616195685150919L;

    /**
     * 路由id
     */
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Integer menuId;
    /**
     * 父路由
     */
    @TableField(value = "parent_id")
    private Integer parentId;
    /**
     * 路由名称
     */
    @TableField(value = "route_name")
    private String routeName;
    /**
     * 菜单名称
     */
    @TableField(value = "menu_name")
    private String menuName;
    /**
     * 菜单路径
     */
    @TableField(value = "menu_url")
    private String menuUrl;
    /**
     * 菜单路径
     */
    @TableField(value = "iframe_url")
    private String iframeUrl;
    /**
     * 0正常，1禁用
     */
    @TableField(value = "status")
    private String status;
    /**
     * 路由权限
     */
    @TableField(value = "perme")
    private String perme;
    /**
     * 菜单图标
     */
    @TableField(value = "icon")
    private String icon;
    /**
     * 图标类型
     */
    @TableField(value = "icon_prefix")
    private String iconPrefix;
    /**
     * 组件路径
     */
    @TableField(value = "local_file_path")
    private String localFilePath;
    /**
     * 菜单排序
     */
    @TableField(value = "order_num")
    private Integer orderNum;
    /**
     * 0缓存，1不缓存
     */
    @TableField(value = "cacheable")
    private String cacheable;
    /**
     * 0显示，1不显示
     */
    @TableField(value = "hidden")
    private String hidden;
    /**
     * 0固定标题栏，1否
     */
    @TableField(value = "affix")
    private String affix;
    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;
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
     * 子路由
     */
    @TableField(exist = false)
    private List<SysMenu> children;

    /**
     * 是否有子路由
     */
    @TableField(exist = false)
    private BooleanEnum isLeaf;


}

