package com.broad.common.web.socket.service;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.broad.common.constant.CacheConstants;
import com.broad.common.constant.HttpStatus;
import com.broad.common.exception.ServiceException;
import com.broad.common.service.RedisService;
import com.broad.common.utils.SpringUtils;
import com.broad.common.web.entity.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * The type User socket server.
 *
 * @Author: XingGao
 * @Date: 2022 /11/22
 * @Description:
 */
@Component
@Slf4j
@Service
@ServerEndpoint("/api/websocket/{sid}")
public class UserSocketServer {
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static CopyOnWriteArraySet<UserSocketServer> webSocketSet = new CopyOnWriteArraySet<>();

    private Session session;
    /**
     * 接收窗口Id
     */
    private String sid = "";


    /**
     * 自检查
     */
    public static synchronized void sendSelfCheck(String sid) {
        // 使用redis防止短时间重复推送
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        // 防止重复发送
        for (UserSocketServer item : webSocketSet) {
            try {
                if (item.sid.equals(sid) && redisService.getCacheObject(CacheConstants.SELF_CHECK + sid) == null) {
                    item.sendMessage(JSON.toJSONString(ResultData.code(HttpStatus.UNAUTHORIZED)));
                    redisService.setCacheObject(CacheConstants.SELF_CHECK + sid, System.currentTimeMillis(), 5L);
                }
            } catch (IOException e) {
                throw new ServiceException("发送消息失败");
            }
        }
    }

    /**
     * 根据id发送消息
     *
     * @param result the result
     * @param sid    the sid
     */
    public static void sendMessageById(Object result, @PathParam("sid") String sid) {
        log.info("发送消息到:" + sid + "，报文:" + result);
        for (UserSocketServer item : webSocketSet) {
            try {
                if (item.sid.equals(sid)) {
                    item.sendMessage(JSON.toJSONString(result));
                }
            } catch (IOException e) {
                throw new ServiceException("发送消息失败");
            }
        }
    }

    /**
     * 群发自定义消息
     *
     * @param result the result
     */
    public static void sendMess(Object result) {
        log.info("群发消息，报文:" + result);
        String msg = JSON.toJSONString(result);
        for (UserSocketServer item : webSocketSet) {
            try {
                item.sendMessage(msg);
            } catch (IOException e) {
                throw new ServiceException("发送消息失败");
            }
        }
    }

    /**
     * 清理已登录用户
     *
     * @param sid 用户id
     */
    private static void removeUser(String sid) {
        for (UserSocketServer item : webSocketSet) {
            if (item.sid.equals(sid)) {
                webSocketSet.remove(item);
                log.info("清理用户:" + sid + ",当前在线人数为:" + getOnlineCount());
            }
        }
    }

    /**
     * Gets online count.
     *
     * @return the online count
     */
    public static synchronized int getOnlineCount() {
        return webSocketSet.size();
    }

    /**
     * 获取所有登录用户的sid
     *
     * @return ids
     */
    public static synchronized List<Long> getLoginId() {
        List<Long> ids = new ArrayList<>();
        webSocketSet.forEach(item -> ids.add(Long.parseLong(item.sid)));
        return ids;
    }

    /**
     * Gets web socket set.
     *
     * @return the web socket set
     */
    public static CopyOnWriteArraySet<UserSocketServer> getWebSocketSet() {
        return webSocketSet;
    }

    /**
     * 连接建立成功调用的方法
     *
     * @param session the session
     * @param sid     the sid
     * @throws IOException the io exception
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) throws IOException {
        this.session = session;
        if (StpUtil.getTokenValueByLoginId(sid) == null) {
            log.info("未登录用户，禁止连接");
            return;
        }
        // 清理已登录重复用户
        removeUser(sid);

        this.sid = sid;
        webSocketSet.add(this);
//        sendInfo(ResultData.success("连接成功"), sid);
        log.info("有新用户开始连接:" + sid + ",当前在线人数为:" + getOnlineCount());

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        //这里写你 释放的时候，要处理的业务
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());

    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message the message
     * @param session the session
     * @ Param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自窗口" + sid + "的信息:" + message);
        //群发消息
        for (UserSocketServer item : webSocketSet) {
            try {
                if (!item.sid.equals(sid)) {
                    item.sendMessage(sid + "：" + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param session the session
     * @param error   the error
     * @ Param session
     * @ Param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     *
     * @param message the message
     * @throws IOException the io exception
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
}
