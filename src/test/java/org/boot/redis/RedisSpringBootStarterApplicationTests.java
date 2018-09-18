package org.boot.redis;

import org.boot.redis.core.SpringJedisClusterService;
import org.boot.redis.core.SpringJedisStandAloneService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisSpringBootStarterApplicationTests {

    /**
     * Redis单机服务
     */
    @Autowired
    SpringJedisStandAloneService standAloneService;

    @Test
    public void contextLoads() {
        //单机版本操作
        //第一个参数 数据库索引
        //第二个参数 key
        //第三个参数 value


        new Thread(()-> System.out.println(standAloneService.exists(1, "test"))).start();

        new Thread(()-> System.out.println(standAloneService.exists(1, "test"))).start();
    }

}
