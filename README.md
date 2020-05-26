```sql
CREATE TABLE `core_contract_channel_mapping` (
  `contract_id` int(11) NOT NULL COMMENT '合约ID',
  `quot_channel` varchar(64) NOT NULL COMMENT '行情数据通道',
  `index_price_listener` varchar(64) NOT NULL COMMENT '最新指数-数据监听器名称，用于计算强平',
  `fl_channel` varchar(64) NOT NULL COMMENT '强平数据通道',
  `fl_listener` varchar(64) NOT NULL COMMENT '强平数据监听器名称，用于接收强平数据',
  `price_cache_listener` varchar(64) NOT NULL COMMENT '实时数据缓存监听器名称，用户缓存最新行情',
  `stop_profit_index_listener` varchar(64) NOT NULL COMMENT '最新指数-数据监听器名称，用于计算止盈',
  `stop_profit_channel` varchar(64) NOT NULL COMMENT '止盈数据通道',
  `stop_profit_listener` varchar(64) NOT NULL COMMENT '止盈数据监听器',
  `stop_loss_index_listener` varchar(64) NOT NULL COMMENT '最新指数-数据监听器名称，用于计算止损',
  `stop_loss_channel` varchar(64) NOT NULL COMMENT '止损数据通道',
  `stop_loss_listener` varchar(64) NOT NULL COMMENT '止损数据监听器',
  `condition_order_index_listener` varchar(64) NOT NULL COMMENT '条件单指数监听器',
  `condition_order_channel` varchar(64) NOT NULL COMMENT '条件单数据通道',
  `condition_order_listener` varchar(64) NOT NULL COMMENT '条件单数据监听器',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`contract_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='合约行情数据与内部通道映射对照表';

