package com.broad.web.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.broad.common.utils.http.HttpUtils;
import com.broad.system.service.SysMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private SysMonitorService sysMonitorService;

    /**
     * Test simple.
     */
    @GetMapping("/test")
    @SaIgnore
    public void testSimple() {
        try {
            HttpUtils.downloadFile("https://github.com/lionsoul2014/ip2region/blob/master/data/ip2region.xdb"
                    , "C:\\Users\\10322\\Downloads\\ip2region.xdb");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test simple 2 string.
     *
     * @return the string
     */
    @GetMapping("/test/1")
    @SaIgnore
    public Object testSimple2() {
        return sysMonitorService.getServer();
    }
}
