package com.imooc.miaosha.web.access;

import com.alibaba.fastjson.JSON;
import com.imooc.miaosha.common.domain.miaosha.MiaoshaUser;
import com.imooc.miaosha.common.result.Result;
import com.imooc.miaosha.web.redis.RedisService;
import com.imooc.miaosha.web.redis.prefix.AccessKey;
import com.imooc.miaosha.common.result.CodeMsg;
import com.imooc.miaosha.common.result.Result;
import com.imooc.miaosha.web.service.MiaoshaUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;


/**
 * @author xushaopeng
 * @date 2018/12/23
 */

@CrossOrigin(origins = "http://127.0.0.1:8050", allowCredentials="true")
@Service

public class AccessInterceptor extends HandlerInterceptorAdapter {
    // 自定义拦截器，类似AOP

    @Autowired
    MiaoshaUserService userService;

    @Autowired
    RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);

            if (accessLimit == null) {
                return true;
            }

            MiaoshaUser user = getUser(request, response);
            UserContext.setUser(user);

            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();

            String key = request.getRequestURI();

            if (needLogin) {
                if (user == null) {
                    addCorsHeader(response);
                    render(response, CodeMsg.SESSION_ERROR);
                    return false;
                }
                key += "_" + user.getId();
            }
            if (seconds == -1 || maxCount == -1) {
                return true;
            }

            AccessKey ak = AccessKey.withExpire(seconds);
            Integer count = redisService.get(ak, key, Integer.class);

            if (count == null) {
                redisService.set(ak, key, 1);
            } else if (count < maxCount) {
                System.out.println(count);
                redisService.incr(ak, key);
            } else {
                System.out.println(count);
                addCorsHeader(response);
                render(response, CodeMsg.ACCESS_LIMIT_REACHED);
                return false;
            }

        }

        return true;
    }

    // https://stackoverflow.com/questions/16351849/origin-is-not-allowed-by-access-control-allow-origin-how-to-enable-cors-using
    private void addCorsHeader(HttpServletResponse response){
        //TODO: externalize the Allow-Origin
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8050");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        response.addHeader("Access-Control-Max-Age", "1728000");
    }

    private void render(HttpServletResponse response, CodeMsg cm) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        OutputStream out = response.getOutputStream();
        String str = JSON.toJSONString(Result.error(cm));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }

    private MiaoshaUser getUser(HttpServletRequest request, HttpServletResponse response) {
        String paramToken = request.getParameter(MiaoshaUserService.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request, MiaoshaUserService.COOKIE_NAME_TOKEN);
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return null;
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        return userService.getByToken(response, token);
    }

    private String getCookieValue(HttpServletRequest request, String cookiName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length <= 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookiName)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
