package com.snow.myseckill.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.snow.myseckill.pojo.Goods;
import com.snow.myseckill.redis.Keyprefix;
import com.snow.myseckill.util.ConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
    private RedisTemplate<String, Object> redisTemplate;

    private ValueOperations<String, Object> operations;

    @PostConstruct
    private void init() {
        operations = redisTemplate.opsForValue();
    }

    public <T> Boolean set(Keyprefix prefix, String key, T value) {
        Objects.requireNonNull(value, "缓存的value为空");
        Objects.requireNonNull(key, "缓存的key suffix为空");
        operations.set(prefix.getprefix() + key, ConvertUtil.beanToString(value), prefix.expireSeconds(), TimeUnit.SECONDS);
        return true;
    }

    public <T> T get(Keyprefix keyprefix, String key, Class<T> clazz) {
        Objects.requireNonNull(key, "获取缓存的key为空");
        Objects.requireNonNull(clazz, "获取缓存的值class为空");
        Object o = operations.get(keyprefix.getprefix() + key);
        return ConvertUtil.objectToBean(o, clazz);
    }

    public Long decr(Keyprefix keyprefix, String key) {
        Objects.requireNonNull(key, "获取缓存的key为空");
        return operations.decrement(keyprefix.getprefix() + key);
    }
    public boolean hasKey(Keyprefix keyprefix, String key){
        Objects.requireNonNull(key, "查询的key为空");
        return Boolean.TRUE.equals(redisTemplate.hasKey(keyprefix.getprefix() + key));
    }

}
