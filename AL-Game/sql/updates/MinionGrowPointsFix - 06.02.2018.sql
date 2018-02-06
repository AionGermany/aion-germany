ALTER TABLE `player_minions`
MODIFY COLUMN `growpoints`  int(6) NOT NULL DEFAULT 0 AFTER `birthday`;