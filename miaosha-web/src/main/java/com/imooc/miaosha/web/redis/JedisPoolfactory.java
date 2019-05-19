package com.imooc.miaosha.web.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author xushaopeng
 * @date 2018/09/17
 */
@Service
public class JedisPoolfactory {

    @Autowired
    RedisConfig redisConfig;

    // 定义一个spring bean，然后在RedisService中进行注入。
    @Bean
    public JedisPool JedisPoolfactory() {

        // 连接池，一般都有一个config
        JedisPoolConfig poolConfig = new JedisPoolConfig();

        poolConfig.setMaxIdle(redisConfig.getPoolMaxIdle());
        poolConfig.setMaxTotal(redisConfig.getPoolMaxTotal());
        // 里面用的是毫秒，所以这里* 1000，代表外面我们用 秒
        poolConfig.setMaxWaitMillis(redisConfig.getPoolMaxWait() * 1000);

        JedisPool jp = new JedisPool(poolConfig,
                redisConfig.getHost(),
                redisConfig.getPort(),
                redisConfig.getTimeout() * 1000);

        return jp;
    }
}
