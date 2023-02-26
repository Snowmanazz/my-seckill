package com.snow.myseckill.rabbitmq;

import com.snow.myseckill.config.MQConfig;
import com.snow.myseckill.pojo.SecKillMsg;
import com.snow.myseckill.pojo.SeckillGoods;
import com.snow.myseckill.pojo.SeckillOrder;
import com.snow.myseckill.redis.OrderKeyPrefix;
import com.snow.myseckill.result.CodeMsg;
import com.snow.myseckill.result.Result;
import com.snow.myseckill.service.GoodsService;
import com.snow.myseckill.service.RedisService;
import com.snow.myseckill.service.SeckillService;
import com.snow.myseckill.util.ConvertUtil;
import com.snow.myseckill.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQReciver {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private SeckillService seckillService;

    @RabbitListener(queues = MQConfig.DIRECT_QUEUE)
    public void secKill(String msg) {
        log.info("receive msg [{}]", msg);
        SecKillMsg killMsg = ConvertUtil.stringToBean(msg, SecKillMsg.class);
        //查询商品
        GoodsVo good = goodsService.findOne(killMsg.getGoodsId());
        //再次判断商品库存
        if (good.getStockCount() <= 0) {
            log.error("库存为[{}],秒杀失败", good.getStockCount());
            return;
        }
        //判断重复秒杀
        SeckillOrder seckillOrder = redisService.get(OrderKeyPrefix.ORDER_KEY, OrderKeyPrefix.orderKey(killMsg.getUser().getId(), killMsg.getGoodsId()), SeckillOrder.class);
        if (seckillOrder != null) {
            log.error(CodeMsg.REPEATE_SECKILL.getMsg());
            return;
        }
        //减少库存 存入订单
        seckillService.seckillOrder(killMsg.getUser(), good, 1);
        log.info("恭喜秒杀成功");
    }
}
