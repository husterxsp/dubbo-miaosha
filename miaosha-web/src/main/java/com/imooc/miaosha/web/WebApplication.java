package com.imooc.miaosha.web;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author xushaopeng
 * @date 2019/05/18
 */
@SpringBootApplication(scanBasePackages = "com.imooc.miaosha.web")
@EnableDiscoveryClient
@EnableDubbo(scanBasePackages = "com.imooc.miaosha.web")
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

}
