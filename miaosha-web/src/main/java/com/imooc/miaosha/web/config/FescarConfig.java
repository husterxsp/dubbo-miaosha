package com.imooc.miaosha.web.config;

import io.seata.spring.annotation.GlobalTransactionScanner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xushaopeng
 * @date 2019/05/19
 */
@Configuration
public class FescarConfig {

    /**
     * init global transaction scanner
     *
     * @Return: GlobalTransactionScanner
     */
    @Bean
    public GlobalTransactionScanner globalTransactionScanner(){
        return new GlobalTransactionScanner("dubbo-miaosha-web-example", "my_test_tx_group");
    }
}
