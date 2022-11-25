package com.broad.framework.rabbit.producer;

import com.broad.common.constant.SimpleMqConstant;
import com.broad.system.entity.SysUserLog;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The type User log producer.
 *
 * @Author: XingGao
 * @Date: 2022 /09/30 10:52
 * @Description: 生产者
 */
@Component
public class UserLogProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 消息体为对象。配置MessageConverter为Jackson2JsonMessageConverter即可
     *
     * @param userLog the user log
     */
    public void sendLogMessage(SysUserLog userLog) {
        rabbitTemplate.convertAndSend(SimpleMqConstant.HANDLER_OBJECT_QUEUE_NAME, userLog);
    }
}
