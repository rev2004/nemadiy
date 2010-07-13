# Sequel Pro dump
# Version 2210
# http://code.google.com/p/sequel-pro
#
# Host: localhost (MySQL 5.1.48)
# Database: diy090
# Generation Time: 2010-07-12 21:56:33 -0500
# ************************************************************

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table app_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `app_user`;

CREATE TABLE `app_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(50) NOT NULL,
  `account_enabled` bit(1) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `password_hint` varchar(255) DEFAULT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `account_expired` bit(1) NOT NULL,
  `account_locked` bit(1) NOT NULL,
  `credentials_expired` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=latin1;

LOCK TABLES `app_user` WRITE;
/*!40000 ALTER TABLE `app_user` DISABLE KEYS */;
INSERT INTO `app_user` (`id`,`version`,`password`,`username`,`account_enabled`,`email`,`password_hint`,`first_name`,`last_name`,`account_expired`,`account_locked`,`credentials_expired`)
VALUES
	(-2,2,'b2cebd873228d3e6753d9b39195730694e3d1bbc','admin',1,'matt@raibledesigns.com','Not a female kitty.','Matt','Raible',0,0,0),
	(-1,1,'12dea96fec20593566ab75692c9949596833adc9','user',1,'matt_raible@yahoo.com','A male kitty.','Tomcat','User',0,0,0);

/*!40000 ALTER TABLE `app_user` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table mirex_profile
# ------------------------------------------------------------

DROP TABLE IF EXISTS `mirex_profile`;

CREATE TABLE `mirex_profile` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address_country` varchar(255) DEFAULT NULL,
  `address_streat_1` varchar(255) DEFAULT NULL,
  `address_streat_2` varchar(255) DEFAULT NULL,
  `address_streat_3` varchar(255) DEFAULT NULL,
  `address_city` varchar(255) DEFAULT NULL,
  `address_region` varchar(255) DEFAULT NULL,
  `address_postcode` varchar(255) DEFAULT NULL,
  `orgnization` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `department` varchar(255) DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  `startYear` varchar(255) DEFAULT NULL,
  `endYear` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `creator_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5EAA0CF36A9AF0D6` (`creator_id`),
  CONSTRAINT `FK5EAA0CF36A9AF0D6` FOREIGN KEY (`creator_id`) REFERENCES `app_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

LOCK TABLES `mirex_profile` WRITE;
/*!40000 ALTER TABLE `mirex_profile` DISABLE KEYS */;
INSERT INTO `mirex_profile` (`id`,`address_country`,`address_streat_1`,`address_streat_2`,`address_streat_3`,`address_city`,`address_region`,`address_postcode`,`orgnization`,`url`,`email`,`firstname`,`lastname`,`title`,`department`,`unit`,`startYear`,`endYear`,`phone`,`creator_id`)
VALUES
	(1,'USA',NULL,NULL,NULL,NULL,NULL,NULL,'UIUC','http://nema.lis.uiuc.edu',NULL,'Nema','Imirsel',NULL,NULL,NULL,'2000','2010',NULL,-1),
	(2,'USA',NULL,NULL,NULL,NULL,NULL,NULL,'UI','http://imirsel.lis.uiuc.edu',NULL,'Imirsel','Imirsel',NULL,NULL,NULL,'2009','now','2173331000',-1),
	(3,'USA',NULL,NULL,NULL,NULL,NULL,NULL,'Imirsel','http://imirsel.lis.uiuc.edu',NULL,'Nemadiy','Imirsel',NULL,NULL,NULL,'2009','now','2173331000',-2);

/*!40000 ALTER TABLE `mirex_profile` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table mirex_submission
# ------------------------------------------------------------

DROP TABLE IF EXISTS `mirex_submission`;

CREATE TABLE `mirex_submission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `hashcode` varchar(10) NOT NULL,
  `privateNote` varchar(255) DEFAULT NULL,
  `readme` varchar(30000) DEFAULT NULL,
  `publicNote` varchar(30000) DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `mirexTask_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `hashcode` (`hashcode`),
  KEY `FKF4E97BC2571AF63D` (`mirexTask_id`),
  KEY `FKF4E97BC2F9EECD7` (`user_id`),
  CONSTRAINT `FKF4E97BC2F9EECD7` FOREIGN KEY (`user_id`) REFERENCES `app_user` (`id`),
  CONSTRAINT `FKF4E97BC2571AF63D` FOREIGN KEY (`mirexTask_id`) REFERENCES `mirex_task` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

LOCK TABLES `mirex_submission` WRITE;
/*!40000 ALTER TABLE `mirex_submission` DISABLE KEYS */;
INSERT INTO `mirex_submission` (`id`,`name`,`url`,`status`,`hashcode`,`privateNote`,`readme`,`publicNote`,`updateTime`,`mirexTask_id`,`user_id`)
VALUES
	(1,'submission 1',NULL,'STARTED','ABC1','private note','readme','public note','2010-01-01 00:00:00',1,-1),
	(2,'submission 2',NULL,'FINISHED','ABC2','private note','readme','public note','1999-10-01 00:00:00',1,-1),
	(3,'submission 3',NULL,'UNKNOWN','EF','private note','readme','public note','2005-03-01 12:34:00',2,-2),
	(4,'submission 4',NULL,'REVIEWED','GZ','private note','readme','public note',NULL,3,-2);

/*!40000 ALTER TABLE `mirex_submission` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table mirex_submission_mirex_profile
# ------------------------------------------------------------

DROP TABLE IF EXISTS `mirex_submission_mirex_profile`;

CREATE TABLE `mirex_submission_mirex_profile` (
  `mirex_submission_id` bigint(20) NOT NULL,
  `contributors_id` bigint(20) NOT NULL,
  KEY `FK5D749F36ACAD5950` (`mirex_submission_id`),
  KEY `FK5D749F36F9089B00` (`contributors_id`),
  CONSTRAINT `FK5D749F36F9089B00` FOREIGN KEY (`contributors_id`) REFERENCES `mirex_profile` (`id`),
  CONSTRAINT `FK5D749F36ACAD5950` FOREIGN KEY (`mirex_submission_id`) REFERENCES `mirex_submission` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `mirex_submission_mirex_profile` WRITE;
/*!40000 ALTER TABLE `mirex_submission_mirex_profile` DISABLE KEYS */;
INSERT INTO `mirex_submission_mirex_profile` (`mirex_submission_id`,`contributors_id`)
VALUES
	(1,1),
	(2,1),
	(2,2),
	(3,3),
	(3,1),
	(4,2);

/*!40000 ALTER TABLE `mirex_submission_mirex_profile` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table mirex_task
# ------------------------------------------------------------

DROP TABLE IF EXISTS `mirex_task`;

CREATE TABLE `mirex_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  `active` bit(1) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `fullname` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

