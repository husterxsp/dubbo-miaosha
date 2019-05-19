package com.imooc.miaosha.web.redis.prefix;

/**
 * @author xushaopeng
 * @date 2018/09/18
 */
public abstract class  BasePrefix implements KeyPrefix {
    private int expireSecond;
    private String prefix;

    public BasePrefix(int expireSecond, String prefix) {
        this.expireSecond = expireSecond;
        this.prefix = prefix;
    }

    public BasePrefix(String prefix) {//0代表永不过期
        this(0, prefix);
    }

    @Override
    public int expireSeconds() {
        return expireSecond;
    }

    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className + ": " + prefix;
    }
}
