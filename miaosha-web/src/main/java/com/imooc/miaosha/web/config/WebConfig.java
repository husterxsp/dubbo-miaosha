package com.imooc.miaosha.web.config;

import com.imooc.miaosha.web.access.AccessInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @author xushaopeng
 * @date 2018/10/02
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    UserArgumentResolver userArgumentResolver;

    @Autowired
    AccessInterceptor accessInterceptor;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);

        argumentResolvers.add(userArgumentResolver);

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(accessInterceptor);
    }

    /**
     * 允许跨域
     * 参考：https://spring.io/guides/gs/rest-service-cors/
     * 可以像如下代码这样设置全局的。
     * 也可以在RestController 注解处设置 @CrossOrigin(origins = "*")
     */

    // 因为前端有使用 withCredentials: true 来设置cookie
    // 所以注意设置 allowCredentials
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
//                .allowedOrigins("http://127.0.0.1:8050/", "http://127.0.0.1:8080/")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
