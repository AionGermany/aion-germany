DROP TABLE IF EXISTS `player_fame`;
CREATE TABLE `player_fame` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `player_id` int(11) NOT NULL,
  `fame_id` int(10) NOT NULL,
  `level` int(10) NOT NULL DEFAULT '1',
  `exp` bigint(30) NOT NULL DEFAULT '0',
  `exp_loss` bigint(30) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=latin1;
