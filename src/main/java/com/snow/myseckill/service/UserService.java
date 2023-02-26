package com.snow.myseckill.service;

import com.snow.myseckill.pojo.User;
import com.snow.myseckill.vo.LoginVo;

import javax.servlet.http.HttpServletResponse;

public interface UserService {

    User getById(long id);

    boolean updatePassword(String token, long id, String pwd);

    String login(HttpServletResponse response, LoginVo loginVo);

}
