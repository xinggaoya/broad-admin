package com.broad.framework.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * The type Visible thread pool task executor.
 *
 * @Author XingGao
 * @Date 2022 /8/29 10:02
 * @Version 1.0
 * @Description ThreadPoolTaskExecutor 子类
 */
@Slf4j
public class VisibleThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    private void showThreadPoolInfo(String prefix) {

        ThreadPoolExecutor threadPoolExecutor = getThreadPoolExecutor();

        if (ObjectUtils.isEmpty(threadPoolExecutor)) {
            return;
        }

        log.info("{}, {},taskCount [{}], completedTaskCount [{}], activeCount [{}], queueSize [{}]",
                getThreadNamePrefix() + System.currentTimeMillis(),
                prefix,
                threadPoolExecutor.getTaskCount(),
                threadPoolExecutor.getCompletedTaskCount(),
                threadPoolExecutor.getActiveCount(),
                threadPoolExecutor.getQueue().size()
        );
    }

    @Override
    public void execute(Runnable task) {
        showThreadPoolInfo("do execute Runnable");
        super.execute(task);
    }

    @Override
    public void execute(Runnable task, long startTimeout) {
        showThreadPoolInfo("do execute Runnable and StartTimeout");
        super.execute(task, startTimeout);
    }

    @Override
    public Future<?> submit(Runnable task) {
        showThreadPoolInfo("do submit Runnable");
        return super.submit(task);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        showThreadPoolInfo("do submit Callable");
        return super.submit(task);
    }

    @Override
    public ListenableFuture<?> submitListenable(Runnable task) {
        showThreadPoolInfo("do submitListenable Runnable");
        return super.submitListenable(task);
    }

    @Override
    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
        showThreadPoolInfo("do submitListenable Callable");
        return super.submitListenable(task);
    }
}
