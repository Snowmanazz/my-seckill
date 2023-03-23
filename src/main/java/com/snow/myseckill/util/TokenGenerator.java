package com.snow.myseckill.util;

public class TokenGenerator {
    private TokenGenerator() {
    }

    private static class LazyHolder {
        private static final IdGenerator ID_GENERATOR = new IdGenerator(0);
    }

    public static IdGenerator getInstance() {
        return LazyHolder.ID_GENERATOR;
    }
}
