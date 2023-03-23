package com.snow.myseckill.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.snow.myseckill.pojo.User;
import com.snow.myseckill.result.CodeMsg;
import com.snow.myseckill.result.Result;
import com.snow.myseckill.service.RedisService;
import com.snow.myseckill.service.SeckillService;
import com.snow.myseckill.service.UserService;
import com.snow.myseckill.util.CookieUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Api(tags = "秒杀")
@Slf4j
@RestController
@RequestMapping("/seckill")
public class SecKillController {

    @Autowired
    private SeckillService seckillService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserService userService;

    //基于令牌桶算法的限流实现类
    RateLimiter rateLimiter = RateLimiter.create(10);

    @ApiOperation(value = "秒杀一 无任何限制")
    @PostMapping("/start")
    public Result<CodeMsg> start(HttpServletRequest request, @RequestParam("goodsId") Long goodsId) {
        if (!rateLimiter.tryAcquire(1000, TimeUnit.MILLISECONDS)) {
            return Result.error(CodeMsg.ACCESS_LIMIT_REACHED);
        }
        String token = CookieUtil.getCookieToken(request, redisService);
        User user = userService.getUserByToken(token);
        //user是否为空
        if (user == null) {
            log.error(CodeMsg.SESSION_ERROR.getMsg());
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //开始秒杀
        log.info("验证登录通过，开始秒杀");
        return seckillService.seckill1(user, goodsId);
    }

}