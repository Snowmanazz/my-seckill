package com.snow.myseckill.filter;

import com.snow.myseckill.pojo.User;
import com.snow.myseckill.redis.UserKeyPrefix;
import com.snow.myseckill.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

@Slf4j
@Component
public class LoginFilter implements Filter {

    @Autowired
    private RedisService redisService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("初始化拦截器-------");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String uri = request.getRequestURI();
        log.info("请求的地址--[{}]", uri);
        Cookie[] cookies = request.getCookies();
        if (uri.matches("/user/.*") && !uri.equals("/user/checkLogin")) {
            return;
        }
        for (Cookie cookie : cookies) {
            if (cookie != null && UserKeyPrefix.TOKEN.getprefix().equals(URLDecoder.decode(cookie.getName(), "utf-8"))) {
                String token = cookie.getValue();
                User user = redisService.get(UserKeyPrefix.TOKEN, token, User.class);
                if (user != null) {
                    log.info("用户已经登陆--{}", user);
                    return;
                }
            }
        }
        //从请求头中获取不到token说明未登录，阻止该请求访问资源
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        log.error("请先登录..");
    }

    @Override
    public void destroy() {
        log.info("销毁拦截器---------");
    }
}
