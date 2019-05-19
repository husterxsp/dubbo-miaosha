package com.imooc.miaosha.common.vo;

import com.imooc.miaosha.common.domain.Order;
import lombok.Data;

/**
 * @author xushaopeng
 * @date 2018/12/09
 */
@Data
public class OrderDetailVo {

    private GoodsVo goods;

    private Order order;

}
