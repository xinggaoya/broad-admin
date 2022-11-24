package com.broad.web.controller;

import com.broad.common.utils.ServletUtils;
import com.broad.common.utils.ip.IpUtils;
import com.broad.framework.socket.service.UserSocketServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: XingGao
 * @Date: 2022/09/30 10:54
 * @Description: 测试
 */
@RestController
@ResponseBody
public class TestController {

    @GetMapping("/test")
    public void testSimple(String message, String sid) {
        UserSocketServer.sendInfo(message, sid);
    }

    @GetMapping("/test/1")
    public String testSimple2() {
        return IpUtils.getIpAddress(IpUtils.getIp(ServletUtils.getRequest()));
    }

}
