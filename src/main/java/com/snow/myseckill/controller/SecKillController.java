package com.snow.myseckill.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.snow.myseckill.pojo.Goods;
import com.snow.myseckill.pojo.User;
import com.snow.myseckill.result.CodeMsg;
import com.snow.myseckill.result.Result;
import com.snow.myseckill.service.GoodsService;
import com.snow.myseckill.service.SeckillService;
import com.sun.org.apache.bcel.internal.classfile.Code;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Api(tags = "秒杀")
@RestController
@RequestMapping("/seckill")
public class SecKillController {

    private static final Logger log = LoggerFactory.getLogger("secKillController");

    @Autowired
    private SeckillService seckillService;


    //内存标记，判断该商品是否卖光,卖光为true
    private Map<Long, Boolean> localMap = new HashMap<>();

    @ApiOperation(value = "秒杀一(最low实现)")
    @RequestMapping("/start")
    public Result<CodeMsg> start(User user, @RequestParam("goodsId") Long goodsId) {
        //user是否为空
        if (user == null) {
            log.error(CodeMsg.SESSION_ERROR.getMsg());
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //查看缓存商品是否秒杀完毕,默认没有秒杀完毕
        if (localMap.getOrDefault(goodsId, false)) {
            log.error(CodeMsg.SECKILL_FAILED.getMsg());
            return Result.error(CodeMsg.SECKILL_OVER);
        }
        //开始秒杀
        return seckillService.seckill1(user, goodsId, localMap);
    }

}