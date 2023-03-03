package com.snow.myseckill.util;

import com.snow.myseckill.pojo.User;
import com.snow.myseckill.redis.UserKeyPrefix;
import com.snow.myseckill.service.RedisService;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class CookieUtil {

    public static final String TOKEN = "token";

    /**
     * 添加到cookie缓存以及response
     *
     * @param response
     * @param token
     * @param user
     */
    public static void addCookie(HttpServletResponse response, String token, User user, RedisService redisService) {
        //添加到redis慌存，然后存入cookie
        redisService.set(UserKeyPrefix.TOKEN, token, user);
        Cookie cookie = new Cookie(TOKEN, token);
        cookie.setMaxAge(UserKeyPrefix.TOKEN.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 获取token
     *
     * @param request
     * @param redisService
     * @return
     */
    public static String getCookieToken(HttpServletRequest request, RedisService redisService) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            try {
                if (cookie != null && TOKEN.equals(URLDecoder.decode(cookie.getName(), "utf-8"))) {
                    String token = cookie.getValue();
                    if (StringUtils.isNoneBlank(token)) {
                        //刷新token
                        redisService.expire(UserKeyPrefix.TOKEN, token);
                    }
                    return token;
                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
