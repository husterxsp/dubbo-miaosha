package com.imooc.miaosha.common.dubbo;


import com.imooc.miaosha.common.response.ObjectResponse;
import com.imooc.miaosha.common.vo.GoodsVo;

public interface GoodsDubboService {

    /**
     * 扣减库存
     */
    ObjectResponse<Object> decreaseStorage(GoodsVo goodsVo);
}
