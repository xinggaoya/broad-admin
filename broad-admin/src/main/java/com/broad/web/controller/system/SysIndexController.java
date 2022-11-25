package com.broad.web.controller.system;

import com.broad.common.config.BroadConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Sys index controller.
 *
 * @Author: XingGao
 * @Date: 2022 /11/21
 * @Description:
 */
@RestController
@ResponseBody
public class SysIndexController {

    @Autowired
    private BroadConfig broadConfig;

    /**
     * Test string.
     *
     * @return the string
     */
    @GetMapping("/")
    public String test() {
        return "欢迎使用".concat(broadConfig.getName()).concat("后台管理系统！当前版本：").concat(broadConfig.getVersion()).concat(";请使用前端访问！");
    }
}
