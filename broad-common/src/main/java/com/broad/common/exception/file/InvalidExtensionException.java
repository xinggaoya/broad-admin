package com.broad.common.exception.file;


import org.apache.tomcat.util.http.fileupload.FileUploadException;

import java.util.Arrays;

/**
 * 文件上传 误异常类
 *
 * @author XingGao
 */
public class InvalidExtensionException extends FileUploadException {
    private static final long serialVersionUID = 1L;

    private String[] allowedExtension;
    private String extension;
    private String filename;

    /**
     * Instantiates a new Invalid extension exception.
     *
     * @param allowedExtension the allowed extension
     * @param extension        the extension
     * @param filename         the filename
     */
    public InvalidExtensionException(String[] allowedExtension, String extension, String filename) {
        super("filename : [" + filename + "], extension : [" + extension + "], allowed extension : [" + Arrays.toString(allowedExtension) + "]");
        this.allowedExtension = allowedExtension;
        this.extension = extension;
        this.filename = filename;
    }

    /**
     * Get allowed extension string [ ].
     *
     * @return the string [ ]
     */
    public String[] getAllowedExtension() {
        return allowedExtension;
    }

    /**
     * Gets extension.
     *
     * @return the extension
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Gets filename.
     *
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * The type Invalid image extension exception.
     */
    public static class InvalidImageExtensionException extends InvalidExtensionException {
        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new Invalid image extension exception.
         *
         * @param allowedExtension the allowed extension
         * @param extension        the extension
         * @param filename         the filename
         */
        public InvalidImageExtensionException(String[] allowedExtension, String extension, String filename) {
            super(allowedExtension, extension, filename);
        }
    }

    /**
     * The type Invalid flash extension exception.
     */
    public static class InvalidFlashExtensionException extends InvalidExtensionException {
        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new Invalid flash extension exception.
         *
         * @param allowedExtension the allowed extension
         * @param extension        the extension
         * @param filename         the filename
         */
        public InvalidFlashExtensionException(String[] allowedExtension, String extension, String filename) {
            super(allowedExtension, extension, filename);
        }
    }

    /**
     * The type Invalid media extension exception.
     */
    public static class InvalidMediaExtensionException extends InvalidExtensionException {
        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new Invalid media extension exception.
         *
         * @param allowedExtension the allowed extension
         * @param extension        the extension
         * @param filename         the filename
         */
        public InvalidMediaExtensionException(String[] allowedExtension, String extension, String filename) {
            super(allowedExtension, extension, filename);
        }
    }

    /**
     * The type Invalid video extension exception.
     */
    public static class InvalidVideoExtensionException extends InvalidExtensionException {
        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new Invalid video extension exception.
         *
         * @param allowedExtension the allowed extension
         * @param extension        the extension
         * @param filename         the filename
         */
        public InvalidVideoExtensionException(String[] allowedExtension, String extension, String filename) {
            super(allowedExtension, extension, filename);
        }
    }
}
