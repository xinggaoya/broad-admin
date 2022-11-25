package com.broad.common.exception.auth;

import org.apache.commons.lang3.StringUtils;

/**
 * 未能通过的角色认证异常
 *
 * @author XingGao
 */
public class NotRoleException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new Not role exception.
     *
     * @param role the role
     */
    public NotRoleException(String role) {
        super(role);
    }

    /**
     * Instantiates a new Not role exception.
     *
     * @param roles the roles
     */
    public NotRoleException(String[] roles) {
        super(StringUtils.join(roles, ","));
    }
}
