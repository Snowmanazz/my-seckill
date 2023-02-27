package com.snow.myseckill.redis;

public class GoodsKeyPrefix extends BasePrefix {
    private GoodsKeyPrefix(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static final GoodsKeyPrefix GOOD_STACK = new GoodsKeyPrefix(0, "good_stack");

}
