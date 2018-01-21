ALTER TABLE `inventory` ADD COLUMN reduction_level INT(11) NOT NULL AFTER `buff_skill`;

ALTER TABLE `players` ADD COLUMN is_highdaeva tinyint(1) NOT NULL DEFAULT '0' AFTER `wardrobe_size`;
ALTER TABLE `players` ADD COLUMN creativity_point int(11) NOT NULL DEFAULT '0' AFTER `is_highdaeva`;
ALTER TABLE `players` ADD COLUMN creativity_step int(11) NOT NULL DEFAULT '0' AFTER `creativity_point`;

CREATE TABLE IF NOT EXISTS `player_transformation` (
  `player_id` int(10) NOT NULL,
  `panel_id` int(5) NOT NULL DEFAULT '0',
  `item_id` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `player_cp` (
  `player_id` int(11) NOT NULL,
  `slot` int(11) NOT NULL,
  `point` int(3) NOT NULL,
  PRIMARY KEY (`player_id`,`slot`),
  CONSTRAINT `player_cp_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;