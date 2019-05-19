package com.imooc.miaosha.web.redis.prefix;

/**
 * @author xushaopeng
 * @date 2018/09/18
 */
public class UserKey extends BasePrefix {

    private UserKey(String prefix) {
        super(0, prefix);
    }

    private UserKey(int expireSecond, String prefix) {
        super(expireSecond, prefix);
    }

    public static UserKey getById = new UserKey("id");

    public static UserKey getByName = new UserKey("name");

}
