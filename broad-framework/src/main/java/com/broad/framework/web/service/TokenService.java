package com.broad.framework.web.service;

import com.broad.common.constant.TokenConstants;
import com.broad.common.service.RedisService;
import com.broad.common.utils.StringUtils;
import com.broad.common.web.entity.SysUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;

/**
 * @author: XingGao
 * @date: 2022/12/4
 * @description:
 */
@Slf4j
@Component
public class TokenService {

    @Value("${jwt.header}")
    private String header;
    @Value("${jwt.expiration}")
    private long expire;
    @Value("${jwt.secret}")
    private String appSecret;

    @Autowired
    private RedisService redisService;

    public String createJwtToken(SysUser user) {
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE, Integer.parseInt(String.valueOf(expire)));
        String JwtToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setSubject("jacob-user")
                .setIssuedAt(new Date())
                .setExpiration(nowTime.getTime())
                .claim("id", user.getId())
                .claim("account", user.getUsername())
                .signWith(SignatureAlgorithm.HS256, appSecret)
                .compact();

        return JwtToken;
    }

    /**
     * 根据token获取会员id
     *
     * @param request request
     * @return 会员id
     */
    public String getMemberIdByJwtToken(HttpServletRequest request) {
        String jwtToken = request.getHeader(header);
        if (StringUtils.isEmpty(jwtToken)) {
            return null;
        }
        try {
            // 这里解析可能会抛异常，所以try catch来捕捉
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(appSecret).parseClaimsJws(jwtToken);
            Claims claims = claimsJws.getBody();
            return (String) claims.get("id");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据token获取用户的account
     *
     * @param request request
     * @return account
     */
    public String getMemberAccountByJwtToken(HttpServletRequest request) {
        String jwtToken = request.getHeader(header);
        if (StringUtils.isEmpty(jwtToken)) {
            return null;
        }
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(appSecret).parseClaimsJws(jwtToken);
            Claims claims = claimsJws.getBody();
            return (String) claims.get("account");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据request判断token是否存在与有效（也就是把token取出来罢了）
     *
     * @param request
     * @return
     */
    public boolean checkToken(HttpServletRequest request) {
        try {
            String jwtToken = request.getHeader(header);
            if (StringUtils.isEmpty(jwtToken)) {
                return false;
            }
            Jwts.parser().setSigningKey(appSecret).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 存入redis
     *
     * @param user 用户
     */
    public void saveToken(SysUser user) {
        redisService.setCacheObject(header.concat(TokenConstants.LOGIN_KEY), user, expire);
    }


}