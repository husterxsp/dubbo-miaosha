package com.imooc.miaosha.web.rocketmq;

import com.imooc.miaosha.web.redis.RedisService;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @author xushaopeng
 * @date 2018/10/05
 */
@Service
public class RocketMQSender {
    private static Logger log = LoggerFactory.getLogger(RocketMQSender.class);

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    public void sendMiaoshaMessage(MiaoshaMessage message) {
        String msg = RedisService.bean2String(message);
        log.info("发送消息:" + msg);
//        rocketMQTemplate.syncSend(springTopic, "Hello, World!");
        rocketMQTemplate.syncSend(MQConfig.MIAOSHA_QUEUE, msg);
    }

}
