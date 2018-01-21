CREATE TABLE IF NOT EXISTS `player_minions` (
  `player_id` int(11) NOT NULL,
  `object_id` int(11) NOT NULL DEFAULT '0',
  `minion_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `grade` varchar(11) NOT NULL,
  `level` varchar(11) NOT NULL,
  `birthday` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;