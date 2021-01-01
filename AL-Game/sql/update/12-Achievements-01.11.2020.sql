DROP TABLE IF EXISTS `player_achievements`;
CREATE TABLE `player_achievements` (
  `object_id` int(11) NOT NULL,
  `id` int(11) NOT NULL,
  `player_id` int(11) NOT NULL,
  `state` tinyint(1) NOT NULL,
  `step` int(11) NOT NULL DEFAULT '0',
  `type` int(11) NOT NULL,
  `start_date` timestamp NULL DEFAULT NULL,
  `end_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`object_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `player_achievement_actions`;
CREATE TABLE `player_achievement_actions` (
  `object_id` int(11) NOT NULL,
  `id` int(11) NOT NULL,
  `player_id` int(11) NOT NULL,
  `state` tinyint(1) NOT NULL,
  `step` int(11) NOT NULL DEFAULT '0',
  `start_date` timestamp NULL DEFAULT NULL,
  `end_date` timestamp NULL DEFAULT NULL,
  `type` int(11) NOT NULL,
  `achievement_obj` int(11) NOT NULL,
  PRIMARY KEY (`object_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;