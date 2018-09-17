package org.boot.redis.core;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @program: redis-spring-boot-starter
 * @description: 连接Redis服务(单机)
 * @author: Mr.Yang
 * @create: 2018-09-17 22:53
 **/
public class SpringJedisStandAloneService implements DisposableBean {

    private static Logger logger = LogManager.getLogger(SpringJedisStandAloneService.class);


    /**
     * 连接池
     **/
    @Setter
    @Getter
    private JedisPool jedisPool;

    public SpringJedisStandAloneService(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    /**
     * 模版执行,自动关闭
     *
     * @param callback 回调接口处理
     * @param args     数组参数
     * @return T 泛型
     * @author Mr.Yang
     * @date 2018/9/17
     */
    private <T> T execute(RedisCallback<T> callback, Object... args) {
        Jedis jedis = null;
        try {
            Object index = args[0];
            // 0-15号库
            if (null != index && Integer.parseInt(index.toString()) > 0 && Integer.parseInt(index.toString()) < 16) {
                jedis = getRedis(Integer.parseInt(index.toString()));
            } else {
                jedis = getRedis();
            }
            return callback.call(jedis, args);
        } catch (JedisConnectionException e) {
            if (jedis != null) {
                jedis.close();
            }
            jedis = getRedis();
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (jedis != null) {
                returnRedis(jedis);
            }
        }
        return null;
    }

    /**
     * index 实例index
     *
     * @param index 数据库位置
     * @author Mr.Yang
     * @date 10:38 2018/5/11
     * @version V1.0.0
     */
    public void select(int index) {
        execute((jedis, parms) -> {
            int index1 = Integer.parseInt(((Object[]) parms)[0].toString());
            return jedis.select(index1);
        }, index);
    }

    /**
     * 连接是否正常
     *
     * @param index 数据库位置
     * @return Boolean 返回true或false
     * @author Mr.Yang
     * @date 10:26 2018/5/11
     * @version V1.0.0
     */
    public Boolean isConnected(int index) {
        return execute((jedis, params) -> jedis.isConnected(), index);
    }

    /**
     * 查看 key 中，指定的字段是否存在。
     *
     * @param index 数据库位置
     * @param key   key标识
     * @return Boolean 返回true或false
     * @author Mr.Yang
     * @date 10:26 2018/5/11
     * @version V1.0.0
     */
    public Boolean exists(int index, String key) {
        return execute((jedis, params) -> {
            String key1 = ((Object[]) params)[1].toString();
            return jedis.exists(key1);
        }, index, key);
    }

    /**
     * 查看哈希表 key 中，指定的字段是否存在。
     *
     * @param index        数据库位置
     * @param mapKey       key标识
     * @param attributeKey 成员字段
     * @return Boolean 返回true或false
     * @author Mr.Yang
     * @date 10:26 2018/5/11
     * @version V1.0.0
     */
    public Boolean hexists(int index, String mapKey, String attributeKey) {
        return execute((jedis, params) -> {
            String key = ((Object[]) params)[1].toString();
            String field = ((Object[]) params)[2].toString();
            return jedis.hexists(key, field);
        }, index, mapKey, attributeKey);
    }

    /**
     * 通过Hash（哈希），获取存储在哈希表中指定字段的值。
     *
     * @param index 数据库位置
     * @param field 成员字段
     * @return String
     * @author Mr.Yang
     * @date 10:26 2018/5/11
     * @version V1.0.0
     */
    public String hget(int index, String key, String field) {
        return execute((jedis, parms) -> {
            String key1 = ((Object[]) parms)[1].toString();
            String field1 = ((Object[]) parms)[2].toString();
            return jedis.hget(key1, field1);
        }, index, key, field);
    }

    /**
     * 通过Hash（哈希），获取在哈希表中指定 key 的所有字段和值
     *
     * @param index 数据库位置
     * @param key   key标识
     * @return Map 获取在哈希表中指定 key 的所有字段和值
     * @author Mr.Yang
     * @date 10:26 2018/5/11
     * @version V1.0.0
     */
    public Map<String, String> hgetAll(int index, String key) {
        return execute((jedis, params) -> {
            String key1 = ((Object[]) params)[1].toString();
            return jedis.hgetAll(key1);
        }, index, key);
    }

    /**
     * 通过Hash（哈希），删除成员字段
     *
     * @param index        数据库位置
     * @param mapKey       key标识
     * @param attributeKey 成员字段
     * @return long 受影响行数
     * @author Mr.Yang
     * @date 10:26 2018/5/11
     * @version V1.0.0
     */
    public Long hdel(int index, String mapKey, String attributeKey) {
        return execute((jedis, params) -> {
            String key = ((Object[]) params)[1].toString();
            String field = ((Object[]) params)[2].toString();
            return jedis.hdel(key, field);
        }, index, mapKey, attributeKey);
    }

    /**
     * 通过Hash（哈希），获取值
     *
     * @param index 数据库位置
     * @param key   key标识
     * @param field 成员字段
     * @param value 值
     * @return 返回影响行
     * @author Mr.Yang
     * @date 10:26 2018/5/11
     * @version V1.0.0
     */
    public Long hset(int index, String key, String field, String value) {
        return execute((jedis, parms) -> {
            String key1 = ((Object[]) parms)[1].toString();
            String field1 = ((Object[]) parms)[2].toString();
            String value1 = ((Object[]) parms)[3].toString();
            return jedis.hset(key1, field1, value1);
        }, index, key, field, value);
    }

    /**
     * 通过key（字符串），获取值
     *
     * @param index 数据库位置
     * @param key   key标识
     * @return 返回值
     * @author Mr.Yang
     * @date 10:26 2018/5/11
     * @version V1.0.0
     */
    public String get(int index, String key) {
        return execute((jedis, parms) -> {
            String key1 = ((Object[]) parms)[1].toString();
            return jedis.get(key1);
        }, index, key);
    }

    /**
     * 设置key的过期时间
     *
     * @param index   数据库位置
     * @param key     key标识
     * @param seconds 过期时间
     * @return 返回1设置 0未设置
     * @author Mr.Yang
     * @date 10:26 2018/5/11
     * @version V1.0.0
     */
    public Long expire(int index, String key, int seconds) {
        return execute((jedis, parms) -> {
            String key1 = ((Object[]) parms)[1].toString();
            String seconds1 = ((Object[]) parms)[2].toString();
            return jedis.expire(key1, Integer.valueOf(seconds1));
        }, index, key, seconds);
    }

    /**
     * 通过二进制key，获取二进制值
     *
     * @param index 数据库位置
     * @param key   key标识
     * @return byte 二进制
     * @author Mr.Yang
     * @date 10:26 2018/5/11
     * @version V1.0.0
     */
    public byte[] getByte(int index, String key) {
        return execute((jedis, parms) -> {
            String key1 = ((Object[]) parms)[1].toString();
            try {
                return jedis.get(key1.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                logger.error(e.getMessage(), e);
            }
            return null;
        }, index, key);
    }

    /**
     * 设置key（字符串）赋值
     *
     * @param index 数据库位置
     * @param key   key标识
     * @param value 值
     * @return 返回状态码 OK
     * @author Mr.Yang
     * @date 10:11 2018/5/11
     * @version V1.0.0
     */
    public String set(int index, String key, String value) {
        return execute((jedis, parms) -> {
            String key1 = ((Object[]) parms)[1].toString();
            String value1 = ((Object[]) parms)[2].toString();
            return jedis.set(key1, value1);
        }, index, key, value);
    }

    /**
     * 设置key(二进制)
     *
     * @param index 数据库位置
     * @param key   key标识
     * @param value 值
     * @return 返回状态码 OK
     * @author Mr.Yang
     * @date 10:11 2018/5/11
     * @version V1.0.0
     */
    public String set(int index, String key, byte[] value) {
        return execute((jedis, parms) -> {
            String key1 = ((Object[]) parms)[1].toString();
            byte[] value1 = (byte[]) ((Object[]) parms)[2];
            try {
                return jedis.set(key1.getBytes("UTF-8"), value1);
            } catch (UnsupportedEncodingException e) {
                logger.error(e.getMessage(), e);
            }
            return null;
        }, index, key, value);
    }

    /**
     * 设置key(字符串)过期时间（单位：秒）
     *
     * @param index   数据库位置
     * @param key     key标识
     * @param value   值
     * @param seconds 过期时间
     * @return 返回状态码 OK
     * @author Mr.Yang
     * @date 10:11 2018/5/11
     * @version V1.0.0
     */
    public String set(int index, String key, String value, int seconds) {
        return execute((jedis, parms) -> {
            String key1 = ((Object[]) parms)[1].toString();
            String value1 = ((Object[]) parms)[2].toString();
            String seconds1 = ((Object[]) parms)[3].toString();
            return jedis.setex(key1, Integer.parseInt(seconds1), value1);
        }, index, key, value, seconds);
    }

    /**
     * 设置key(二进制)过期时间（单位：秒）
     *
     * @param index   数据库位置
     * @param key     key标识
     * @param value   值
     * @param seconds 过期时间
     * @author Mr.Yang
     * @date 10:11 2018/5/11
     * @version V1.0.0
     */
    public void set(int index, String key, byte[] value, int seconds) {
        execute((jedis, parms) -> {
            String key1 = ((Object[]) parms)[1].toString();
            byte[] value1 = (byte[]) ((Object[]) parms)[2];
            String seconds1 = ((Object[]) parms)[3].toString();
            try {
                jedis.setex(key1.getBytes("UTF-8"), Integer.parseInt(seconds1), value1);
            } catch (UnsupportedEncodingException e) {
                logger.error(e.getMessage(), e);
            }
            return null;
        }, index, key, value, seconds);
    }

    /**
     * 批量set新增
     *
     * @param index 数据库位置
     * @author Mr.Yang
     * @date 10:11 2018/5/11
     * @version V1.0.0
     */
    public void setPipeLine(int index, List<RedisKVPO> list) {
        execute((jedis, parms) -> {
            Pipeline p = jedis.pipelined();
            @SuppressWarnings("unchecked")
            List<RedisKVPO> values = (List<RedisKVPO>) ((Object[]) parms)[1];
            for (RedisKVPO po : values) {
                p.set(po.getK(), po.getV());
            }
            p.sync();
            return null;
        }, index, list);
    }

    /**
     * 根据key删除
     *
     * @param index 数据库位置
     * @param key   key标识
     * @return 返回删除影响行数
     * @author Mr.Yang
     * @date 10:11 2018/5/11
     * @version V1.0.0
     */
    public Long del(int index, String key) {
        return execute((jedis, parms) -> {
            String key1 = ((Object[]) parms)[1].toString();
            return jedis.del(key1);
        }, index, key);
    }

    /**
     * 获取列表长度
     *
     * @param index 数据库位置
     * @param key   key标识
     * @return 返回集合长度
     * @author Mr.Yang
     * @date 10:11 2018/5/11
     * @version V1.0.0
     */
    public Long llen(int index, String key) {
        return execute((jedis, parms) -> {
            String key1 = ((Object[]) parms)[1].toString();
            return jedis.llen(key1);
        }, index, key);
    }

    /**
     * 将一个或多个值插入到列表头部
     *
     * @param index 数据库位置
     * @param key   key标识
     * @param value 值
     * @return 返回整个元素的数量
     * @author Mr.Yang
     * @date 10:11 2018/5/11
     * @version V1.0.0
     */
    public Long lpush(int index, String key, String value) {
        return execute((jedis, parms) -> {
            String key1 = ((Object[]) parms)[1].toString();
            String value1 = ((Object[]) parms)[2].toString();
            return jedis.lpush(key1, value1);
        }, index, key, value);
    }

    /**
     * List列表key，一次命令新增
     *
     * @param index 数据库位置
     * @param key   key标识
     * @author Mr.Yang
     * @date 10:11 2018/5/11
     * @version V1.0.0
     */
    public void lpushPipeLine(int index, String key, List<String> values) {
        execute((RedisCallback<String>) (jedis, parms) -> {
            String key1 = ((Object[]) parms)[1].toString();
            List<String> values1 = (List<String>) ((Object[]) parms)[2];
            Pipeline p = jedis.pipelined();
            for (String value : values1) {
                p.lpush(key1, value);
            }
            p.sync();
            return null;
        }, index, key, values);
    }

    /**
     * 获取列表指定范围内的元素
     *
     * @param index 数据库位置
     * @param key   key标识
     * @param start 开始
     * @param end   截至
     * @return List集合
     * @author Mr.Yang
     * @date 10:11 2018/5/11
     * @version V1.0.0
     */
    public List<String> lrange(int index, String key, long start, long end) {
        return execute((jedis, parms) -> {
            Object[] ps = ((Object[]) parms);
            String key1 = ps[1].toString();
            long start1 = Long.parseLong(ps[2].toString());
            long end1 = Long.parseLong(ps[3].toString());
            return jedis.lrange(key1, start1, end1);
        }, index, key, start, end);
    }

    /**
     * 原子性自增
     *
     * @param index 数据库位置
     * @param key   key标识
     * @return 返回自增值
     * @author Mr.Yang
     * @date 10:11 2018/5/11
     * @version V1.0.0
     */
    public Long incr(int index, String key) {
        return execute((jedis, parms) -> {
            String key1 = ((Object[]) parms)[1].toString();
            return jedis.incr(key1);
        }, index, key);
    }

    /**
     * 向集合添加一个(多个成员不支持)
     *
     * @param index 数据库位置
     * @param key   key标识
     * @param value 值
     * @return 返回插入影响行数
     * @author Mr.Yang
     * @date 10:11 2018/5/11
     * @version V1.0.0
     */
    public Long sadd(int index, String key, String value) {
        return execute((jedis, parms) -> {
            String key1 = ((Object[]) parms)[1].toString();
            String value1 = ((Object[]) parms)[2].toString();
            return jedis.sadd(key1, value1);
        }, index, key, value);
    }

    /**
     * 返回集合中的所有成员
     *
     * @param index 数据库位置
     * @param key   key标识
     * @return Set集合
     * @author Mr.Yang
     * @date 10:11 2018/5/11
     * @version V1.0.0
     */
    public Set<String> smembers(int index, String key) {
        return execute((jedis, parms) -> {
            Object[] ps = ((Object[]) parms);
            String key1 = ps[1].toString();
            return jedis.smembers(key1);
        }, index, key);
    }

    /**
     * 移出并获取列表的最后一个元素
     *
     * @param index 数据库位置
     * @param key   key标识
     * @return List列表
     * @author Mr.Yang
     * @date 10:11 2018/5/11
     * @version V1.0.0
     */
    public List<String> brpop(int index, String key) {
        return execute((jedis, parms) -> {
            Object[] ps = ((Object[]) parms);
            String key1 = ps[1].toString();
            return jedis.brpop(0, key1);
        }, index, key);
    }

    /**
     * 对hash里面的value进行自增
     *
     * @param index 数据库位置
     * @param key   key标识
     * @param field 成员字段
     * @param value 自增步长
     * @author Mr.Yang
     * @date 10:26 2018/5/11
     * @version V1.0.0
     */
    public Long hincrby(int index, String key, String field, long value) {
        return execute((jedis, parms) -> {
            String key1 = ((Object[]) parms)[1].toString();
            String field1 = ((Object[]) parms)[2].toString();
            Long value1 = Long.valueOf(((Object[]) parms)[3].toString());
            return jedis.hincrBy(key1, field1, value1);
        }, index, key, field, value);
    }

    /**
     * 将 key 中储存的数字加上指定的增量值。
     *
     * @param index 数据库位置
     * @param key   key标识
     * @param value 自增步长
     * @return String 返回旧值
     * @author Mr.Yang
     * @date 10:26 2018/5/11
     * @version V1.0.0
     */
    public Long incrBy(int index, String key, long value) {
        return execute((jedis, parms) -> {
            String key1 = ((Object[]) parms)[1].toString();
            long value1 = Long.valueOf(((Object[]) parms)[2].toString());
            return jedis.incrBy(key1, value1);
        }, index, key, value);
    }

    /**
     * 将 key 中储存的数字减去指定的增量值。
     *
     * @param index 数据库位置
     * @param key   key标识
     * @param value 自减步长
     * @return String 返回旧值
     * @author Mr.Yang
     * @date 10:26 2018/5/11
     * @version V1.0.0
     */
    public Long decrBy(int index, String key, long value) {
        return execute((jedis, parms) -> {
            String key1 = ((Object[]) parms)[1].toString();
            long value1 = Long.valueOf(((Object[]) parms)[2].toString());
            return jedis.decrBy(key1, value1);
        }, index, key, value);
    }


    /**
     * 关闭服务时，自动销毁
     *
     * @author Mr.Yang
     * @date 2018/9/17
     */
    @Override
    public void destroy() {
        jedisPool.destroy();
    }

    /**
     * 默认选择db0库
     *
     * @return Jedis
     * @author Mr.Yang
     * @date 2018/9/17
     */
    private Jedis getRedis() {
        return getRedis(0);
    }

    /**
     * 选择db库
     *
     * @param index db库标记
     * @return Jedis
     * @author Mr.Yang
     * @date 2018/9/17
     */
    private Jedis getRedis(int index) {
        Jedis jedis = jedisPool.getResource();
        jedis.select(index);
        return jedis;
    }

    /**
     * 返回连接池
     *
     * @param jedis 连接
     * @author Mr.Yang
     * @date 2018/9/17
     */
    private void returnRedis(Jedis jedis) {
        jedis.close();
    }
}