INSERT INTO `cfd_contract_channel_mapping`(`contract_id`, `quot_channel`, `index_price_listener`, `fl_channel`, `fl_listener`, `price_cache_listener`, `stop_profit_index_listener`, `stop_profit_channel`, `stop_profit_listener`, `stop_loss_index_listener`, `stop_loss_channel`, `stop_loss_listener`, `condition_order_index_listener`, `condition_order_channel`, `condition_order_listener`, `description`, `create_time`) VALUES (1, 'btc_quot', 'btc_price_listener', 'btc_fl', 'btc_fl_listener', 'btc_cache_listener', 'btc_profit_index_listener', 'btc_profit_channel', 'btc_profit_listener', 'btc_loss_index_listener', 'btc_loss_channel', 'btc_loss_listener', 'btc_order_index_listener', 'btc_order_channel', 'btc_order_listener', 'btc通道', '2020-03-30 18:08:15');
INSERT INTO `cfd_contract_channel_mapping`(`contract_id`, `quot_channel`, `index_price_listener`, `fl_channel`, `fl_listener`, `price_cache_listener`, `stop_profit_index_listener`, `stop_profit_channel`, `stop_profit_listener`, `stop_loss_index_listener`, `stop_loss_channel`, `stop_loss_listener`, `condition_order_index_listener`, `condition_order_channel`, `condition_order_listener`, `description`, `create_time`) VALUES (2, 'eth_quot', 'eth_price_listener', 'eth_fl', 'eth_fl_listener', 'eth_cache_listener', 'eth_profit_index_listener', 'eth_profit_channel', 'eth_profit_listener', 'eth_loss_index_listener', 'eth_loss_channel', 'eth_loss_listener', 'eth_order_index_listener', 'eth_order_channel', 'eth_order_listener', 'eth通道', '2020-03-30 18:20:19');
INSERT INTO `cfd_contract_channel_mapping`(`contract_id`, `quot_channel`, `index_price_listener`, `fl_channel`, `fl_listener`, `price_cache_listener`, `stop_profit_index_listener`, `stop_profit_channel`, `stop_profit_listener`, `stop_loss_index_listener`, `stop_loss_channel`, `stop_loss_listener`, `condition_order_index_listener`, `condition_order_channel`, `condition_order_listener`, `description`, `create_time`) VALUES (3, 'xrp_quot', 'xrp_price_listener', 'xrp_fl', 'xrp_fl_listener', 'xrp_cache_listener', 'xrp_profit_index_listener', 'xrp_profit_channel', 'xrp_profit_listener', 'xrp_loss_index_listener', 'xrp_loss_channel', 'xrp_loss_listener', 'xrp_order_index_listener', 'xrp_order_channel', 'xrp_order_listener', 'xrp通道', '2020-03-30 18:21:03');
INSERT INTO `cfd_contract_channel_mapping`(`contract_id`, `quot_channel`, `index_price_listener`, `fl_channel`, `fl_listener`, `price_cache_listener`, `stop_profit_index_listener`, `stop_profit_channel`, `stop_profit_listener`, `stop_loss_index_listener`, `stop_loss_channel`, `stop_loss_listener`, `condition_order_index_listener`, `condition_order_channel`, `condition_order_listener`, `description`, `create_time`) VALUES (4, 'ltc_quot', 'ltc_price_listener', 'ltc_fl', 'ltc_fl_listener', 'ltc_cache_listener', 'ltc_profit_index_listener', 'ltc_profit_channel', 'ltc_profit_listener', 'ltc_loss_index_listener', 'ltc_loss_channel', 'ltc_loss_listener', 'ltc_order_index_listener', 'ltc_order_channel', 'ltc_order_listener', 'ltc通道', '2020-03-30 18:21:03');
INSERT INTO `cfd_contract_channel_mapping`(`contract_id`, `quot_channel`, `index_price_listener`, `fl_channel`, `fl_listener`, `price_cache_listener`, `stop_profit_index_listener`, `stop_profit_channel`, `stop_profit_listener`, `stop_loss_index_listener`, `stop_loss_channel`, `stop_loss_listener`, `condition_order_index_listener`, `condition_order_channel`, `condition_order_listener`, `description`, `create_time`) VALUES (5, 'bch_quot', 'bch_price_listener', 'bch_fl', 'bch_fl_listener', 'bch_cache_listener', 'bch_profit_index_listener', 'bch_profit_channel', 'bch_profit_listener', 'bch_loss_index_listener', 'bch_loss_channel', 'bch_loss_listener', 'bch_order_index_listener', 'bch_order_channel', 'bch_order_listener', 'bch通道', '2020-03-30 18:21:03');
INSERT INTO `cfd_contract_channel_mapping`(`contract_id`, `quot_channel`, `index_price_listener`, `fl_channel`, `fl_listener`, `price_cache_listener`, `stop_profit_index_listener`, `stop_profit_channel`, `stop_profit_listener`, `stop_loss_index_listener`, `stop_loss_channel`, `stop_loss_listener`, `condition_order_index_listener`, `condition_order_channel`, `condition_order_listener`, `description`, `create_time`) VALUES (6, 'eos_quot', 'eos_price_listener', 'eos_fl', 'eos_fl_listener', 'eos_cache_listener', 'eos_profit_index_listener', 'eos_profit_channel', 'eos_profit_listener', 'eos_loss_index_listener', 'eos_loss_channel', 'eos_loss_listener', 'eos_order_index_listener', 'eos_order_channel', 'eos_order_listener', 'eos通道', '2020-03-30 18:21:03');
INSERT INTO `cfd_contract_channel_mapping`(`contract_id`, `quot_channel`, `index_price_listener`, `fl_channel`, `fl_listener`, `price_cache_listener`, `stop_profit_index_listener`, `stop_profit_channel`, `stop_profit_listener`, `stop_loss_index_listener`, `stop_loss_channel`, `stop_loss_listener`, `condition_order_index_listener`, `condition_order_channel`, `condition_order_listener`, `description`, `create_time`) VALUES (7, 'etc_quot', 'etc_price_listener', 'etc_fl', 'etc_fl_listener', 'etc_cache_listener', 'etc_profit_index_listener', 'etc_profit_channel', 'etc_profit_listener', 'etc_loss_index_listener', 'etc_loss_channel', 'etc_loss_listener', 'etc_order_index_listener', 'etc_order_channel', 'etc_order_listener', 'etc通道', '2020-03-30 18:21:03');
INSERT INTO `cfd_contract_channel_mapping`(`contract_id`, `quot_channel`, `index_price_listener`, `fl_channel`, `fl_listener`, `price_cache_listener`, `stop_profit_index_listener`, `stop_profit_channel`, `stop_profit_listener`, `stop_loss_index_listener`, `stop_loss_channel`, `stop_loss_listener`, `condition_order_index_listener`, `condition_order_channel`, `condition_order_listener`, `description`, `create_time`) VALUES (8, 'trx_quot', 'trx_price_listener', 'trx_fl', 'trx_fl_listener', 'trx_cache_listener', 'trx_profit_index_listener', 'trx_profit_channel', 'trx_profit_listener', 'trx_loss_index_listener', 'trx_loss_channel', 'trx_loss_listener', 'trx_order_index_listener', 'trx_order_channel', 'trx_order_listener', 'trx通道', '2020-03-30 18:21:03');
INSERT INTO `cfd_contract_channel_mapping`(`contract_id`, `quot_channel`, `index_price_listener`, `fl_channel`, `fl_listener`, `price_cache_listener`, `stop_profit_index_listener`, `stop_profit_channel`, `stop_profit_listener`, `stop_loss_index_listener`, `stop_loss_channel`, `stop_loss_listener`, `condition_order_index_listener`, `condition_order_channel`, `condition_order_listener`, `description`, `create_time`) VALUES (9, 'bsv_quot', 'bsv_price_listener', 'bsv_fl', 'bsv_fl_listener', 'bsv_cache_listener', 'bsv_profit_index_listener', 'bsv_profit_channel', 'bsv_profit_listener', 'bsv_loss_index_listener', 'bsv_loss_channel', 'bsv_loss_listener', 'bsv_order_index_listener', 'bsv_order_channel', 'bsv_order_listener', 'bsv通道', '2020-03-30 18:21:03');


