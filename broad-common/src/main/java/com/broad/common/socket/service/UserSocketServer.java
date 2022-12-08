package com.broad.common.socket.service;

import com.alibaba.fastjson2.JSON;
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
    private static int onlineCount = 0;
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static CopyOnWriteArraySet<UserSocketServer> webSocketSet = new CopyOnWriteArraySet<>();

    private Session session;

    /**
     * 接收sid
     */
    private String sid = "";

    /**
     * 群发自定义消息
     *
     * @param message the message
     * @param sid     the sid
     */
    public static void sendInfo(Object message, @PathParam("sid") String sid) {
        log.info("推送消息到窗口" + sid + "，推送内容:" + message);
        String msg = JSON.toJSONString(ResultData.success(message));
        for (UserSocketServer item : webSocketSet) {
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
                if (sid == null) {
                    item.sendMessage(msg);
                } else if (item.sid.equals(sid)) {
                    item.sendMessage(msg);
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * 禁止重复登录
     *
     * @param sid 用户id
     */
    private static Boolean removeUser(String sid) {
        boolean flag = false;
        for (UserSocketServer item : webSocketSet) {
            if (item.sid.equals(sid)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * Gets online count.
     *
     * @return the online count
     */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /**
     * Add online count.
     */
    public static synchronized void addOnlineCount() {
        UserSocketServer.onlineCount++;
    }

    /**
     * Sub online count.
     */
    public static synchronized void subOnlineCount() {
        UserSocketServer.onlineCount--;
    }

    /**
     * 获取所有登录用户的sid
     *
     * @return ids
     */
    public static synchronized List<Long> getLoginId() {
        List<Long> ids = new ArrayList<>();
        webSocketSet.forEach(item -> {
            ids.add(Long.parseLong(item.sid));
        });
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
        // 禁止重复登录
        if (removeUser(sid)) {
            log.info("用户已登录，禁止重复登录");
            return;
        }
        webSocketSet.add(this);
        this.sid = sid;
        addOnlineCount();
//        sendInfo(ResultData.success("连接成功"), sid);
        log.info("有新用户开始连接:" + sid + ",当前在线人数为:" + getOnlineCount());

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        subOnlineCount();
        //断开连接情况下，更新主板占用情况为释放
        log.info("释放的sid为：" + sid);
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
