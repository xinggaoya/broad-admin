package com.broad.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * The type Web socket config.
 *
 * @Author: XingGao
 * @Date: 2022 /11/22
 * @Description:
 */
@Configuration
public class WebSocketConfig {
    /**
     * Server endpoint exporter server endpoint exporter.
     *
     * @return the server endpoint exporter
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}