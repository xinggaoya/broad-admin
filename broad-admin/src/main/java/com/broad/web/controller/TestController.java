package com.broad.web.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.broad.common.utils.file.FileUtils;
import com.broad.framework.web.TokenUtil;
import com.broad.system.entity.SysUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    @SaIgnore
    public SysUser testSimple(String message, String sid) {
//        UserSocketServer.sendInfo(message, sid);
        return TokenUtil.getLoginUser();
    }

    /**
     * Test simple 2 string.
     *
     * @return the string
     */
    @GetMapping("/test/1")
    public List<String> testSimple2() {
        String path = FileUtils.LOCAL_UPLOAD_PATH;
        List<String> list = new ArrayList<>();
        List<File> fileList = FileUtils.getAllFiles(path);
        for (File file : fileList) {
            // 裁剪文件路径
            String filePath = file.getPath().substring(path.length());
            list.add("http://localhost:8000/upload/" + filePath);
        }
        return list;
    }

}
