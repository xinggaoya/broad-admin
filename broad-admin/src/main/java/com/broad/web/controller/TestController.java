package com.broad.web.controller;

import com.broad.common.utils.ServletUtils;
import com.broad.common.utils.ip.IpUtils;
import com.broad.framework.rabbit.producer.UserLogProducer;
import com.broad.system.entity.SysUserLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @Author: XingGao
 * @Date: 2022/09/30 10:54
 * @Description: 测试
 */
@RestController
public class TestController {
    @Autowired
    private UserLogProducer userLogProducer;

    @GetMapping("/test")
    public void testSimple() {
        for (int i = 0; i < 20; i++) {
            userLogProducer.sendLogMessage(SysUserLog.builder().adminId(i).createTime(new Date()).logDescription("开始").build());
        }
    }

    @GetMapping("/test/1")
    public String testSimple2() {
        return IpUtils.getIpAddress(IpUtils.getIp(ServletUtils.getRequest()));
    }
}
