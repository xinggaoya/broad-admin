package com.broad.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 管理员表(SysAdmin)实体类
 *
 * @author XingGao
 * @since 2022 -07-09 17:19:40
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("sys_admin")
public class SysAdmin implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    @TableField(value = "user_name")
    @NotNull(message = "用户名不能为空")
    private String userName;

    /**
     * 昵称
     */
    @TableField(value = "nick_name")
    private String nickName;

    /**
     * 部门ID
     */
    @TableField(value = "dept_id")
    private Integer deptId;

    /**
     * 头像
     */
    @TableField(value = "avatar")
    private String avatar;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 手机
     */
    @TableField(value = "mobile")
    private String mobile;

    /**
     * 登录IP
     */
    @TableField(value = "last_ip")
    private String lastIp;

    /**
     * 登录时间
     */
    @TableField(value = "last_logintime")
    private Date lastLogintime;

    /**
     * 登录IP
     */
    @TableField(value = "last_loginip")
    private String lastLoginip;

    /**
     * 密码
     */
    @TableField(value = "password")
    @NotNull(message = "密码不能为空")
    private String password;

    /**
     * 密码盐
     */
    @TableField(value = "salt")
    private String salt;

    /**
     * 签名
     */
    @TableField(value = "motto")
    private String motto;

    /**
     * 状态:1=禁用,0=启用
     */
    @TableField(value = "user_status")
    private String userStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(exist = false)
    private Integer roleId;

    @TableField(exist = false)
    private String groupName;

    @TableField(exist = false)
    private String codeId;

    @TableField(exist = false)
    private String codeValue;

    @TableField(exist = false)
    private String tokenValue;

    @TableField(exist = false)
    private String deptName;

}

