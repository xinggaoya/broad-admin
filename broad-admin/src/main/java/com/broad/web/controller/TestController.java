package com.broad.web.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.broad.common.constant.NoticeConstants;
import com.broad.common.utils.file.FileUtils;
import com.broad.common.web.entity.ResultData;
import com.broad.common.web.socket.SysNotice;
import com.broad.common.web.socket.service.UserSocketServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
    public SysNotice testSimple(String message, String sid) throws IOException {
        SysNotice sysNotice = new SysNotice();
        sysNotice.setTitle("您有一条新的消息");
        sysNotice.setDescription("You have a new message");
        sysNotice.setContent("登录成功");
        sysNotice.setType(NoticeConstants.NOTICE_TYPE);
        sysNotice.setConfirm(NoticeConstants.NOTICE_CONFIRM);
        sysNotice.setMeta(new Date());
        ResultData resultData = ResultData.success(sysNotice);
        UserSocketServer.sendMessageById(resultData, sid);
        return sysNotice;
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
