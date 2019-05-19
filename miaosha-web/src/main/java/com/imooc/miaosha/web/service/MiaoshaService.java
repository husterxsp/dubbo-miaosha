package com.imooc.miaosha.web.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.imooc.miaosha.common.domain.Order;
import com.imooc.miaosha.common.domain.miaosha.MiaoshaOrder;
import com.imooc.miaosha.common.domain.miaosha.MiaoshaUser;
import com.imooc.miaosha.common.dubbo.GoodsDubboService;
import com.imooc.miaosha.common.dubbo.OrderDubboService;
import com.imooc.miaosha.common.enums.RspStatusEnum;
import com.imooc.miaosha.common.exception.DefaultException;
import com.imooc.miaosha.common.response.ObjectResponse;
import com.imooc.miaosha.common.vo.GoodsVo;
import com.imooc.miaosha.web.redis.RedisService;
import com.imooc.miaosha.web.redis.prefix.MiaoshaKey;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @author xushaopeng
 * @date 2018/10/03
 */
@Service
public class MiaoshaService {

    private static char[] ops = new char[]{'+', '-', '*'};
    @Autowired
    GoodsService goodsService;


    @Autowired
    OrderService orderService;

    @Reference(version = "1.0.0")
    OrderDubboService orderDubboService;

    @Reference(version = "1.0.0")
    GoodsDubboService goodsDubboService;

    @Autowired
    RedisService redisService;

    private static int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer) engine.eval(exp);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // @Transactional
    @GlobalTransactional(timeoutMills = 300000, name = "dubbo-miaosha-web-example")
    public Order miaosha(MiaoshaUser user, GoodsVo goods) {

        System.out.println("开始全局事务，XID = " + RootContext.getXID());

        // 减库存 下订单 写入秒杀订单
//        boolean success = goodsService.reduceStock(goods);

        ObjectResponse<Object> goodsObjectResponse = goodsDubboService.decreaseStorage(goods);

        ObjectResponse<Order> orderObjectResponse = orderDubboService.createOrder(user, goods);

        if (goodsObjectResponse.getStatus() != 200 || orderObjectResponse.getStatus() != 200) {
            throw new DefaultException(RspStatusEnum.FAIL);
        }

        return null;
    }

    public long getMiaoshaResult(Long userId, long goodsId) {
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
        if (order != null) {
            //秒杀成功
            return order.getOrderId();
        } else {
//            boolean isOver = getGoodsOver(goodsId);
//            if (isOver) {
//                // 商品已经秒杀完了
//                return -1;
//            } else {
//                // 还在排队中
//                return 0;
//            }
        }
        return 0;
    }

    public boolean checkPath(MiaoshaUser user, long goodsId, String path) {
        if (user == null || path == null) {
            return false;
        }
        String pathOld = redisService.get(MiaoshaKey.getMiaoshaPath, user.getId() + "_" + goodsId, String.class);

        return path.equals(pathOld);
    }

    public BufferedImage createVerifyCode(MiaoshaUser user, long goodsId) {
        if (user == null || goodsId <= 0) {
            return null;
        }
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 20));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int rnd = calc(verifyCode);
        redisService.set(MiaoshaKey.getMiaoshaVerifyCode, user.getId() + "_" + goodsId, rnd);
        //输出图片
        return image;
    }

    /**
     * + - *
     */
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = "" + num1 + op1 + num2 + op2 + num3;
        return exp;
    }

    public boolean checkVerifyCode(MiaoshaUser user, long goodsId, int verifyCode) {
        if (user == null || goodsId <= 0) {
            return false;
        }
        Integer codeOld = redisService.get(MiaoshaKey.getMiaoshaVerifyCode, user.getId() + "_" + goodsId, Integer.class);
        if (codeOld == null || codeOld - verifyCode != 0) {
            return false;
        }

        redisService.delete(MiaoshaKey.getMiaoshaVerifyCode, user.getId() + "_" + goodsId);

        return true;
    }
}
