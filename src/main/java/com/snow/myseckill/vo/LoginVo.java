package com.snow.myseckill.vo;

import lombok.Data;

@Data
public class LoginVo {

    private String mobile;

    private String password;
    @Override
    public String toString() {
        return "LoginVo{" +
                "mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
