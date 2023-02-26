package com.snow.myseckill.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sk_user")
public class User {
    @TableId
    private Long id;
    private String nickname;
    private String password;
    private String salt;
    private String head;
    @TableField("register_date")
    private Date registerDate;
    @TableField("last_login_date")
    private Date lastLoginDate;
    @TableField("login_count")
    private Integer loginCount;
}
