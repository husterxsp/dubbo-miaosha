package com.imooc.miaosha.web.rocketmq;

import com.imooc.miaosha.common.domain.miaosha.MiaoshaOrder;
import com.imooc.miaosha.common.domain.miaosha.MiaoshaUser;
import com.imooc.miaosha.web.redis.RedisService;
import com.imooc.miaosha.web.service.GoodsService;
import com.imooc.miaosha.web.service.MiaoshaService;
import com.imooc.miaosha.web.service.OrderService;
import com.imooc.miaosha.common.vo.GoodsVo;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author xushaopeng
 * @date 2018/10/05
 */
@Service
@RocketMQMessageListener(topic = MQConfig.MIAOSHA_QUEUE, consumerGroup = "my-group")
public class RocketMQReceiver implements RocketMQListener<String> {

    private static Logger log = LoggerFactory.getLogger(RocketMQReceiver.class);

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @Override
    public void onMessage(String message) {

        log.info("收到消息" + message);

        MiaoshaMessage msg  = RedisService.string2Bean(message, MiaoshaMessage.class);
        MiaoshaUser user = msg.getUser();
        long goodsId = msg.getGoodsId();

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            return;
        }

        //判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return;
        }

        try {
            //减库存 下订单 写入秒杀订单
            miaoshaService.miaosha(user, goods);
        } catch (Exception e) {
            log.info("抛出异常");
            log.info(e.toString());
        }


    }

}
