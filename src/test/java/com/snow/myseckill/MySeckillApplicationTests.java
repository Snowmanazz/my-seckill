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

@SpringBootTest
class MySeckillApplicationTests {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserMappper userMappper;
    @Autowired
    private SecKillGoodsMapper goodsMapper;
    @Autowired
    private OrderMapper orderMapper;

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
        System.out.println(userMappper.selectById(18217272828L));
        System.out.println(goodsMapper.getGoodsVoByGoodsId(1));
    }

    @Test
    void testRabbimq(){
        mqSender.testSend();
    }

}
