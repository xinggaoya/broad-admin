package com.broad.web;

import com.broad.common.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * The type Template application.
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.broad.**")
@MapperScan(basePackages = "com.broad.*.mapper")
@EnableAsync
@Slf4j
public class BroadApplication {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        SpringApplication.run(BroadApplication.class, args);
        log.info("{} Startup completed, compilation time: {}, total time: {}ms",
                "Broad-Admin"
                , DateUtils.getTime()
                , System.currentTimeMillis() - start);
    }

}
