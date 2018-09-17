package org.boot.redis.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * @program: redis-spring-boot-starter
 * @description: jedis配置
 * @author: Mr.Yang
 * @create: 2018-06-30 21:58
 **/
@ConfigurationProperties(prefix = SpringJedisProperties.REDIS_PREFIX)
@Data
public class SpringJedisProperties {

    static final String REDIS_PREFIX = "spring.jedis";

    /**
     * Jedis connection pool
     **/
    private static JedisPool pool;
    /**
     * Maximum number of connections
     **/
    private int maxTotal = 10;
    /**
     * Minimum number of connections
     **/
    private int minIdle = 1;
    /**
     * Maximum waiting time
     **/
    private long maxWaitMillis = 30000;
    /**
     * Test to get the correct connection
     **/
    private boolean testOnBorrow = false;
    /**
     * Redis stand-alone address
     **/
    private String host = "localhost";

    /**
     * Redis cluster address
     */
    private List<String> nodes;
    /**
     * Redis port
     **/
    private int port = 6379;
    /**
     * overtime time
     **/
    private int timeout = 30000;
    /**
     * Authentication password
     **/
    private String auth;
}