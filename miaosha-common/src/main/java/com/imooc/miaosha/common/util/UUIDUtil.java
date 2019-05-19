package com.imooc.miaosha.common.util;

import java.util.UUID;

/**
 * @author xushaopeng
 * @date 2018/10/02
 */
public class UUIDUtil {
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
