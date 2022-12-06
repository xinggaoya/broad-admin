package com.broad.framework.security.filter;

import com.broad.common.utils.StringUtils;
import com.broad.framework.security.service.SecurityUserService;
import com.broad.framework.web.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: XingGao
 * @date: 2022/12/4
 * @description: JWT登录授权过滤器
 */
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    /**
     * 直接将我们前面写好的service注入进来，通过service获取到当前用户的权限
     */
    @Autowired
    private SecurityUserService userDetailsService;
    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        // 获取到当前用户的account
        String account = tokenService.getMemberAccountByJwtToken(httpServletRequest);

        // 当token中的username不为空时进行验证token是否是有效的token
        if (StringUtils.isNotNull(account) && SecurityContextHolder.getContext().getAuthentication() == null) {
            // token中username不为空，并且Context中的认证为空，进行token验证

            // 获取到用户的信息，也就是获取到用户的权限
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(account);
            // 验证当前token是否有效
            if (tokenService.checkToken(httpServletRequest)) {

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                //将authentication放入SecurityContextHolder中
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        // 放行给下个过滤器
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
