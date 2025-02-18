package com.broad.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 字典数据表(SysDictData)表实体类
 *
 * @author XingGao
 * @since 2022 -10-13 15:00:02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_dict_data")
public class SysDictData implements Serializable {

    private static final long serialVersionUID = 388951409245590777L;

    /**
     * 字典编码
     */
    @TableId(value = "dict_code", type = IdType.AUTO)
    private Long dictCode;

    /**
     * 字典排序
     */
    @TableField(value = "dict_sort")
    private Integer dictSort;
    /**
     * 字典标签
     */
    @TableField(value = "dict_label")
    private String dictLabel;
    /**
     * 字典键值
     */
    @TableField(value = "dict_value")
    private String dictValue;
    /**
     * 字典类型
     */
    @TableField(value = "dict_type")
    private String dictType;
    /**
     * 样式属性（其他样式扩展）
     */
    @TableField(value = "css_class")
    private String cssClass;
    /**
     * 表格回显样式
     */
    @TableField(value = "list_class")
    private String listClass;
    /**
     * 是否默认（Y是 N否）
     */
    @TableField(value = "is_default")
    private String isDefault;
    /**
     * 状态（0正常 1停用）
     */
    @TableField(value = "status")
    private String status;

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
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

}
