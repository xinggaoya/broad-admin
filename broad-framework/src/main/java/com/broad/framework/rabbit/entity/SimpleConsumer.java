package com.broad.framework.rabbit.entity;

import com.broad.common.constant.SimpleMqConstant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Author: XingGao
 * @Date: 2022/09/30 10:51
 * @Description: 消费者
 */
@Component
@Slf4j
public class SimpleConsumer {

    @RabbitListener(queuesToDeclare = @Queue(SimpleMqConstant.HANDLER_OBJECT_QUEUE_NAME))
    @RabbitHandler
    public void receiveObject(Simple simple) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String message = objectMapper.writeValueAsString(simple);
        log.info("消费队列:{}", message);
    }
}