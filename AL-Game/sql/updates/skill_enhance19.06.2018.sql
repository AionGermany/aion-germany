ALTER TABLE `inventory` ADD COLUMN isEnhance tinyint(1) NOT NULL DEFAULT '0' AFTER `luna_reskin`;
ALTER TABLE `inventory` ADD COLUMN enhanceSkillId int(11) NOT NULL DEFAULT '0' AFTER `isEnhance`;
ALTER TABLE `inventory` ADD COLUMN enhanceSkillEnchant int(11) NOT NULL DEFAULT '0' AFTER `enhanceSkillId`;