package com.broad.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.util.Date;

/**
 * 管理员表(SysAdmin)实体类
 *
 * @author XingGao
 * @since 2022-07-09 17:19:40
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("sys_admin")
public class SysAdmin extends Model<SysAdmin> {
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 昵称
     */
    @TableField(value = "nick_name")
    private String nickName;

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
     * 登录失败次数
     */
    @TableField(value = "login_failure")
    private Integer loginFailure;

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
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 状态:0=禁用,1=启用
     */
    @TableField(value = "status")
    private Integer status;

}

