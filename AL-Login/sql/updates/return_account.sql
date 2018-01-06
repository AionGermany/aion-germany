ALTER TABLE `account_data` ADD `return_account` tinyint(1) NOT NULL DEFAULT '0';
ALTER TABLE `account_data` ADD `return_end` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
