package org.boot.redis;

import org.boot.redis.core.SpringJedisStandAloneService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisSpringBootStarterApplicationTests {

    @Autowired
    SpringJedisStandAloneService service;

    @Test
    public void contextLoads() {
        System.out.println(service.set(1, "test", "123"));
        System.out.println(service.exists(1, "test"));

        System.out.println(service.incr(2, "test"));
    }

}
