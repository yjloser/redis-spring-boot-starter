redis-spring-boot-starter
=========================

自定义连接redis-starter项目
--------------------------

## application.properties
**在单机环境下可以不配置，按照规范大家配置一下**  

    spring.jedis.port=22001
    spring.jedis.host=192.168.81.121
    spring.jedis.auth=redis2018
    spring.jedis.stand.alone.enabled=true

**在集群环境下必须配置，按照规范大家配置一下**  

    spring.jedis.cluster.enabled=true
    spring.jedis.nodes=192.168.81.121:7000,192.168.81.121:7001,192.168.81.121:7002
    
**公共配置选项**

    spring.jedis.maxWaitMillis=30000
    spring.jedis.minIdle=5000
    spring.jedis.timeout=30000
    spring.jedis.testOnBorrow=true
    spring.jedis.maxTotal=10000



单元测试
-------------------------
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

## 关于Redis集群，如只是部署在本地测试,直接使用docker拉取镜像更加方便！
（注意！！当前Redis集群要和代码在同一台服务器中，当前容器创建集群时使用127.0.0.1地址）
------------------------------------
拉取镜像 docker pull tommy351/redis-cluster
启动容器 docker run -p 7000:7000 -p 7001:7001 -p 7002:7002 -p 7003:7003 -p 7004:7004 -p 7005:7005 tommy351/redis-cluster:3.2
