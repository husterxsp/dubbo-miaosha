package com.imooc.miaosha.common.vo;

import com.imooc.miaosha.common.domain.Goods;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xushaopeng
 * @date 2018/10/03
 */
@Data
public class GoodsVo extends Goods implements Serializable {

    private Double miaoshaPrice;

    private Integer stockCount;

    private Date startDate;

    private Date endDate;

    @Override
    public String toString() {
        return "GoodsVo{" +
                "miaoshaPrice=" + miaoshaPrice +
                ", stockCount=" + stockCount +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                "} " + super.toString();
    }
}