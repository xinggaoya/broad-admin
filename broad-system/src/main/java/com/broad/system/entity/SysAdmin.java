package com.broad.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
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
@ApiModel(value = "管理员表(SysAdmin)实体类")
public class SysAdmin extends Model<SysAdmin> {

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID")
    private Integer id;

    /**
     * 用户名
     */
    @TableField(value = "user_name")
    @NotNull(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名")
    private String userName;

    /**
     * 昵称
     */
    @TableField(value = "nick_name")
    @ApiModelProperty(value = "昵称")
    private String nickName;

    /**
     * 头像
     */
    @TableField(value = "avatar")
    @ApiModelProperty(value = "头像")
    private String avatar;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    @ApiModelProperty(value = "邮箱")
    private String email;

    /**
     * 手机
     */
    @TableField(value = "mobile")
    @ApiModelProperty(value = "手机")
    private String mobile;

    /**
     * 登录失败次数
     */
    @TableField(value = "login_failure")
    @ApiModelProperty(value = "登录失败次数")
    private Integer loginFailure;

    /**
     * 登录时间
     */
    @TableField(value = "last_logintime")
    @ApiModelProperty(value = "登录时间")
    private Date lastLogintime;

    /**
     * 登录IP
     */
    @TableField(value = "last_loginip")
    @ApiModelProperty(value = "登录IP")
    private String lastLoginip;

    /**
     * 密码
     */
    @TableField(value = "password")
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 密码盐
     */
    @TableField(value = "salt")
    @ApiModelProperty(value = "密码盐")
    private String salt;

    /**
     * 签名
     */
    @TableField(value = "motto")
    @ApiModelProperty(value = "签名")
    private String motto;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 状态:0=禁用,1=启用
     */
    @TableField(value = "status")
    @ApiModelProperty(value = "状态:0=禁用,1=启用")
    private Integer status;

}

