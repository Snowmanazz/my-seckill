package com.snow.myseckill.service;

import com.snow.myseckill.redis.Keyprefix;
import com.snow.myseckill.util.ConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.Set;
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
        if (prefix.expireSeconds() <= 0) {
            operations.set(prefix.getprefix() + key, ConvertUtil.beanToString(value));
        } else {
            operations.set(prefix.getprefix() + key, ConvertUtil.beanToString(value), prefix.expireSeconds(), TimeUnit.SECONDS);
        }
        return true;
    }

    public void deleteBatch(Keyprefix prefix, String match) {
        Objects.requireNonNull(match, "匹配规则为空");
        Set<String> keys = redisTemplate.keys(prefix.getprefix() + match);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
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

    public boolean hasKey(Keyprefix keyprefix, String key) {
        Objects.requireNonNull(key, "查询的key为空");
        return Boolean.TRUE.equals(redisTemplate.hasKey(keyprefix.getprefix() + key));
    }

    public void expire(Keyprefix keyprefix, String key) {
        Long expire = redisTemplate.getExpire(keyprefix.getprefix() + key, TimeUnit.SECONDS);
        if (expire == null || expire <= 60 * 60) {
            redisTemplate.expire(keyprefix.getprefix() + key, keyprefix.expireSeconds(), TimeUnit.SECONDS);
        }
    }

}