CREATE TABLE `core_contract_leverage` (
  `contract_id` int(11) NOT NULL COMMENT '合约id，0通配',
  `leverage` int(11) NOT NULL COMMENT '杠杆倍数，梯度值',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`contract_id`,`leverage`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='合约杠杆配置表';

INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (1, 1, '2020-04-05 15:24:15', '2020-04-05 15:24:15');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (1, 5, '2020-04-05 15:24:21', '2020-04-05 15:24:21');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (1, 10, '2020-04-05 15:24:25', '2020-04-05 15:24:25');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (1, 20, '2020-04-05 15:24:44', '2020-04-05 15:24:44');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (1, 50, '2020-04-05 15:24:49', '2020-04-05 15:24:49');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (1, 100, '2020-04-05 15:24:54', '2020-04-05 15:24:54');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (1, 150, '2020-04-05 15:24:54', '2020-04-11 14:08:13');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (2, 1, '2020-04-05 15:25:00', '2020-04-05 15:25:00');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (2, 5, '2020-04-05 15:25:04', '2020-04-05 15:25:04');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (2, 10, '2020-04-05 15:25:10', '2020-04-05 15:25:10');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (2, 20, '2020-04-05 15:25:15', '2020-04-05 15:25:15');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (2, 50, '2020-04-05 15:25:20', '2020-04-05 15:25:20');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (2, 100, '2020-04-05 15:25:25', '2020-04-05 15:25:25');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (3, 1, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (3, 5, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (3, 10, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (3, 20, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (3, 35, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (4, 1, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (4, 5, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (4, 10, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (4, 20, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (4, 50, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (5, 1, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (5, 5, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (5, 10, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (5, 20, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (5, 50, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (5, 75, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (6, 1, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (6, 5, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (6, 10, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (6, 20, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (6, 50, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (7, 1, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (7, 5, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (7, 10, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (7, 20, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (7, 35, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (8, 1, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (8, 5, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (8, 10, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (8, 20, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (8, 35, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (9, 1, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (9, 5, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (9, 10, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (9, 20, '2020-04-11 14:14:06', '2020-04-11 14:14:06');
INSERT INTO `core_contract_leverage`(`contract_id`, `leverage`, `create_time`, `update_time`) VALUES (9, 35, '2020-04-11 14:14:06', '2020-04-11 14:14:06');


CREATE TABLE `core_contract_order_currency` (
  `contract_id` int(11) NOT NULL COMMENT '合约id',
  `currency_id` int(11) NOT NULL COMMENT '下单币种id',
  `min_order_amt` decimal(36,18) NOT NULL COMMENT '最小下单金额',
  `max_order_amt` decimal(36,18) NOT NULL COMMENT '最大下单金额',
  `lot_size` decimal(36,18) NOT NULL COMMENT '最小交易量单位',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`contract_id`,`currency_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='合约下单币种表';
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (1, 7, 2.000000000000000000, 5000.000000000000000000, 0.001000000000000000, '2020-04-01 20:39:37', '2020-04-16 23:30:44');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (1, 8, 1.000000000000000000, 10000.000000000000000000, 0.001000000000000000, '2020-04-01 20:36:12', '2020-04-09 12:42:33');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (1, 999999, 100.000000000000000000, 10000.000000000000000000, 0.001000000000000000, '2020-04-01 20:36:29', '2020-04-13 03:03:37');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (2, 7, 2.000000000000000000, 5000.000000000000000000, 0.001000000000000000, '2020-04-01 20:41:03', '2020-04-16 23:30:44');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (2, 8, 1.000000000000000000, 10000.000000000000000000, 0.001000000000000000, '2020-04-01 20:40:37', '2020-04-09 12:42:34');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (2, 999999, 100.000000000000000000, 10000.000000000000000000, 0.001000000000000000, '2020-04-01 20:40:50', '2020-04-13 03:03:37');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (3, 7, 2.000000000000000000, 5000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-16 23:30:44');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (3, 8, 1.000000000000000000, 10000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-11 15:12:14');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (3, 999999, 100.000000000000000000, 10000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-13 03:03:37');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (4, 7, 2.000000000000000000, 5000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-16 23:30:44');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (4, 8, 1.000000000000000000, 10000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-11 15:12:14');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (4, 999999, 100.000000000000000000, 10000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-13 03:03:37');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (5, 7, 2.000000000000000000, 5000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-16 23:30:44');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (5, 8, 1.000000000000000000, 10000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-11 15:12:14');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (5, 999999, 100.000000000000000000, 10000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-13 03:03:37');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (6, 7, 2.000000000000000000, 5000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-16 23:30:44');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (6, 8, 1.000000000000000000, 10000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-11 15:12:14');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (6, 999999, 100.000000000000000000, 10000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-13 03:03:37');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (7, 7, 2.000000000000000000, 5000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-16 23:30:44');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (7, 8, 1.000000000000000000, 10000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-11 15:12:14');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (7, 999999, 100.000000000000000000, 10000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-13 03:03:37');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (8, 7, 2.000000000000000000, 5000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-16 23:30:44');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (8, 8, 1.000000000000000000, 10000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-11 15:12:14');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (8, 999999, 100.000000000000000000, 10000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-13 03:03:37');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (9, 7, 2.000000000000000000, 5000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-16 23:30:44');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (9, 8, 1.000000000000000000, 10000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-11 15:12:14');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (9, 999999, 100.000000000000000000, 10000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-13 03:03:37');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (10, 7, 2.000000000000000000, 5000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-16 23:30:44');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (10, 8, 1.000000000000000000, 10000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-11 15:12:14');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (10, 999999, 100.000000000000000000, 10000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-13 03:03:37');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (11, 7, 2.000000000000000000, 5000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-16 23:30:44');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (11, 8, 1.000000000000000000, 10000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-11 15:12:14');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (11, 999999, 100.000000000000000000, 10000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-13 03:03:37');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (12, 7, 2.000000000000000000, 5000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-16 23:30:44');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (12, 8, 1.000000000000000000, 10000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-11 15:12:14');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (12, 999999, 100.000000000000000000, 10000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-13 03:03:37');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (13, 7, 2.000000000000000000, 5000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-16 23:30:44');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (13, 8, 1.000000000000000000, 10000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-11 15:12:14');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (13, 999999, 100.000000000000000000, 10000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-13 03:03:37');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (14, 7, 2.000000000000000000, 5000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-16 23:30:44');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (14, 8, 1.000000000000000000, 10000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-11 15:12:14');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (14, 999999, 100.000000000000000000, 10000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-13 03:03:37');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (15, 7, 2.000000000000000000, 5000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-16 23:30:44');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (15, 8, 1.000000000000000000, 10000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-11 15:12:14');
INSERT INTO core`core_contract_order_currency`(`contract_id`, `currency_id`, `min_order_amt`, `max_order_amt`, `lot_size`, `create_time`, `update_time`) VALUES (15, 999999, 100.000000000000000000, 10000.000000000000000000, 0.001000000000000000, '2020-04-11 15:12:14', '2020-04-13 03:03:37');


CREATE TABLE `core_contract` (
  `contract_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '合约id',
  `symbol` varchar(255) NOT NULL COMMENT '合约symbol',
  `open_fee_rate` decimal(36,18) NOT NULL COMMENT '开仓手续费率',
  `close_fee_rate` decimal(36,18) NOT NULL COMMENT '平仓手续费率',
  `funding_fee_rate` decimal(36,18) NOT NULL COMMENT '资金费率',
  `price_tick` decimal(36,18) NOT NULL COMMENT '最小报价单位，下单数量是该值的整数倍',
  `bid_spread` decimal(36,18) NOT NULL COMMENT '买点差',
  `ask_spread` decimal(36,18) NOT NULL COMMENT '卖点差',
  `max_loss_ratio` decimal(36,18) NOT NULL COMMENT '最大亏损比例，超过该值即强平',
  `clear_interval` int(11) NOT NULL COMMENT '结算间隔',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`contract_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='价差合约表';
INSERT INTO `core_contract`(`contract_id`, `symbol`, `open_fee_rate`, `close_fee_rate`, `funding_fee_rate`, `price_tick`, `bid_spread`, `ask_spread`, `max_loss_ratio`, `clear_interval`, `create_time`, `update_time`) VALUES (1, 'BTC-USDT', 0.000000000000000000, 0.001000000000000000, 0.000000000000000000, 0.500000000000000000, 1.000000000000000000, -1.000000000000000000, 0.900000000000000000, 8, '2020-04-01 20:10:10', '2020-04-11 14:02:12');
INSERT INTO `core_contract`(`contract_id`, `symbol`, `open_fee_rate`, `close_fee_rate`, `funding_fee_rate`, `price_tick`, `bid_spread`, `ask_spread`, `max_loss_ratio`, `clear_interval`, `create_time`, `update_time`) VALUES (2, 'ETH-USDT', 0.000000000000000000, 0.001000000000000000, 0.000000000000000000, 0.010000000000000000, 0.080000000000000000, -0.080000000000000000, 0.900000000000000000, 6, '2020-04-01 20:10:10', '2020-04-11 14:03:31');
INSERT INTO `core_contract`(`contract_id`, `symbol`, `open_fee_rate`, `close_fee_rate`, `funding_fee_rate`, `price_tick`, `bid_spread`, `ask_spread`, `max_loss_ratio`, `clear_interval`, `create_time`, `update_time`) VALUES (3, 'XRP-USDT', 0.000000000000000000, 0.001000000000000000, 0.000000000000000000, 0.000100000000000000, 0.000200000000000000, -0.000200000000000000, 0.900000000000000000, 6, '2020-04-01 20:10:10', '2020-04-11 14:03:36');
INSERT INTO `core_contract`(`contract_id`, `symbol`, `open_fee_rate`, `close_fee_rate`, `funding_fee_rate`, `price_tick`, `bid_spread`, `ask_spread`, `max_loss_ratio`, `clear_interval`, `create_time`, `update_time`) VALUES (4, 'LTC-USDT', 0.000000000000000000, 0.001000000000000000, 0.000000000000000000, 0.010000000000000000, 0.050000000000000000, -0.050000000000000000, 0.900000000000000000, 6, '2020-04-01 20:10:10', '2020-04-11 14:04:05');
INSERT INTO `core_contract`(`contract_id`, `symbol`, `open_fee_rate`, `close_fee_rate`, `funding_fee_rate`, `price_tick`, `bid_spread`, `ask_spread`, `max_loss_ratio`, `clear_interval`, `create_time`, `update_time`) VALUES (5, 'BCH-USDT', 0.000000000000000000, 0.001000000000000000, 0.000000000000000000, 0.010000000000000000, 0.100000000000000000, -0.100000000000000000, 0.900000000000000000, 6, '2020-04-01 20:10:10', '2020-04-10 22:17:25');
INSERT INTO `core_contract`(`contract_id`, `symbol`, `open_fee_rate`, `close_fee_rate`, `funding_fee_rate`, `price_tick`, `bid_spread`, `ask_spread`, `max_loss_ratio`, `clear_interval`, `create_time`, `update_time`) VALUES (6, 'EOS-USDT', 0.000000000000000000, 0.001000000000000000, 0.000000000000000000, 0.000100000000000000, 0.001400000000000000, -0.001400000000000000, 0.900000000000000000, 6, '2020-04-01 20:10:10', '2020-04-11 14:05:53');
INSERT INTO `core_contract`(`contract_id`, `symbol`, `open_fee_rate`, `close_fee_rate`, `funding_fee_rate`, `price_tick`, `bid_spread`, `ask_spread`, `max_loss_ratio`, `clear_interval`, `create_time`, `update_time`) VALUES (7, 'ETC-USDT', 0.000000000000000000, 0.001000000000000000, 0.000000000000000000, 0.000100000000000000, 0.003000000000000000, -0.003000000000000000, 0.900000000000000000, 6, '2020-04-01 20:10:10', '2020-04-11 14:06:28');
INSERT INTO `core_contract`(`contract_id`, `symbol`, `open_fee_rate`, `close_fee_rate`, `funding_fee_rate`, `price_tick`, `bid_spread`, `ask_spread`, `max_loss_ratio`, `clear_interval`, `create_time`, `update_time`) VALUES (8, 'TRX-USDT', 0.000000000000000000, 0.001000000000000000, 0.000000000000000000, 0.000010000000000000, 0.000020000000000000, -0.000020000000000000, 0.900000000000000000, 6, '2020-04-01 20:10:10', '2020-04-11 14:07:13');
INSERT INTO `core_contract`(`contract_id`, `symbol`, `open_fee_rate`, `close_fee_rate`, `funding_fee_rate`, `price_tick`, `bid_spread`, `ask_spread`, `max_loss_ratio`, `clear_interval`, `create_time`, `update_time`) VALUES (9, 'BSV-USDT', 0.000000000000000000, 0.001000000000000000, 0.000000000000000000, 0.010000000000000000, 0.150000000000000000, -0.150000000000000000, 0.900000000000000000, 6, '2020-04-01 20:10:10', '2020-04-11 14:07:50');


```