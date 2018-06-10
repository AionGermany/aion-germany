/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50133
 Source Host           : localhost:3306
 Source Schema         : al_server_gs

 Target Server Type    : MySQL
 Target Server Version : 50133
 File Encoding         : 65001

 Date: 08/06/2018 19:25:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for player_events_window
-- ----------------------------
DROP TABLE IF EXISTS `player_events_window`;
CREATE TABLE `player_events_window`  (
  `account_id` int(11) NULL DEFAULT NULL,
  `event_id` int(11) NULL DEFAULT NULL,
  `last_stamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `elapsed` double(11, 0) NULL DEFAULT 0
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
