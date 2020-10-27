DROP TABLE IF EXISTS `real_item_rnd_bonus`;
CREATE TABLE `real_item_rnd_bonus` (
  `id` int(100) NOT NULL AUTO_INCREMENT,
  `item_obj_id` int(11) NOT NULL DEFAULT '0',
  `stat_name` varchar(50) DEFAULT NULL,
  `stat_val` int(11) DEFAULT NULL,
  `is_fusion` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

