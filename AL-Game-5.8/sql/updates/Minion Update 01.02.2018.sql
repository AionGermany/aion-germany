ALTER TABLE `player_minions` MODIFY COLUMN `birthday` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP AFTER `level`;
ALTER TABLE `player_minions` ADD COLUMN is_locked INT(1) NOT NULL AFTER `growthpoints`;
ALTER TABLE `player_minions` ADD COLUMN buff_bag VARCHAR(255) NULL DEFAULT NULL AFTER `is_locked`;