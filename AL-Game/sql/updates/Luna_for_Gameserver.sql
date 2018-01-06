ALTER TABLE `inventory` ADD COLUMN luna_reskin tinyint(1) NOT NULL DEFAULT '0' AFTER `reduction_level`;

ALTER TABLE `players` ADD COLUMN luna_consume int(11) NOT NULL DEFAULT '0' AFTER `creativity_step`;
ALTER TABLE `players` ADD COLUMN muni_keys int(11) NOT NULL DEFAULT '0' AFTER `creativity_step`;
ALTER TABLE `players` ADD COLUMN luna_consume_count int(11) NOT NULL DEFAULT '0' AFTER `creativity_step`;
ALTER TABLE `players` ADD COLUMN wardrobe_slot int(11) NOT NULL DEFAULT '2' AFTER `creativity_step`;

CREATE TABLE IF NOT EXISTS `player_luna_shop` (
  `player_id` int(10) NOT NULL,
  `free_under` tinyint(1) NOT NULL,
  `free_munition` tinyint(1) NOT NULL,
  `free_chest` tinyint(1) NOT NULL,
  PRIMARY KEY (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `player_wardrobe` (
  `player_id` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `slot` int(11) NOT NULL,
  `reskin_count` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`player_id`,`item_id`),
  CONSTRAINT `player_wardrobe_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;