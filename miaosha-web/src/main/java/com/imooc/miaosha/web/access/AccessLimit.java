package com.imooc.miaosha.web.access;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author xushaopeng
 * @date 2018/12/23
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface AccessLimit {
    int seconds() default -1;
    int maxCount() default -1;
    boolean needLogin() default true;
}
