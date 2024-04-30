package com.broad.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: XingGao
 * @Date: 2022 /07/12 14:45
 * @Description:
 */
@Component
@Data
@ConfigurationProperties(prefix = "broad")
public class BroadConfig {

    /**
     * 项目名称
     */
    private String name;

    /**
     * 项目版本
     */
    private String version;

    /**
     *  作者
     */
    private String author;

    /**
     * 系统文件目录
     */
    private String systemFileDir;

    /**
     * 私钥
     */
    private String privateKey;

    /**
     * 公钥
     */
    private String publicKey;
}
