package com.imooc.miaosha.common.domain.miaosha;

import lombok.Data;

import java.util.Date;

/**
 * @author xushaopeng
 * @date 2018/10/03
 */
@Data
public class MiaoshaGoods {
    private Long id;
    private Long goodsId;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
