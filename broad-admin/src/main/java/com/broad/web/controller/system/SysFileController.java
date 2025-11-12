package com.broad.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.broad.common.utils.StringUtils;
import com.broad.common.utils.file.FileUtils;
import com.broad.common.utils.file.FileTypeUtils;
import com.broad.common.utils.file.MimeTypeUtils;
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
    @SaCheckLogin
    public ResultData uploadFile(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResultData.error("上传文件不能为空");
        }
        String ext = FileTypeUtils.getExtension(file);
        if (!java.util.Arrays.asList(MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION).contains(ext)) {
            return ResultData.error("不支持的文件类型");
        }
        String original = file.getOriginalFilename();
        if (StringUtils.isNotEmpty(original) && !FileUtils.isValidFilename(original)) {
            return ResultData.error("非法文件名");
        }
        return ResultData.success(FileUtils.saveFileToLocal(file));
    }

    /**
     * 查询文件列表
     *
     * @return the result data
     */
    @GetMapping("/list")
    @SaCheckLogin
    public ResultData list() {
        return ResultData.success(FileUtils.getAllFiles(FileUtils.LOCAL_UPLOAD_PATH));
    }
}
