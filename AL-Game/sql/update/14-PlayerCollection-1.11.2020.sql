DROP TABLE IF EXISTS `player_collection_infos`;
CREATE TABLE IF NOT EXISTS `player_collection_infos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `player_id` int(11) NOT NULL,
  `type` enum('COMMON','ANCIENT','RELIC','EVENT') NOT NULL,
  `level` int(10) NOT NULL DEFAULT '1',
  `exp` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `player_collections`;
CREATE TABLE IF NOT EXISTS `player_collections` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `player_id` int(11) NOT NULL,
  `collection_id` int(11) NOT NULL,
  `complete` tinyint(1) NOT NULL DEFAULT '0',
  `step` int(10) NOT NULL DEFAULT '0',
  `item1` tinyint(1) NOT NULL DEFAULT '0',
  `item2` tinyint(1) NOT NULL DEFAULT '0',
  `item3` tinyint(1) NOT NULL DEFAULT '0',
  `item4` tinyint(1) NOT NULL DEFAULT '0',
  `item5` tinyint(1) NOT NULL DEFAULT '0',
  `item6` tinyint(1) NOT NULL DEFAULT '0',
  `item7` tinyint(1) NOT NULL DEFAULT '0',
  `item8` tinyint(1) NOT NULL DEFAULT '0',
  `item9` tinyint(1) NOT NULL DEFAULT '0',
  `item10` tinyint(1) NOT NULL DEFAULT '0',
  `item11` tinyint(1) NOT NULL DEFAULT '0',
  `item12` tinyint(1) NOT NULL DEFAULT '0',
  `item13` tinyint(1) NOT NULL DEFAULT '0',
  `item14` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;