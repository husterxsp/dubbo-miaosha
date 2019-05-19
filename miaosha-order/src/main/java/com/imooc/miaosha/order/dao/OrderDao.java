package com.imooc.miaosha.order.dao;

import com.imooc.miaosha.common.domain.Order;
import com.imooc.miaosha.common.domain.miaosha.MiaoshaOrder;
import com.imooc.miaosha.common.domain.miaosha.MiaoshaUser;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * @author xushaopeng
 * @date 2018/10/03
 */
@Component
@Mapper
public interface OrderDao {

    @Select("select * from miaosha_order where user_id=#{userId} and goods_id=#{goodsId}")
    MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);

    /**
     * SelectKey 用于设置返回值
     */
    @Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date)values("
            + "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createDate} )")
    @SelectKey(keyColumn = "id", keyProperty = "id", resultType = long.class, before = false, statement = "select last_insert_id()")
    long insert(Order order);

    @Insert("insert into miaosha_order (user_id, goods_id, order_id) values(#{userId}, #{goodsId}, #{orderId})")
    int inertMiaoshaOrder(MiaoshaOrder miaoshaOrder);

    @Select("select * from order_info where id = #{orderId}")
    Order getOrderById(@Param("orderId") long orderId);

    @Select("select * from miaosha_order,order_info where (miaosha_order.user_id = #{id} and miaosha_order.order_id = order_info.id)")
    ArrayList<Order> getOrderByUser(MiaoshaUser user);

    // 删除所有数据
    @Delete("delete from order_info")
    void deleteOrders();

    @Delete("delete from miaosha_order")
    void deleteMiaoshaOrders();
}
