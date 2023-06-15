package com.broad.web.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.broad.common.utils.file.FileUtils;
import com.broad.common.utils.http.HttpUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @PostMapping("/test/1")
    @SaIgnore
    public List<String> testSimple2(@RequestBody Map<String, Object> map) {
        System.out.println(map);
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
