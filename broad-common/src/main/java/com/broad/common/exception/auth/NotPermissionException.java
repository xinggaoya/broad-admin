package com.broad.common.exception.auth;

import java.util.Arrays;

/**
 * 未能通过的权限认证异常
 *
 * @author XingGao
 */
public class NotPermissionException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new Not permission exception.
     *
     * @param permission the permission
     */
    public NotPermissionException(String permission) {
        super(permission);
    }

    /**
     * Instantiates a new Not permission exception.
     *
     * @param permissions the permissions
     */
    public NotPermissionException(String[] permissions) {
        super(Arrays.toString(permissions));
    }
}
