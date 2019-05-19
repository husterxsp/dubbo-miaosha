package com.imooc.miaosha.web.service;

import com.imooc.miaosha.web.dao.MiaoshaUserDao;
import com.imooc.miaosha.common.domain.miaosha.MiaoshaUser;
import com.imooc.miaosha.common.exception.GlobalException;
import com.imooc.miaosha.web.redis.RedisService;
import com.imooc.miaosha.web.redis.prefix.MiaoshaUserKey;
import com.imooc.miaosha.common.result.CodeMsg;
import com.imooc.miaosha.common.util.MD5Util;
import com.imooc.miaosha.common.util.UUIDUtil;
import com.imooc.miaosha.common.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @author xushaopeng
 * @date 2018/09/19
 */
@Service
public class MiaoshaUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    MiaoshaUserDao miaoshaUserDao;
    @Autowired
    RedisService redisService;

    public MiaoshaUser getById(long id) {
        //取缓存
        MiaoshaUser user = redisService.get(MiaoshaUserKey.getById, "" + id, MiaoshaUser.class);
        if (user != null) {
            return user;
        }
        //取数据库
        user = miaoshaUserDao.getById(id);
        if (user != null) {
            redisService.set(MiaoshaUserKey.getById, "" + id, user);
        }
        return user;
    }

    // 高性能网站设计之缓存更新的套路：http://blog.csdn.net/tTU1EvLDeLFq5btqiK/article/details/78693323
    public boolean updatePassword(String token, long id, String formPass) {
        //取user
        MiaoshaUser user = getById(id);
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //更新数据库
        MiaoshaUser toBeUpdate = new MiaoshaUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPass2DbPass(formPass, user.getSalt()));

        miaoshaUserDao.update(toBeUpdate);

        //处理缓存
        redisService.delete(MiaoshaUserKey.getById, "" + id);

        user.setPassword(toBeUpdate.getPassword());
        redisService.set(MiaoshaUserKey.token, token, user);

        return true;
    }

    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        //延长有效期
        if (user != null) {
            addCookie(response, token, user);
        }
        return user;
    }


    public MiaoshaUser login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }

        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();

        //判断手机号是否存在
        MiaoshaUser user = getById(Long.parseLong(mobile));
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }

        //验证密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPass2DbPass(formPass, saltDB);

        if (!calcPass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }

        //生成cookie
        String token = UUIDUtil.uuid();


        // 写入redis
        redisService.set(MiaoshaUserKey.token, token, user);

        System.out.println("登录");
        addCookie(response, token, user);
        return user;
    }

    private void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {

        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);

    }

    public void register(HttpServletResponse response, Map<String, String> register) {

        String mobile = register.get("mobile");
        String formPass = register.get("password");
        String nickname = register.get("nickname");

        //判断手机号是否存在
        if (getById(Long.parseLong(mobile)) != null) {
            throw new GlobalException(CodeMsg.MOBILE_ALREADY_EXIST);
        }

        MiaoshaUser user = new MiaoshaUser();

        String saltDB = "1a2b3c4d";
        String calcPass = MD5Util.formPass2DbPass(formPass, saltDB);

        user.setPassword(calcPass);
        user.setSalt(saltDB);
        user.setId(Long.valueOf(mobile));
        user.setNickname(nickname);
        user.setRegisterDate(new Date());

        miaoshaUserDao.insert(user);

    }

    // 仅用于jmeter测试，批量生成token
    public String createToken(HttpServletResponse response, LoginVo loginVo) {

        String mobile = loginVo.getMobile();

        MiaoshaUser user = getById(Long.valueOf(mobile));

        String token = UUIDUtil.uuid();

        return token;
    }
}
