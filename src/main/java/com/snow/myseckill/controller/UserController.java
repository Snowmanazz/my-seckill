package com.snow.myseckill.controller;

import com.snow.myseckill.exception.GlobalException;
import com.snow.myseckill.pojo.User;
import com.snow.myseckill.redis.UserKeyPrefix;
import com.snow.myseckill.result.CodeMsg;
import com.snow.myseckill.result.Result;
import com.snow.myseckill.service.RedisService;
import com.snow.myseckill.service.UserService;
import com.snow.myseckill.vo.LoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Api(tags = "用户")
@Slf4j
@RestController
@RequestMapping("/main")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("用户登录")
    @RequestMapping("/login")
    public Result<String> login(HttpServletResponse response, LoginVo loginVo) {
        log.info("登录信息[{}]", loginVo);
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        if (StringUtils.isBlank(loginVo.getMobile())) {
            throw new GlobalException(CodeMsg.MOBILE_EMPTY);
        }
        if (StringUtils.isBlank(loginVo.getPassword())) {
            throw new GlobalException(CodeMsg.PASSWORD_EMPTY);
        }
        return userService.login(response, loginVo);
    }

//    @ApiOperation("注册用户")
//    @RequestMapping("/register")
//    public Result<CodeMsg> register(User user){
//
//    }
//
//    @ApiOperation("注销登录")
//    @RequestMapping("/logout")
//    public Result<CodeMsg> logout(String token){
//
//    }
}
