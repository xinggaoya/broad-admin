package com.broad.common.utils.file;

/**
 * 媒体类型工具类
 *
 * @author XingGao
 */
public class MimeTypeUtils {
    /**
     * The constant IMAGE_PNG.
     */
    public static final String IMAGE_PNG = "image/png";

    /**
     * The constant IMAGE_JPG.
     */
    public static final String IMAGE_JPG = "image/jpg";

    /**
     * The constant IMAGE_JPEG.
     */
    public static final String IMAGE_JPEG = "image/jpeg";

    /**
     * The constant IMAGE_BMP.
     */
    public static final String IMAGE_BMP = "image/bmp";

    /**
     * The constant IMAGE_GIF.
     */
    public static final String IMAGE_GIF = "image/gif";

    /**
     * The constant IMAGE_EXTENSION.
     */
    public static final String[] IMAGE_EXTENSION = {"bmp", "gif", "jpg", "jpeg", "png"};

    /**
     * The constant FLASH_EXTENSION.
     */
    public static final String[] FLASH_EXTENSION = {"swf", "flv"};

    /**
     * The constant MEDIA_EXTENSION.
     */
    public static final String[] MEDIA_EXTENSION = {"swf", "flv", "mp3", "wav", "wma", "wmv", "mid", "avi", "mpg",
            "asf", "rm", "rmvb"};

    /**
     * The constant VIDEO_EXTENSION.
     */
    public static final String[] VIDEO_EXTENSION = {"mp4", "avi", "rmvb"};

    /**
     * The constant DEFAULT_ALLOWED_EXTENSION.
     */
    public static final String[] DEFAULT_ALLOWED_EXTENSION = {
            // 图片
            "bmp", "gif", "jpg", "jpeg", "png",
            // word excel powerpoint
            "doc", "docx", "xls", "xlsx", "ppt", "pptx", "html", "htm", "txt",
            // 压缩文件
            "rar", "zip", "gz", "bz2",
            // 视频格式
            "mp4", "avi", "rmvb",
            // pdf
            "pdf"};

    /**
     * Gets extension.
     *
     * @param prefix the prefix
     * @return the extension
     */
    public static String getExtension(String prefix) {
        switch (prefix) {
            case IMAGE_PNG:
                return "png";
            case IMAGE_JPG:
                return "jpg";
            case IMAGE_JPEG:
                return "jpeg";
            case IMAGE_BMP:
                return "bmp";
            case IMAGE_GIF:
                return "gif";
            default:
                return "";
        }
    }
}
