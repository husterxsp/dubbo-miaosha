package com.imooc.miaosha.common.dubbo;


import com.imooc.miaosha.common.domain.Order;
import com.imooc.miaosha.common.domain.miaosha.MiaoshaUser;
import com.imooc.miaosha.common.response.ObjectResponse;
import com.imooc.miaosha.common.vo.GoodsVo;

public interface OrderDubboService {

    /**
     * 创建订单
     */
    ObjectResponse<Order> createOrder(MiaoshaUser user, GoodsVo goods);

}
