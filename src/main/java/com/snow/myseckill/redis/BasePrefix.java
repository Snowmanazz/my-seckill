package com.snow.myseckill.redis;

import java.util.Objects;

public class BasePrefix implements Keyprefix {
    private int expireSeconds;
    private String prefix;

    public BasePrefix(String prefix) {
        this(-1, prefix);
    }

    public BasePrefix(int expireSeconds, String prefix) {
        Objects.requireNonNull(prefix, "key prefix 不能为空");
        this.expireSeconds = expireSeconds;
        this.prefix = prefix + "-";
    }

    @Override
    public int expireSeconds() {
        return expireSeconds;
    }

    @Override
    public String getprefix() {
        String className = getClass().getSimpleName();
        return className + ":" + prefix;
    }
}
