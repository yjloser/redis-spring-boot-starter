package org.boot.redis.core;

/**
 * @program: redis-switch
 * @description: Key-Value实体类
 * @author: Mr.Yang
 * @create: 2018-06-30 21:58
 **/
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

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }
}