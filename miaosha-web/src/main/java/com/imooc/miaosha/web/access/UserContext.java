package com.imooc.miaosha.web.access;

import com.imooc.miaosha.common.domain.miaosha.MiaoshaUser;

/**
 * @author xushaopeng
 * @date 2018/12/23
 */
public class UserContext {

    private static ThreadLocal<MiaoshaUser> userHolder = new ThreadLocal<MiaoshaUser>();

    public static void setUser(MiaoshaUser user) {
        userHolder.set(user);
    }

    public static MiaoshaUser getUser() {
        return userHolder.get();
    }

}
