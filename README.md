redis-spring-boot-starter
=========================

自定义连接redis-starter项目
--------------------------

#application.properties
###在单机环境下可以不配置，按照规范大家配置一下<br>
    spring.jedis.port=22001
    spring.jedis.host=192.168.81.121
    spring.jedis.stand.alone.enabled=true

###在集群环境下必须配置，按照规范大家配置一下<br>
    spring.jedis.cluster.enabled=true
    spring.jedis.nodes=192.168.1.2:6379,192.168.1.3:6379

    spring.jedis.maxWaitMillis=30000
    spring.jedis.minIdle=5000
    spring.jedis.auth=redis2018
    spring.jedis.timeout=30000
    spring.jedis.testOnBorrow=false
    spring.jedis.maxTotal=10000

