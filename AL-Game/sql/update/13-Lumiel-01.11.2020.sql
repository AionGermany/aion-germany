DROP TABLE IF EXISTS `lumiel_transform`;
CREATE TABLE IF NOT EXISTS `lumiel_transform` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `player_id` int(11) NOT NULL,
  `lumiel_id` int(11) NOT NULL,
  `points` bigint(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
