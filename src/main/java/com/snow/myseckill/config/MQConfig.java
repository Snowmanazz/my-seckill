package com.snow.myseckill.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MQConfig {
    public static final String DIRECT_QUEUE = "seckill";


    /**
     * direct模式，绑定交换机
     * @return
     */
    public Queue queue() {
        return new Queue(DIRECT_QUEUE, true);
    }
}
