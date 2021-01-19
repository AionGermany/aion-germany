
-- ----------------------------
-- Table structure for account_transform
-- ----------------------------
DROP TABLE IF EXISTS `account_transform`;
CREATE TABLE `account_transform` (
  `account_id` int(11) NOT NULL,
  `card_id` int(11) NOT NULL,
  `count` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for player_transform_collections
-- ----------------------------
DROP TABLE IF EXISTS `player_transform_collections`;
CREATE TABLE `player_transform_collections` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `player_id` int(11) NOT NULL,
  `collection_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `players` ADD `last_transfo` INT(10) NOT NULL DEFAULT '0' AFTER `world_play_time`; 