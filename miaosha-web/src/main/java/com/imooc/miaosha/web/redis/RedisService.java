package com.imooc.miaosha.web.redis;

import com.alibaba.fastjson.JSON;
import com.imooc.miaosha.web.redis.prefix.KeyPrefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author xushaopeng
 * @date 2018/09/16
 */
@Service
public class RedisService {

    @Autowired
    JedisPool jedisPool;

    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {

        // 通过getResource方法返回一个jedis连接。
        try (Jedis jedis = jedisPool.getResource()) {
            //生成真正的key
            String realKey = prefix.getPrefix() + key;

            String str = jedis.get(realKey);
            T t = string2Bean(str, clazz);
            return t;
        }
    }

    public <T> Boolean exists(KeyPrefix prefix, String key, Class<T> clazz) {

        try (Jedis jedis = jedisPool.getResource()) {
            String realKey = prefix.getPrefix() + key;
            return jedis.exists(realKey);
        }
    }

    public <T> Long incr(KeyPrefix prefix, String key) {

        try (Jedis jedis = jedisPool.getResource()) {
            String realKey = prefix.getPrefix() + key;
            return jedis.incr(realKey);
        }
    }

    public <T> Long decr(KeyPrefix prefix, String key) {

        try (Jedis jedis = jedisPool.getResource()) {
            String realKey = prefix.getPrefix() + key;
            return jedis.decr(realKey);
        }
    }

    public <T> Boolean set(KeyPrefix prefix, String key, T value) {

        try (Jedis jedis = jedisPool.getResource()) {

            String str = bean2String(value);

            if (str == null || str.length() <= 0) {
                return false;
            }
            //生成真正的key
            String realKey = prefix.getPrefix() + key;

            int seconds = prefix.expireSeconds();
            if (seconds <= 0) {
                jedis.set(realKey, str);
            } else {
                jedis.setex(realKey, seconds, str);
            }

            return true;
        }

    }

    public static  <T> String bean2String(T value) {
        if (value == null) {
            return null;
        }

        Class<?> clazz = value.getClass();

        // ??? 为什么需要int 和 Integer?
        if (clazz == int.class || clazz == Integer.class) {
            return "" + value;
        } else if (clazz == String.class) {
            return value.toString();
            // long.class 代表什么？Long.class?
        } else if (clazz == long.class || clazz == Long.class) {
            return "" + value;
        }

        return JSON.toJSONString(value);
    }

    public static <T> T string2Bean(String str, Class<T> clazz) {

        if (str == null || str.length() <= 0) {
            return null;
        }

        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(str);
        } else if (clazz == String.class) {
            return (T) str;
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(str);
        }

        return JSON.toJavaObject(JSON.parseObject(str), clazz);
    }

    /**
     * 删除
     */
    public boolean delete(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            long ret = jedis.del(realKey);
            return ret > 0;
        } finally {
            returnToPool(jedis);
        }
    }

    private void returnToPool(Jedis jedis) {
        if (jedis != null) {
            // 这里调用close，其实是返回连接池中去了。
            jedis.close();
        }
    }
}
