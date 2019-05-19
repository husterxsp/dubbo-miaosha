package com.imooc.miaosha.goods;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author xushaopeng
 * @date 2019/05/19
 */
@SpringBootApplication(scanBasePackages = "com.imooc.miaosha.goods")
@EnableDiscoveryClient
//@MapperScan({"com.imooc.miaosha.order.mapper"})
@EnableDubbo(scanBasePackages = "com.imooc.miaosha.goods")
public class GoodsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class, args);
    }

}
