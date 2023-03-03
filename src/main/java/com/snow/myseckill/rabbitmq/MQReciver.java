package com.snow.myseckill.rabbitmq;

import com.snow.myseckill.config.MQConfig;
import com.snow.myseckill.pojo.SecKillMsg;
import com.snow.myseckill.redis.OrderKeyPrefix;
import com.snow.myseckill.result.CodeMsg;
import com.snow.myseckill.result.Result;
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
        log.info("收到秒杀信息---------[{}]", msg);
        SecKillMsg killMsg = ConvertUtil.stringToBean(msg, SecKillMsg.class);
        //减少库存 存入订单
        log.info("正在下订单中---------");
        boolean order = seckillService.seckillOrder(killMsg.getUser(), killMsg.getGoodsId(), 1);
        if (order){
            log.info("恭喜[{}]秒杀成功", killMsg.getUser().getNickname());
        }else {
            log.error("未知错误，请稍后再试");
        }
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
