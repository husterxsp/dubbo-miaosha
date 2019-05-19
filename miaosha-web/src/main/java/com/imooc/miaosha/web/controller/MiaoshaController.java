package com.imooc.miaosha.web.controller;

import com.imooc.miaosha.web.access.AccessLimit;
import com.imooc.miaosha.common.domain.miaosha.MiaoshaOrder;
import com.imooc.miaosha.common.domain.miaosha.MiaoshaUser;
import com.imooc.miaosha.web.rocketmq.MiaoshaMessage;
import com.imooc.miaosha.web.redis.RedisService;
import com.imooc.miaosha.web.redis.prefix.GoodsKey;
import com.imooc.miaosha.web.redis.prefix.MiaoshaKey;
import com.imooc.miaosha.common.result.CodeMsg;
import com.imooc.miaosha.common.result.Result;
import com.imooc.miaosha.web.rocketmq.RocketMQSender;
import com.imooc.miaosha.web.service.GoodsService;
import com.imooc.miaosha.web.service.MiaoshaService;
import com.imooc.miaosha.web.service.MiaoshaUserService;
import com.imooc.miaosha.web.service.OrderService;
import com.imooc.miaosha.common.util.UUIDUtil;
import com.imooc.miaosha.common.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.List;

/**
 * @author xushaopeng
 * @date 2018/10/03
 */
//@CrossOrigin(origins = "http://127.0.0.1:8050", allowCredentials="true")
@RestController
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {

    private static Logger log = LoggerFactory.getLogger(MiaoshaController.class);

    @Autowired
    MiaoshaUserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @Autowired
    RocketMQSender mqSender;

    /**
     * 开始秒杀
     */
    @AccessLimit(seconds = 5, maxCount = 5, needLogin = true)
//    @RequestMapping("/{path}")
    @RequestMapping("/")
//    public Result<Integer> miaosha(MiaoshaUser user,
//                                   @RequestParam("goodsId") long goodsId,
//                                   @PathVariable("path") String path) {

                public Result<Integer> miaosha(MiaoshaUser user,
                                   @RequestParam("goodsId") long goodsId ){


            log.info("秒杀请求");

        // 全局拦截器判断登录
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        //验证path
//        boolean check = miaoshaService.checkPath(user, goodsId, path);
//        if (!check) {
//            return Result.error(CodeMsg.REQUEST_ILLEGAL);
//        }

        // 判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }

        // redis预减库存
        long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, String.valueOf(goodsId));
        if (stock < 0) {
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        // 入队
        log.info("加入消息队列");

        MiaoshaMessage message = new MiaoshaMessage();

        message.setGoodsId(goodsId);

        message.setUser(user);


        mqSender.sendMiaoshaMessage(message);

        // 排队中
        return Result.success(0);
    }

    @AccessLimit(seconds = 5, maxCount = 5, needLogin = true)
    @RequestMapping(value = "/path", method = RequestMethod.GET)
    public Result<String> getMiaoshaPath(HttpServletRequest request, MiaoshaUser user,
                                         @RequestParam("goodsId") long goodsId,
                                         @RequestParam(value = "verifyCode", defaultValue = "0") int verifyCode) {

        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

//        // 查询访问次数
//        String url = request.getRequestURI();
//        String key = url + "_" + user.getId();
//        Integer count = redisService.get(AccessKey.access, key, Integer.class);
//        if (count == null) {
//            redisService.set(AccessKey.access, key, 1);
//        } else if (count < 5) {
//            redisService.incr(AccessKey.access, key);
//        } else {
//            return Result.error(CodeMsg.ACCESS_LIMIT_REACHED);
//        }

        boolean check = miaoshaService.checkVerifyCode(user, goodsId, verifyCode);
        if (!check) {
            return Result.error(CodeMsg.VERTIFYCODE_ILLEGAL);
        }

        String path = UUIDUtil.uuid();

        redisService.set(MiaoshaKey.getMiaoshaPath, user.getId() + "_" + goodsId, path);

        return Result.success(path);
    }

    @AccessLimit(seconds = 5, maxCount = 5, needLogin = true)
    @RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
    public Result<String> getMiaoshaVerifyCod(HttpServletResponse response, MiaoshaUser user,
                                              @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        try {
            BufferedImage image = miaoshaService.createVerifyCode(user, goodsId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(CodeMsg.MIAOSHA_FAIL);
        }
    }

    /**
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     */
    @AccessLimit(needLogin = true)
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(MiaoshaUser user, @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result = miaoshaService.getMiaoshaResult(user.getId(), goodsId);
        return Result.success(result);
    }

    /**
     * 初始化后回调
     * 初始时把商品列表都加入redis中缓存
     */
    @Override
    public void afterPropertiesSet() {
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        if (goodsVoList == null) {
            return;
        }

        for (GoodsVo goods : goodsVoList) {
            redisService.set(GoodsKey.getMiaoshaGoodsStock, String.valueOf(goods.getId()), goods.getStockCount());
        }

    }
}
