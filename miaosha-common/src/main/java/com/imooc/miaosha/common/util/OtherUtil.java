package com.imooc.miaosha.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * @author xushaopeng
 * @date 2018/10/03
 */
public class OtherUtil {
    public static Map<String, Object> Object2Map(Object o) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(o, Map.class);
    }
}
