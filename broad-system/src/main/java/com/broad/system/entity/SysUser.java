package com.broad.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.broad.common.annotation.Crypto;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 管理员表(SysUser)实体类
 *
 * @author XingGao
 * @since 2022 -07-09 17:19:40
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("sys_user")
public class SysUser implements Serializable {

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
    @Crypto
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
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(exist = false)
    private String codeId;

    @TableField(exist = false)
    private String codeValue;

    @TableField(exist = false)
    private List<Integer> roleIds;

    @TableField(exist = false)
    private String tokenValue;

    @TableField(exist = false)
    private String deptName;

}
