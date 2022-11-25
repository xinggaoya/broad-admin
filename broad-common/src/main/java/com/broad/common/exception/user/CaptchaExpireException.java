package com.broad.common.exception.user;

/**
 * 验证码失效异常类
 *
 * @author XingGao
 */
public class CaptchaExpireException extends UserException {
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new Captcha expire exception.
     */
    public CaptchaExpireException() {
        super("user.jcaptcha.expire", null);
    }
}
