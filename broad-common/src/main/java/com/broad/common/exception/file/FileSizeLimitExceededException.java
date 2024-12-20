package com.broad.common.exception.file;

/**
 * 文件名大小限制异常类
 *
 * @author XingGao
 */
public class FileSizeLimitExceededException extends FileException {
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new File size limit exceeded exception.
     *
     * @param defaultMaxSize the default max size
     */
    public FileSizeLimitExceededException(long defaultMaxSize) {
        super("upload.exceed.maxSize", new Object[]{defaultMaxSize});
    }
}
