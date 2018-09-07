ALTER TABLE `player_events_window`
MODIFY COLUMN `elapsed`  double(11,0) NOT NULL DEFAULT 0 AFTER `last_stamp`,
ADD COLUMN `reward_recived_count`  int(11) NOT NULL DEFAULT 0 AFTER `elapsed`;