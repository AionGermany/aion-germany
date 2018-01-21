SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `abyss_rank`
-- ----------------------------
DROP TABLE IF EXISTS `abyss_rank`;
CREATE TABLE `abyss_rank` (
  `player_id` int(11) NOT NULL,
  `daily_ap` int(11) NOT NULL,
  `daily_gp` int(11) NOT NULL,
  `weekly_ap` int(11) NOT NULL,
  `weekly_gp` int(11) NOT NULL,
  `ap` int(11) NOT NULL,
  `gp` int(11) NOT NULL,
  `rank` int(2) NOT NULL DEFAULT '1',
  `top_ranking` int(4) NOT NULL,
  `daily_kill` int(5) NOT NULL,
  `weekly_kill` int(5) NOT NULL,
  `all_kill` int(4) NOT NULL DEFAULT '0',
  `max_rank` int(2) NOT NULL DEFAULT '1',
  `last_kill` int(5) NOT NULL,
  `last_ap` int(11) NOT NULL,
  `last_gp` int(11) NOT NULL,
  `last_update` decimal(20,0) NOT NULL,
  `rank_pos` int(11) NOT NULL DEFAULT '0',
  `old_rank_pos` int(11) NOT NULL DEFAULT '0',
  `rank_ap` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`player_id`),
  CONSTRAINT `abyss_rank_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of abyss_rank
-- ----------------------------

-- ----------------------------
-- Table structure for `announcements`
-- ----------------------------
DROP TABLE IF EXISTS `announcements`;
CREATE TABLE `announcements` (
  `id` int(3) NOT NULL AUTO_INCREMENT,
  `announce` text NOT NULL,
  `faction` enum('ALL','ASMODIANS','ELYOS') NOT NULL DEFAULT 'ALL',
  `type` enum('SHOUT','ORANGE','YELLOW','WHITE','SYSTEM') NOT NULL DEFAULT 'SYSTEM',
  `delay` int(4) NOT NULL DEFAULT '1800',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of announcements
-- ----------------------------

-- ----------------------------
-- Table structure for `banned_hdd`
-- ----------------------------
DROP TABLE IF EXISTS `banned_hdd`;
CREATE TABLE `banned_hdd` (
  `uniId` int(10) NOT NULL AUTO_INCREMENT,
  `hdd_serial` varchar(50) NOT NULL,
  `time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `details` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`uniId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of banned_hdd
-- ----------------------------

-- ----------------------------
-- Table structure for `blocks`
-- ----------------------------
DROP TABLE IF EXISTS `blocks`;
CREATE TABLE `blocks` (
  `player` int(11) NOT NULL,
  `blocked_player` int(11) NOT NULL,
  `reason` varchar(100) NOT NULL DEFAULT '',
  PRIMARY KEY (`player`,`blocked_player`),
  KEY `blocked_player` (`blocked_player`),
  CONSTRAINT `blocks_ibfk_1` FOREIGN KEY (`player`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `blocks_ibfk_2` FOREIGN KEY (`blocked_player`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of blocks
-- ----------------------------

-- ----------------------------
-- Table structure for `bookmark`
-- ----------------------------
DROP TABLE IF EXISTS `bookmark`;
CREATE TABLE `bookmark` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `char_id` int(11) NOT NULL,
  `x` float NOT NULL,
  `y` float NOT NULL,
  `z` float NOT NULL,
  `world_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bookmark
-- ----------------------------

-- ----------------------------
-- Table structure for `broker`
-- ----------------------------
DROP TABLE IF EXISTS `broker`;
CREATE TABLE `broker` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `item_pointer` int(11) NOT NULL DEFAULT '0',
  `item_id` int(11) NOT NULL,
  `item_count` bigint(20) NOT NULL,
  `item_creator` varchar(50) DEFAULT NULL,
  `seller` varchar(50) DEFAULT NULL,
  `price` bigint(20) NOT NULL DEFAULT '0',
  `broker_race` enum('ELYOS','ASMODIAN') NOT NULL,
  `expire_time` timestamp NOT NULL DEFAULT '2010-01-01 02:00:00',
  `settle_time` timestamp NOT NULL DEFAULT '2010-01-01 02:00:00',
  `seller_id` int(11) NOT NULL,
  `is_sold` tinyint(1) NOT NULL,
  `is_settled` tinyint(1) NOT NULL,
  `is_partsale` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `seller_id` (`seller_id`),
  CONSTRAINT `broker_ibfk_1` FOREIGN KEY (`seller_id`) REFERENCES `players` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of broker
-- ----------------------------

