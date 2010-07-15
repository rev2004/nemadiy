# Sequel Pro dump
# Version 2210
# http://code.google.com/p/sequel-pro
#
# Host: nema.lis.uiuc.edu (MySQL 5.0.77)
# Database: nemadatarepository040
# Generation Time: 2010-07-15 15:42:00 +0100
# ************************************************************

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table collection
# ------------------------------------------------------------

DROP TABLE IF EXISTS `collection`;

CREATE TABLE `collection` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(128) NOT NULL,
  `description` text NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;



# Dump of table collection_track_link
# ------------------------------------------------------------

DROP TABLE IF EXISTS `collection_track_link`;

CREATE TABLE `collection_track_link` (
  `collection_id` int(11) NOT NULL,
  `track_id` varchar(32) default '',
  UNIQUE KEY `collection_id` (`collection_id`,`track_id`),
  KEY `track_id` (`track_id`),
  CONSTRAINT `collection_id_track_link` FOREIGN KEY (`collection_id`) REFERENCES `collection` (`id`),
  CONSTRAINT `collection_track_link_ibfk_1` FOREIGN KEY (`track_id`) REFERENCES `track` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table dataset
# ------------------------------------------------------------

DROP TABLE IF EXISTS `dataset`;

CREATE TABLE `dataset` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(128) NOT NULL default '',
  `description` text NOT NULL,
  `collection_id` int(11) NOT NULL default '1',
  `subset_set_id` int(11) default NULL,
  `num_splits` int(11) NOT NULL default '1',
  `num_set_per_split` int(11) default '1',
  `split_class` varchar(255) default NULL,
  `split_parameters_string` text,
  `subject_track_metadata_type_id` int(11) NOT NULL default '-1',
  `filter_track_metadata_type_id` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `collection_id` (`collection_id`),
  KEY `name` (`name`),
  KEY `subject_track_metadata_id` (`subject_track_metadata_type_id`),
  KEY `subset_set_id` (`subset_set_id`),
  CONSTRAINT `dataset_ibfk_1` FOREIGN KEY (`subject_track_metadata_type_id`) REFERENCES `track_metadata_definitions` (`id`),
  CONSTRAINT `dataset_ibfk_2` FOREIGN KEY (`collection_id`) REFERENCES `collection` (`id`),
  CONSTRAINT `dataset_ibfk_4` FOREIGN KEY (`subset_set_id`) REFERENCES `trackList` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8;



# Dump of table file
# ------------------------------------------------------------

DROP TABLE IF EXISTS `file`;

CREATE TABLE `file` (
  `id` int(11) NOT NULL auto_increment,
  `track_id` varchar(32) NOT NULL,
  `path` varchar(512) NOT NULL,
  `site` int(11) NOT NULL default '1',
  PRIMARY KEY  (`id`),
  KEY `track_id` (`track_id`),
  KEY `path` (`path`(255)),
  KEY `site` (`site`),
  CONSTRAINT `file_ibfk_1` FOREIGN KEY (`track_id`) REFERENCES `track` (`id`) ON DELETE CASCADE,
  CONSTRAINT `file_ibfk_2` FOREIGN KEY (`site`) REFERENCES `site` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=247403 DEFAULT CHARSET=utf8;



# Dump of table file_file_metadata_link
# ------------------------------------------------------------

DROP TABLE IF EXISTS `file_file_metadata_link`;

CREATE TABLE `file_file_metadata_link` (
  `id` int(11) NOT NULL auto_increment,
  `file_id` int(11) NOT NULL default '-1',
  `file_metadata_id` int(11) NOT NULL default '-1',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `file_id` (`file_id`,`file_metadata_id`),
  UNIQUE KEY `file_metadata_id` (`file_metadata_id`,`file_id`),
  CONSTRAINT `file_file_metadata_link_ibfk_1` FOREIGN KEY (`file_id`) REFERENCES `file` (`id`) ON DELETE CASCADE,
  CONSTRAINT `file_metadata_id` FOREIGN KEY (`file_metadata_id`) REFERENCES `file_metadata` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1025397 DEFAULT CHARSET=utf8;



# Dump of table file_metadata
# ------------------------------------------------------------

DROP TABLE IF EXISTS `file_metadata`;

CREATE TABLE `file_metadata` (
  `id` int(11) NOT NULL auto_increment,
  `metadata_type_id` int(11) NOT NULL default '-1',
  `value` varchar(180) NOT NULL default '',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `metadata_type_id` (`metadata_type_id`,`value`),
  UNIQUE KEY `value` (`value`,`metadata_type_id`),
  CONSTRAINT `file_metadata_metadata_type_id` FOREIGN KEY (`metadata_type_id`) REFERENCES `file_metadata_definitions` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=208 DEFAULT CHARSET=utf8;



# Dump of table file_metadata_definitions
# ------------------------------------------------------------

DROP TABLE IF EXISTS `file_metadata_definitions`;

CREATE TABLE `file_metadata_definitions` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(128) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;



# Dump of table legacy_file_paths
# ------------------------------------------------------------

DROP TABLE IF EXISTS `legacy_file_paths`;

CREATE TABLE `legacy_file_paths` (
  `file_id` int(11) NOT NULL,
  `old_path` varchar(256) NOT NULL,
  KEY `file_id` (`file_id`),
  KEY `old_path` (`old_path`(255)),
  CONSTRAINT `legacy_file_paths_ibfk_1` FOREIGN KEY (`file_id`) REFERENCES `file` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table published_results
# ------------------------------------------------------------

DROP TABLE IF EXISTS `published_results`;

CREATE TABLE `published_results` (
  `id` int(11) NOT NULL auto_increment,
  `dataset_id` int(11) NOT NULL default '-1',
  `system_name` varchar(128) NOT NULL,
  `result_path` varchar(512) NOT NULL,
  `username` varchar(128) NOT NULL default '',
  `last_updated` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `username` (`username`,`dataset_id`,`system_name`),
  KEY `dataset_id` (`dataset_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;



# Dump of table published_task_results
# ------------------------------------------------------------

DROP TABLE IF EXISTS `published_task_results`;

CREATE TABLE `published_task_results` (
  `id` int(11) NOT NULL auto_increment,
  `task_id` int(11) NOT NULL default '-1',
  `system_name` varchar(128) NOT NULL,
  `result_path` varchar(512) NOT NULL,
  `submission_code` varchar(128) NOT NULL default '',
  `last_updated` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `set_id` int(11) NOT NULL default '-1',
  `file_type` varchar(128) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `submission_code` (`submission_code`),
  KEY `set_id` (`set_id`),
  KEY `task_id` (`task_id`),
  CONSTRAINT `published_task_results_ibfk_2` FOREIGN KEY (`set_id`) REFERENCES `trackList` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table site
# ------------------------------------------------------------

DROP TABLE IF EXISTS `site`;

CREATE TABLE `site` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(128) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `site_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;



# Dump of table task
# ------------------------------------------------------------

DROP TABLE IF EXISTS `task`;

CREATE TABLE `task` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(128) NOT NULL,
  `description` text NOT NULL,
  `subject_track_metadata` int(11) NOT NULL,
  `dataset_id` int(11) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `subject_track_metadata` (`subject_track_metadata`),
  KEY `dataset_id` (`dataset_id`),
  CONSTRAINT `task_ibfk_2` FOREIGN KEY (`subject_track_metadata`) REFERENCES `track_metadata_definitions` (`id`),
  CONSTRAINT `task_ibfk_3` FOREIGN KEY (`dataset_id`) REFERENCES `dataset` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;



# Dump of table track
# ------------------------------------------------------------

DROP TABLE IF EXISTS `track`;

CREATE TABLE `track` (
  `id` varchar(32) NOT NULL default '',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table track_metadata
# ------------------------------------------------------------

DROP TABLE IF EXISTS `track_metadata`;

CREATE TABLE `track_metadata` (
  `id` int(11) NOT NULL auto_increment,
  `metadata_type_id` int(11) NOT NULL default '-1',
  `value` mediumtext NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `metadata_type_id` (`metadata_type_id`),
  CONSTRAINT `track_metadata_ibfk_1` FOREIGN KEY (`metadata_type_id`) REFERENCES `track_metadata_definitions` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=213226 DEFAULT CHARSET=utf8;



# Dump of table track_metadata_definitions
# ------------------------------------------------------------

DROP TABLE IF EXISTS `track_metadata_definitions`;

CREATE TABLE `track_metadata_definitions` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(128) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;



# Dump of table track_track_metadata_link
# ------------------------------------------------------------

DROP TABLE IF EXISTS `track_track_metadata_link`;

CREATE TABLE `track_track_metadata_link` (
  `id` int(11) NOT NULL auto_increment,
  `track_id` varchar(32) NOT NULL default '-1',
  `track_metadata_id` int(11) NOT NULL default '-1',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `track_id` (`track_id`,`track_metadata_id`),
  KEY `track_metadata_id` (`track_metadata_id`),
  CONSTRAINT `track_track_metadata_link_ibfk_1` FOREIGN KEY (`track_id`) REFERENCES `track` (`id`) ON DELETE CASCADE,
  CONSTRAINT `track_track_metadata_link_track_metadata_id` FOREIGN KEY (`track_metadata_id`) REFERENCES `track_metadata` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=405169 DEFAULT CHARSET=utf8;



# Dump of table trackList
# ------------------------------------------------------------

DROP TABLE IF EXISTS `trackList`;

CREATE TABLE `trackList` (
  `id` int(11) NOT NULL auto_increment,
  `dataset_id` int(11) default NULL,
  `set_type_id` int(11) default NULL,
  `split_number` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `set_type_id` (`set_type_id`),
  KEY `dataset_id` (`dataset_id`),
  CONSTRAINT `set_type_id` FOREIGN KEY (`set_type_id`) REFERENCES `trackList_type_definitions` (`id`),
  CONSTRAINT `trackList_ibfk_1` FOREIGN KEY (`dataset_id`) REFERENCES `dataset` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=132 DEFAULT CHARSET=utf8;



# Dump of table trackList_track_link
# ------------------------------------------------------------

DROP TABLE IF EXISTS `trackList_track_link`;

CREATE TABLE `trackList_track_link` (
  `set_id` int(11) default NULL,
  `track_id` varchar(32) NOT NULL default '',
  UNIQUE KEY `set_id` (`set_id`,`track_id`),
  KEY `set_track_link_track_id` (`track_id`),
  CONSTRAINT `trackList_track_link_ibfk_1` FOREIGN KEY (`set_id`) REFERENCES `trackList` (`id`) ON DELETE CASCADE,
  CONSTRAINT `trackList_track_link_ibfk_2` FOREIGN KEY (`track_id`) REFERENCES `track` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='InnoDB free: 7168 kB; (`set_id`) REFER `nemadatarepository02';



# Dump of table trackList_type_definitions
# ------------------------------------------------------------

DROP TABLE IF EXISTS `trackList_type_definitions`;

CREATE TABLE `trackList_type_definitions` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(64) default NULL,
  PRIMARY KEY  (`id`),
  KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;






/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
