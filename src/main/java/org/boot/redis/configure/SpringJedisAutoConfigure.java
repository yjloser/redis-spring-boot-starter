package org.boot.redis.configure;

import org.boot.redis.core.SpringJedisClusterService;
import org.boot.redis.core.SpringJedisProperties;
import org.boot.redis.core.SpringJedisStandAloneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnJava;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.system.JavaVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * @program: redis-spring-boot-starter
 * @description: jedis自动配置
 * @author: Mr.Yang
 * @create: 2018-09-17 22:51
 **/
@Configuration
@ConditionalOnClass(value = {
        SpringJedisStandAloneService.class,
        SpringJedisClusterService.class
})
@EnableConfigurationProperties(SpringJedisProperties.class)
public class SpringJedisAutoConfigure {

    /**
     * jedis资源
     */
    @Autowired
    private SpringJedisProperties properties;

    /**
     * 分隔符
     */
    private final String CONFIG_LOCATION_DELIMITERS = ":,; \t\n";


    /**
     * 注册单机实例Bean
     *
     * @return 返回单机实例
     * @author Mr.Yang
     * @date 2018/9/17
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "spring.jedis.stand.alone.enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnJava(value = JavaVersion.EIGHT, range = ConditionalOnJava.Range.EQUAL_OR_NEWER)
    SpringJedisStandAloneService standAloneService() {
        //放入连接池
        return new SpringJedisStandAloneService(new SpringJedisStandAloneConfig(properties));
    }

    /**
     * 注册集群实例Bean
     *
     * @return 返回集群实例
     * @author Mr.Yang
     * @date 2018/9/17
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "spring.jedis.cluster.enabled", havingValue = "true")
    @ConditionalOnJava(value = JavaVersion.EIGHT, range = ConditionalOnJava.Range.EQUAL_OR_NEWER)
    SpringJedisClusterService clusterService() {
        //如果使用jedisCluster在外部处理解析
        //设置集群node
        Set<HostAndPort> nodes = new HashSet<>();
        //循环处理
        properties.getNodes().forEach(node -> {
            //冒号分割
            String[] nodeArray = StringUtils.tokenizeToStringArray(node, CONFIG_LOCATION_DELIMITERS);
            //加入节点
            nodes.add(new HostAndPort(nodeArray[0], Integer.valueOf(nodeArray[1])));
        });
        //配置连接池
        JedisPoolConfig config = new JedisPoolConfig();
        // 设置池配置项值
        config.setMaxTotal(properties.getMaxTotal());
        config.setMaxWaitMillis(properties.getMaxWaitMillis());
        config.setMaxIdle(properties.getMinIdle());
        config.setTestOnBorrow(properties.isTestOnBorrow());
        //集群配置
        return new SpringJedisClusterService(new SpringJedisClusterConfig(nodes, config));
    }
}
