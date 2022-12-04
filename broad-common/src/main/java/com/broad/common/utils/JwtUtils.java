package com.broad.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author: XingGao
 * @date: 2022/12/4
 * @description:
 */
@Slf4j
@Component
public class JwtUtils {

    private static String header;
    private static long expire;
    private static String appSecret;

    @Value("${jwt.header}")
    public static void setHeader(String header) {
        JwtUtils.header = header;
    }

    public static String createJwtToken(String id, String account) {
        String JwtToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setSubject("jacob-user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .claim("id", id)
                .claim("account", account)
                .signWith(SignatureAlgorithm.HS256, appSecret)
                .compact();

        return JwtToken;
    }

    public static String createJwtToken(String id) {

        String JwtToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setSubject("jacob-user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .claim("id", id)
                .signWith(SignatureAlgorithm.HS256, appSecret)
                .compact();

        return JwtToken;
    }

    /**
     * 根据token，判断token是否存在与有效
     *
     * @param jwtToken
     * @return
     */
    public static boolean checkToken(String jwtToken) {
        if (StringUtils.isEmpty(jwtToken)) {
            return false;
        }
        try {
            Jwts.parser().setSigningKey(appSecret).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 根据request判断token是否存在与有效（也就是把token取出来罢了）
     *
     * @param request
     * @return
     */
    public static boolean checkToken(HttpServletRequest request) {
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
     * 根据token获取会员id
     *
     * @param request
     * @return
     */
    public static String getMemberIdByJwtToken(HttpServletRequest request) {
        String jwtToken = request.getHeader(header);
        if (StringUtils.isEmpty(jwtToken)) {
            return "";
        }
        try {
            // 这里解析可能会抛异常，所以try catch来捕捉
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(appSecret).parseClaimsJws(jwtToken);
            Claims claims = claimsJws.getBody();
            return (String) claims.get("id");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 根据token获取用户的account
     *
     * @param jwtToken token
     * @return account
     */
    public static String getMemberAccountByJwtToken(String jwtToken) {
        if (StringUtils.isEmpty(jwtToken)) {
            return "";
        }
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(appSecret).parseClaimsJws(jwtToken);
            Claims claims = claimsJws.getBody();
            return (String) claims.get("account");
        } catch (Exception e) {
            log.error("解析token出错");
            return "";
        }
    }

    @Value("${jwt.expiration}")
    public void setAppSecret(long EXPIRE) {
        JwtUtils.expire = EXPIRE;
    }

    @Value("${jwt.secret}")
    public void setAppSecret(String APP_SECRET) {
        JwtUtils.appSecret = APP_SECRET;
    }
}