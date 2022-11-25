package com.broad.web.controller;

import com.broad.common.socket.service.UserSocketServer;
import com.broad.common.utils.ServletUtils;
import com.broad.common.utils.ip.IpUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Test controller.
 *
 * @Author: XingGao
 * @Date: 2022 /09/30 10:54
 * @Description: 测试
 */
@RestController
@ResponseBody
public class TestController {

    /**
     * Test simple.
     *
     * @param message the message
     * @param sid     the sid
     */
    @GetMapping("/test")
    public void testSimple(String message, String sid) {
        UserSocketServer.sendInfo(message, sid);
    }

    /**
     * Test simple 2 string.
     *
     * @return the string
     */
    @GetMapping("/test/1")
    public String testSimple2() {
        return IpUtils.getIpAddress(IpUtils.getIp(ServletUtils.getRequest()));
    }

}
