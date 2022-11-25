package com.broad.common.exception.file;


import com.broad.common.exception.base.BaseException;

/**
 * 文件信息异常类
 *
 * @author XingGao
 */
public class FileException extends BaseException {
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new File exception.
     *
     * @param code the code
     * @param args the args
     */
    public FileException(String code, Object[] args) {
        super("file", code, args, null);
    }

}
