-- MySQL dump 10.13  Distrib 5.1.41, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: diy090
-- ------------------------------------------------------
-- Server version	5.1.41-3ubuntu12.8

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
  `account_enabled` bit(1) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(200) NOT NULL,
  `version` int(11) DEFAULT NULL,
  `profile_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `profile_id` (`profile_id`),
  KEY `FK459C5729112A54FD` (`profile_id`),
  CONSTRAINT `FK459C5729112A54FD` FOREIGN KEY (`profile_id`) REFERENCES `mirex_profile` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_user`
--

LOCK TABLES `app_user` WRITE;
/*!40000 ALTER TABLE `app_user` DISABLE KEYS */;
INSERT INTO `app_user` VALUES (1,'\0','\0','\0','','?????????????????????????','https://www.google.com/accounts/o8/id?id=aitoawluccmlcclb-g1u00lsqtgfanthvvf8xrw',0,1),(2,'\0','\0','\0','','????????????????????','https://www.google.com/accounts/o8/id?id=aitoawlwimoraefnyns5btsbremhlhwnenzf0oi',0,2);
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
  `submission_id` bigint(20) NOT NULL,
  `note_order` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKC6A600E87057DF17` (`author_id`),
  KEY `FKC6A600E8A0F2F586` (`submission_id`),
  CONSTRAINT `FKC6A600E8A0F2F586` FOREIGN KEY (`submission_id`) REFERENCES `mirex_submission` (`id`),
  CONSTRAINT `FKC6A600E87057DF17` FOREIGN KEY (`author_id`) REFERENCES `app_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mirex_note`
--

LOCK TABLES `mirex_note` WRITE;
/*!40000 ALTER TABLE `mirex_note` DISABLE KEYS */;
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
  `organization` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `startYear` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5EAA0CF37B859CEF` (`owner_id`),
  CONSTRAINT `FK5EAA0CF37B859CEF` FOREIGN KEY (`owner_id`) REFERENCES `app_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mirex_profile`
--

LOCK TABLES `mirex_profile` WRITE;
/*!40000 ALTER TABLE `mirex_profile` DISABLE KEYS */;
INSERT INTO `mirex_profile` VALUES (1,'Champaign','USA','61801','IL','','','','GSLIS','nemamirex@gmail.com','2011','Nema','Mirex','UIUC','','2000',NULL,'imirsel','http://www.music-ir.org/','349edefa-e9d7-44e6-892e-76b9c133aafa',1),(2,'','USA','','','','','','physics','zggame@gmail.com','2010','Guojun','Zhu','UIUC','','2008',NULL,'','','d5350753-5ddc-4c6a-b993-0aa68100ecdc',2);
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
  `resourcePath` varchar(255) DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `mirexTask_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `hashcode` (`hashcode`),
  KEY `FKF4E97BC2571AF63D` (`mirexTask_id`),
  KEY `FKF4E97BC2F9EECD7` (`user_id`),
  CONSTRAINT `FKF4E97BC2F9EECD7` FOREIGN KEY (`user_id`) REFERENCES `app_user` (`id`),
  CONSTRAINT `FKF4E97BC2571AF63D` FOREIGN KEY (`mirexTask_id`) REFERENCES `mirex_task` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mirex_submission`
--

LOCK TABLES `mirex_submission` WRITE;
/*!40000 ALTER TABLE `mirex_submission` DISABLE KEYS */;
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
  KEY `FK5D749F3672FF6D4E` (`contributors_id`),
  CONSTRAINT `FK5D749F3672FF6D4E` FOREIGN KEY (`contributors_id`) REFERENCES `mirex_profile` (`id`),
  CONSTRAINT `FK5D749F36ACAD5950` FOREIGN KEY (`mirex_submission_id`) REFERENCES `mirex_submission` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mirex_submission_mirex_profile`
--

LOCK TABLES `mirex_submission_mirex_profile` WRITE;
/*!40000 ALTER TABLE `mirex_submission_mirex_profile` DISABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mirex_task`
--

LOCK TABLES `mirex_task` WRITE;
/*!40000 ALTER TABLE `mirex_task` DISABLE KEYS */;
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
INSERT INTO `preference_value` VALUES (1,'emailPrefFlowResult','true'),(2,'emailPrefAbortFlow','true'),(3,'emailPrefStartFlow','true'),(4,'emailPrefEndFlow','true'),(5,'emailPrefFlowResult','true'),(6,'emailPrefAbortFlow','true'),(7,'emailPrefStartFlow','true'),(8,'emailPrefEndFlow','true');
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
INSERT INTO `user_prefs` VALUES (1,1),(1,2),(1,3),(1,4),(2,5),(2,6),(2,7),(2,8);
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
INSERT INTO `user_role` VALUES (1,-2),(2,-2),(1,-1),(1,1);
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

-- Dump completed on 2010-12-13 17:26:47
