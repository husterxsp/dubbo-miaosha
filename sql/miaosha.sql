# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.17)
# Database: miaosha
# Generation Time: 2019-02-24 14:25:43 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table goods
# ------------------------------------------------------------

DROP TABLE IF EXISTS `goods`;

CREATE TABLE `goods` (
  `id` bigint(20) NOT NULL,
  `goods_name` varchar(30) DEFAULT NULL,
  `goods_title` varchar(64) DEFAULT NULL,
  `goods_img` varchar(64) DEFAULT NULL,
  `goods_detail` longtext,
  `goods_price` decimal(10,2) DEFAULT NULL,
  `goods_stock` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `goods` WRITE;
/*!40000 ALTER TABLE `goods` DISABLE KEYS */;

INSERT INTO `goods` (`id`, `goods_name`, `goods_title`, `goods_img`, `goods_detail`, `goods_price`, `goods_stock`)
VALUES
	(1,'iphoneX','Apple/苹果iPhone X 全网通4G手机苹果X 10','/img/iphonex.png','Apple/苹果iPhone X 全网通4G手机苹果X 10',7788.00,100),
	(2,'华为 P20 PRO','Huawei/华为 P20 PRO全网通4G智能手机','/img/p20pro.png','Huawei/华为 P20 PRO 8G+256G 全网通4G智能手机',5299.00,50),
	(3,'荣耀9i','Huawei/荣耀9i','/img/荣耀9i.jpg','Huawei/荣耀9i',999.00,9999),
	(4,'魅族PRO7','meizu/魅族PRO7','/img/魅族PRO7.jpg','魅族PRO7',1599.00,200);

/*!40000 ALTER TABLE `goods` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table miaosha_goods
# ------------------------------------------------------------

DROP TABLE IF EXISTS `miaosha_goods`;

CREATE TABLE `miaosha_goods` (
  `id` bigint(20) NOT NULL,
  `goods_id` bigint(20) DEFAULT NULL,
  `miaosha_price` decimal(10,2) DEFAULT NULL,
  `stock_count` int(11) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `goods_id` (`goods_id`),
  CONSTRAINT `miaosha_goods_ibfk_1` FOREIGN KEY (`goods_id`) REFERENCES `goods` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `miaosha_goods` WRITE;
/*!40000 ALTER TABLE `miaosha_goods` DISABLE KEYS */;

INSERT INTO `miaosha_goods` (`id`, `goods_id`, `miaosha_price`, `stock_count`, `start_date`, `end_date`)
VALUES
	(1,1,0.01,89,'2018-06-13 11:05:50','2018-06-13 11:08:00'),
	(2,2,0.01,436,'2018-06-01 00:00:00','2022-10-01 22:56:15'),
	(3,3,899.00,551,'2018-06-17 23:05:28','2018-12-01 23:05:34'),
	(4,4,1333.00,18,'2018-06-17 23:05:31','2021-08-17 23:05:42');

/*!40000 ALTER TABLE `miaosha_goods` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table miaosha_order
# ------------------------------------------------------------

DROP TABLE IF EXISTS `miaosha_order`;

CREATE TABLE `miaosha_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `goods_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_uid_gid` (`user_id`,`goods_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table miaosha_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `miaosha_user`;

CREATE TABLE `miaosha_user` (
  `id` bigint(20) NOT NULL COMMENT '用户手机号码',
  `nickname` varchar(255) DEFAULT '',
  `password` varchar(32) DEFAULT NULL COMMENT 'md5(md5(pass明文+固定salt)+salt)',
  `salt` varchar(10) DEFAULT '',
  `head` varchar(128) DEFAULT NULL COMMENT '头像，云存储的ID',
  `register_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '注册时间',
  `last_login_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '上次登陆时间',
  `login_count` int(11) DEFAULT '0' COMMENT '登陆次数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table order_info
# ------------------------------------------------------------

DROP TABLE IF EXISTS `order_info`;

CREATE TABLE `order_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `goods_id` bigint(20) DEFAULT NULL,
  `delivery_addr_id` bigint(20) DEFAULT NULL COMMENT '收获地址',
  `goods_name` varchar(30) DEFAULT NULL COMMENT '冗余过来的商品名称',
  `goods_count` int(11) DEFAULT NULL COMMENT 's数量',
  `goods_price` decimal(10,2) DEFAULT NULL,
  `order_channel` tinyint(4) DEFAULT NULL COMMENT '订单渠道，1在线，2android，3ios',
  `status` tinyint(4) DEFAULT NULL COMMENT '订单状态，0新建未支付，1已支付，2已发货，3已收货，4已退款，5已完成',
  `create_date` datetime DEFAULT NULL,
  `pay_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `order_info` WRITE;
/*!40000 ALTER TABLE `order_info` DISABLE KEYS */;

INSERT INTO `order_info` (`id`, `user_id`, `goods_id`, `delivery_addr_id`, `goods_name`, `goods_count`, `goods_price`, `order_channel`, `status`, `create_date`, `pay_date`)
VALUES
	(25170,15600853611,2,NULL,'华为 P20 PRO',1,0.01,1,0,'2019-02-24 08:21:30',NULL);

/*!40000 ALTER TABLE `order_info` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;


# ------------------------- seata -------------------------

DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log` (
                          `id` bigint(20) NOT NULL AUTO_INCREMENT,
                          `branch_id` bigint(20) NOT NULL,
                          `xid` varchar(100) NOT NULL,
                          `rollback_info` longblob NOT NULL,
                          `log_status` int(11) NOT NULL,
                          `log_created` datetime NOT NULL,
                          `log_modified` datetime NOT NULL,
                          `ext` varchar(100) DEFAULT NULL,
                          PRIMARY KEY (`id`),
                          UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=94 DEFAULT CHARSET=utf8;