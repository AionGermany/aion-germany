/*
Navicat MySQL Data Transfer

Source Server         : Localhost
Source Server Version : 50616
Source Host           : localhost:3306
Source Database       : al45_server_ws

Target Server Type    : MYSQL
Target Server Version : 50616
File Encoding         : 65001

Date: 2015-01-26 04:09:09
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for news
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news` (
  `id_news` int(11) NOT NULL AUTO_INCREMENT,
  `title` text,
  `date` text,
  `content` text,
  PRIMARY KEY (`id_news`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of news
-- ----------------------------
INSERT INTO `news` VALUES ('29', 'Web for Aion Server', null,
 '<div style=\"text-align: center;\"><img src=\"img/news/news_1post.png\"><br><div style=\"text-align: left;\">The Web binding is based on Server Aion Web 1.1.
 The main work has focused on mutations both design and that was done. Some added features. Do not remove the logo SAW<br><br><span style=\"font-weight: bold;\">Key features:</span><br><span style=\"font-weight: bold;\">
 Functions:</span><br>• Registration page protected bots<br>• Liny Account<br>• Admin panel with server settings and other...<br>• Change Account Password<br>•
 Change E-Mail<br>• News management System <br>• Integration with aiondatabase.com and aion.yg.com<br>• Drop list server<br>• Automatic Plant<br><br><span style=\"font-weight: bold;\">
 Information:</span><br>• Displays information about the status of the server and the number of players online<br>• Statistics with the number of accounts, characters, GM-s, legions on 
 the server<br>• List of GMs in Game<br>• Displays detailed information on individual player<br>• Page with Players in Game<br>• Page with the 100 best players in the Server<br>• 
 Page rating legions<br>• Page from the richest players server<br>• Page rating abyss<br>• Page for downloading files<br></div></div>');