LOCK TABLES `mirex_task` WRITE;
/*!40000 ALTER TABLE `mirex_task` DISABLE KEYS */;
INSERT INTO `mirex_task` (`id`,`name`,`url`,`active`,`description`,`fullname`)
VALUES
	(1,'QBT',NULL,1,NULL,'Query By Tapping'),
	(2,'QBSH',NULL,1,NULL,'Query By Singing and Humming'),
	(3,'Chord',NULL,1,NULL,'Chord Description'),
	(4,'Melody',NULL,0,NULL,'Melody Finding');

/*!40000 ALTER TABLE `mirex_task` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table preference_value
# ------------------------------------------------------------

DROP TABLE IF EXISTS `preference_value`;

CREATE TABLE `preference_value` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `value` varchar(100) NOT NULL,
  `mkey` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=latin1;

LOCK TABLES `preference_value` WRITE;
/*!40000 ALTER TABLE `preference_value` DISABLE KEYS */;
INSERT INTO `preference_value` (`id`,`value`,`mkey`)
VALUES
	(1,'FALSE','emailPrefStartFlow'),
	(2,'TRUE','emailPrefEndFlow'),
	(3,'TRUE','emailPrefAbortFlow'),
	(4,'TRUE','emailPrefFlowResult'),
	(5,'false','emailPrefStartFlow'),
	(6,'true','emailPrefEndFlow'),
	(7,'true','emailPrefAbortFlow'),
	(8,'true','emailPrefFlowResult');

/*!40000 ALTER TABLE `preference_value` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `description` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` (`id`,`name`,`description`)
VALUES
	(-2,'ROLE_USER','Default role for all Users'),
	(-1,'ROLE_ADMIN','Administrator role (can edit Users)'),
	(1,'MIREX_RUNNER','Runner of Mirex');

/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table submission
# ------------------------------------------------------------

DROP TABLE IF EXISTS `submission`;

CREATE TABLE `submission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `jobId` bigint(20) NOT NULL,
  `dateCreated` datetime DEFAULT NULL,
  `userId` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK84363B4CE68F30EE` (`userId`),
  CONSTRAINT `FK84363B4CE68F30EE` FOREIGN KEY (`userId`) REFERENCES `app_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

LOCK TABLES `submission` WRITE;
/*!40000 ALTER TABLE `submission` DISABLE KEYS */;
INSERT INTO `submission` (`id`,`name`,`type`,`jobId`,`dateCreated`,`userId`)
VALUES
	(1,'one','Inherits',1,NULL,-2),
	(2,'one','Feature Extraction',2,NULL,-2),
	(3,'one','Classification',3,NULL,-2),
	(4,'one','Evaluation',4,NULL,-2),
	(5,'one','Analysis',5,NULL,-2);

/*!40000 ALTER TABLE `submission` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table user_prefs
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user_prefs`;

CREATE TABLE `user_prefs` (
  `user_id` bigint(20) NOT NULL,
  `preferences_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`preferences_id`),
  UNIQUE KEY `preferences_id` (`preferences_id`),
  KEY `FK7327AD3C209E61FB` (`preferences_id`),
  KEY `FK7327AD3CF9EECD7` (`user_id`),
  CONSTRAINT `FK7327AD3CF9EECD7` FOREIGN KEY (`user_id`) REFERENCES `app_user` (`id`),
  CONSTRAINT `FK7327AD3C209E61FB` FOREIGN KEY (`preferences_id`) REFERENCES `preference_value` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `user_prefs` WRITE;
/*!40000 ALTER TABLE `user_prefs` DISABLE KEYS */;
INSERT INTO `user_prefs` (`user_id`,`preferences_id`)
VALUES
	(-1,1),
	(-1,2),
	(-1,3),
	(-1,4),
	(-2,5),
	(-2,6),
	(-2,7),
	(-2,8);

/*!40000 ALTER TABLE `user_prefs` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table user_role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user_role`;

CREATE TABLE `user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FK143BF46A6A7428F7` (`role_id`),
  KEY `FK143BF46AF9EECD7` (`user_id`),
  CONSTRAINT `FK143BF46AF9EECD7` FOREIGN KEY (`user_id`) REFERENCES `app_user` (`id`),
  CONSTRAINT `FK143BF46A6A7428F7` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` (`user_id`,`role_id`)
VALUES
	(-1,-2),
	(-2,-1);

/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;





/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
