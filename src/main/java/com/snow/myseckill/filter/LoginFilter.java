//package com.snow.myseckill.filter;
//
//import com.snow.myseckill.pojo.User;
//import com.snow.myseckill.redis.UserKeyPrefix;
//import com.snow.myseckill.service.RedisService;
//import com.snow.myseckill.service.UserService;
//import com.snow.myseckill.util.CookieUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Slf4j
//@Component
//public class LoginFilter implements Filter {
//
//    @Autowired
//    private RedisService redisService;
//
//    @Autowired
//    private UserService userService;
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        log.info("初始化拦截器-------");
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        String uri = request.getRequestURI();
//        log.info(uri);
//        if (uri.equals("/favicon.ico")) return;
//        if (uri.matches("/swagger-ui.html.*?") || uri.matches("/user/.*") && !uri.equals("/user/checkLogin")) {
//            log.info("request pass [{}]", uri);
//            chain.doFilter(request, response);
//            return;
//        }
//        String token = CookieUtil.getCookieToken(request, redisService);
//        User user = userService.getUserByToken(token);
//        if (user != null) {
//            chain.doFilter(request, response);
//            return;
//        }
//        //从请求头中获取不到token说明未登录，阻止该请求访问资源
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json;charset=utf-8");
//        log.error("请先登录..");
//    }
//
//    @Override
//    public void destroy() {
//        log.info("销毁拦截器---------");
//    }
//
//}
