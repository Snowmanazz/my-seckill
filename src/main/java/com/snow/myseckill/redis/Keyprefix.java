package com.snow.myseckill.redis;

public interface Keyprefix {
    int expireSeconds();

    String getprefix();
}
