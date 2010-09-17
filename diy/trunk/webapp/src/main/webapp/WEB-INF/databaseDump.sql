-- MySQL dump 10.13  Distrib 5.1.41, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: diy090
-- ------------------------------------------------------
-- Server version	5.1.41-3ubuntu12.3

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
-- Current Database: `diy090`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `diy090` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `diy090`;

--
-- Table structure for table `app_user`
--

DROP TABLE IF EXISTS `app_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_expired` bit(1) NOT NULL,
  `account_locked` bit(1) NOT NULL,
  `credentials_expired` bit(1) NOT NULL,
  `email` varchar(255) NOT NULL,
  `account_enabled` bit(1) DEFAULT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `password_hint` varchar(255) DEFAULT NULL,
  `username` varchar(50) NOT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_user`
--

LOCK TABLES `app_user` WRITE;
/*!40000 ALTER TABLE `app_user` DISABLE KEYS */;
INSERT INTO `app_user` VALUES (-2,'\0','\0','\0','amitku@uiuc.edu','','Kumar','Amit','b2cebd873228d3e6753d9b39195730694e3d1bbc','Amit.','admin',2),(-1,'\0','\0','\0','kumaramit01@gmail.com','','Tomcat','User','12dea96fec20593566ab75692c9949596833adc9','A male kitty.','user',1);
/*!40000 ALTER TABLE `app_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mirex_note`
--

DROP TABLE IF EXISTS `mirex_note`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mirex_note` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(3000) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `author_id` bigint(20) DEFAULT NULL,
  `submission_id` bigint(20) DEFAULT NULL,
  `note_order` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKC6A600E87057DF17` (`author_id`),
  KEY `FKC6A600E8A0F2F586` (`submission_id`),
  CONSTRAINT `FKC6A600E8A0F2F586` FOREIGN KEY (`submission_id`) REFERENCES `mirex_submission` (`id`),
  CONSTRAINT `FKC6A600E87057DF17` FOREIGN KEY (`author_id`) REFERENCES `app_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mirex_note`
--

LOCK TABLES `mirex_note` WRITE;
/*!40000 ALTER TABLE `mirex_note` DISABLE KEYS */;
INSERT INTO `mirex_note` VALUES (10,'this has been reviewed \r\n\r\n\r\n\r\nOK','2010-07-18 02:05:56','PUBLIC',-2,1,0);
/*!40000 ALTER TABLE `mirex_note` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mirex_profile`
--

DROP TABLE IF EXISTS `mirex_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mirex_profile` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address_city` varchar(255) DEFAULT NULL,
  `address_country` varchar(255) DEFAULT NULL,
  `address_postcode` varchar(255) DEFAULT NULL,
  `address_region` varchar(255) DEFAULT NULL,
  `address_streat_1` varchar(255) DEFAULT NULL,
  `address_streat_2` varchar(255) DEFAULT NULL,
  `address_streat_3` varchar(255) DEFAULT NULL,
  `department` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `endYear` varchar(255) DEFAULT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `orgnization` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `startYear` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `creator_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5EAA0CF36A9AF0D6` (`creator_id`),
  CONSTRAINT `FK5EAA0CF36A9AF0D6` FOREIGN KEY (`creator_id`) REFERENCES `app_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mirex_profile`
--

LOCK TABLES `mirex_profile` WRITE;
/*!40000 ALTER TABLE `mirex_profile` DISABLE KEYS */;
INSERT INTO `mirex_profile` VALUES (1,NULL,'USA',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2010','Nema','Imirsel','UIUC',NULL,'2000',NULL,NULL,'http://nema.lis.uiuc.edu',-1),(2,NULL,'USA',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'now','Imirsel','Imirsel','UI','2173331000','2009',NULL,NULL,'http://imirsel.lis.uiuc.edu',-1),(3,NULL,'USA',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'now','Nemadiy','Imirsel','Imirsel','2173331000','2009',NULL,NULL,'http://imirsel.lis.uiuc.edu',-2);
/*!40000 ALTER TABLE `mirex_profile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mirex_submission`
--

DROP TABLE IF EXISTS `mirex_submission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mirex_submission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createTime` datetime DEFAULT NULL,
  `hashcode` varchar(10) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `readme` varchar(30000) DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `mirexTask_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `hashcode` (`hashcode`),
  KEY `FKF4E97BC2571AF63D` (`mirexTask_id`),
  KEY `FKF4E97BC2F9EECD7` (`user_id`),
  CONSTRAINT `FKF4E97BC2571AF63D` FOREIGN KEY (`mirexTask_id`) REFERENCES `mirex_task` (`id`),
  CONSTRAINT `FKF4E97BC2F9EECD7` FOREIGN KEY (`user_id`) REFERENCES `app_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mirex_submission`
--

