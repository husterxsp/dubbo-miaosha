package com.imooc.miaosha.web.redis.prefix;

/**
 * @author xushaopeng
 * @date 2018/12/22
 */
public class MiaoshaKey extends BasePrefix {

    public static MiaoshaKey isGoodsOver = new MiaoshaKey(0, "go");
    public static MiaoshaKey getMiaoshaPath = new MiaoshaKey(60, "mp");
    public static MiaoshaKey getMiaoshaVerifyCode = new MiaoshaKey(300, "vc");

    private MiaoshaKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
}