package com.broad.framework.security;

import cn.dev33.satoken.listener.SaTokenListener;
import cn.dev33.satoken.stp.SaLoginModel;
import com.broad.common.web.socket.UserSocket;
import org.springframework.stereotype.Component;

/**
 * The type Sa token listener.
 *
 * @Author: XingGao
 * @Date: 2022 /10/13 13:54
 * @Description: 自定义侦听器的实现
 */
@Component
public class SaTokenListenerImpl implements SaTokenListener {

    /**
     * 每次登录时触发
     */
    @Override
    public void doLogin(String loginType, Object loginId, String tokenValue, SaLoginModel loginModel) {
//        SysNotice sysNotice = new SysNotice();
//        sysNotice.setTitle("您有一条新的消息");
//        sysNotice.setDescription("You have a new message");
//        sysNotice.setContent("登录成功");
//        sysNotice.setType(NoticeConstants.NOTICE_TYPE);
//        sysNotice.setConfirm(NoticeConstants.NOTICE_CONFIRM);
//        sysNotice.setMeta(LocalDateTime.now());
//        ResultData resultData = ResultData.success(sysNotice);
//        UserSocketServer.sendMessageById(resultData, loginId.toString());
    }

    /**
     * 每次注销时触发
     */
    @Override
    public void doLogout(String loginType, Object loginId, String tokenValue) {
    }

    /**
     * 每次被踢下线时触发
     */
    @Override
    public void doKickout(String loginType, Object loginId, String tokenValue) {
        UserSocket.sendSelfCheck(loginId.toString());
    }

    /**
     * 每次被顶下线时触发
     */
    @Override
    public void doReplaced(String loginType, Object loginId, String tokenValue) {
        UserSocket.sendSelfCheck(loginId.toString());
    }

    /**
     * 每次被封禁时触发
     */
    @Override
    public void doDisable(String loginType, Object loginId, String service, int level, long disableTime) {
        UserSocket.sendSelfCheck(loginId.toString());
    }

    /**
     * 每次被解封时触发
     */
    @Override
    public void doUntieDisable(String loginType, Object loginId, String service) {
    }

    /**
     * 每次二级认证时触发
     */
    @Override
    public void doOpenSafe(String loginType, String tokenValue, String service, long safeTime) {
    }

    /**
     * 每次退出二级认证时触发
     */
    @Override
    public void doCloseSafe(String loginType, String tokenValue, String service) {
    }

    /**
     * 每次创建Session时触发
     */
    @Override
    public void doCreateSession(String id) {
    }

    /**
     * 每次注销Session时触发
     */
    @Override
    public void doLogoutSession(String id) {
    }

    /**
     * 每次Token续期时触发
     */
    @Override
    public void doRenewTimeout(String tokenValue, Object loginId, long timeout) {
    }
}

