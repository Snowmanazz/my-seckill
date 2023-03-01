package com.snow.myseckill.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
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

    public static final String ERROR_EXCHANGE = "errorExchange";

    public static final String SEC_KILL_ROUTING_KEY = "seckill";

    public static final String TEST_ROUTING_KEY = "test";

    public static final String ERROR_QUEUE = "error";

    public static final String ERROR_ROUTING_KEY = "error";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    private void initRabbitTemplate() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 消息是否到达交换机
     *
     * @param data
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(CorrelationData data, boolean ack, String cause) {
        if (!ack){
            log.info("确认消息送到交换机(Exchange)结果：");
            log.info("相关数据：{}", data);
            log.info("错误原因：{}", cause);
        }
    }

    /**
     * 未到达队列会触发该方法
     *
     * @param returnedMessage
     */
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.info("\n确认消息送到队列(Queue)结果：");
        log.info("发生消息：{}", returnedMessage.getMessage());
        log.info("回应码：{}", returnedMessage.getReplyCode());
        log.info("回应信息：{}", returnedMessage.getReplyText());
        log.info("交换机：{}", returnedMessage.getExchange());
        log.info("路由键：{}", returnedMessage.getRoutingKey());
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
    public Queue errorQueue() {
        return new Queue(ERROR_QUEUE, true, false, false);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE);
    }

    @Bean
    public DirectExchange errorExchange() {
        return new DirectExchange(ERROR_EXCHANGE, true, false);
    }


    @Bean
    public Binding bindingSecKillDirect() {
        return BindingBuilder.bind(queue()).to(directExchange()).with(SEC_KILL_ROUTING_KEY);
    }

    @Bean
    public Binding bindingTestDirect() {
        return BindingBuilder.bind(testQueue()).to(directExchange()).with(TEST_ROUTING_KEY);
    }

    @Bean
    public Binding bindingErrorDirect() {
        return BindingBuilder.bind(errorQueue()).to(errorExchange()).with(ERROR_ROUTING_KEY);
    }

    /**
     * 设置MessageRecoverer
     */
    @Bean
    public MessageRecoverer messageRecoverer() {
        //AmqpTemplate和RabbitTemplate都可以
        return new RepublishMessageRecoverer(rabbitTemplate, ERROR_EXCHANGE, ERROR_ROUTING_KEY);
    }

}