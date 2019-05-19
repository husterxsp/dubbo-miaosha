package com.imooc.miaosha.web.redis.prefix;

/**
 * @author xushaopeng
 * @date 2018/09/18
 */
public interface KeyPrefix {

    int expireSeconds();

    String getPrefix();

}