ALTER TABLE `player_events_window`
MODIFY COLUMN `elapsed`  int(11) NOT NULL DEFAULT 0 AFTER `last_stamp`;