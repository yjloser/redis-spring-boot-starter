package org.boot.redis.core;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.boot.redis.configure.SpringJedisClusterConfig;
import org.springframework.beans.factory.DisposableBean;
import redis.clients.jedis.JedisCluster;

/**
 * @program: redis-spring-boot-starter
 * @description: 连接Redis服务(集群)
 * @author: Mr.Yang
 * @create: 2018-09-18 00:33
 **/
@Slf4j
public class SpringJedisClusterService implements DisposableBean {

    /**
     * 连接池
     **/
    @Setter
    @Getter
    private JedisCluster jedisPool;

    public SpringJedisClusterService(SpringJedisClusterConfig jedisClusterConfig) {
        this.jedisPool = jedisClusterConfig;
    }

    @Override
    public void destroy() throws Exception {
        jedisPool.close();
    }
}
