package com.broad.system.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 字典类型表(SysDictType)表实体类
 *
 * @author XingGao
 * @since 2022-10-13 15:00:02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_dict_type")
public class SysDictType implements Serializable {

    private static final long serialVersionUID = -79941953271366353L;

    /**
     * 字典主键
     */
    @TableId(value = "dict_id", type = IdType.AUTO)
    private Long dictId;

    /**
     * 字典名称
     */
    @TableField(value = "dict_name")
    private String dictName;
    /**
     * 字典类型
     */
    @TableField(value = "dict_type")
    private String dictType;
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

