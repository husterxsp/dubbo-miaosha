package com.imooc.miaosha.web.rocketmq;

import com.imooc.miaosha.common.domain.miaosha.MiaoshaUser;
import lombok.Data;

/**
 * @author xushaopeng
 * @date 2018/10/06
 */
@Data
public class MiaoshaMessage {

    MiaoshaUser user;

    long goodsId;

}
