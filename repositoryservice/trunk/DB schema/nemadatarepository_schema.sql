# Sequel Pro dump
# Version 654
# http://code.google.com/p/sequel-pro
#
# Host: nema.lis.uiuc.edu (MySQL 5.0.22)
# Database: nemadatarepository
# Generation Time: 2009-12-10 02:58:43 +0000
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table collection_track_link
# ------------------------------------------------------------

DROP TABLE IF EXISTS `collection_track_link`;

CREATE TABLE `collection_track_link` (
  `collection_id` int(11) NOT NULL,
  `track_id` varchar(32) default '',
  UNIQUE KEY `collection_id` (`collection_id`,`track_id`),
  KEY `track_id` (`track_id`),
  CONSTRAINT `collection_id_track_link` FOREIGN KEY (`collection_id`) REFERENCES `collection` (`id`),
  CONSTRAINT `track_id` FOREIGN KEY (`track_id`) REFERENCES `track` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table dataset
# ------------------------------------------------------------

DROP TABLE IF EXISTS `dataset`;

CREATE TABLE `dataset` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(128) NOT NULL default '',
  `description` text NOT NULL,
  `collection_id` int(11) NOT NULL default '-1',
  `subset_set_id` int(11) default NULL,
  `num_splits` int(11) NOT NULL default '1',
  `num_set_per_split` int(11) default '1',
  `split_class` varchar(255) default NULL,
  `split_parameters_string` text,
  `subject_track_metadata_type_id` int(11) NOT NULL default '-1',
  `filter_track_metadata_type_id` int(11) default NULL,
  `task_id` int(11) NOT NULL default '-1',
  PRIMARY KEY  (`id`),
  KEY `collection_id` (`collection_id`),
  KEY `name` (`name`),
  KEY `subject_track_metadata_id` (`subject_track_metadata_type_id`),
  KEY `task_id` (`task_id`),
  CONSTRAINT `collection_id` FOREIGN KEY (`collection_id`) REFERENCES `collection` (`id`),
  CONSTRAINT `task_id` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table file
# ------------------------------------------------------------

DROP TABLE IF EXISTS `file`;

CREATE TABLE `file` (
  `id` int(11) NOT NULL auto_increment,
  `track_id` varchar(32) NOT NULL,
  `path` varchar(512) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `track_id` (`track_id`),
  KEY `path` (`path`(255)),
  CONSTRAINT `file_track_id` FOREIGN KEY (`track_id`) REFERENCES `track` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table file_file_metadata_link
# ------------------------------------------------------------

DROP TABLE IF EXISTS `file_file_metadata_link`;

CREATE TABLE `file_file_metadata_link` (
  `id` int(11) NOT NULL auto_increment,
  `file_id` int(11) NOT NULL default '-1',
  `file_metadata_id` int(11) NOT NULL default '-1',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `file_id` (`file_id`,`file_metadata_id`),
  KEY `file_metadata_id` (`file_metadata_id`),
  CONSTRAINT `file_id` FOREIGN KEY (`file_id`) REFERENCES `file` (`id`),
  CONSTRAINT `file_metadata_id` FOREIGN KEY (`file_metadata_id`) REFERENCES `file_metadata` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table file_metadata
# ------------------------------------------------------------

DROP TABLE IF EXISTS `file_metadata`;

CREATE TABLE `file_metadata` (
  `id` int(11) NOT NULL auto_increment,
  `metadata_type_id` int(11) NOT NULL default '-1',
  `value` varchar(180) NOT NULL default '',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `metadata_type_id` (`metadata_type_id`,`value`),
  CONSTRAINT `file_metadata_metadata_type_id` FOREIGN KEY (`metadata_type_id`) REFERENCES `file_metadata_definitions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table file_metadata_definitions
# ------------------------------------------------------------

DROP TABLE IF EXISTS `file_metadata_definitions`;

CREATE TABLE `file_metadata_definitions` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(128) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table set
# ------------------------------------------------------------

DROP TABLE IF EXISTS `set`;

CREATE TABLE `set` (
  `id` int(11) NOT NULL auto_increment,
  `dataset_id` int(11) default NULL,
  `set_type_id` int(11) default NULL,
  `split_number` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `set_type_id` (`set_type_id`),
  KEY `dataset_id` (`dataset_id`),
  CONSTRAINT `dataset_id` FOREIGN KEY (`dataset_id`) REFERENCES `dataset` (`id`),
  CONSTRAINT `set_type_id` FOREIGN KEY (`set_type_id`) REFERENCES `set_type_definitions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table set_track_link
# ------------------------------------------------------------

DROP TABLE IF EXISTS `set_track_link`;

CREATE TABLE `set_track_link` (
  `set_id` int(11) default NULL,
  `track_id` varchar(32) NOT NULL default '',
  UNIQUE KEY `set_id` (`set_id`,`track_id`),
  KEY `set_track_link_track_id` (`track_id`),
  CONSTRAINT `set_track_link_track_id` FOREIGN KEY (`track_id`) REFERENCES `track` (`id`),
  CONSTRAINT `set_id` FOREIGN KEY (`set_id`) REFERENCES `set` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table set_type_definitions
# ------------------------------------------------------------

DROP TABLE IF EXISTS `set_type_definitions`;

CREATE TABLE `set_type_definitions` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(64) default NULL,
  PRIMARY KEY  (`id`),
  KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table task
# ------------------------------------------------------------

DROP TABLE IF EXISTS `task`;

CREATE TABLE `task` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(128) NOT NULL,
  `description` mediumtext,
  `subject_track_metadata_type_id` int(11) NOT NULL default '-1',
  `task_type_id` int(11) NOT NULL default '-1',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `task_type_id` (`task_type_id`),
  KEY `subject_track_metadata_type_id` (`subject_track_metadata_type_id`),
  CONSTRAINT `subject_track_metadata_type_id` FOREIGN KEY (`subject_track_metadata_type_id`) REFERENCES `track_metadata_definitions` (`id`),
  CONSTRAINT `task_type_id` FOREIGN KEY (`task_type_id`) REFERENCES `task_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table task_type
# ------------------------------------------------------------

DROP TABLE IF EXISTS `task_type`;

CREATE TABLE `task_type` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(128) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



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
  `value` varchar(180) NOT NULL default '',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `metadata_type_id` (`metadata_type_id`,`value`),
  CONSTRAINT `metadata_type_id` FOREIGN KEY (`metadata_type_id`) REFERENCES `track_metadata_definitions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table track_metadata_definitions
# ------------------------------------------------------------

DROP TABLE IF EXISTS `track_metadata_definitions`;

CREATE TABLE `track_metadata_definitions` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(128) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



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
  KEY `track_id_2` (`track_id`),
  CONSTRAINT `track_track_metadata_link_track_id` FOREIGN KEY (`track_id`) REFERENCES `track` (`id`),
  CONSTRAINT `track_track_metadata_link_track_metadata_id` FOREIGN KEY (`track_metadata_id`) REFERENCES `track_metadata` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;






/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
