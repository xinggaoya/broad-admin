package com.broad.web;

import com.alibaba.druid.support.json.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Date;

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
        log.info("{} 启动完成,编译时间：{},总耗时：{}ms",
                "Broad-Admin"
                , JSONUtils.toJSONString(new Date())
                , System.currentTimeMillis() - start);
    }

}