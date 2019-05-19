package com.imooc.miaosha.goods.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.imooc.miaosha.common.domain.miaosha.MiaoshaGoods;
import com.imooc.miaosha.common.dubbo.GoodsDubboService;
import com.imooc.miaosha.common.enums.RspStatusEnum;
import com.imooc.miaosha.common.response.ObjectResponse;
import com.imooc.miaosha.common.vo.GoodsVo;
import com.imooc.miaosha.goods.dao.GoodsDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author xushaopeng
 * @date 2019/05/19
 */
@Service(version = "1.0.0", protocol = "${dubbo.protocol.id}",
        application = "${dubbo.application.id}", registry = "${dubbo.registry.id}",
        timeout = 3000)
public class GoodsDubboServiceImpl implements GoodsDubboService {

    @Autowired
    GoodsDao goodsDao;

    @Override
    public ObjectResponse decreaseStorage(GoodsVo goodsVo) {

        MiaoshaGoods g = new MiaoshaGoods();
        g.setGoodsId(goodsVo.getId());
        int storage = goodsDao.reduceStock(g.getGoodsId());

        ObjectResponse<Object> response = new ObjectResponse<>();
        if (storage > 0){
            response.setStatus(RspStatusEnum.SUCCESS.getCode());
            response.setMessage(RspStatusEnum.SUCCESS.getMessage());
            return response;
        }

        response.setStatus(RspStatusEnum.FAIL.getCode());
        response.setMessage(RspStatusEnum.FAIL.getMessage());
        return response;

    }

}