LOCK TABLES `mirex_submission` WRITE;
/*!40000 ALTER TABLE `mirex_submission` DISABLE KEYS */;
INSERT INTO `mirex_submission` VALUES (1,'1992-07-01 18:25:10','ABC1','submission 1','readme','REVIEWED','2010-07-18 02:05:56',NULL,1,-1),(2,'1985-07-11 18:25:19','ABC2','submission 2','readme','FINISHED','1999-10-01 00:00:00',NULL,1,-1),(3,'2003-07-10 18:25:35','EF','submission 3','readme','UNKNOWN','2005-03-01 12:34:00',NULL,2,-2),(4,'2010-07-01 18:24:50','GZ','submission 4','readme','REVIEWED','2010-07-12 18:24:41',NULL,3,-2);
/*!40000 ALTER TABLE `mirex_submission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mirex_submission_mirex_profile`
--

DROP TABLE IF EXISTS `mirex_submission_mirex_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mirex_submission_mirex_profile` (
  `mirex_submission_id` bigint(20) NOT NULL,
  `contributors_id` bigint(20) NOT NULL,
  `contributor_rank` int(11) NOT NULL,
  PRIMARY KEY (`mirex_submission_id`,`contributor_rank`),
  KEY `FK5D749F36ACAD5950` (`mirex_submission_id`),
  KEY `FK5D749F36F9089B00` (`contributors_id`),
  CONSTRAINT `FK5D749F36F9089B00` FOREIGN KEY (`contributors_id`) REFERENCES `mirex_profile` (`id`),
  CONSTRAINT `FK5D749F36ACAD5950` FOREIGN KEY (`mirex_submission_id`) REFERENCES `mirex_submission` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mirex_submission_mirex_profile`
--

LOCK TABLES `mirex_submission_mirex_profile` WRITE;
/*!40000 ALTER TABLE `mirex_submission_mirex_profile` DISABLE KEYS */;
INSERT INTO `mirex_submission_mirex_profile` VALUES (1,1,0),(2,1,0),(3,1,1),(2,2,1),(4,2,0),(3,3,0);
/*!40000 ALTER TABLE `mirex_submission_mirex_profile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mirex_task`
--

DROP TABLE IF EXISTS `mirex_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mirex_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `fullname` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mirex_task`
--

LOCK TABLES `mirex_task` WRITE;
/*!40000 ALTER TABLE `mirex_task` DISABLE KEYS */;
INSERT INTO `mirex_task` VALUES (1,'',NULL,'Query By Tapping','QBT',NULL),(2,'',NULL,'Query By Singing and Humming','QBSH',NULL),(3,'',NULL,'Chord Description','Chord',NULL),(4,'\0',NULL,'Melody Finding','Melody',NULL);
/*!40000 ALTER TABLE `mirex_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `preference_value`
--

DROP TABLE IF EXISTS `preference_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `preference_value` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mkey` varchar(100) NOT NULL,
  `value` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `preference_value`
--

LOCK TABLES `preference_value` WRITE;
/*!40000 ALTER TABLE `preference_value` DISABLE KEYS */;
INSERT INTO `preference_value` VALUES (1,'emailPrefStartFlow','FALSE'),(2,'emailPrefEndFlow','TRUE'),(3,'emailPrefAbortFlow','TRUE'),(4,'emailPrefFlowResult','TRUE'),(5,'emailPrefStartFlow','false'),(6,'emailPrefEndFlow','true'),(7,'emailPrefAbortFlow','true'),(8,'emailPrefFlowResult','true');
/*!40000 ALTER TABLE `preference_value` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(64) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (-2,'Default role for all Users','ROLE_USER'),(-1,'Administrator role (can edit Users)','ROLE_ADMIN'),(1,'Runner of Mirex','MIREX_RUNNER');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `submission`
--

DROP TABLE IF EXISTS `submission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `submission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dateCreated` datetime DEFAULT NULL,
  `jobId` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `userId` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK84363B4CE68F30EE` (`userId`),
  CONSTRAINT `FK84363B4CE68F30EE` FOREIGN KEY (`userId`) REFERENCES `app_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `submission`
--

LOCK TABLES `submission` WRITE;
/*!40000 ALTER TABLE `submission` DISABLE KEYS */;
INSERT INTO `submission` VALUES (1,NULL,1,'one','Inherits',-2),(2,NULL,2,'one','Feature Extraction',-2),(3,NULL,3,'one','Classification',-2),(4,NULL,4,'one','Evaluation',-2),(5,NULL,5,'one','Analysis',-2);
/*!40000 ALTER TABLE `submission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_prefs`
--

DROP TABLE IF EXISTS `user_prefs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_prefs`
--

LOCK TABLES `user_prefs` WRITE;
/*!40000 ALTER TABLE `user_prefs` DISABLE KEYS */;
INSERT INTO `user_prefs` VALUES (-1,1),(-1,2),(-1,3),(-1,4),(-2,5),(-2,6),(-2,7),(-2,8);
/*!40000 ALTER TABLE `user_prefs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FK143BF46A6A7428F7` (`role_id`),
  KEY `FK143BF46AF9EECD7` (`user_id`),
  CONSTRAINT `FK143BF46AF9EECD7` FOREIGN KEY (`user_id`) REFERENCES `app_user` (`id`),
  CONSTRAINT `FK143BF46A6A7428F7` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (-1,-2),(-2,-1);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2010-07-21 17:11:21
