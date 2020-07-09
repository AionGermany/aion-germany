DROP TABLE IF EXISTS `transformations`;
CREATE TABLE `transformations` (
  `player_id` int(11) NOT NULL,
  `transformation_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `grade` varchar(11) NOT NULL,
  `count` int(11) NOT NULL,
  PRIMARY KEY (`player_id`,`transformation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;