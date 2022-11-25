package com.broad.common.exception.user;


import com.broad.common.exception.base.BaseException;

/**
 * 用户信息异常类
 *
 * @author XingGao
 */
public class UserException extends BaseException {
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new User exception.
     *
     * @param code the code
     * @param args the args
     */
    public UserException(String code, Object[] args) {
        super("user", code, args, null);
    }
}