-- ----------------------------
-- Table structure for `challenge_tasks`
-- ----------------------------
DROP TABLE IF EXISTS `challenge_tasks`;
CREATE TABLE `challenge_tasks` (
  `task_id` int(11) NOT NULL,
  `quest_id` int(10) NOT NULL,
  `owner_id` int(11) NOT NULL,
  `owner_type` enum('LEGION','TOWN') NOT NULL,
  `complete_count` int(3) unsigned NOT NULL DEFAULT '0',
  `complete_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`task_id`,`quest_id`,`owner_id`,`owner_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of challenge_tasks
-- ----------------------------

-- ----------------------------
-- Table structure for `craft_cooldowns`
-- ----------------------------
DROP TABLE IF EXISTS `craft_cooldowns`;
CREATE TABLE `craft_cooldowns` (
  `player_id` int(11) NOT NULL,
  `delay_id` int(11) unsigned NOT NULL,
  `reuse_time` bigint(13) unsigned NOT NULL,
  PRIMARY KEY (`player_id`,`delay_id`),
  CONSTRAINT `craft_cooldowns_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of craft_cooldowns
-- ----------------------------

-- ----------------------------
-- Table structure for `event_items`
-- ----------------------------
DROP TABLE IF EXISTS `event_items`;
CREATE TABLE `event_items` (
  `player_id` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `counts` int(10) unsigned NOT NULL,
  PRIMARY KEY (`player_id`,`item_id`),
  CONSTRAINT `event_items_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of event_items
-- ----------------------------

-- ----------------------------
-- Table structure for `friends`
-- ----------------------------
DROP TABLE IF EXISTS `friends`;
CREATE TABLE `friends` (
  `player` int(11) NOT NULL,
  `friend` int(11) NOT NULL,
  `note` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`player`,`friend`),
  KEY `friend` (`friend`),
  CONSTRAINT `friends_ibfk_1` FOREIGN KEY (`player`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `friends_ibfk_2` FOREIGN KEY (`friend`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of friends
-- ----------------------------

-- ----------------------------
-- Table structure for `guides`
-- ----------------------------
DROP TABLE IF EXISTS `guides`;
CREATE TABLE `guides` (
  `guide_id` int(11) NOT NULL AUTO_INCREMENT,
  `player_id` int(11) NOT NULL,
  `title` varchar(80) NOT NULL,
  PRIMARY KEY (`guide_id`),
  KEY `player_id` (`player_id`),
  CONSTRAINT `guides_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of guides
-- ----------------------------

-- ----------------------------
-- Table structure for `guild_quests`
-- ----------------------------
DROP TABLE IF EXISTS `guild_quests`;
CREATE TABLE `guild_quests` (
  `player_id` int(11) NOT NULL,
  `guild_id` int(2) NOT NULL DEFAULT '0',
  `recently_taken_quest` int(6) NOT NULL DEFAULT '0',
  `completion_timestamp` timestamp NULL DEFAULT NULL,
  `currently_started_quest` int(6) NOT NULL DEFAULT '0',
  PRIMARY KEY (`player_id`),
  CONSTRAINT `guild_quests_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of guild_quests
-- ----------------------------

-- ----------------------------
-- Table structure for `houses`
-- ----------------------------
DROP TABLE IF EXISTS `houses`;
CREATE TABLE `houses` (
  `id` int(10) NOT NULL,
  `player_id` int(10) NOT NULL DEFAULT '0',
  `building_id` int(10) NOT NULL,
  `address` int(10) NOT NULL,
  `acquire_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `settings` int(11) NOT NULL DEFAULT '0',
  `status` enum('ACTIVE','SELL_WAIT','INACTIVE','NOSALE') NOT NULL DEFAULT 'ACTIVE',
  `fee_paid` tinyint(1) NOT NULL DEFAULT '1',
  `next_pay` timestamp NULL DEFAULT NULL,
  `sell_started` timestamp NULL DEFAULT NULL,
  `sign_notice` binary(130) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `address_2` (`address`),
  KEY `address` (`address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of houses
-- ----------------------------

-- ----------------------------
-- Table structure for `house_bids`
-- ----------------------------
DROP TABLE IF EXISTS `house_bids`;
CREATE TABLE `house_bids` (
  `player_id` int(10) NOT NULL,
  `house_id` int(10) NOT NULL,
  `bid` bigint(20) NOT NULL,
  `bid_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`player_id`,`house_id`,`bid`),
  KEY `house_id_ibfk_1` (`house_id`),
  CONSTRAINT `house_id_ibfk_1` FOREIGN KEY (`house_id`) REFERENCES `houses` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of house_bids
-- ----------------------------

-- ----------------------------
-- Table structure for `house_object_cooldowns`
-- ----------------------------
DROP TABLE IF EXISTS `house_object_cooldowns`;
CREATE TABLE `house_object_cooldowns` (
  `player_id` int(11) NOT NULL,
  `object_id` int(11) NOT NULL,
  `reuse_time` bigint(20) NOT NULL,
  PRIMARY KEY (`player_id`,`object_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of house_object_cooldowns
-- ----------------------------

-- ----------------------------
-- Table structure for `house_scripts`
-- ----------------------------
DROP TABLE IF EXISTS `house_scripts`;
CREATE TABLE `house_scripts` (
  `house_id` int(11) NOT NULL,
  `index` tinyint(4) NOT NULL,
  `script` mediumtext,
  PRIMARY KEY (`house_id`,`index`),
  CONSTRAINT `houses_id_ibfk_1` FOREIGN KEY (`house_id`) REFERENCES `houses` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;

-- ----------------------------
-- Records of house_scripts
-- ----------------------------

-- ----------------------------
-- Table structure for `inventory`
-- ----------------------------
DROP TABLE IF EXISTS `inventory`;
CREATE TABLE `inventory` (
  `item_unique_id` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `item_count` bigint(20) NOT NULL DEFAULT '0',
  `item_color` int(11) NOT NULL DEFAULT '0',
  `color_expires` int(11) NOT NULL DEFAULT '0',
  `item_creator` varchar(50) DEFAULT NULL,
  `expire_time` int(11) NOT NULL DEFAULT '0',
  `activation_count` int(11) NOT NULL DEFAULT '0',
  `item_owner` int(11) NOT NULL,
  `is_equiped` tinyint(1) NOT NULL DEFAULT '0',
  `is_soul_bound` tinyint(1) NOT NULL DEFAULT '0',
  `slot` bigint(20) NOT NULL DEFAULT '0',
  `item_location` tinyint(1) DEFAULT '0',
  `enchant` tinyint(1) DEFAULT '0',
  `item_skin` int(11) NOT NULL DEFAULT '0',
  `fusioned_item` int(11) NOT NULL DEFAULT '0',
  `optional_socket` int(1) NOT NULL DEFAULT '0',
  `optional_fusion_socket` int(1) NOT NULL DEFAULT '0',
  `charge` mediumint(9) NOT NULL DEFAULT '0',
  `rnd_bonus` smallint(6) DEFAULT NULL,
  `rnd_count` smallint(6) NOT NULL DEFAULT '0',
  `pack_count` smallint(6) NOT NULL DEFAULT '0',
  `authorize` int(11) NOT NULL DEFAULT '0',
  `is_packed` tinyint(1) NOT NULL DEFAULT '0',
  `is_amplified` tinyint(1) NOT NULL DEFAULT '0',
  `buff_skill` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`item_unique_id`),
  KEY `item_location` (`item_location`) USING HASH,
  KEY `index3` (`item_owner`,`item_location`,`is_equiped`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of inventory
-- ----------------------------

-- ----------------------------
-- Table structure for `item_cooldowns`
-- ----------------------------
DROP TABLE IF EXISTS `item_cooldowns`;
CREATE TABLE `item_cooldowns` (
  `player_id` int(11) NOT NULL,
  `delay_id` int(11) NOT NULL,
  `use_delay` int(10) unsigned NOT NULL,
  `reuse_time` bigint(13) NOT NULL,
  PRIMARY KEY (`player_id`,`delay_id`),
  CONSTRAINT `item_cooldowns_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of item_cooldowns
-- ----------------------------

-- ----------------------------
-- Table structure for `item_stones`
-- ----------------------------
DROP TABLE IF EXISTS `item_stones`;
CREATE TABLE `item_stones` (
  `item_unique_id` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `slot` int(2) NOT NULL,
  `category` int(2) NOT NULL DEFAULT '0',
  `polishNumber` int(11) NOT NULL DEFAULT '0',
  `polishCharge` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`item_unique_id`,`slot`,`category`),
  CONSTRAINT `item_stones_ibfk_1` FOREIGN KEY (`item_unique_id`) REFERENCES `inventory` (`item_unique_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of item_stones
-- ----------------------------

-- ----------------------------
-- Table structure for `legions`
-- ----------------------------
DROP TABLE IF EXISTS `legions`;
CREATE TABLE `legions` (
  `id` int(11) NOT NULL,
  `name` varchar(32) DEFAULT NULL,
  `level` int(1) NOT NULL DEFAULT '1',
  `contribution_points` bigint(20) NOT NULL DEFAULT '0',
  `deputy_permission` int(11) NOT NULL DEFAULT '7692',
  `centurion_permission` int(11) NOT NULL DEFAULT '7176',
  `legionary_permission` int(11) NOT NULL DEFAULT '6144',
  `volunteer_permission` int(11) NOT NULL DEFAULT '2048',
  `disband_time` int(11) NOT NULL DEFAULT '0',
  `rank_cp` int(11) NOT NULL DEFAULT '0',
  `rank_pos` int(11) NOT NULL DEFAULT '0',
  `old_rank_pos` int(11) NOT NULL DEFAULT '0',
  `world_owner` int(11) NOT NULL DEFAULT '0',
  `description` varchar(255) NOT NULL DEFAULT '',
  `joinType` int(1) NOT NULL DEFAULT '0',
  `minJoinLevel` int(3) NOT NULL DEFAULT '0',
  `territory` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_unique` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of legions
-- ----------------------------

-- ----------------------------
-- Table structure for `legion_announcement_list`
-- ----------------------------
DROP TABLE IF EXISTS `legion_announcement_list`;
CREATE TABLE `legion_announcement_list` (
  `legion_id` int(11) NOT NULL,
  `announcement` varchar(256) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY `legion_id` (`legion_id`),
  CONSTRAINT `legion_announcement_list_ibfk_1` FOREIGN KEY (`legion_id`) REFERENCES `legions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of legion_announcement_list
-- ----------------------------

-- ----------------------------
-- Table structure for `legion_emblems`
-- ----------------------------
DROP TABLE IF EXISTS `legion_emblems`;
CREATE TABLE `legion_emblems` (
  `legion_id` int(11) NOT NULL,
  `emblem_id` int(1) NOT NULL DEFAULT '0',
  `color_r` int(3) NOT NULL DEFAULT '0',
  `color_g` int(3) NOT NULL DEFAULT '0',
  `color_b` int(3) NOT NULL DEFAULT '0',
  `emblem_type` enum('DEFAULT','CUSTOM') NOT NULL DEFAULT 'DEFAULT',
  `emblem_data` longblob,
  PRIMARY KEY (`legion_id`),
  CONSTRAINT `legion_emblems_ibfk_1` FOREIGN KEY (`legion_id`) REFERENCES `legions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of legion_emblems
-- ----------------------------

-- ----------------------------
-- Table structure for `legion_history`
-- ----------------------------
DROP TABLE IF EXISTS `legion_history`;
CREATE TABLE `legion_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `legion_id` int(11) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `history_type` enum('CREATE','JOIN','KICK','APPOINTED','EMBLEM_REGISTER','EMBLEM_MODIFIED','ITEM_DEPOSIT','ITEM_WITHDRAW','KINAH_DEPOSIT','KINAH_WITHDRAW','LEVEL_UP') NOT NULL,
  `name` varchar(32) DEFAULT NULL,
  `tab_id` smallint(3) NOT NULL DEFAULT '0',
  `description` varchar(30) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `legion_id` (`legion_id`),
  CONSTRAINT `legion_history_ibfk_1` FOREIGN KEY (`legion_id`) REFERENCES `legions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of legion_history
-- ----------------------------

-- ----------------------------
-- Table structure for `legion_join_requests`
-- ----------------------------
DROP TABLE IF EXISTS `legion_join_requests`;
CREATE TABLE `legion_join_requests` (
  `legionId` int(11) NOT NULL DEFAULT '0',
  `playerId` int(11) NOT NULL DEFAULT '0',
  `playerName` varchar(64) NOT NULL DEFAULT '',
  `playerClassId` int(2) NOT NULL DEFAULT '0',
  `playerRaceId` int(2) NOT NULL DEFAULT '0',
  `playerLevel` int(4) NOT NULL DEFAULT '0',
  `playerGenderId` int(2) NOT NULL DEFAULT '0',
  `joinRequestMsg` varchar(40) NOT NULL DEFAULT '',
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`legionId`,`playerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of legion_join_requests
-- ----------------------------

-- ----------------------------
-- Table structure for `legion_members`
-- ----------------------------
DROP TABLE IF EXISTS `legion_members`;
CREATE TABLE `legion_members` (
  `legion_id` int(11) NOT NULL,
  `player_id` int(11) NOT NULL,
  `nickname` varchar(10) NOT NULL DEFAULT '',
  `rank` enum('BRIGADE_GENERAL','CENTURION','LEGIONARY','DEPUTY','VOLUNTEER') NOT NULL DEFAULT 'VOLUNTEER',
  `selfintro` varchar(32) DEFAULT '',
  `challenge_score` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`player_id`),
  KEY `player_id` (`player_id`),
  KEY `legion_id` (`legion_id`),
  CONSTRAINT `legion_members_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `legion_members_ibfk_2` FOREIGN KEY (`legion_id`) REFERENCES `legions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of legion_members
-- ----------------------------

-- ----------------------------
-- Table structure for `mail`
-- ----------------------------
DROP TABLE IF EXISTS `mail`;
CREATE TABLE `mail` (
  `mail_unique_id` int(11) NOT NULL,
  `mail_recipient_id` int(11) NOT NULL,
  `sender_name` varchar(26) NOT NULL,
  `mail_title` varchar(32) NOT NULL,
  `mail_message` varchar(1000) NOT NULL,
  `unread` tinyint(4) NOT NULL DEFAULT '1',
  `attached_item_id` int(11) NOT NULL,
  `attached_kinah_count` bigint(20) NOT NULL,
  `express` tinyint(4) NOT NULL DEFAULT '0',
  `recieved_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`mail_unique_id`),
  KEY `mail_recipient_id` (`mail_recipient_id`),
  CONSTRAINT `FK_mail` FOREIGN KEY (`mail_recipient_id`) REFERENCES `players` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mail
-- ----------------------------

-- ----------------------------
-- Table structure for `network_ban`
-- ----------------------------
DROP TABLE IF EXISTS `network_ban`;
CREATE TABLE `network_ban` (
  `uniId` int(10) NOT NULL AUTO_INCREMENT,
  `ip` varchar(50) NOT NULL,
  `time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `details` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`uniId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of network_ban
-- ----------------------------

-- ----------------------------
-- Table structure for `old_names`
-- ----------------------------
DROP TABLE IF EXISTS `old_names`;
CREATE TABLE `old_names` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `player_id` int(11) NOT NULL,
  `old_name` varchar(50) NOT NULL,
  `new_name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of old_names
-- ----------------------------

-- ----------------------------
-- Table structure for `petitions`
-- ----------------------------
DROP TABLE IF EXISTS `petitions`;
CREATE TABLE `petitions` (
  `id` bigint(11) NOT NULL,
  `player_id` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `message` text NOT NULL,
  `add_data` varchar(255) DEFAULT NULL,
  `time` bigint(11) NOT NULL DEFAULT '0',
  `status` enum('PENDING','IN_PROGRESS','REPLIED') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of petitions
-- ----------------------------

-- ----------------------------
-- Table structure for `players`
-- ----------------------------
DROP TABLE IF EXISTS `players`;
CREATE TABLE `players` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `account_id` int(11) NOT NULL,
  `account_name` varchar(50) NOT NULL,
  `exp` bigint(20) NOT NULL DEFAULT '0',
  `recoverexp` bigint(20) NOT NULL DEFAULT '0',
  `x` float NOT NULL,
  `y` float NOT NULL,
  `z` float NOT NULL,
  `heading` int(11) NOT NULL,
  `world_id` int(11) NOT NULL,
  `world_owner` int(11) NOT NULL DEFAULT '0',
  `gender` enum('MALE','FEMALE') NOT NULL,
  `race` enum('ASMODIANS','ELYOS') NOT NULL,
  `player_class` enum('WARRIOR','GLADIATOR','TEMPLAR','SCOUT','ASSASSIN','RANGER','MAGE','SORCERER','SPIRIT_MASTER','PRIEST','CLERIC','CHANTER','ENGINEER','GUNNER','ARTIST','BARD','RIDER','ALL') NOT NULL,
  `creation_date` timestamp NULL DEFAULT NULL,
  `deletion_date` timestamp NULL DEFAULT NULL,
  `last_online` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `cube_expands` tinyint(1) NOT NULL DEFAULT '0',
  `advanced_stigma_slot_size` tinyint(1) NOT NULL DEFAULT '0',
  `warehouse_size` tinyint(1) NOT NULL DEFAULT '0',
  `mailbox_letters` tinyint(4) unsigned NOT NULL DEFAULT '0',
  `title_id` int(3) NOT NULL DEFAULT '-1',
  `bonus_title_id` int(3) NOT NULL DEFAULT '-1',
  `dp` int(3) NOT NULL DEFAULT '0',
  `soul_sickness` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `reposte_energy` bigint(20) NOT NULL DEFAULT '0',
  `goldenstar_energy` bigint(20) NOT NULL DEFAULT '0',
  `growth_energy` bigint(20) NOT NULL DEFAULT '0',
  `bg_points` int(11) NOT NULL DEFAULT '0',
  `online` tinyint(1) NOT NULL DEFAULT '0',
  `note` text,
  `mentor_flag_time` int(11) NOT NULL DEFAULT '0',
  `initial_gamestats` int(11) NOT NULL DEFAULT '0',
  `last_transfer_time` decimal(20,0) NOT NULL DEFAULT '0',
  `fatigue` int(11) NOT NULL DEFAULT '0',
  `fatigueRecover` int(11) NOT NULL DEFAULT '0',
  `fatigueReset` int(11) NOT NULL DEFAULT '0',
  `stamps` int(11) NOT NULL DEFAULT '0',
  `last_stamp` timestamp NULL DEFAULT NULL,
  `rewarded_pass` int(1) NOT NULL DEFAULT '0',
  `joinRequestLegionId` int(11) NOT NULL DEFAULT '0',
  `joinRequestState` enum('NONE','DENIED','ACCEPTED') NOT NULL DEFAULT 'NONE',
  `frenzy_points` int(4) NOT NULL DEFAULT '0' COMMENT 'Upgrade Arcade FrenzyPoints',
  `frenzy_count` int(1) NOT NULL DEFAULT '0',
  `show_inventory` int(1) NOT NULL DEFAULT '1',
  `bonus_buff_time` timestamp NULL DEFAULT NULL,
  `bonus_type` enum('RETURN','NEW','NORMAL') NOT NULL DEFAULT 'NORMAL',
  `luna_coins` bigint(20) NOT NULL,
  `wardrobe_size` int(4) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_unique` (`name`),
  KEY `account_id` (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of players
-- ----------------------------

-- ----------------------------
-- Table structure for `player_appearance`
-- ----------------------------
DROP TABLE IF EXISTS `player_appearance`;
CREATE TABLE `player_appearance` (
  `player_id` int(11) NOT NULL,
  `voice` int(11) NOT NULL,
  `skin_rgb` int(11) NOT NULL,
  `hair_rgb` int(11) NOT NULL,
  `eye_rgb` int(11) NOT NULL,
  `lip_rgb` int(11) NOT NULL,
  `face` int(11) NOT NULL,
  `hair` int(11) NOT NULL,
  `deco` int(11) NOT NULL,
  `tattoo` int(11) NOT NULL,
  `face_contour` int(11) NOT NULL,
  `expression` int(11) NOT NULL,
  `pupil_shape` int(11) NOT NULL,
  `remove_mane` int(11) NOT NULL,
  `right_eye_rgb` int(11) NOT NULL,
  `eye_lash_shape` int(11) NOT NULL,
  `jaw_line` int(11) NOT NULL,
  `forehead` int(11) NOT NULL,
  `eye_height` int(11) NOT NULL,
  `eye_space` int(11) NOT NULL,
  `eye_width` int(11) NOT NULL,
  `eye_size` int(11) NOT NULL,
  `eye_shape` int(11) NOT NULL,
  `eye_angle` int(11) NOT NULL,
  `brow_height` int(11) NOT NULL,
  `brow_angle` int(11) NOT NULL,
  `brow_shape` int(11) NOT NULL,
  `nose` int(11) NOT NULL,
  `nose_bridge` int(11) NOT NULL,
  `nose_width` int(11) NOT NULL,
  `nose_tip` int(11) NOT NULL,
  `cheek` int(11) NOT NULL,
  `lip_height` int(11) NOT NULL,
  `mouth_size` int(11) NOT NULL,
  `lip_size` int(11) NOT NULL,
  `smile` int(11) NOT NULL,
  `lip_shape` int(11) NOT NULL,
  `jaw_height` int(11) NOT NULL,
  `chin_jut` int(11) NOT NULL,
  `ear_shape` int(11) NOT NULL,
  `head_size` int(11) NOT NULL,
  `neck` int(11) NOT NULL,
  `neck_length` int(11) NOT NULL,
  `shoulder_size` int(11) NOT NULL,
  `torso` int(11) NOT NULL,
  `chest` int(11) NOT NULL,
  `waist` int(11) NOT NULL,
  `hips` int(11) NOT NULL,
  `arm_thickness` int(11) NOT NULL,
  `hand_size` int(11) NOT NULL,
  `leg_thickness` int(11) NOT NULL,
  `facial_rate` int(11) NOT NULL,
  `foot_size` int(11) NOT NULL,
  `arm_length` int(11) NOT NULL,
  `leg_length` int(11) NOT NULL,
  `shoulders` int(11) NOT NULL,
  `face_shape` int(11) NOT NULL,
  `pupil_size` int(11) NOT NULL,
  `upper_torso` int(11) NOT NULL,
  `fore_arm_thickness` int(11) NOT NULL,
  `hand_span` int(11) NOT NULL,
  `calf_thickness` int(11) NOT NULL,
  `height` float NOT NULL,
  PRIMARY KEY (`player_id`),
  CONSTRAINT `player_id_fk` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_appearance
-- ----------------------------

-- ----------------------------
-- Table structure for `player_bind_point`
-- ----------------------------
DROP TABLE IF EXISTS `player_bind_point`;
CREATE TABLE `player_bind_point` (
  `player_id` int(11) NOT NULL,
  `map_id` int(11) NOT NULL,
  `x` float NOT NULL,
  `y` float NOT NULL,
  `z` float NOT NULL,
  `heading` int(3) NOT NULL,
  PRIMARY KEY (`player_id`),
  CONSTRAINT `player_bind_point_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_bind_point
-- ----------------------------

-- ----------------------------
-- Table structure for `player_cooldowns`
-- ----------------------------
DROP TABLE IF EXISTS `player_cooldowns`;
CREATE TABLE `player_cooldowns` (
  `player_id` int(11) NOT NULL,
  `cooldown_id` int(6) NOT NULL,
  `reuse_delay` bigint(13) NOT NULL,
  PRIMARY KEY (`player_id`,`cooldown_id`),
  CONSTRAINT `player_cooldowns_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_cooldowns
-- ----------------------------

-- ----------------------------
-- Table structure for `player_effects`
-- ----------------------------
DROP TABLE IF EXISTS `player_effects`;
CREATE TABLE `player_effects` (
  `player_id` int(11) NOT NULL,
  `skill_id` int(11) NOT NULL,
  `skill_lvl` tinyint(4) NOT NULL,
  `current_time` int(11) NOT NULL,
  `end_time` bigint(13) NOT NULL,
  PRIMARY KEY (`player_id`,`skill_id`),
  CONSTRAINT `player_effects_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_effects
-- ----------------------------

-- ----------------------------
-- Table structure for `player_emotions`
-- ----------------------------
DROP TABLE IF EXISTS `player_emotions`;
CREATE TABLE `player_emotions` (
  `player_id` int(11) NOT NULL,
  `emotion` int(11) NOT NULL,
  `remaining` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`player_id`,`emotion`),
  CONSTRAINT `player_emotions_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_emotions
-- ----------------------------

-- ----------------------------
-- Table structure for `player_game_stats`
-- ----------------------------
DROP TABLE IF EXISTS `player_game_stats`;
CREATE TABLE `player_game_stats` (
  `player_id` int(11) NOT NULL,
  `defense_physic` int(11) NOT NULL DEFAULT '1',
  `block` int(11) NOT NULL DEFAULT '1',
  `parry` int(11) NOT NULL DEFAULT '1',
  `magical_critical` int(11) NOT NULL DEFAULT '1',
  `evasion` int(11) NOT NULL DEFAULT '1',
  `precision` int(11) NOT NULL DEFAULT '1',
  `attack` int(11) NOT NULL DEFAULT '1',
  `magical_precision` int(11) NOT NULL DEFAULT '1',
  `attack_speed` int(11) NOT NULL DEFAULT '1',
  `magical_resist` int(11) NOT NULL DEFAULT '1',
  `magical_attack` int(11) NOT NULL DEFAULT '1',
  `main_hand_magical_attack` int(11) NOT NULL DEFAULT '1',
  `off_hand_magical_attack` int(11) NOT NULL DEFAULT '1',
  `physical_critical` int(11) NOT NULL DEFAULT '1',
  `attack_range` int(11) NOT NULL DEFAULT '1',
  `magical_defense` int(11) NOT NULL DEFAULT '1',
  `agility` int(11) NOT NULL DEFAULT '1',
  `knowledge` int(11) NOT NULL DEFAULT '1',
  `will` int(11) NOT NULL DEFAULT '1',
  `magical_boost` int(11) NOT NULL DEFAULT '1',
  `magical_boost_resist` int(11) NOT NULL DEFAULT '1',
  `physical_critical_resist` int(11) NOT NULL DEFAULT '1',
  `magical_critical_resist` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`player_id`),
  CONSTRAINT `player_game_stats` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_game_stats
-- ----------------------------

-- ----------------------------
-- Table structure for `player_life_stats`
-- ----------------------------
DROP TABLE IF EXISTS `player_life_stats`;
CREATE TABLE `player_life_stats` (
  `player_id` int(11) NOT NULL,
  `hp` int(11) NOT NULL DEFAULT '1',
  `mp` int(11) NOT NULL DEFAULT '1',
  `fp` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`player_id`),
  CONSTRAINT `FK_player_life_stats` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_life_stats
-- ----------------------------

-- ----------------------------
-- Table structure for `player_macrosses`
-- ----------------------------
DROP TABLE IF EXISTS `player_macrosses`;
CREATE TABLE `player_macrosses` (
  `player_id` int(11) NOT NULL,
  `order` int(3) NOT NULL,
  `macro` text NOT NULL,
  UNIQUE KEY `main` (`player_id`,`order`),
  CONSTRAINT `player_macrosses_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_macrosses
-- ----------------------------

-- ----------------------------
-- Table structure for `player_motions`
-- ----------------------------
DROP TABLE IF EXISTS `player_motions`;
CREATE TABLE `player_motions` (
  `player_id` int(11) NOT NULL,
  `motion_id` int(3) NOT NULL,
  `time` int(11) NOT NULL DEFAULT '0',
  `active` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`player_id`,`motion_id`) USING BTREE,
  CONSTRAINT `motions_player_id_fk` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_motions
-- ----------------------------

-- ----------------------------
-- Table structure for `player_npc_factions`
-- ----------------------------
DROP TABLE IF EXISTS `player_npc_factions`;
CREATE TABLE `player_npc_factions` (
  `player_id` int(11) NOT NULL,
  `faction_id` int(2) NOT NULL,
  `active` tinyint(1) NOT NULL,
  `time` int(11) NOT NULL,
  `state` enum('NOTING','START','COMPLETE') NOT NULL DEFAULT 'NOTING',
  `quest_id` int(6) NOT NULL DEFAULT '0',
  PRIMARY KEY (`player_id`,`faction_id`),
  CONSTRAINT `player_npc_factions_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_npc_factions
-- ----------------------------

-- ----------------------------
-- Table structure for `player_passkey`
-- ----------------------------
DROP TABLE IF EXISTS `player_passkey`;
CREATE TABLE `player_passkey` (
  `account_id` int(11) NOT NULL,
  `passkey` varchar(32) NOT NULL DEFAULT '',
  PRIMARY KEY (`account_id`,`passkey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_passkey
-- ----------------------------

-- ----------------------------
-- Table structure for `player_passports`
-- ----------------------------
DROP TABLE IF EXISTS `player_passports`;
CREATE TABLE `player_passports` (
  `account_id` int(11) NOT NULL,
  `passport_id` int(11) NOT NULL,
  `stamps` int(11) NOT NULL DEFAULT '0',
  `last_stamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `rewarded` tinyint(1) NOT NULL DEFAULT '0',
  UNIQUE KEY `account_passport` (`account_id`,`passport_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_passports
-- ----------------------------

-- ----------------------------
-- Table structure for `player_pets`
-- ----------------------------
DROP TABLE IF EXISTS `player_pets`;
CREATE TABLE `player_pets` (
  `player_id` int(11) NOT NULL,
  `pet_id` int(11) NOT NULL,
  `decoration` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `hungry_level` tinyint(4) NOT NULL DEFAULT '0',
  `feed_progress` int(11) NOT NULL DEFAULT '0',
  `reuse_time` bigint(20) NOT NULL DEFAULT '0',
  `birthday` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `mood_started` bigint(20) NOT NULL DEFAULT '0',
  `counter` int(11) NOT NULL DEFAULT '0',
  `mood_cd_started` bigint(20) NOT NULL DEFAULT '0',
  `gift_cd_started` bigint(20) NOT NULL DEFAULT '0',
  `dopings` varchar(80) CHARACTER SET ascii DEFAULT NULL,
  `despawn_time` timestamp NULL DEFAULT NULL,
  `expire_time` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`player_id`,`pet_id`),
  CONSTRAINT `FK_player_pets` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_pets
-- ----------------------------

-- ----------------------------
-- Table structure for `player_punishments`
-- ----------------------------
DROP TABLE IF EXISTS `player_punishments`;
CREATE TABLE `player_punishments` (
  `player_id` int(11) NOT NULL,
  `punishment_type` enum('PRISON','GATHER','CHARBAN') NOT NULL,
  `start_time` int(10) unsigned DEFAULT '0',
  `duration` int(10) unsigned DEFAULT '0',
  `reason` text,
  PRIMARY KEY (`player_id`,`punishment_type`),
  CONSTRAINT `player_punishments_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_punishments
-- ----------------------------

-- ----------------------------
-- Table structure for `player_quests`
-- ----------------------------
DROP TABLE IF EXISTS `player_quests`;
CREATE TABLE `player_quests` (
  `player_id` int(11) NOT NULL,
  `quest_id` int(10) unsigned NOT NULL DEFAULT '0',
  `status` varchar(10) NOT NULL DEFAULT 'NONE',
  `quest_vars` int(10) unsigned NOT NULL DEFAULT '0',
  `complete_count` int(3) unsigned NOT NULL DEFAULT '0',
  `next_repeat_time` timestamp NULL DEFAULT NULL,
  `reward` smallint(3) DEFAULT NULL,
  `complete_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`player_id`,`quest_id`),
  CONSTRAINT `player_quests_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_quests
-- ----------------------------

-- ----------------------------
-- Table structure for `player_recipes`
-- ----------------------------
DROP TABLE IF EXISTS `player_recipes`;
CREATE TABLE `player_recipes` (
  `player_id` int(11) NOT NULL,
  `recipe_id` int(11) NOT NULL,
  PRIMARY KEY (`player_id`,`recipe_id`),
  CONSTRAINT `player_recipes_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_recipes
-- ----------------------------

-- ----------------------------
-- Table structure for `player_registered_items`
-- ----------------------------
DROP TABLE IF EXISTS `player_registered_items`;
CREATE TABLE `player_registered_items` (
  `player_id` int(10) NOT NULL,
  `item_unique_id` int(10) NOT NULL,
  `item_id` int(10) NOT NULL,
  `expire_time` int(20) DEFAULT NULL,
  `color` int(11) DEFAULT NULL,
  `color_expires` int(11) NOT NULL DEFAULT '0',
  `owner_use_count` int(10) NOT NULL DEFAULT '0',
  `visitor_use_count` int(10) NOT NULL DEFAULT '0',
  `x` float NOT NULL DEFAULT '0',
  `y` float NOT NULL DEFAULT '0',
  `z` float NOT NULL DEFAULT '0',
  `h` smallint(3) DEFAULT NULL,
  `area` enum('NONE','INTERIOR','EXTERIOR','ALL','DECOR') NOT NULL DEFAULT 'NONE',
  `floor` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`player_id`,`item_unique_id`,`item_id`),
  CONSTRAINT `player_regitems_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_registered_items
-- ----------------------------

-- ----------------------------
-- Table structure for `player_settings`
-- ----------------------------
DROP TABLE IF EXISTS `player_settings`;
CREATE TABLE `player_settings` (
  `player_id` int(11) NOT NULL,
  `settings_type` tinyint(1) NOT NULL,
  `settings` blob NOT NULL,
  PRIMARY KEY (`player_id`,`settings_type`),
  CONSTRAINT `ps_pl_fk` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_settings
-- ----------------------------

-- ----------------------------
-- Table structure for `player_skills`
-- ----------------------------
DROP TABLE IF EXISTS `player_skills`;
CREATE TABLE `player_skills` (
  `player_id` int(11) NOT NULL,
  `skill_id` int(11) NOT NULL,
  `skill_level` int(3) NOT NULL DEFAULT '1',
  PRIMARY KEY (`player_id`,`skill_id`),
  CONSTRAINT `player_skills_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_skills
-- ----------------------------

-- ----------------------------
-- Table structure for `player_titles`
-- ----------------------------
DROP TABLE IF EXISTS `player_titles`;
CREATE TABLE `player_titles` (
  `player_id` int(11) NOT NULL,
  `title_id` int(11) NOT NULL,
  `remaining` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`player_id`,`title_id`),
  CONSTRAINT `player_titles_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_titles
-- ----------------------------

-- ----------------------------
-- Table structure for `player_vars`
-- ----------------------------
DROP TABLE IF EXISTS `player_vars`;
CREATE TABLE `player_vars` (
  `player_id` int(11) NOT NULL,
  `param` varchar(255) NOT NULL,
  `value` varchar(255) DEFAULT NULL,
  `time` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`player_id`,`param`),
  CONSTRAINT `player_vars_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_vars
-- ----------------------------

-- ----------------------------
-- Table structure for `player_world_bans`
-- ----------------------------
DROP TABLE IF EXISTS `player_world_bans`;
CREATE TABLE `player_world_bans` (
  `player` int(11) NOT NULL,
  `by` varchar(255) NOT NULL,
  `duration` bigint(11) NOT NULL,
  `date` bigint(11) NOT NULL,
  `reason` varchar(255) NOT NULL,
  PRIMARY KEY (`player`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_world_bans
-- ----------------------------

-- ----------------------------
-- Table structure for `portal_cooldowns`
-- ----------------------------
DROP TABLE IF EXISTS `portal_cooldowns`;
CREATE TABLE `portal_cooldowns` (
  `player_id` int(11) NOT NULL,
  `world_id` int(11) NOT NULL,
  `reuse_time` bigint(13) NOT NULL,
  `entry_count` int(2) NOT NULL,
  PRIMARY KEY (`player_id`,`world_id`),
  CONSTRAINT `portal_cooldowns_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of portal_cooldowns
-- ----------------------------

-- ----------------------------
-- Table structure for `server_variables`
-- ----------------------------
DROP TABLE IF EXISTS `server_variables`;
CREATE TABLE `server_variables` (
  `key` varchar(30) NOT NULL,
  `value` varchar(30) NOT NULL,
  PRIMARY KEY (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of server_variables
-- ----------------------------
INSERT INTO `server_variables` VALUES ('time', '3341');

-- ----------------------------
-- Table structure for `siege_locations`
-- ----------------------------
DROP TABLE IF EXISTS `siege_locations`;
CREATE TABLE `siege_locations` (
  `id` int(11) NOT NULL,
  `race` enum('ELYOS','ASMODIANS','BALAUR') NOT NULL,
  `legion_id` int(11) NOT NULL,
  `occupy_count` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of siege_locations
-- ----------------------------

-- ----------------------------
-- Table structure for `siege_spawns`
-- ----------------------------
DROP TABLE IF EXISTS `siege_spawns`;
CREATE TABLE `siege_spawns` (
  `spawn_id` int(10) NOT NULL,
  `siege_id` int(10) NOT NULL,
  `race` enum('ELYOS','ASMODIANS','BALAUR') NOT NULL,
  `protector` int(10) DEFAULT '0',
  `stype` enum('PEACE','GUARD','ARTIFACT','PROTECTOR','MINE','PORTAL','GENERATOR','SPRING','RACEPROTECTOR','UNDERPASS') DEFAULT NULL,
  PRIMARY KEY (`spawn_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of siege_spawns
-- ----------------------------

-- ----------------------------
-- Table structure for `skill_motions`
-- ----------------------------
DROP TABLE IF EXISTS `skill_motions`;
CREATE TABLE `skill_motions` (
  `motion_name` varchar(255) NOT NULL DEFAULT '',
  `skill_id` int(11) NOT NULL,
  `attack_speed` int(11) NOT NULL,
  `weapon_type` varchar(255) NOT NULL,
  `off_weapon_type` varchar(255) NOT NULL,
  `time` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`motion_name`,`skill_id`,`attack_speed`,`weapon_type`,`off_weapon_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of skill_motions
-- ----------------------------

-- ----------------------------
-- Table structure for `spawns`
-- ----------------------------
DROP TABLE IF EXISTS `spawns`;
CREATE TABLE `spawns` (
  `spawn_id` int(10) NOT NULL AUTO_INCREMENT,
  `npc_id` int(10) NOT NULL,
  `npc_name` varchar(50) NOT NULL DEFAULT '',
  `map_id` int(10) NOT NULL,
  `pool_size` int(5) NOT NULL DEFAULT '1',
  `anchor` varchar(100) DEFAULT NULL,
  `handler` enum('RIFT','STATIC') DEFAULT NULL,
  `spawn_time` enum('ALL','DAY','NIGHT') NOT NULL DEFAULT 'ALL',
  `walker_id` int(10) NOT NULL DEFAULT '0',
  `random_walk` int(10) NOT NULL DEFAULT '0',
  `static_id` int(10) NOT NULL DEFAULT '0',
  `fly` tinyint(1) NOT NULL DEFAULT '0',
  `respawn_time` int(10) NOT NULL DEFAULT '0',
  `last_despawn_time` timestamp NULL DEFAULT NULL,
  `date_added` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `author` varchar(50) NOT NULL DEFAULT 'system',
  PRIMARY KEY (`spawn_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of spawns
-- ----------------------------

-- ----------------------------
-- Table structure for `surveys`
-- ----------------------------
DROP TABLE IF EXISTS `surveys`;
CREATE TABLE `surveys` (
  `unique_id` int(11) NOT NULL AUTO_INCREMENT,
  `owner_id` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `item_count` decimal(20,0) NOT NULL DEFAULT '1',
  `html_text` text NOT NULL,
  `html_radio` varchar(100) NOT NULL DEFAULT 'accept',
  `used` tinyint(1) NOT NULL DEFAULT '0',
  `used_time` varchar(100) NOT NULL DEFAULT '',
  PRIMARY KEY (`unique_id`),
  KEY `owner_id` (`owner_id`),
  CONSTRAINT `surveys_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of surveys
-- ----------------------------

-- ----------------------------
-- Table structure for `tasks`
-- ----------------------------
DROP TABLE IF EXISTS `tasks`;
CREATE TABLE `tasks` (
  `id` int(5) NOT NULL,
  `task_type` enum('SHUTDOWN','RESTART') NOT NULL,
  `trigger_type` enum('FIXED_IN_TIME') NOT NULL,
  `trigger_param` text NOT NULL,
  `exec_param` text,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tasks
-- ----------------------------
INSERT INTO `tasks` VALUES ('1', 'RESTART', 'FIXED_IN_TIME', '04:20:00', '60 5 320');

-- ----------------------------
-- Table structure for `towns`
-- ----------------------------
DROP TABLE IF EXISTS `towns`;
CREATE TABLE `towns` (
  `id` int(11) NOT NULL,
  `level` int(11) NOT NULL DEFAULT '1',
  `points` int(10) NOT NULL DEFAULT '0',
  `race` enum('ELYOS','ASMODIANS') NOT NULL,
  `level_up_date` timestamp NOT NULL DEFAULT '1970-01-01 07:00:01',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of towns
-- ----------------------------

-- ----------------------------
-- Table structure for `web_reward`
-- ----------------------------
DROP TABLE IF EXISTS `web_reward`;
CREATE TABLE `web_reward` (
  `unique` int(11) NOT NULL AUTO_INCREMENT,
  `item_owner` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `item_count` decimal(20,0) NOT NULL DEFAULT '1',
  `rewarded` tinyint(1) NOT NULL DEFAULT '0',
  `added` varchar(70) DEFAULT '',
  `received` varchar(70) DEFAULT '',
  PRIMARY KEY (`unique`),
  KEY `item_owner` (`item_owner`),
  CONSTRAINT `web_reward_ibfk_1` FOREIGN KEY (`item_owner`) REFERENCES `players` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of web_reward
-- ----------------------------

-- ----------------------------
-- Table structure for `weddings`
-- ----------------------------
DROP TABLE IF EXISTS `weddings`;
CREATE TABLE `weddings` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `player1` int(11) NOT NULL,
  `player2` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `player1` (`player1`),
  KEY `player2` (`player2`),
  CONSTRAINT `weddings_ibfk_1` FOREIGN KEY (`player1`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `weddings_ibfk_2` FOREIGN KEY (`player2`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of weddings
-- ----------------------------
