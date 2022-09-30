package com.broad.framework.rabbit.entity;

import com.broad.common.constant.SimpleMqConstant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: XingGao
 * @Date: 2022/09/30 10:52
 * @Description:
 */
@Component
public class SimpleProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 消息体为对象。配置MessageConverter为Jackson2JsonMessageConverter即可
     *
     * @param simple
     */
    public void sendOrderMessage(Simple simple) {
        rabbitTemplate.convertAndSend(SimpleMqConstant.HANDLER_OBJECT_QUEUE_NAME, simple);
    }
}
