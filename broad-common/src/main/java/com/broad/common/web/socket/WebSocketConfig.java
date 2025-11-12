package com.broad.common.web.socket;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * WebSocket配置类
 * 提供WebSocket端点的配置和依赖注入支持
 *
 * @author XingGao
 */
@Slf4j
public class WebSocketConfig extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        // 获取请求头信息
        String userAgent = getHeader(request, "User-Agent");
        String ipAddress = getRemoteAddress(request);

        // 从URL参数或请求头获取token
        String token = getTokenFromRequest(request);

        // 验证token（userId将在onOpen时从路径参数获取）
        boolean isValid = StrUtil.isNotBlank(token) && isValidToken(token);

        // 将信息存储到用户属性中，供后续使用
        sec.getUserProperties().put("user-agent", userAgent);
        sec.getUserProperties().put("remoteAddress", ipAddress);
        sec.getUserProperties().put("token", token);
        sec.getUserProperties().put("isValid", isValid);

        if (isValid) {
            // 获取用户名
            String username = getUsernameFromToken(token);
            if (username != null) {
                sec.getUserProperties().put("username", username);
            }
        }

        log.debug("WebSocket握手: isValid={}, userAgent={}, ipAddress={}",
                  isValid, userAgent, ipAddress);

        super.modifyHandshake(sec, request, response);
    }

    /**
     * 从请求中提取Token
     */
    private String getTokenFromRequest(HandshakeRequest request) {
        // 方式1：从URL参数获取（推荐）
        String query = request.getQueryString();
        if (StrUtil.isNotBlank(query) && query.contains("satoken=")) {
            String[] params = query.split("&");
            for (String param : params) {
                if (param.startsWith("satoken=")) {
                    return param.substring(8);
                }
            }
        }

        // 方式2：从Header获取
        String headerToken = getHeader(request, "satoken");
        if (StrUtil.isNotBlank(headerToken)) {
            return headerToken;
        }

        // 方式3：从Cookie获取
        String cookie = getHeader(request, "Cookie");
        if (StrUtil.isNotBlank(cookie) && cookie.contains("satoken=")) {
            String[] cookies = cookie.split(";");
            for (String c : cookies) {
                String trimmed = c.trim();
                if (trimmed.startsWith("satoken=")) {
                    return trimmed.substring(8);
                }
            }
        }

        return null;
    }

    /**
     * 验证Token是否有效
     */
    private boolean isValidToken(String token) {
        try {
            Object loginId = StpUtil.getLoginIdByToken(token);
            return loginId != null && StpUtil.isLogin(loginId.toString());
        } catch (Exception e) {
            log.warn("Token验证失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 从Token获取用户名
     */
    private String getUsernameFromToken(String token) {
        try {
            Object loginId = StpUtil.getLoginIdByToken(token);
            if (loginId != null) {
                return "用户" + loginId.toString();
            }
        } catch (Exception e) {
            log.warn("从Token获取用户信息失败: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 获取请求头
     */
    private String getHeader(HandshakeRequest request, String headerName) {
        try {
            var headers = request.getHeaders().get(headerName);
            if (headers != null && !headers.isEmpty()) {
                String header = headers.get(0);
                return StrUtil.isNotBlank(header) ? header : "unknown";
            }
            return "unknown";
        } catch (Exception e) {
            return "unknown";
        }
    }

    /**
     * 获取远程客户端IP地址
     */
    private String getRemoteAddress(HandshakeRequest request) {
        try {
            String xForwardedFor = getHeader(request, "X-Forwarded-For");
            if (StrUtil.isNotBlank(xForwardedFor) && !"unknown".equals(xForwardedFor)) {
                String[] ips = xForwardedFor.split(",");
                return ips.length > 0 ? ips[0].trim() : "unknown";
            }

            String xRealIp = getHeader(request, "X-Real-IP");
            if (StrUtil.isNotBlank(xRealIp) && !"unknown".equals(xRealIp)) {
                return xRealIp;
            }

            return "unknown";
        } catch (Exception e) {
            log.warn("获取远程地址失败: {}", e.getMessage());
            return "unknown";
        }
    }
}
