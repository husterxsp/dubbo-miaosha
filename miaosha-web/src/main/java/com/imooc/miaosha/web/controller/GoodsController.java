package com.imooc.miaosha.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.miaosha.web.redis.RedisService;
import com.imooc.miaosha.web.service.GoodsService;
import com.imooc.miaosha.web.service.MiaoshaUserService;
import com.imooc.miaosha.common.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author xushaopeng
 * @date 2018/10/02
 */
@RestController
public class GoodsController {

    private static Logger log = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    MiaoshaUserService miaoshaUserService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/list")
    public List<GoodsVo> list() {

        List<GoodsVo> goodsList = goodsService.listGoodsVo();

        return goodsList;
    }

    @RequestMapping("/detail/{goodsId}")
    public Map<String, Object> list(@PathVariable("goodsId") long goodsId) {

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0, remainSeconds = 0;
        if (now < startAt) {
            // 没开始
            miaoshaStatus = 0;
            remainSeconds = (int) (startAt - now) / 1000;
        } else if (now > endAt) {
            // 已结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {
            miaoshaStatus = 1;
            remainSeconds = 0;
        }

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> ret = mapper.convertValue(goods, Map.class);
        ret.put("miaoshaStatus", miaoshaStatus);
        ret.put("remainSeconds", remainSeconds);
        return ret;
    }
}
