DROP TABLE IF EXISTS `player_skill_skins`;
CREATE TABLE `player_skill_skins`  (
  `player_id` int(11) NULL DEFAULT 0,
  `skin_id` int(11) NULL DEFAULT 0,
  `remaining` bigint(22) NULL DEFAULT 0,
  `active` int(1) NULL DEFAULT 0,
  PRIMARY KEY (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


