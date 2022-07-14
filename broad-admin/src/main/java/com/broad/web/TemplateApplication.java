package com.broad.web;

import com.alibaba.druid.support.json.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.Date;

@SpringBootApplication
@ComponentScan(basePackages = "com.broad.**")
@MapperScan(basePackages = "com.broad.**.mapper")
@Slf4j
public class TemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemplateApplication.class, args);
        log.info("{} 启动完成,编译时间：{}","Broad-Admin", JSONUtils.toJSONString(new Date()));
    }

}
