ALTER TABLE `inventory` ADD COLUMN grind_socket int(1) NOT NULL DEFAULT '0' AFTER `skin_skill`;
ALTER TABLE `inventory` ADD COLUMN grind_color int(11) NOT NULL DEFAULT '0' AFTER `grind_socket`;
ALTER TABLE `inventory` ADD COLUMN grind_stone bigint(20) NOT NULL DEFAULT '0' AFTER `grind_color`;
ALTER TABLE `inventory` ADD COLUMN grind_slot int(10) NOT NULL DEFAULT '0' AFTER `grind_stone`;
ALTER TABLE `inventory` ADD COLUMN contaminated int(1) NOT NULL DEFAULT '0' AFTER `grind_slot`;
