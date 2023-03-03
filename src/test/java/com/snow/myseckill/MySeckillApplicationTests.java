package com.snow.myseckill;

import com.snow.myseckill.mapper.SecKillGoodsMapper;
import com.snow.myseckill.mapper.OrderMapper;
import com.snow.myseckill.mapper.UserMappper;
import com.snow.myseckill.rabbitmq.MQSender;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class MySeckillApplicationTests {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SecKillGoodsMapper goodsMapper;


    @Autowired
    MQSender mqSender;
    @Test
    void testRedis() {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set("a","test");
        System.out.println(ops.get("a"));
        System.out.println(stringRedisTemplate.hasKey("a"));
        stringRedisTemplate.delete("a");
    }
    @Test
    void testMysql(){
        Integer integer = goodsMapper.reduceGoods(1, 1);
        System.out.println(integer);
    }

    @Test
    void testRabbimq() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            TimeUnit.SECONDS.sleep(1);
            mqSender.testSend();
        }
    }

}
