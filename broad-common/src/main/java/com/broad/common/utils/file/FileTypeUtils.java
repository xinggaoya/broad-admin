package com.broad.common.utils.file;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.apache.tika.Tika;

import java.util.Objects;

/**
 * 文件类型工具类
 *
 * @author XingGao
 */
public class FileTypeUtils {
    /**
     * 获取文件类型
     * <p>
     * 例如: hello.txt, 返回: txt
     *
     * @param file 文件名
     * @return 后缀（不含".")
     */
    public static String getExtension(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isEmpty(extension)) {
            extension = MimeTypeUtils.getExtension(Objects.requireNonNull(file.getContentType()));
        }
        return extension;
    }

    /**
     * 获取文件类型
     *
     * @param fileName 文件名
     * @return 后缀（不含".")
     */
    public static String getExtension(String fileName) {
        return FilenameUtils.getExtension(fileName);
    }

    /**
     * 获取文件类型
     * 
     * @param photoByte 文件字节码
     * @return 后缀（不含".")
     */
    public static String getExtension(byte[] photoByte) {
        String fileType = getFileType(photoByte);
        return StringUtils.substring(fileType, fileType.indexOf("/") + 1);
    }

    /**
     * 获取文件内容类型
     * 
     * @param photoByte 文件字节码
     * @return 类型
     */
    public static String getFileType(byte[] photoByte) {
        String defaultType = "application/octet-stream";
        if (photoByte == null) {
            return defaultType;
        }
        try {
            Tika tika = new Tika();
            String type = tika.detect(photoByte);
            return type != null ? type : defaultType;
        } catch (Exception e) {
            return defaultType;
        }
    }
}