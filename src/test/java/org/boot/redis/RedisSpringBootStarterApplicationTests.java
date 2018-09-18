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
    SpringJedisClusterService clusterService;

    /**
     * Redis单机服务
     */
    @Autowired
    SpringJedisStandAloneService standAloneService;


    @Test
    public void contextLoads() {
        //单机redis
        new Thread(() -> System.out.println(standAloneService.set(0, "test1", "123"))).start();
        new Thread(() -> System.out.println(standAloneService.exists(0, "test2"))).start();
        //集群redis
        new Thread(() -> System.out.println(clusterService.exists("test"))).start();

    }

}
