ALTER TABLE `player_minions`
MODIFY COLUMN `growthpoints`  int(6) NOT NULL DEFAULT 0 AFTER `birthday`;
ALTER TABLE `player_minions`
MODIFY COLUMN `is_locked`  int(1) NOT NULL DEFAULT 0 AFTER `growthpoints`;