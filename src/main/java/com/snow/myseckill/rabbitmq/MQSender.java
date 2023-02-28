package com.snow.myseckill.rabbitmq;

import com.snow.myseckill.config.MQConfig;
import com.snow.myseckill.pojo.SecKillMsg;
import com.snow.myseckill.util.ConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class MQSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendSecKill(SecKillMsg secMsg) {
        String msg = ConvertUtil.beanToString(secMsg);
        log.info("send message [{}]", msg);
        amqpTemplate.convertAndSend(MQConfig.SEC_KILL_ROUTING_KEY, msg, new CorrelationData(UUID.randomUUID().toString()));
    }

    public void testSend() {
        String msg = "this is test ms";
        log.info("send message [{}]", msg);
        amqpTemplate.convertAndSend(MQConfig.TEST_ROUTING_KEY, msg, new CorrelationData(UUID.randomUUID().toString()).toString());
    }
}
