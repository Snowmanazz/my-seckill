package com.snow.myseckill.service;

import com.snow.myseckill.pojo.*;
import com.snow.myseckill.rabbitmq.MQSender;
import com.snow.myseckill.redis.GoodsKeyPrefix;
import com.snow.myseckill.redis.OrderKeyPrefix;
import com.snow.myseckill.result.CodeMsg;
import com.snow.myseckill.result.Result;
import com.snow.myseckill.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class SeckillService {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MQSender mqSender;

    public Result<CodeMsg> seckill1(User user, long goodsId, Map<Long, Boolean> localMap) {
        //从缓存预减商品
        long gStock = redisService.decr(GoodsKeyPrefix.GOOD_STACK, "" + goodsId);
        log.info("redis中缓存为 [{}]", gStock);
        //可能是缓存没有存储再从数据库中查询
        if (gStock < 0) {
            //将所有商品信息刷新到redis缓存
            freshRedisStock(localMap);
            //再从redis预减
            gStock = redisService.decr(GoodsKeyPrefix.GOOD_STACK, "" + goodsId);
            //如果任然没有，说明秒杀已经结束
            if (gStock < 0) {
                //刷新本地内存
                localMap.put(goodsId, true);
                log.info("{}号{}", goodsId, CodeMsg.SECKILL_OVER.getMsg());
                return Result.error(CodeMsg.SECKILL_OVER);
            }
            //判断重复秒杀
            SeckillOrder seckillOrder = redisService.get(OrderKeyPrefix.ORDER_KEY, OrderKeyPrefix.orderKey(user.getId(), goodsId), SeckillOrder.class);
            if (seckillOrder != null) {
                log.error(CodeMsg.REPEATE_SECKILL.getMsg());
                return Result.error(CodeMsg.REPEATE_SECKILL);
            }
            SecKillMsg msg = new SecKillMsg(user, goodsId);
            mqSender.sendSecKill(msg);
            //排队
            return Result.success(CodeMsg.SUCCESS);
        }
        return Result.error(CodeMsg.SECKILL_FAILED);
    }

    public void seckillOrder(User user, GoodsVo goods, int gcount) {
        //减库存
        boolean success = goodsService.reduceGood(goods.getId(), gcount);
        if (success) {
            //下订单
            OrderInfo order = orderService.createOrder(user, goods);
            //存redis
            if (order != null) {
                redisService.set(OrderKeyPrefix.ORDER_KEY, OrderKeyPrefix.orderKey(user.getId(), goods.getId()), order);
            }
        }
    }

    private void freshRedisStock(Map<Long, Boolean> localMap) {
        List<GoodsVo> goodsList = goodsService.goodsList();
        if (goodsList == null || goodsList.isEmpty()) {
            return;
        }
        for (GoodsVo good : goodsList) {
            //将所有商品写入缓存
            redisService.set(GoodsKeyPrefix.GOOD_STACK, "" + good.getId(), good.getStockCount());
            //写入本地内存
            localMap.put(good.getId(), good.getStockCount() <= 0);
        }
    }
}
