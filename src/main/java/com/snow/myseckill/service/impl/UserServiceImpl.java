package com.snow.myseckill.service.impl;

import com.snow.myseckill.mapper.UserMappper;
import com.snow.myseckill.pojo.User;
import com.snow.myseckill.service.UserService;
import com.snow.myseckill.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMappper userMappper;


    @Override
    public User getById(long id) {
        //从缓存获取

        //从数据库获取

        //刷新缓存
        return null;
    }

    @Override
    public boolean updatePassword(String token, long id, String pwd) {
        return false;
    }

    @Override
    public String login(HttpServletResponse response, LoginVo loginVo) {
        return null;
    }
}
