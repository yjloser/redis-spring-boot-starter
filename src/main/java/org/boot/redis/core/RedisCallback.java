package org.boot.redis.core;

import redis.clients.jedis.Jedis;

/**
 * @program: redis-switch
 * @description: Redis回调模板
 * @author: Mr.Yang
 * @create: 2018-06-30 21:58
 **/
@FunctionalInterface
public interface RedisCallback<T> {

    /**
     * 回调
     *
     * @param jedis  连接源
     * @param params 参数
     * @return T 泛型
     * @author: Mr.Yang
     * @date 9:55 2018/5/11
     * @version V1.0.0
     */
      T call(Jedis jedis, Object params);
}
