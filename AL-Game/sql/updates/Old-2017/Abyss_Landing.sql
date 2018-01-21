DROP TABLE IF EXISTS `abyss_landing`;
CREATE TABLE `abyss_landing` (
  `id` int(11) NOT NULL,
  `level` int(11) NOT NULL DEFAULT '1',
  `points` int(10) NOT NULL DEFAULT '0',
  `siege` int(10) NOT NULL DEFAULT '0',
  `commander` int(10) NOT NULL DEFAULT '0',
  `artefact` int(10) NOT NULL DEFAULT '0',
  `base` int(10) NOT NULL DEFAULT '0',
  `monuments` int(10) NOT NULL DEFAULT '0',
  `quest` int(10) NOT NULL DEFAULT '0',
  `facility` int(10) NOT NULL DEFAULT '0',
  `race` enum('ELYOS','ASMODIANS') NOT NULL,
  `level_up_date` timestamp NOT NULL DEFAULT '2015-01-01 01:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `special_landing`;
CREATE TABLE `special_landing` (
  `id` int(2) NOT NULL,
  `type` enum('SPAWN','DESPAWN') NOT NULL DEFAULT 'DESPAWN'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;