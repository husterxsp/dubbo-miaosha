package com.imooc.miaosha.web.config;

import com.imooc.miaosha.web.access.UserContext;
import com.imooc.miaosha.common.domain.miaosha.MiaoshaUser;
import com.imooc.miaosha.web.service.MiaoshaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author xushaopeng
 * @date 2018/10/02
 */
// 这里定义的是全局拦截器，处理所有的参数，用户信息可以放在这里处理，也可以放在定义的拦截器里处理（com.imooc.miaosha.access）
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    MiaoshaUserService miaoshaUserService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        Class clazz = parameter.getParameterType();

        // 满足条件才会执行下面的处理函数
        return clazz == MiaoshaUser.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
//        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
//
//        String paramToken = request.getParameter(MiaoshaUserService.COOKIE_NAME_TOKEN);
//        String cookieToken = getCookieValue(request, MiaoshaUserService.COOKIE_NAME_TOKEN);
//
//        if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
//            return null;
//        }
//        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
//        return miaoshaUserService.getByToken(response, token);


        // com.imooc.miaosha.access 的拦截器先执行，这里后执行，先设置了UserContext，这里就直接取了。
        return UserContext.getUser();

    }

//    private String getCookieValue(HttpServletRequest request, String cookiName) {
//        Cookie[]  cookies = request.getCookies();
//        if (cookies == null || cookies.length <= 0) {
//            return null;
//        }
//
//        for(Cookie cookie : cookies) {
//            if(cookie.getName().equals(cookiName)) {
//                return cookie.getValue();
//            }
//        }
//        return null;
//    }
}
