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
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.HashMap;
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
    /**
     * 内存标记，判断该商品是否卖光,卖光为true
     */
    private static Map<Long, Boolean> localMap;

    @PostConstruct
    private void init() {
        //将所有商品信息刷新到redis缓存以及本地
        log.info("将所有商品库存刷新到redis缓存、以及本地缓存");
        freshRedisStock();
    }

    public Result<CodeMsg> seckill1(User user, long goodsId) {
        //查看商品是否是秒杀商品
        if (!localMap.containsKey(goodsId)) {
            log.error(CodeMsg.SECKILL_GOODS_NOT_EXSITS.getMsg());
            return Result.error(CodeMsg.SECKILL_GOODS_NOT_EXSITS);
        }
        //查看缓存商品是否秒杀完毕,默认没有秒杀完毕
        if (localMap.get(goodsId)) {
            log.error(CodeMsg.SECKILL_FAILED.getMsg());
            return Result.error(CodeMsg.SECKILL_OVER);
        }
        //判断重复秒杀
        boolean isSecKill = redisService.hasKey(OrderKeyPrefix.ORDER_KEY, OrderKeyPrefix.orderKey(user.getId(), goodsId));
        if (isSecKill) {
            log.error(CodeMsg.REPEATE_SECKILL.getMsg());
            return Result.error(CodeMsg.REPEATE_SECKILL);
        }
        //从缓存预减商品
        long gStock = redisService.decr(GoodsKeyPrefix.GOOD_STACK, "" + goodsId);
        log.info("redis中缓存为 [{}]", gStock);
        if (gStock < 0) {
            //说明秒杀已经结束
            //刷新本地内存
            localMap.put(goodsId, true);
            log.info("{}号{}", goodsId, CodeMsg.SECKILL_OVER.getMsg());
            return Result.error(CodeMsg.SECKILL_OVER);
        }
        SecKillMsg msg = new SecKillMsg(user, goodsId);
        mqSender.sendSecKill(msg);
        //排队
        return Result.success(CodeMsg.SUCCESS);
    }

    @Transactional(rollbackFor = Exception.class)
    public void seckillOrder(User user, long goodsId, int gcount) {
        //查询商品
        GoodsVo goods = goodsService.findOne(goodsId);
        //再次判断商品库存
        if (goods.getStockCount() <= 0) {
            log.error("库存为[{}],秒杀失败", goods.getStockCount());
            return;
        }
        //减库存
        boolean success = goodsService.reduceGood(goodsId, gcount);
        if (success) {
            //下订单
            OrderInfo order = orderService.createOrder(user, goods);
            //存redis
            if (order != null) {
                redisService.set(OrderKeyPrefix.ORDER_KEY, OrderKeyPrefix.orderKey(user.getId(), goodsId), order);
            }
        }
    }

    private void freshRedisStock() {
        List<GoodsVo> goodsList = goodsService.goodsList();
        if (goodsList == null || goodsList.isEmpty()) {
            return;
        }
        localMap = new HashMap<>(goodsList.size());
        for (GoodsVo good : goodsList) {
            //将所有商品写入缓存
            redisService.set(GoodsKeyPrefix.GOOD_STACK, "" + good.getId(), good.getStockCount());
            //写入本地内存
            localMap.put(good.getId(), good.getStockCount() <= 0);
        }
    }
}
