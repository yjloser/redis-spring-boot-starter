redis-spring-boot-starter
=========================

自定义连接redis-starter项目
--------------------------

## application.properties
**在单机环境下可以不配置，按照规范大家配置一下**  

    spring.jedis.port=22001
    spring.jedis.host=192.168.81.121
    spring.jedis.stand.alone.enabled=true

**在集群环境下必须配置，按照规范大家配置一下**  

    spring.jedis.cluster.enabled=true
    spring.jedis.nodes=192.168.1.2:6379,192.168.1.3:6379  
    
**公共配置选项**

    spring.jedis.maxWaitMillis=30000
    spring.jedis.minIdle=5000
    spring.jedis.auth=redis2018
    spring.jedis.timeout=30000
    spring.jedis.testOnBorrow=false
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
        SpringJedisStandAloneService standAloneService;

        /**
         * Redis集群服务 正在实现
         */
        @Autowired
        SpringJedisClusterService clusterService;

        @Test
        public void contextLoads() {
            //单机版本操作
            //第一个参数 数据库索引
            //第二个参数 key
            //第三个参数 value
            System.out.println(standAloneService.set(1, "test", "123"));
            System.out.println(standAloneService.exists(1, "test"));
            System.out.println(standAloneService.incr(2, "test"));
        }

    }
