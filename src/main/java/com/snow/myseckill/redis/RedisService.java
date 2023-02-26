package com.snow.myseckill.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private ValueOperations<String, String> operations;

    @PostConstruct
    private void init() {
        operations = redisTemplate.opsForValue();
    }

    public Boolean set(Keyprefix prefix, String key, String value) {
        Objects.requireNonNull(value, "缓存的value为空");
        Objects.requireNonNull(key, "缓存的key suffix为空");
        operations.set(prefix.getprefix() + key, value, prefix.expireSeconds(), TimeUnit.SECONDS);
        return true;
    }

    public String get(Keyprefix keyprefix, String key) {
        Objects.requireNonNull(key, "获取缓存的key为空");
        return operations.get(keyprefix.getprefix() + key);
    }

    public Long decr(Keyprefix keyprefix, String key){
        Objects.requireNonNull(key, "获取缓存的key为空");
        return operations.decrement(keyprefix.getprefix() + key);
    }

}
