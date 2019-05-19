### 分布式电商项目

主要用到的技术：

- Redis：主要是做缓存，分布式session。
- RocketMQ：消息队列，解耦、异步、削峰。
- seata：分布式事务。
- dubbo：分布式RPC调用。 



### 运行

加载sql/miaosha.sql文件

启动 redis

启动 RocketMQ：`nameserver`、`broker`

启动seata

启动注册中心nacos

启动各个模块：商品（goods）、订单（order）、业务逻辑（web）

启动前端

```
$ cd fe
$ npm install
$ npm run serve
```

访问前端页面  http://127.0.0.1:9000/


### 参考

- http://coding.imooc.com/class/168.html
- https://github.com/qiurunze123/miaosha
- https://github.com/seata/seata-samples/tree/master/springboot-dubbo-seata
