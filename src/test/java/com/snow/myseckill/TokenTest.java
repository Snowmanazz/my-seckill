package com.snow.myseckill;

import com.snow.myseckill.util.IdGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TokenTest {

    @Test
    public void testTokenGenerate(){
        IdGenerator idGenerator = new IdGenerator(0);
        Runnable r = () -> {
            System.out.println(idGenerator.nextId());
        };
        for (int i = 0; i < 100000; i++) {
            r.run();
        }
    }
}
