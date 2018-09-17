package org.boot.redis.core;

import lombok.Data;

/**
 * @program: redis-switch
 * @description: Key-Value实体类
 * @author: Mr.Yang
 * @create: 2018-06-30 21:58
 **/
@Data
public class RedisKVPO {
    /**
     * key
     **/
    private String k;

    /**
     * 值
     **/
    private String v;

    public RedisKVPO() {
    }

    public RedisKVPO(String k, String v) {
        this.k = k;
        this.v = v;
    }
}