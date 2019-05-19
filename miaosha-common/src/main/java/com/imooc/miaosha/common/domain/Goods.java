package com.imooc.miaosha.common.domain;

import lombok.Data;

/**
 * @author xushaopeng
 * @date 2018/10/03
 */
@Data
public class Goods {
    private Long id;
    private String goodsName;
    private String goodsTitle;
    private String goodsImg;
    private String goodsDetail;
    private Double goodsPrice;
    private Integer goodsStock;
}
