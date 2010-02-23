-- MySQL dump 10.10
--
-- Host: localhost    Database: diy050
-- ------------------------------------------------------
-- Server version	5.0.22

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `diy050`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `diy050` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `diy050`;

--
-- Table structure for table `app_user`
--

DROP TABLE IF EXISTS `app_user`;
CREATE TABLE `app_user` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` int(11) default NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(50) NOT NULL,
  `account_enabled` bit(1) default NULL,
  `email` varchar(255) NOT NULL,
  `password_hint` varchar(255) default NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `account_expired` bit(1) NOT NULL,
  `account_locked` bit(1) NOT NULL,
  `credentials_expired` bit(1) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `app_user`
--


/*!40000 ALTER TABLE `app_user` DISABLE KEYS */;
LOCK TABLES `app_user` WRITE;
INSERT INTO `app_user` VALUES (-2,1,'d033e22ae348aeb5660fc2140aec35850c4da997','admin','','kumaramit01@gmail.com','Not a female kitty.','Amit','Kumar','\0','\0','\0'),(-1,1,'12dea96fec20593566ab75692c9949596833adc9','user','','amitku@illinois.edu','A male kitty.','Amit','Kumar','\0','\0','\0');
UNLOCK TABLES;
/*!40000 ALTER TABLE `app_user` ENABLE KEYS */;

--
-- Table structure for table `preference_value`
--

DROP TABLE IF EXISTS `preference_value`;
CREATE TABLE `preference_value` (
  `id` bigint(20) NOT NULL auto_increment,
  `value` varchar(100) NOT NULL,
  `mkey` varchar(100) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `preference_value`
--


/*!40000 ALTER TABLE `preference_value` DISABLE KEYS */;
LOCK TABLES `preference_value` WRITE;
INSERT INTO `preference_value` VALUES (1,'FALSE','emailPrefStartFlow'),(2,'TRUE','emailPrefEndFlow'),(3,'TRUE','emailPrefAbortFlow'),(4,'TRUE','emailPrefFlowResult'),(5,'FALSE','emailPrefStartFlow'),(6,'TRUE','emailPrefEndFlow'),(7,'TRUE','emailPrefAbortFlow'),(8,'TRUE','emailPrefFlowResult');
UNLOCK TABLES;
/*!40000 ALTER TABLE `preference_value` ENABLE KEYS */;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL auto_increment,
  `name` varchar(20) default NULL,
  `description` varchar(64) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `role`
--


/*!40000 ALTER TABLE `role` DISABLE KEYS */;
LOCK TABLES `role` WRITE;
INSERT INTO `role` VALUES (-2,'ROLE_USER','Default role for all Users'),(-1,'ROLE_ADMIN','Administrator role (can edit Users)');
UNLOCK TABLES;
/*!40000 ALTER TABLE `role` ENABLE KEYS */;

--
-- Table structure for table `submission`
--

DROP TABLE IF EXISTS `submission`;
CREATE TABLE `submission` (
  `id` bigint(20) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `jobId` bigint(20) NOT NULL,
  `dateCreated` datetime default NULL,
  `userId` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FK84363B4CE68F30EE` (`userId`),
  CONSTRAINT `FK84363B4CE68F30EE` FOREIGN KEY (`userId`) REFERENCES `app_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `submission`
--


/*!40000 ALTER TABLE `submission` DISABLE KEYS */;
LOCK TABLES `submission` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `submission` ENABLE KEYS */;

--
-- Table structure for table `user_prefs`
--

DROP TABLE IF EXISTS `user_prefs`;
CREATE TABLE `user_prefs` (
  `user_id` bigint(20) NOT NULL,
  `preferences_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`user_id`,`preferences_id`),
  UNIQUE KEY `preferences_id` (`preferences_id`),
  KEY `FK7327AD3C209E61FB` (`preferences_id`),
  KEY `FK7327AD3CF9EECD7` (`user_id`),
  CONSTRAINT `FK7327AD3CF9EECD7` FOREIGN KEY (`user_id`) REFERENCES `app_user` (`id`),
  CONSTRAINT `FK7327AD3C209E61FB` FOREIGN KEY (`preferences_id`) REFERENCES `preference_value` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user_prefs`
--


/*!40000 ALTER TABLE `user_prefs` DISABLE KEYS */;
LOCK TABLES `user_prefs` WRITE;
INSERT INTO `user_prefs` VALUES (-1,1),(-1,2),(-1,3),(-1,4),(-2,5),(-2,6),(-2,7),(-2,8);
UNLOCK TABLES;
/*!40000 ALTER TABLE `user_prefs` ENABLE KEYS */;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`user_id`,`role_id`),
  KEY `FK143BF46A6A7428F7` (`role_id`),
  KEY `FK143BF46AF9EECD7` (`user_id`),
  CONSTRAINT `FK143BF46AF9EECD7` FOREIGN KEY (`user_id`) REFERENCES `app_user` (`id`),
  CONSTRAINT `FK143BF46A6A7428F7` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user_role`
--


/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
LOCK TABLES `user_role` WRITE;
INSERT INTO `user_role` VALUES (-1,-2),(-2,-1);
UNLOCK TABLES;
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

