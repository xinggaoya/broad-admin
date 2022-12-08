package com.broad.framework.web.service;

import com.broad.common.constant.TokenConstants;
import com.broad.common.exception.auth.NotLoginException;
import com.broad.common.service.RedisService;
import com.broad.common.utils.StringUtils;
import com.broad.framework.web.entity.TokenEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: XingGao
 * @date: 2022/12/4
 * @description:
 */
@Slf4j
@Component
public class TokenService {

    @Value("${broad-security.header}")
    private String header;
    @Value("${broad-security.expiration}")
    private long expire;
    @Value("${broad-security.secret}")
    private String appSecret;

    @Value("${broad-security.isSingle}")
    private Boolean isSingle;

    @Autowired
    private RedisService redisService;

    /**
     * 存入redis
     *
     * @param id 用户
     */
    public void saveLoginToken(String uuid, Object id) {
        String key = header.concat(TokenConstants.LOGIN_KEY).concat(String.valueOf(id));
        String tokenKey = header.concat(TokenConstants.TOKEN_KEY).concat(uuid);

        TokenEntity tokenEntity = new TokenEntity();
        if (redisService.hasKey(key) && !isSingle) {
            tokenEntity = redisService.getCacheObject(key);
            tokenEntity.getTokenSignList().add(uuid);
        } else {
            List<String> tokenSignList = new ArrayList<>();
            tokenSignList.add(uuid);
            tokenEntity.setTokenSignList(tokenSignList);
            tokenEntity.setId(key);
            tokenEntity.setCreateTime(System.currentTimeMillis());

            // 用户登录时，修改之前的token状态
            updateTokenStatus(key, TokenConstants.LOGIN_DOWN_LINE);
        }
        // 保存session
        redisService.setCacheObject(key, tokenEntity, expire);
        // 保存token
        redisService.setCacheObject(tokenKey, id, expire);
    }


    /**
     * 获取用户Id
     *
     * @param request request
     * @return id
     */
    public Object getLoginId(HttpServletRequest request) {
        String token = request.getHeader(header);
        if (StringUtils.isBlank(token)) {
            throw new AccountExpiredException("请先登录");
        }
        String key = header.concat(TokenConstants.TOKEN_KEY).concat(token);

        Integer code = redisService.getCacheObject(key);
        if (code == null) {
            throw new AccountExpiredException("登录无效");
        }
        if (code == TokenConstants.LOGIN_DOWN_LINE) {
            throw new NotLoginException("您的账号已在其他地方登录");
        }
        if (code == TokenConstants.LOGIN_EXPIRE) {
            throw new NotLoginException("登录已过期");
        }
        return code;
    }

    public Integer getLoginIdAsInt(HttpServletRequest request) {
        return getLoginId(request) == null ? null : Integer.parseInt(String.valueOf(getLoginId(request)));
    }

    public String getLoginIdAsString(HttpServletRequest request) {
        return getLoginId(request) == null ? null : String.valueOf(getLoginId(request));
    }

    /**
     * 退出登录
     *
     * @param uuid uuid
     */
    public void logout(String uuid) {
        redisService.deleteObject(header.concat(TokenConstants.LOGIN_KEY).concat(uuid));
    }


    /**
     * 修改token状态
     *
     * @param key  key
     * @param code code
     */
    public void updateTokenStatus(String key, int code) {
        if (redisService.hasKey(key)) {
            String tokenKey = header.concat(TokenConstants.TOKEN_KEY);
            TokenEntity tokenEntity = redisService.getCacheObject(key);
            List<String> tokenSignList = tokenEntity.getTokenSignList();
            tokenSignList.forEach(token -> {
                redisService.setCacheObject(tokenKey.concat(token), code);
            });
        }
    }

    // 处理异常


}