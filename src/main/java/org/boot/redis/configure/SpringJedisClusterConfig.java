package org.boot.redis.configure;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.Set;

/**
 * @program: redis-spring-boot-starter
 * @description:
 * @author: Mr.Yang
 * @create: 2018-09-18 09:27
 **/
public class SpringJedisClusterConfig extends JedisCluster {

    /**
     * 构造
     *
     * @param nodes      集群节点
     * @param poolConfig 连接池
     * @author Mr.Yang
     * @date 2018/9/18 0018
     */
    public SpringJedisClusterConfig(Set<HostAndPort> nodes, GenericObjectPoolConfig poolConfig) {
        super(nodes,poolConfig);
    }


}
