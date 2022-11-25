package com.broad.common.exception.file;

/**
 * 文件名称超长限制异常类
 *
 * @author XingGao
 */
public class FileNameLengthLimitExceededException extends FileException {
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new File name length limit exceeded exception.
     *
     * @param defaultFileNameLength the default file name length
     */
    public FileNameLengthLimitExceededException(int defaultFileNameLength) {
        super("upload.filename.exceed.length", new Object[]{defaultFileNameLength});
    }
}
