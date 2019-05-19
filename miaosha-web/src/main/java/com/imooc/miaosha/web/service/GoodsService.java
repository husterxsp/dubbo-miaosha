package com.imooc.miaosha.web.service;

import com.imooc.miaosha.common.domain.miaosha.MiaoshaGoods;
import com.imooc.miaosha.common.vo.GoodsVo;
import com.imooc.miaosha.web.dao.GoodsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xushaopeng
 * @date 2018/10/03
 */

@Service
public class GoodsService {
    @Autowired
    GoodsDao goodsDao;

    public List<GoodsVo> listGoodsVo() {
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }


//    public boolean reduceStock(GoodsVo goods) {
//        MiaoshaGoods g = new MiaoshaGoods();
//        g.setGoodsId(goods.getId());
//        int ret = goodsDao.reduceStock(g.getGoodsId());
//        return ret > 0;
//    }

}
