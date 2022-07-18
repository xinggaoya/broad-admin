package com.broad.framework.entity;

import com.broad.framework.config.RabbitConfig;
import com.broad.system.entity.SysAdmin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: XingGao
 * @Date: 2022/07/18 14:02
 * @Description:
 */
@Component
@RabbitListener(queues = RabbitConfig.QUEUE_A)
@Slf4j
public class MsgReceiver {

    @RabbitHandler
    public void process(String message) {
        log.warn("1接收到处理队列{}当中的消息： " + message, RabbitConfig.QUEUE_A);
    }

}

