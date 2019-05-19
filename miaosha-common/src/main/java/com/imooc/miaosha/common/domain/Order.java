package com.imooc.miaosha.common.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author xushaopeng
 * @date 2018/10/03
 */
@Data
public class Order {
    private Long id;
    private Long userId;
    private Long goodsId;
    private Long deliveryAddrId;
    private String goodsName;
    private Integer goodsCount;
    private Double goodsPrice;
    private Integer orderChannel;
    private Integer status;
    private Date createDate;
    private Date payDate;
}
