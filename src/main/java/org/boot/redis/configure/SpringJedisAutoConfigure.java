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

/**
 * @program: redis-spring-boot-starter
 * @description: jedis自动配置
 * @author: Mr.Yang
 * @create: 2018-09-17 22:51
 **/
@Configuration
@ConditionalOnClass(SpringJedisStandAloneService.class)
@EnableConfigurationProperties(SpringJedisProperties.class)
public class SpringJedisAutoConfigure {

    @Autowired
    private SpringJedisProperties properties;


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
    @ConditionalOnJava(value = JavaVersion.EIGHT,range= ConditionalOnJava.Range.EQUAL_OR_NEWER)
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
    @ConditionalOnJava(value = JavaVersion.EIGHT,range= ConditionalOnJava.Range.EQUAL_OR_NEWER)
    SpringJedisClusterService clusterService() {

        System.out.println("===========集群Bean注册至IOC===========");

        return null;
    }
}
