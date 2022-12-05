package com.broad.web.controller.system;

import com.broad.common.utils.file.FileUtils;
import com.broad.common.web.controller.BaseController;
import com.broad.common.web.entity.ResultData;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Sys file controller.
 *
 * @Author: XingGao
 * @Date: 2022 /11/21
 * @Description:
 */
@RestController
@ResponseBody
@RequestMapping("file")
public class SysFileController extends BaseController {

    /**
     * 上传文件
     *
     * @param file the file
     * @return the result data
     */
    @PostMapping("/upload")
    public ResultData uploadFile(@RequestParam("file") MultipartFile file) {
        return ResultData.success(FileUtils.saveFileToLocal(file));
    }

    /**
     * 查询文件列表
     *
     * @return the result data
     */
    @GetMapping("/list")
    public ResultData list() {
        return ResultData.success(FileUtils.getAllFiles(FileUtils.LOCAL_UPLOAD_PATH));
    }
}
