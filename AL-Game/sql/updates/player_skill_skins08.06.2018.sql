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

 Date: 08/06/2018 19:32:05
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for player_skill_skins
-- ----------------------------
DROP TABLE IF EXISTS `player_skill_skins`;
CREATE TABLE `player_skill_skins`  (
  `player_id` int(11) NULL DEFAULT 0,
  `skin_id` int(11) NULL DEFAULT 0,
  `remaining` bigint(22) NULL DEFAULT 0,
  `active` int(1) NULL DEFAULT 0
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
