package com.snow.myseckill.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
public class MQConfig implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    public static final String DIRECT_QUEUE = "seckill";
    public static final String TEST_QUEUE = "test";

    public static final String DIRECT_EXCHANGE = "directExchange";

    public static final String SEC_KILL_ROUTING_KEY = "seckill";

    public static final String TEST_ROUTING_KEY = "test";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    private void initRabbitTemplate() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 手动ack，消息是否到达交换机
     *
     * @param data
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(CorrelationData data, boolean ack, String cause) {
        if (ack) {
            log.info("{}消息成功到达交换机", data.getId());
        } else {
            log.error("{}未到达交换机，原因[{}]", data.getId(), cause);
        }
    }

    /**
     * 未到达队列会触发该方法
     *
     * @param returnedMessage
     */
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.error("{}消息未到达对列", returnedMessage.toString());
    }

    /**
     * direct模式，绑定交换机
     * durable : 是否持久化
     * exclusive ： 是否独享
     * autoDelete : 是否自动删除
     *
     * @return
     */
    @Bean
    public Queue queue() {
        return new Queue(DIRECT_QUEUE, true, false, false);
    }

    @Bean
    public Queue testQueue() {
        return new Queue(TEST_QUEUE, true, false, false);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE);
    }

    @Bean
    public Binding bindingSecKillDirect(){
        return BindingBuilder.bind(queue()).to(directExchange()).with(SEC_KILL_ROUTING_KEY);
    }

    @Bean
    public Binding bindingTestDirect(){
        return BindingBuilder.bind(testQueue()).to(directExchange()).with(TEST_ROUTING_KEY);
    }
}