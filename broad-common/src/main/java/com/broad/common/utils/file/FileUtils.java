package com.broad.common.utils.file;

import com.broad.common.utils.DateUtils;
import com.broad.common.utils.StringUtils;
import com.broad.common.utils.uuid.IdUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件处理工具类
 *
 * @author XingGao
 */
public class FileUtils extends org.apache.commons.io.FileUtils {
    /**
     * 字符常量：斜杠 {@code '/'}
     */
    public static final char SLASH = '/';

    /**
     * 字符常量：反斜杠 {@code '\\'}
     */
    public static final char BACKSLASH = '\\';

    public static String FILENAME_PATTERN = "[a-zA-Z0-9_\\-\\|\\.\\u4e00-\\u9fa5]+";

    /**
     * 本地文件上传路径
     */
    public static final String LOCAL_UPLOAD_PATH = System.getProperty("user.dir") + "/upload";

    /**
     * 输出指定文件的byte数组
     *
     * @param filePath 文件路径
     * @param os       输出流
     * @throws IOException IO异常
     */
    public static void writeBytes(String filePath, OutputStream os) throws IOException {
        FileInputStream fis = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new FileNotFoundException(filePath);
            }
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int length;
            while ((length = fis.read(b)) > 0) {
                os.write(b, 0, length);
            }
        } finally {
            IOUtils.close(os);
            IOUtils.close(fis);
        }
    }

    /**
     * 删除文件
     *
     * @param filePath 文件
     * @return 是否删除成功
     */
    public static boolean deleteFile(String filePath) {
        boolean flag = false;
        File file = new File(filePath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            flag = file.delete();
        }
        return flag;
    }

    /**
     * 文件名称验证
     *
     * @param filename 文件名称
     * @return true 正常 false 非法
     */
    public static boolean isValidFilename(String filename) {
        return filename.matches(FILENAME_PATTERN);
    }

    /**
     * 检查文件是否可下载
     *
     * @param resource 需要下载的文件
     * @return true 正常 false 非法
     */
    public static boolean checkAllowDownload(String resource) {
        // 禁止目录上跳级别
        if (StringUtils.contains(resource, "..")) {
            return false;
        }

        // 检查允许下载的文件规则
        return ArrayUtils.contains(MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION, FilenameUtils.getExtension(resource));
    }

    /**
     * 下载文件名重新编码
     *
     * @param request  请求对象
     * @param fileName 文件名
     * @return 编码后的文件名
     * @throws UnsupportedEncodingException 编码异常
     */
    public static String setFileDownloadHeader(HttpServletRequest request, String fileName)
            throws UnsupportedEncodingException {
        final String agent = request.getHeader("USER-AGENT");
        String filename = fileName;
        if (agent.contains("MSIE")) {
            // IE浏览器
            filename = URLEncoder.encode(filename, StandardCharsets.UTF_8);
            filename = filename.replace("+", " ");
        } else if (agent.contains("Firefox")) {
            // 火狐浏览器
            filename = new String(fileName.getBytes(StandardCharsets.UTF_8), "ISO8859-1");
        } else if (agent.contains("Chrome")) {
            // google浏览器
            filename = URLEncoder.encode(filename, StandardCharsets.UTF_8);
        } else {
            // 其它浏览器
            filename = URLEncoder.encode(filename, StandardCharsets.UTF_8);
        }
        return filename;
    }

    /**
     * 返回文件名
     *
     * @param filePath 文件
     * @return 文件名
     */
    public static String getName(String filePath) {
        if (null == filePath) {
            return null;
        }
        int len = filePath.length();
        if (0 == len) {
            return filePath;
        }
        if (isFileSeparator(filePath.charAt(len - 1))) {
            // 以分隔符结尾的去掉结尾分隔符
            len--;
        }

        int begin = 0;
        char c;
        for (int i = len - 1; i > -1; i--) {
            c = filePath.charAt(i);
            if (isFileSeparator(c)) {
                // 查找最后一个路径分隔符（/或者\）
                begin = i + 1;
                break;
            }
        }

        return filePath.substring(begin, len);
    }

    /**
     * 是否为Windows或者Linux（Unix）文件分隔符<br>
     * Windows平台下分隔符为\，Linux（Unix）为/
     *
     * @param c 字符
     * @return 是否为Windows或者Linux（Unix）文件分隔符
     */
    public static boolean isFileSeparator(char c) {
        return SLASH == c || BACKSLASH == c;
    }

    /**
     * 下载文件名重新编码
     *
     * @param response     响应对象
     * @param realFileName 真实文件名
     * @throws UnsupportedEncodingException 编码异常
     */
    public static void setAttachmentResponseHeader(HttpServletResponse response, String realFileName)
            throws UnsupportedEncodingException {
        String percentEncodedFileName = URLEncoder.encode(realFileName, StandardCharsets.UTF_8);

        StringBuilder contentDispositionValue = new StringBuilder();
        contentDispositionValue.append("attachment; filename=")
                .append(percentEncodedFileName)
                .append(";")
                .append("filename*=")
                .append("utf-8''")
                .append(percentEncodedFileName);

        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition,download-filename");
        response.setHeader("Content-disposition", contentDispositionValue.toString());
        response.setHeader("download-filename", percentEncodedFileName);
    }

    /**
     * 保存文件到本地
     *
     * @param file 上传的文件
     * @return 文件访问路径
     */
    public static String saveFileToLocal(MultipartFile file) {
        if (file == null) {
            throw new RuntimeException("上传文件不能为空");
        }
        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();
        // 获取文件后缀
        String extension = FileTypeUtils.getExtension(file);
        // 生成新的文件名
        String fileName = DateUtils.dateTime() + "_" + IdUtils.fastSimpleUUID() + "." + extension;
        // 创建年月日目录
        String datePath = DateUtils.datePath();
        // 生成文件存储的目录
        File directory = new File(LOCAL_UPLOAD_PATH + "/" + datePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        // 生成文件存储的路径
        String filePath = directory.getPath() + "/" + fileName;
        try {
            // 保存文件
            file.transferTo(new File(filePath));
            return datePath + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("保存文件失败", e);
        }
    }

    /**
     * 获取指定目录下的所有文件
     *
     * @param path 目录路径
     * @return 文件列表
     */
    public static List<String> getAllFiles(String path) {
        List<String> files = new ArrayList<>();
        File file = new File(path);
        if (!file.exists()) {
            return files;
        }
        File[] tempList = file.listFiles();
        if (tempList == null) {
            return files;
        }
        for (File value : tempList) {
            if (value.isFile()) {
                files.add(value.getName());
            }
        }
        return files;
    }
}
