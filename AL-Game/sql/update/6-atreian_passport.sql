-- ----------------------------
-- Table structure for atreian_passports
-- ----------------------------
DROP TABLE IF EXISTS `atreian_passports`;
CREATE TABLE `atreian_passports`  (
  `account_id` int(11) NOT NULL,
  `passport_id` int(11) NOT NULL,
  `stamps` int(11) NOT NULL DEFAULT 0,
  `last_stamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `rewarded` tinyint(1) NOT NULL DEFAULT 0,
  `available` int(1) NULL DEFAULT NULL,
  UNIQUE INDEX `account_passport`(`account_id`, `passport_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;
