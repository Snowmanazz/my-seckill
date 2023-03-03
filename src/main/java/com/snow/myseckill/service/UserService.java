package com.snow.myseckill.service;

import com.snow.myseckill.pojo.User;
import com.snow.myseckill.result.CodeMsg;
import com.snow.myseckill.result.Result;
import com.snow.myseckill.vo.LoginVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService {

    User getById(long id);

    boolean updatePassword(String token, long id, String pwd);

    Result<String> login(HttpServletResponse response, LoginVo loginVo);

    Result<CodeMsg> check(HttpServletRequest request);

    void logout(HttpServletRequest request, HttpServletResponse response);

    User getUserByToken(String token);
}
