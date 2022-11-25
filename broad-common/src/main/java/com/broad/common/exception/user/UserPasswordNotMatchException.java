package com.broad.common.exception.user;

/**
 * 用户密码不正确或不符合规范异常类
 *
 * @author XingGao
 */
public class UserPasswordNotMatchException extends UserException {
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new User password not match exception.
     */
    public UserPasswordNotMatchException() {
        super("user.password.not.match", null);
    }
}
