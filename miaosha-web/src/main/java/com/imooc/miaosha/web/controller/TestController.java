package com.imooc.miaosha.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.imooc.miaosha.common.domain.miaosha.MiaoshaUser;
import com.imooc.miaosha.common.dubbo.OrderDubboService;
import com.imooc.miaosha.common.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author xushaopeng
 * @date 2019/05/18
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @Reference(version = "1.0.0")
    OrderDubboService orderDubboService;

    /**
     * 模拟用户购买商品下单业务逻辑流程
     * @Param:
     * @Return:
     */
    @GetMapping("/buy")
    void handleBusiness(){

        log.info("orderDubboService");

        orderDubboService.createOrder(new MiaoshaUser(), new GoodsVo());

    }
}