package com.broad.framework.config;

import com.broad.common.constant.ThreadPoolConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * The type Executor config.
 *
 * @Author XingGao
 * @Date 2022 /8/29 9:45
 * @Version 1.0
 */
@Configuration
@Slf4j
public class ThreadPoolConfig {
    /**
     * 核心线程数（默认线程数）
     */
    private final int corePoolSize = 10;
    /**
     * 最大线程数
     */
    private final int maxPoolSize = 200;
    /**
     * 队列容量
     */
    private final int queueCapacity = 10;
    /**
     * 线程名称前缀
     */
    private final String namePrefix = "broad-framework-async-";

    /**
     * 线程池维护线程所允许的空闲时间
     *
     * @return the executor
     */
    @Bean(name = ThreadPoolConstant.SERVICE_EXECUTOR)
    public Executor asyncServiceExecutor() {
        log.info("start asyncServiceExecutor...");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 配置核心线程数
        executor.setCorePoolSize(corePoolSize);
        // 配置最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        // 配置队列大小
        executor.setQueueCapacity(queueCapacity);
        // 配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix(namePrefix);
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 执行初始化
        executor.initialize();
        return executor;
    }

    /**
     * 异步日志处理线程池
     *
     * @return the executor
     */
    @Bean(name = ThreadPoolConstant.ASYNC_POOL)
    public Executor asyncLogExecutor() {
        log.info("start asyncLogExecutor...");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 配置核心线程数
        executor.setCorePoolSize(5);
        // 配置最大线程数
        executor.setMaxPoolSize(10);
        // 配置队列大小
        executor.setQueueCapacity(50);
        // 配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("broad-log-async-");
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 执行初始化
        executor.initialize();
        return executor;
    }
}
