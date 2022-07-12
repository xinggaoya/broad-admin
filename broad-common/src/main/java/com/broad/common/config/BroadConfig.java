package com.broad.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: JinJun
 * @Date: 2022/07/12 14:45
 * @Description:
 */
@Component
@Data
@ConfigurationProperties(prefix = "broad")
public class BroadConfig {

    private String captchaType;
}
