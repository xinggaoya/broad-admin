package com.broad.framework.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author: XingGao
 * @date: 2022/12/4
 * @description:
 */
public interface SecurityUserService extends UserDetailsService {
    UserDetails loadUserById(Long id) throws UsernameNotFoundException;
}
