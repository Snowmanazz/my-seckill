package com.snow.myseckill.interceptor;

import com.snow.myseckill.pojo.User;
import com.snow.myseckill.redis.UserKeyPrefix;
import com.snow.myseckill.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        log.info("请求的地址--[{}]", uri);
        Cookie[] cookies = request.getCookies();
        if (uri.matches("/user/.*")) {
            return true;
        }
        for (Cookie cookie : cookies) {
            if (cookie != null && UserKeyPrefix.TOKEN.getprefix().equals(URLDecoder.decode(cookie.getName(), "utf-8"))) {
                String token = cookie.getValue();
                User user = redisService.get(UserKeyPrefix.TOKEN, token, User.class);
                if (user != null) {
                    log.info("用户已经登陆--{}", user);
                    return true;
                }
            }
        }
        //从请求头中获取不到token说明未登录，阻止该请求访问资源
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        log.error("请先登录..");
        return false;
    }

}
