package com.snow.myseckill.rabbitmq;

import com.snow.myseckill.config.MQConfig;
import com.snow.myseckill.pojo.SecKillMsg;
import com.snow.myseckill.service.RedisService;
import com.snow.myseckill.util.ConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendSecKill(SecKillMsg secMsg) {
        String msg = ConvertUtil.beanToString(secMsg);
        log.info("send message [{}]", msg);
        amqpTemplate.convertAndSend(MQConfig.DIRECT_QUEUE, msg);
    }

    public void testSend(){
        amqpTemplate.convertAndSend(MQConfig.TEST_QUEUE, "this is test msg");
    }
}
