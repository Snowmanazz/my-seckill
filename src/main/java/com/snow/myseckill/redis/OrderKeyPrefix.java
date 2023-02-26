package com.snow.myseckill.redis;

public class OrderKeyPrefix extends BasePrefix{
    public OrderKeyPrefix(String prefix) {
        super(prefix);
    }
    public static final OrderKeyPrefix ORDER_KEY = new OrderKeyPrefix("seckill");
}
