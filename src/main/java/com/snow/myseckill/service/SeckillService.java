package com.snow.myseckill.service;

import com.snow.myseckill.pojo.Goods;
import com.snow.myseckill.pojo.User;
import com.snow.myseckill.redis.GoodsKeyPrefix;
import com.snow.myseckill.redis.RedisService;
import com.snow.myseckill.result.CodeMsg;
import com.snow.myseckill.result.Result;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class SeckillService {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RedisService redisService;

    public Result<CodeMsg> seckill1(User user, long goodsId) {
        //从缓存预减商品
        long gStock = redisService.decr(GoodsKeyPrefix.GOOD_STACK, "" + goodsId);
        log.info("redis中缓存为 [{}]", gStock);
        //可能是缓存没有存储再从数据库中查询
        if (gStock < 0) {
            //将所有商品信息刷新到缓存
            Goods good = goodsService.findOne(goodsId);
            log.info("没有找到缓存商品，从库里查询商品信息 [{}]", good);
            Integer goodsStock = good.getGoodsStock();
            //数据库中查询也没有，说明秒杀已经结束
            if (goodsStock < 0) {
                log.info("{}-{}{}", good.getId(), good.getGoodsName(), CodeMsg.SECKILL_OVER.getMsg());
                return Result.error(CodeMsg.SECKILL_OVER);
            }
            //将商品缓存写入redis
            return Result.success(CodeMsg.SUCCESS);
        }
        return Result.error(CodeMsg.SECKILL_FAILED);
    }
}
