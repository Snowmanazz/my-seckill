package com.snow.myseckill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.snow.myseckill.mapper")
public class MySeckillApplication {

    public static void main(String[] args) {
        SpringApplication.run(MySeckillApplication.class, args);
    }

}
