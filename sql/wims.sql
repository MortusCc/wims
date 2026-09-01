-- ============================================================
-- 仓库库存管理系统（WIMS）数据库初始化脚本
-- 设计依据：指导书 1.2.1.5（微服务一库一域）：
--   product    商品管理  wims_product
--   inbound    入库管理  wims_inbound
--   outbound   出库管理  wims_outbound
--   inventory  库存查询  wims_inventory
--   alert      库存预警  wims_alert
--   statistics 库存统计  wims_statistics
-- 适用：MySQL 8.0
-- 说明：示例数据为仓储业务示例（生活日用品），并埋入低于安全库存的商品，
--       用于后续库存预警、统计演示
-- ============================================================

-- ---------- 1. 商品库 wims_product ----------
CREATE DATABASE IF NOT EXISTS `wims_product`
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;
USE `wims_product`;
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_code` varchar(50) NOT NULL COMMENT '商品编码（唯一）',
  `product_name` varchar(100) NOT NULL COMMENT '商品名称',
  `spec` varchar(100) DEFAULT NULL COMMENT '规格描述，如 24瓶/箱',
  `unit` varchar(20) DEFAULT NULL COMMENT '计量单位，如 箱/包/袋/桶/瓶',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_product_code` (`product_code`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `product` (`product_code`,`product_name`,`spec`,`unit`) VALUES ('SP0001','矿泉水','24瓶/箱','箱');
INSERT INTO `product` (`product_code`,`product_name`,`spec`,`unit`) VALUES ('SP0002','抽纸','3层120抽/包','包');
INSERT INTO `product` (`product_code`,`product_name`,`spec`,`unit`) VALUES ('SP0003','大米','10kg/袋','袋');
INSERT INTO `product` (`product_code`,`product_name`,`spec`,`unit`) VALUES ('SP0004','食用油','5L/桶','桶');
INSERT INTO `product` (`product_code`,`product_name`,`spec`,`unit`) VALUES ('SP0005','洗衣液','2kg/瓶','瓶');

-- ---------- 2. 库存库 wims_inventory ----------
CREATE DATABASE IF NOT EXISTS `wims_inventory`
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;
USE `wims_inventory`;
DROP TABLE IF EXISTS `stock`;
CREATE TABLE `stock` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL COMMENT '商品id（对应 wims_product.product.id）',
  `product_name` varchar(100) DEFAULT NULL COMMENT '商品名称（冗余，便于查询展示）',
  `quantity` int NOT NULL DEFAULT 0 COMMENT '当前库存数量',
  `safe_stock` int NOT NULL DEFAULT 0 COMMENT '安全库存阈值：低于该数量触发“库存不足”预警，高于某上限可触发“库存过剩”',
  `update_time` datetime DEFAULT NULL COMMENT '最近更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_product_id` (`product_id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;

-- 示例：抽纸(2)与洗衣液(5)库存低于安全阈值，用于预警演示
INSERT INTO `stock` (`product_id`,`product_name`,`quantity`,`safe_stock`,`update_time`) VALUES (1,'矿泉水',400,100,NOW());
INSERT INTO `stock` (`product_id`,`product_name`,`quantity`,`safe_stock`,`update_time`) VALUES (2,'抽纸',50,200,NOW());
INSERT INTO `stock` (`product_id`,`product_name`,`quantity`,`safe_stock`,`update_time`) VALUES (3,'大米',180,100,NOW());
INSERT INTO `stock` (`product_id`,`product_name`,`quantity`,`safe_stock`,`update_time`) VALUES (4,'食用油',500,120,NOW());
INSERT INTO `stock` (`product_id`,`product_name`,`quantity`,`safe_stock`,`update_time`) VALUES (5,'洗衣液',20,50,NOW());

-- ---------- 3. 入库库 wims_inbound ----------
CREATE DATABASE IF NOT EXISTS `wims_inbound`
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;
USE `wims_inbound`;
DROP TABLE IF EXISTS `inbound_record`;
CREATE TABLE `inbound_record` (
  `id` int NOT NULL AUTO_INCREMENT,
  `bill_no` varchar(32) NOT NULL COMMENT '入库单号',
  `product_id` int NOT NULL COMMENT '商品id',
  `product_name` varchar(100) DEFAULT NULL COMMENT '商品名称（快照）',
  `quantity` int NOT NULL COMMENT '入库数量',
  `operator` varchar(50) DEFAULT NULL COMMENT '经办人',
  `inbound_time` datetime DEFAULT NULL COMMENT '入库时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_bill_no` (`bill_no`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `inbound_record` (`bill_no`,`product_id`,`product_name`,`quantity`,`operator`,`inbound_time`) VALUES ('RK20260801001',1,'矿泉水',300,'张伟',NOW());
INSERT INTO `inbound_record` (`bill_no`,`product_id`,`product_name`,`quantity`,`operator`,`inbound_time`) VALUES ('RK20260801002',4,'食用油',500,'张伟',NOW());
INSERT INTO `inbound_record` (`bill_no`,`product_id`,`product_name`,`quantity`,`operator`,`inbound_time`) VALUES ('RK20260802001',2,'抽纸',80,'李娜',NOW());

-- ---------- 4. 出库库 wims_outbound ----------
CREATE DATABASE IF NOT EXISTS `wims_outbound`
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;
USE `wims_outbound`;
DROP TABLE IF EXISTS `outbound_record`;
CREATE TABLE `outbound_record` (
  `id` int NOT NULL AUTO_INCREMENT,
  `bill_no` varchar(32) NOT NULL COMMENT '出库单号',
  `product_id` int NOT NULL COMMENT '商品id',
  `product_name` varchar(100) DEFAULT NULL COMMENT '商品名称（快照）',
  `quantity` int NOT NULL COMMENT '出库数量',
  `operator` varchar(50) DEFAULT NULL COMMENT '经办人',
  `outbound_time` datetime DEFAULT NULL COMMENT '出库时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_bill_no` (`bill_no`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `outbound_record` (`bill_no`,`product_id`,`product_name`,`quantity`,`operator`,`outbound_time`) VALUES ('CK20260801001',1,'矿泉水',100,'王强',NOW());
INSERT INTO `outbound_record` (`bill_no`,`product_id`,`product_name`,`quantity`,`operator`,`outbound_time`) VALUES ('CK20260801002',3,'大米',120,'王强',NOW());
INSERT INTO `outbound_record` (`bill_no`,`product_id`,`product_name`,`quantity`,`operator`,`outbound_time`) VALUES ('CK20260802001',4,'食用油',200,'赵敏',NOW());

-- ---------- 5. 预警库 wims_alert ----------
CREATE DATABASE IF NOT EXISTS `wims_alert`
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;
USE `wims_alert`;
DROP TABLE IF EXISTS `alert_record`;
CREATE TABLE `alert_record` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL COMMENT '商品id',
  `product_name` varchar(100) DEFAULT NULL COMMENT '商品名称',
  `alert_type` varchar(20) NOT NULL COMMENT '预警类型：LOW=库存不足(低于安全库存) HIGH=库存过剩',
  `cur_quantity` int DEFAULT NULL COMMENT '触发预警时的库存数量',
  `safe_stock` int DEFAULT NULL COMMENT '当时的安全库存阈值',
  `alert_time` datetime DEFAULT NULL COMMENT '预警时间',
  `status` int NOT NULL DEFAULT 0 COMMENT '处理状态：0=未处理 1=已处理',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `alert_record` (`product_id`,`product_name`,`alert_type`,`cur_quantity`,`safe_stock`,`alert_time`,`status`) VALUES (2,'抽纸','LOW',50,200,NOW(),0);
INSERT INTO `alert_record` (`product_id`,`product_name`,`alert_type`,`cur_quantity`,`safe_stock`,`alert_time`,`status`) VALUES (5,'洗衣液','LOW',20,50,NOW(),0);

-- ---------- 6. 统计库 wims_statistics ----------
CREATE DATABASE IF NOT EXISTS `wims_statistics`
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;
USE `wims_statistics`;
DROP TABLE IF EXISTS `daily_summary`;
CREATE TABLE `daily_summary` (
  `id` int NOT NULL AUTO_INCREMENT,
  `stat_date` date NOT NULL COMMENT '统计日期',
  `product_id` int NOT NULL COMMENT '商品id',
  `product_name` varchar(100) DEFAULT NULL COMMENT '商品名称',
  `in_qty` int NOT NULL DEFAULT 0 COMMENT '当日入库数量',
  `out_qty` int NOT NULL DEFAULT 0 COMMENT '当日出库数量',
  `stock_qty` int NOT NULL DEFAULT 0 COMMENT '当日库存数量',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_date_product` (`stat_date`,`product_id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `daily_summary` (`stat_date`,`product_id`,`product_name`,`in_qty`,`out_qty`,`stock_qty`) VALUES ('2026-08-01',1,'矿泉水',300,100,400);
INSERT INTO `daily_summary` (`stat_date`,`product_id`,`product_name`,`in_qty`,`out_qty`,`stock_qty`) VALUES ('2026-08-01',3,'大米',0,120,180);
