package com.broad.common.web.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * 管理员表(SysUser)实体类
 *
 * @author XingGao
 * @since 2022 -07-09 17:19:40
 */
@Data
@TableName("sys_user")
public class SysUser implements UserDetails {

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
     * 登录地址
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
    private String codeId;

    @TableField(exist = false)
    private String codeValue;

    @TableField(exist = false)
    private List<Integer> roleIds;

    @TableField(exist = false)
    private String tokenValue;

    @TableField(exist = false)
    private String deptName;

    @TableField(exist = false)
    private Set<String> roleCode;
    @TableField(exist = false)
    private Set<String> permissions;

    /**
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (roleCode != null) {
            roleCode.forEach(role -> authorities.add((GrantedAuthority) () -> role));
        }
        return authorities;
    }

    /**
     * @return
     */
    @Override
    public String getUsername() {
        return this.userName;
    }

    /**
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return "0".equals(this.userStatus);
    }

    /**
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
