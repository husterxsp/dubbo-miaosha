package com.imooc.miaosha.web.redis.prefix;

/**
 * @author xushaopeng
 * @date 2018/12/23
 */
public class AccessKey extends BasePrefix {


    public static KeyPrefix access = new AccessKey(5, "access");

    private AccessKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static AccessKey withExpire(int expireSeconds) {
        return new AccessKey(expireSeconds, "access");
    }

}