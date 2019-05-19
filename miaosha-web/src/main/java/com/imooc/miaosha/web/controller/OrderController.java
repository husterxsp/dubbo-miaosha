package com.imooc.miaosha.web.controller;

import com.imooc.miaosha.web.access.AccessLimit;
import com.imooc.miaosha.common.domain.Order;
import com.imooc.miaosha.common.domain.miaosha.MiaoshaUser;
import com.imooc.miaosha.web.redis.RedisService;
import com.imooc.miaosha.common.result.CodeMsg;
import com.imooc.miaosha.common.result.Result;
import com.imooc.miaosha.web.service.GoodsService;
import com.imooc.miaosha.web.service.MiaoshaUserService;
import com.imooc.miaosha.web.service.OrderService;
import com.imooc.miaosha.common.vo.GoodsVo;
import com.imooc.miaosha.common.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * @author xushaopeng
 * @date 2018/12/09
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    MiaoshaUserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;

    @AccessLimit(needLogin = true)
    @RequestMapping("/detail")
    public Result<OrderDetailVo> detail(MiaoshaUser user, @RequestParam("orderId") long orderId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        Order order = orderService.getOrderById(orderId);

        if (order == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }

        long goodsId = order.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);

        OrderDetailVo vo = new OrderDetailVo();
        vo.setOrder(order);
        vo.setGoods(goods);

        return Result.success(vo);
    }

    @AccessLimit(needLogin = true)
    @RequestMapping("/list")
    public Result<ArrayList<Order>> list(MiaoshaUser user) {

        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        ArrayList<Order> order = orderService.getOrderByUser(user);

        return Result.success(order);
    }

}
