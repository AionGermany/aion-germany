ALTER TABLE `account_data` ADD COLUMN luna int(11) NOT NULL DEFAULT '0' AFTER `balance`;
ALTER TABLE `account_rewards` ADD COLUMN luna decimal(20,0) NOT NULL DEFAULT '0' AFTER `rewarded`;