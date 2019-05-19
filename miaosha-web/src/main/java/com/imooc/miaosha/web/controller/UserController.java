package com.imooc.miaosha.web.controller;

import com.imooc.miaosha.web.access.AccessLimit;
import com.imooc.miaosha.common.domain.miaosha.MiaoshaUser;
import com.imooc.miaosha.web.redis.RedisService;
import com.imooc.miaosha.web.redis.prefix.MiaoshaUserKey;
import com.imooc.miaosha.common.result.Result;
import com.imooc.miaosha.web.service.MiaoshaUserService;
import com.imooc.miaosha.common.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

/**
 * @author xushaopeng
 * @date 2018/10/03
 */

//@CrossOrigin(origins = "http://127.0.0.1:8050", allowCredentials="true")
@RestController
public class UserController {

    private static Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    MiaoshaUserService miaoshaUserService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/test")
    public Result<String> test(HttpServletResponse response) {

        Cookie cookie = new Cookie("1", "1");
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
//        cookie.setDomain("127.0.0.1");
        response.addCookie(cookie);

        return Result.success("");
    }

    @AccessLimit(needLogin = true)
    @RequestMapping("/info")
    public Result<MiaoshaUser> info(MiaoshaUser user) {

        return Result.success(user);
    }

    @PostMapping("/register")
    public Result<String> register(HttpServletResponse response, @RequestBody Map<String, String> user) {

        log.info("register: " + user.toString());

        miaoshaUserService.register(response, user);

        return Result.success("");
    }

    // @Valid JSR303 标准
    // 这里 @Valid 和 LoginVo里的NotNull等注解，以及GlobalExceptionHandler一起提供检查
    @PostMapping("/login")
    public Result<MiaoshaUser> login(HttpServletResponse response, @Valid LoginVo loginVo) {
        // VO value object
        // 使用这种格式，前端用form-data的格式？

        log.info("login: " + loginVo.toString());

        MiaoshaUser user = miaoshaUserService.login(response, loginVo);

        // 登录成功，返回用户信息
        return Result.success(user);

    }

    @PostMapping("/create_token")
    public Result<String> create_token(HttpServletResponse response, @Valid LoginVo loginVo) {

        log.info(loginVo.toString());
        String token = miaoshaUserService.createToken(response, loginVo);
        return Result.success(token);

    }
}