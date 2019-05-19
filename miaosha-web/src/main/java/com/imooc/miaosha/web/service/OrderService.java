package com.imooc.miaosha.web.service;

import com.imooc.miaosha.common.domain.Order;
import com.imooc.miaosha.common.domain.miaosha.MiaoshaOrder;
import com.imooc.miaosha.common.domain.miaosha.MiaoshaUser;
import com.imooc.miaosha.web.dao.OrderDao;
import com.imooc.miaosha.web.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @author xushaopeng
 * @date 2018/10/03
 */
@Service
public class OrderService {
    @Autowired
    OrderDao orderDao;

    @Autowired
    RedisService redisService;


    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(Long userId, long goodsId) {
        return orderDao.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
    }

/**
 * createOrder 放到order模块
 * */
//    @Transactional
//    public Order createOrder(MiaoshaUser user, GoodsVo goods) {
//        Order order = new Order();
//
//        order.setCreateDate(new Date());
//        order.setDeliveryAddrId(0L);
//        order.setGoodsCount(1);
//        order.setGoodsId(goods.getId());
//        order.setGoodsName(goods.getGoodsName());
//        order.setGoodsPrice(goods.getMiaoshaPrice());
//        order.setOrderChannel(1);
//        order.setStatus(0);
//        order.setUserId(user.getId());
//
//        orderDao.insert(order);
//
//        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
//        miaoshaOrder.setGoodsId(goods.getId());
//        miaoshaOrder.setOrderId(order.getId());
//        miaoshaOrder.setUserId(user.getId());
//
//        orderDao.inertMiaoshaOrder(miaoshaOrder);
//
//        return order;
//
//    }

    public Order getOrderById(long orderId) {
        return orderDao.getOrderById(orderId);
    }

    public ArrayList<Order> getOrderByUser(MiaoshaUser user) {
        return orderDao.getOrderByUser(user);
    }

    public void deleteOrders() {
        orderDao.deleteOrders();
        orderDao.deleteMiaoshaOrders();
    }
}
