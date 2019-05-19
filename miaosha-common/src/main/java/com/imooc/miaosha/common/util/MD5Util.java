package com.imooc.miaosha.common.util;

import org.apache.commons.codec.digest.DigestUtils;

// import org.springframework.util.DigestUtils;

/**
 * @author xushaopeng
 * @date 2018/09/18
 */
public class MD5Util {
    private static final String salt = "1a2b3c4d";

    public static String md5(String str) {

        return DigestUtils.md5Hex(str);

//        return DigestUtils.md5DigestAsHex(str.getBytes());
    }

    public static String inputPass2FormPass(String inputPass) {
        return md5(inputPass + salt);
    }

    public static String formPass2DbPass(String formPass, String salt) {
        return md5(formPass + salt);
    }

    public static String inputPass2DbPass(String inputPass, String salt) {
        return formPass2DbPass(inputPass2FormPass(inputPass), salt);
    }

    public static void main(String[] args) {
        System.out.println(inputPass2FormPass("123"));
        System.out.println(formPass2DbPass(inputPass2FormPass("123"), "1a2b3c4d"));
    }

}
