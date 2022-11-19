package com.broad.web.controller;

import com.broad.common.utils.ServletUtils;
import com.broad.common.utils.ip.IpUtils;
import com.broad.framework.rabbit.entity.Simple;
import com.broad.framework.rabbit.entity.SimpleProducer;
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
    private SimpleProducer simpleProducer;

    @GetMapping("/test")
    public void testSimple() {
        for (int i = 0; i < 10; i++) {
            simpleProducer.sendOrderMessage(Simple.builder().createTime(new Date()).name("JulyWhj").age(i).no("ID-0001").phone("138XXXXXXXX").build());
        }
    }

    @GetMapping("/test/1")
    public String testSimple2() {
        return IpUtils.getIpAddress(IpUtils.getIp(ServletUtils.getRequest()));
    }
}
