package com.snow.myseckill;

import com.snow.myseckill.mapper.UserMappper;
import com.snow.myseckill.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.Date;

@SpringBootTest
public class UserAccountTest {


    @Autowired
    UserMappper userMappper;

    @Test
    public void registerButch(){
        for (int i = 0; i < 100000; i++) {
            User user = new User();
            user.setId(19181800000L + i);
            user.setNickname("用户"+i);
            user.setPassword("b7797cce01b4b131b433b6acf4add449");
            user.setHead(null);
            user.setSalt("1a2b3c4d");
            user.setRegisterDate(new Timestamp(new Date().getTime()));
            user.setLastLoginDate(new Timestamp(new Date().getTime()));
            user.setLoginCount(1);
            userMappper.insert(user);
        }
    }

}
