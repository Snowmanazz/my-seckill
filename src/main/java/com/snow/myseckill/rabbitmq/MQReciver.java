package com.snow.myseckill.rabbitmq;

import com.snow.myseckill.config.MQConfig;
import com.snow.myseckill.pojo.SecKillMsg;
import com.snow.myseckill.service.SeckillService;
import com.snow.myseckill.util.ConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
@Slf4j
public class MQReciver {

    @Autowired
    private SeckillService seckillService;

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(name = MQConfig.DIRECT_QUEUE),               //queue
                    key = {MQConfig.SEC_KILL_ROUTING_KEY},                      //routing key
                    exchange = @Exchange(name = MQConfig.DIRECT_EXCHANGE)       //exchange
            )
    }, concurrency = "2")                                                       //并发数
    public void secKill(String msg) {
        log.info("receive msg [{}]", msg);
        SecKillMsg killMsg = ConvertUtil.stringToBean(msg, SecKillMsg.class);
        //减少库存 存入订单
        seckillService.seckillOrder(killMsg.getUser(), killMsg.getGoodsId(), 1);
        log.info("恭喜秒杀成功");
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(name = MQConfig.TEST_QUEUE),
                    key = {MQConfig.TEST_ROUTING_KEY},
                    exchange = @Exchange(name = MQConfig.DIRECT_EXCHANGE)
            )
    }, concurrency = "2")
    public void testReceiver(String msg) {
        log.info("get msg {}", msg);
        log.info("resolve msg {}", msg);
    }
}
