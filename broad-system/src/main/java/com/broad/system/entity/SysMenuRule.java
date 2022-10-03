package com.broad.system.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 菜单和权限规则表(SysMenuRule)实体类
 *
 * @author XingGao
 * @since 2022-07-13 09:36:31
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("sys_menu_rule")
public class SysMenuRule implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 上级菜单
     */
    @TableField(value = "pid")
    private Integer pid;

    /**
     * 类型:menu_dir=菜单目录,menu=菜单项,button=页面按钮
     */
    @TableField(value = "type")
    private String type;

    /**
     * 标题
     */
    @TableField(value = "title")
    @NotNull(message = "标题不能为空")
    private String title;

    /**
     * 规则名称
     */
    @TableField(value = "name")
    @NotNull(message = "规则名称不能为空")
    private String name;

    /**
     * 路由路径
     */
    @TableField(value = "path")
    private String path;

    /**
     * 图标
     */
    @TableField(value = "icon")
    private String icon;

    /**
     * 菜单类型:tab=选项卡,link=链接,iframe=Iframe
     */
    @TableField(value = "menu_type")
    private String menuType;


    /**
     * Url
     */
    @TableField(value = "url")
    private String url;

    /**
     * 组件路径
     */
    @TableField(value = "component")
    private String component;

    /**
     * 缓存:0=关闭,1=开启
     */
    @TableField(value = "keepalive")
    private Integer keepalive;

    /**
     * 扩展属性:none=无,add_rules_only=只添加为路由,add_menu_only=只添加为菜单
     */
    @TableField(value = "extend")
    private String extend;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 权重(排序)
     */
    @TableField(value = "weigh")
    private Long weigh;

    /**
     * 状态:0=禁用,1=启用
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;


    @TableField(exist = false)
    private List<SysMenuRule> children ;

}

