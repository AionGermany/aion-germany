ALTER TABLE `player_minions`
MODIFY COLUMN `birthday` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' AFTER `level`;