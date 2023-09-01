-- ----------------------------
-- Table structure for player_shugo_sweep
-- ----------------------------
CREATE TABLE `player_shugo_sweep` (
  `player_id` int(11) NOT NULL,
  `free_dice` int(10) NOT NULL DEFAULT '0',
  `sweep_step` int(10) NOT NULL DEFAULT '0',
  `board_id` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
