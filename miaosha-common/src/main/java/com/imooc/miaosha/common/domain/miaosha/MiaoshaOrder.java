package com.imooc.miaosha.common.domain.miaosha;

import lombok.Data;

/**
 * @author xushaopeng
 * @date 2018/10/03
 */
@Data
public class MiaoshaOrder {
    private Long id;
    private Long userId;
    private Long orderId;
    private Long goodsId;
}
