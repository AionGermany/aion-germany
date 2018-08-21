ALTER TABLE `player_events_window`
DROP PRIMARY KEY,
ADD PRIMARY KEY (`account_id`, `event_id`);
