package com.snow.myseckill.redis;

public class UserKeyPrefix extends BasePrefix{

    private static final int TOKEN_EXPIRE = 3600 * 24 *2;

    private UserKeyPrefix(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static final UserKeyPrefix TOKEN = new UserKeyPrefix(TOKEN_EXPIRE, "token");

    public static final UserKeyPrefix USER_ID = new UserKeyPrefix(0, "id");

}
