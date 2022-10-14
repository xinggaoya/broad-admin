package com.broad.web.controller;

import com.broad.framework.rabbit.entity.Simple;
import com.broad.framework.rabbit.entity.SimpleProducer;
import com.broad.system.entity.SysMenu;
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
    public SysMenu testSimple2(SysMenu sysMenu) {
        System.out.println(sysMenu.toString());
        return sysMenu;
    }
}
