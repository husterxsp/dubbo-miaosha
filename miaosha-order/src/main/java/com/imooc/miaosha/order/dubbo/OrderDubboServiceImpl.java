package com.imooc.miaosha.order.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.imooc.miaosha.common.domain.Order;
import com.imooc.miaosha.common.domain.miaosha.MiaoshaOrder;
import com.imooc.miaosha.common.domain.miaosha.MiaoshaUser;
import com.imooc.miaosha.common.dubbo.OrderDubboService;
import com.imooc.miaosha.common.enums.RspStatusEnum;
import com.imooc.miaosha.common.response.ObjectResponse;
import com.imooc.miaosha.common.vo.GoodsVo;
import com.imooc.miaosha.order.dao.OrderDao;
import io.seata.core.context.RootContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author xushaopeng
 * @date 2019/05/17
 */
@Service(version = "1.0.0", protocol = "${dubbo.protocol.id}",
        application = "${dubbo.application.id}", registry = "${dubbo.registry.id}",
        timeout = 3000)
public class OrderDubboServiceImpl implements OrderDubboService {

    @Autowired
    OrderDao orderDao;

    boolean flag = true;

    @Override
    public ObjectResponse<Order> createOrder(MiaoshaUser user, GoodsVo goods) {

        System.out.println("全局事务id ：" + RootContext.getXID());

        ObjectResponse<Order> response = new ObjectResponse<>();

        Order order = new Order();

        order.setCreateDate(new Date());
        order.setDeliveryAddrId(0L);
        order.setGoodsCount(1);
        order.setGoodsId(goods.getId());
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsPrice(goods.getMiaoshaPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setUserId(user.getId());

        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goods.getId());
        miaoshaOrder.setUserId(user.getId());

        // 参考seata example 代码
        try {
            orderDao.insert(order);

            miaoshaOrder.setOrderId(order.getId());

            orderDao.inertMiaoshaOrder(miaoshaOrder);
        } catch (Exception e) {
            response.setStatus(RspStatusEnum.FAIL.getCode());
            response.setMessage(RspStatusEnum.FAIL.getMessage());
            return response;
        }


        if (flag) {
            throw new RuntimeException("测试抛异常后，分布式事务回滚！");
        }

        response.setStatus(RspStatusEnum.SUCCESS.getCode());
        response.setMessage(RspStatusEnum.SUCCESS.getMessage());
        return response;
    }
}
