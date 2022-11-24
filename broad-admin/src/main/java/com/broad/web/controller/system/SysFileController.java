package com.broad.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.broad.common.utils.file.FileUtils;
import com.broad.common.web.controller.BaseController;
import com.broad.common.web.entity.ResultData;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: XingGao
 * @Date: 2022/11/21
 * @Description:
 */
@RestController
@ResponseBody
@RequestMapping("file")
public class SysFileController extends BaseController {

    @PostMapping("/upload")
    @SaCheckLogin
    public ResultData uploadFile(@RequestParam("file") MultipartFile file) {
        return ResultData.success(FileUtils.saveFileToLocal(file));
    }
}
