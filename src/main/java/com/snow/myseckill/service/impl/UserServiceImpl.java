package com.snow.myseckill.service.impl;

import com.snow.myseckill.exception.GlobalException;
import com.snow.myseckill.mapper.UserMappper;
import com.snow.myseckill.pojo.User;
import com.snow.myseckill.redis.UserKeyPrefix;
import com.snow.myseckill.result.CodeMsg;
import com.snow.myseckill.result.Result;
import com.snow.myseckill.service.RedisService;
import com.snow.myseckill.service.UserService;
import com.snow.myseckill.util.CookieUtil;
import com.snow.myseckill.util.IdGenerator;
import com.snow.myseckill.util.MD5Util;
import com.snow.myseckill.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMappper userMappper;

    @Autowired
    private RedisService redisService;

    @Override
    public User getById(long id) {
        //从缓存获取
        boolean exists = redisService.hasKey(UserKeyPrefix.USER_ID, "" + id);
        if (exists) {
            return redisService.get(UserKeyPrefix.USER_ID, "" + id, User.class);
        }
        //从数据库获取
        User user = userMappper.selectById(id);
        if (user != null) {
            //刷新缓存
            redisService.set(UserKeyPrefix.USER_ID, "" + id, user);
        }
        return user;
    }

    @Override
    public boolean updatePassword(String token, long id, String pwd) {
        return false;
    }

    @Override
    public Result<String> login(HttpServletResponse response, LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        User user = getById(Long.parseLong(mobile));
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        String dbPass = user.getPassword();
        String salt = user.getSalt();
        String calcPass = MD5Util.inputPassToDbPass(password, salt);
        if (!Objects.equals(dbPass, calcPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        IdGenerator idGenerator = new IdGenerator(0);
        String token = "" + idGenerator.nextId();
        log.info("log token --[{}]", token);
        CookieUtil.addCookie(response, token, user, redisService);
        log.info("login success ");
        return Result.success(token);
    }

    @Override
    public Result<CodeMsg> check(HttpServletRequest request) {
        String token = CookieUtil.getCookieToken(request, redisService);
        User user = getUserByToken(token);
        return user != null ? Result.success(CodeMsg.SUCCESS) : Result.error(CodeMsg.SESSION_ERROR);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String token = CookieUtil.getCookieToken(request, redisService);
        redisService.delete(UserKeyPrefix.TOKEN, token);
        Cookie cookie = new Cookie(CookieUtil.TOKEN, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }


    /**
     * 根据token获取用户信息
     *
     * @param token
     * @return
     */
    @Override
    public User getUserByToken(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        boolean exists = redisService.hasKey(UserKeyPrefix.TOKEN, token);
        if (!exists) {
            return null;
        }
        return redisService.get(UserKeyPrefix.TOKEN, token, User.class);
    }
}
