package com.imooc.miaosha.web.controller;

import com.imooc.miaosha.common.domain.User;
import com.imooc.miaosha.web.redis.RedisService;
import com.imooc.miaosha.web.redis.prefix.GoodsKey;
import com.imooc.miaosha.web.redis.prefix.UserKey;
import com.imooc.miaosha.common.result.Result;
import com.imooc.miaosha.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xushaopeng
 * @date 2018/09/16
 */
@RestController
public class SampleController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;


    @GetMapping("/redis/get")
    public Result<User> redisGet() {
        User user = redisService.get(UserKey.getById, "1", User.class);
        return Result.success(user);
    }

    @GetMapping("/redis/set")
    public Result<Boolean> redisSet() {

        redisService.set(UserKey.getById, "1", new User(1, "1111"));

        return Result.success(true);
    }

    @GetMapping("/db/get")
    public Result<User> dbGet() {
        User user = userService.getById(1);

        return Result.success(user);
    }

    @GetMapping("/db/tx")
    public Result<Boolean> dbTx() {
        userService.tx();

        return Result.success(true);
    }

    @GetMapping("/db/test")
    public Result<Boolean> dbTest() {
        for (int i = 0; i < 500000; i++) {
            (new Thread(){
                @Override
                public void run() {

                    System.out.println(redisService.decr(GoodsKey.getMiaoshaGoodsStock, "0"));

                }
            }).start();
        }

        return Result.success(true);
    }

}
