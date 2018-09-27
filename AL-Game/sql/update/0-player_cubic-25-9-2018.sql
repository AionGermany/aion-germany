-- ----------------------------
-- Table structure for player_cubic
-- ----------------------------
DROP TABLE IF EXISTS `player_cubic`;
CREATE TABLE `player_cubic`  (
  `player_id` int(11) NOT NULL,
  `cubic_id` int(11) NOT NULL,
  `rank` int(11) NOT NULL,
  `level` int(11) NOT NULL,
  `stat_value` int(11) NOT NULL,
  `category` int(11) NOT NULL,
  PRIMARY KEY (`player_id`, `cubic_id`) USING BTREE,
  CONSTRAINT `fk_player_cubic` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;