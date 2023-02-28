package com.snow.myseckill.rabbitmq;

import com.rabbitmq.client.Channel;
import com.snow.myseckill.config.MQConfig;
import com.snow.myseckill.pojo.SecKillMsg;
import com.snow.myseckill.service.SeckillService;
import com.snow.myseckill.util.ConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class MQReciver {

    @Autowired
    private SeckillService seckillService;

    @RabbitListener(queues = MQConfig.DIRECT_QUEUE)
    @RabbitHandler
    public void secKill(String msg, Message message, Channel channel) {
        log.info("receive msg [{}]", msg);
        final long deliveryTag = message.getMessageProperties().getDeliveryTag();
        SecKillMsg killMsg = ConvertUtil.stringToBean(msg, SecKillMsg.class);
        //减少库存 存入订单
        try {
            seckillService.seckillOrder(killMsg.getUser(), killMsg.getGoodsId(), 1);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("秒杀失败");
        }
        log.info("恭喜秒杀成功");
    }

    @RabbitListener(queues = MQConfig.TEST_QUEUE)
    @RabbitHandler
    public void testReceiver(String msg, Message message, Channel channel) {
        try {
//            int a = 0/1;
            log.info("get msg {}", msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
