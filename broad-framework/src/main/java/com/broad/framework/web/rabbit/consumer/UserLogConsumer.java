package com.broad.framework.web.rabbit.consumer;

import com.broad.common.constant.SimpleMqConstant;
import com.broad.system.entity.SysUserLog;
import com.broad.system.service.SysUserLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The type User log consumer.
 *
 * @Author: XingGao
 * @Date: 2022 /09/30 10:51
 * @Description: 消费者
 */
@Component
@Slf4j
public class UserLogConsumer {

    @Autowired
    private SysUserLogService userLogService;

    /**
     * Consumer log 1.
     *
     * @param userLog the user log
     */
    @RabbitListener(queuesToDeclare = @Queue(SimpleMqConstant.HANDLER_OBJECT_QUEUE_NAME))
    @RabbitHandler
    public void consumerLog1(SysUserLog userLog) {
        userLogService.save(userLog);
    }

    /**
     * Consumer log 2.
     *
     * @param userLog the user log
     */
    @RabbitListener(queuesToDeclare = @Queue(SimpleMqConstant.HANDLER_OBJECT_QUEUE_NAME))
    @RabbitHandler
    public void consumerLog2(SysUserLog userLog) {
        userLogService.save(userLog);
    }
}