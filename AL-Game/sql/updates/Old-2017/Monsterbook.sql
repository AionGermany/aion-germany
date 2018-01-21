CREATE TABLE IF NOT EXISTS `player_monsterbook` (
  `player_id` int(11) NOT NULL,
  `id` int(11) NOT NULL,
  `kill_count` int(11) NOT NULL,
  `level` int(11) NOT NULL,
  `claim_reward` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`player_id`,`id`),
  CONSTRAINT `fk_player_monsterbook` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;