package com.snow.myseckill.rabbitmq;

import com.rabbitmq.client.Channel;
import com.snow.myseckill.config.MQConfig;
import com.snow.myseckill.pojo.SecKillMsg;
import com.snow.myseckill.service.SeckillService;
import com.snow.myseckill.util.ConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
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
                    value = @Queue(name = MQConfig.DIRECT_QUEUE),
                    key = {MQConfig.SEC_KILL_ROUTING_KEY},
                    exchange = @Exchange(name = MQConfig.DIRECT_EXCHANGE)
            )
    })
    @RabbitHandler
    public void secKill(String msg, Message message, Channel channel) {
        log.info("receive msg [{}]", msg);
        final long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            SecKillMsg killMsg = ConvertUtil.stringToBean(msg, SecKillMsg.class);
            //减少库存 存入订单
            seckillService.seckillOrder(killMsg.getUser(), killMsg.getGoodsId(), 1);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("秒杀失败, {}", e.getMessage());
        }
        log.info("恭喜秒杀成功");
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(name = MQConfig.TEST_QUEUE),
                    key = {MQConfig.TEST_ROUTING_KEY},
                    exchange = @Exchange(name = MQConfig.DIRECT_EXCHANGE)
            )
    })
    @RabbitHandler
    public void testReceiver(String msg) {
        try {
            log.info("get msg {}", msg);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
