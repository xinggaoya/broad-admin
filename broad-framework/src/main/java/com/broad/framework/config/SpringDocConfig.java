package com.broad.framework.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringDoc API文档配置
 * 
 * @author XingGao
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Broad Admin API",
        version = "2.2.0",
        description = "Broad Admin Service API Documentation",
        contact = @Contact(name = "XingGao")
    ),
    security = @SecurityRequirement(name = "Bearer")
)
@SecurityScheme(
    name = "Bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer",
    in = SecuritySchemeIn.HEADER
)
public class SpringDocConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI();
    }
} 