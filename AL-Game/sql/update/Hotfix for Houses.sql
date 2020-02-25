-- ----------------------------
-- Table structure for `houses`
-- ----------------------------
DROP TABLE IF EXISTS `houses`;
CREATE TABLE `houses` (
`id` int(10) NOT NULL,
`player_id` int(10) NOT NULL DEFAULT '0',
`building_id` int(10) NOT NULL,
`address` int(10) NOT NULL,
`acquire_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
`settings` int(11) NOT NULL DEFAULT '0',
`status` enum('ACTIVE','SELL_WAIT','INACTIVE','NOSALE') NOT NULL DEFAULT 'ACTIVE',
`fee_paid` tinyint(1) NOT NULL DEFAULT '1',
`next_pay` timestamp NULL DEFAULT NULL,
`sell_started` timestamp NULL DEFAULT NULL,
`sign_notice` binary(130) DEFAULT NULL,
PRIMARY KEY (`id`),
UNIQUE KEY `address` (`address`) USING BTREE,
KEY `address_2` (`address`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;