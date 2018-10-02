DROP TABLE IF EXISTS `player_ranking`;
CREATE TABLE `player_ranking` (
  `player_id` int(10) NOT NULL,
  `table_id` int(10) NOT NULL,
  `rank` int(10) NOT NULL DEFAULT '0',
  `last_rank` int(10) NOT NULL DEFAULT '0',
  `points` int(10) NOT NULL DEFAULT '0',
  `last_points` int(10) NOT NULL DEFAULT '0',
  `high_points` int(10) NOT NULL DEFAULT '0',
  `low_points` int(10) NOT NULL DEFAULT '0',
  `position_match` int(10) NOT NULL DEFAULT '5',
  PRIMARY KEY (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;