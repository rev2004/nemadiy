-- phpMyAdmin SQL Dump
-- version 3.3.2deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jul 13, 2010 at 06:26 PM
-- Server version: 5.1.41
-- PHP Version: 5.3.2-1ubuntu4.2

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `diyTest`
--

-- --------------------------------------------------------

--
-- Table structure for table `app_user`
--

CREATE TABLE IF NOT EXISTS `app_user` (
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=32 ;

--
-- Dumping data for table `app_user`
--

INSERT INTO `app_user` (`id`, `version`, `password`, `username`, `account_enabled`, `email`, `password_hint`, `first_name`, `last_name`, `account_expired`, `account_locked`, `credentials_expired`) VALUES
(-2, 2, 'b2cebd873228d3e6753d9b39195730694e3d1bbc', 'admin', b'1', 'matt@raibledesigns.com', 'Not a female kitty.', 'Matt', 'Raible', b'0', b'0', b'0'),
(-1, 1, '12dea96fec20593566ab75692c9949596833adc9', 'user', b'1', 'matt_raible@yahoo.com', 'A male kitty.', 'Tomcat', 'User', b'0', b'0', b'0');

-- --------------------------------------------------------

--
-- Table structure for table `mirex_profile`
--

CREATE TABLE IF NOT EXISTS `mirex_profile` (
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
  KEY `FK5EAA0CF36A9AF0D6` (`creator_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `mirex_profile`
--

INSERT INTO `mirex_profile` (`id`, `address_country`, `address_streat_1`, `address_streat_2`, `address_streat_3`, `address_city`, `address_region`, `address_postcode`, `orgnization`, `url`, `email`, `firstname`, `lastname`, `title`, `department`, `unit`, `startYear`, `endYear`, `phone`, `creator_id`) VALUES
(1, 'USA', NULL, NULL, NULL, NULL, NULL, NULL, 'UIUC', 'http://nema.lis.uiuc.edu', NULL, 'Nema', 'Imirsel', NULL, NULL, NULL, '2000', '2010', NULL, -1),
(2, 'USA', NULL, NULL, NULL, NULL, NULL, NULL, 'UI', 'http://imirsel.lis.uiuc.edu', NULL, 'Imirsel', 'Imirsel', NULL, NULL, NULL, '2009', 'now', '2173331000', -1),
(3, 'USA', NULL, NULL, NULL, NULL, NULL, NULL, 'Imirsel', 'http://imirsel.lis.uiuc.edu', NULL, 'Nemadiy', 'Imirsel', NULL, NULL, NULL, '2009', 'now', '2173331000', -2);

-- --------------------------------------------------------

--
-- Table structure for table `mirex_submission`
--

CREATE TABLE IF NOT EXISTS `mirex_submission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `status` varchar(255) NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `hashcode` varchar(10) NOT NULL,
  `readme` varchar(30000) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `publicNote` varchar(30000) DEFAULT NULL,
  `privateNote` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `mirexTask_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `hashcode` (`hashcode`),
  KEY `FKF4E97BC2571AF63D` (`mirexTask_id`),
  KEY `FKF4E97BC2F9EECD7` (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `mirex_submission`
--

INSERT INTO `mirex_submission` (`id`, `status`, `url`, `updateTime`, `createTime`, `hashcode`, `readme`, `name`, `publicNote`, `privateNote`, `user_id`, `mirexTask_id`) VALUES
(1, 'STARTED', NULL, '2010-01-01 00:00:00', '1992-07-01 18:25:10', 'ABC1', 'readme', 'submission 1', 'public note', 'private note', -1, 1),
(2, 'FINISHED', NULL, '1999-10-01 00:00:00', '1985-07-11 18:25:19', 'ABC2', 'readme', 'submission 2', 'public note', 'private note', -1, 1),
(3, 'UNKNOWN', NULL, '2005-03-01 12:34:00', '2003-07-10 18:25:35', 'EF', 'readme', 'submission 3', 'public note', 'private note', -2, 2),
(4, 'REVIEWED', NULL, '2010-07-12 18:24:41', '2010-07-01 18:24:50', 'GZ', 'readme', 'submission 4', 'public note', 'private note', -2, 3);

-- --------------------------------------------------------

--
-- Table structure for table `mirex_submission_mirex_profile`
--

CREATE TABLE IF NOT EXISTS `mirex_submission_mirex_profile` (
  `mirex_submission_id` bigint(20) NOT NULL,
  `contributors_id` bigint(20) NOT NULL,
  KEY `FK5D749F36ACAD5950` (`mirex_submission_id`),
  KEY `FK5D749F36F9089B00` (`contributors_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `mirex_submission_mirex_profile`
--

INSERT INTO `mirex_submission_mirex_profile` (`mirex_submission_id`, `contributors_id`) VALUES
(1, 1),
(2, 1),
(2, 2),
(3, 3),
(3, 1),
(4, 2);

-- --------------------------------------------------------

--
-- Table structure for table `mirex_task`
--

CREATE TABLE IF NOT EXISTS `mirex_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  `active` bit(1) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `fullname` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `mirex_task`
--

INSERT INTO `mirex_task` (`id`, `name`, `url`, `active`, `description`, `fullname`) VALUES
(1, 'QBT', NULL, b'1', NULL, 'Query By Tapping'),
(2, 'QBSH', NULL, b'1', NULL, 'Query By Singing and Humming'),
(3, 'Chord', NULL, b'1', NULL, 'Chord Description'),
(4, 'Melody', NULL, b'0', NULL, 'Melody Finding');

-- --------------------------------------------------------

--
-- Table structure for table `preference_value`
--

CREATE TABLE IF NOT EXISTS `preference_value` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `value` varchar(100) NOT NULL,
  `mkey` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=43 ;

--
-- Dumping data for table `preference_value`
--

INSERT INTO `preference_value` (`id`, `value`, `mkey`) VALUES
(1, 'FALSE', 'emailPrefStartFlow'),
(2, 'TRUE', 'emailPrefEndFlow'),
(3, 'TRUE', 'emailPrefAbortFlow'),
(4, 'TRUE', 'emailPrefFlowResult'),
(5, 'false', 'emailPrefStartFlow'),
(6, 'true', 'emailPrefEndFlow'),
(7, 'true', 'emailPrefAbortFlow'),
(8, 'true', 'emailPrefFlowResult');

-- --------------------------------------------------------

--
-- Table structure for table `role`
--

CREATE TABLE IF NOT EXISTS `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `description` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=8 ;

--
-- Dumping data for table `role`
--

INSERT INTO `role` (`id`, `name`, `description`) VALUES
(-2, 'ROLE_USER', 'Default role for all Users'),
(-1, 'ROLE_ADMIN', 'Administrator role (can edit Users)'),
(1, 'MIREX_RUNNER', 'Runner of Mirex');

-- --------------------------------------------------------

--
-- Table structure for table `submission`
--

CREATE TABLE IF NOT EXISTS `submission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `jobId` bigint(20) NOT NULL,
  `dateCreated` datetime DEFAULT NULL,
  `userId` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK84363B4CE68F30EE` (`userId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

--
-- Dumping data for table `submission`
--

INSERT INTO `submission` (`id`, `name`, `type`, `jobId`, `dateCreated`, `userId`) VALUES
(1, 'one', 'Inherits', 1, NULL, -2),
(2, 'one', 'Feature Extraction', 2, NULL, -2),
(3, 'one', 'Classification', 3, NULL, -2),
(4, 'one', 'Evaluation', 4, NULL, -2),
(5, 'one', 'Analysis', 5, NULL, -2);

-- --------------------------------------------------------

--
-- Table structure for table `user_prefs`
--

CREATE TABLE IF NOT EXISTS `user_prefs` (
  `user_id` bigint(20) NOT NULL,
  `preferences_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`preferences_id`),
  UNIQUE KEY `preferences_id` (`preferences_id`),
  KEY `FK7327AD3C209E61FB` (`preferences_id`),
  KEY `FK7327AD3CF9EECD7` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user_prefs`
--

INSERT INTO `user_prefs` (`user_id`, `preferences_id`) VALUES
(-1, 1),
(-1, 2),
(-1, 3),
(-1, 4),
(-2, 5),
(-2, 6),
(-2, 7),
(-2, 8);

-- --------------------------------------------------------

--
-- Table structure for table `user_role`
--

CREATE TABLE IF NOT EXISTS `user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FK143BF46A6A7428F7` (`role_id`),
  KEY `FK143BF46AF9EECD7` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user_role`
--

INSERT INTO `user_role` (`user_id`, `role_id`) VALUES
(-1, -2),
(-2, -1);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `mirex_profile`
--
ALTER TABLE `mirex_profile`
  ADD CONSTRAINT `FK5EAA0CF36A9AF0D6` FOREIGN KEY (`creator_id`) REFERENCES `app_user` (`id`);

--
-- Constraints for table `mirex_submission`
--
ALTER TABLE `mirex_submission`
  ADD CONSTRAINT `FKF4E97BC2F9EECD7` FOREIGN KEY (`user_id`) REFERENCES `app_user` (`id`),
  ADD CONSTRAINT `FKF4E97BC2571AF63D` FOREIGN KEY (`mirexTask_id`) REFERENCES `mirex_task` (`id`);

--
-- Constraints for table `mirex_submission_mirex_profile`
--
ALTER TABLE `mirex_submission_mirex_profile`
  ADD CONSTRAINT `FK5D749F36F9089B00` FOREIGN KEY (`contributors_id`) REFERENCES `mirex_profile` (`id`),
  ADD CONSTRAINT `FK5D749F36ACAD5950` FOREIGN KEY (`mirex_submission_id`) REFERENCES `mirex_submission` (`id`);

--
-- Constraints for table `submission`
--
ALTER TABLE `submission`
  ADD CONSTRAINT `FK84363B4CE68F30EE` FOREIGN KEY (`userId`) REFERENCES `app_user` (`id`);

--
-- Constraints for table `user_prefs`
--
ALTER TABLE `user_prefs`
  ADD CONSTRAINT `FK7327AD3CF9EECD7` FOREIGN KEY (`user_id`) REFERENCES `app_user` (`id`),
  ADD CONSTRAINT `FK7327AD3C209E61FB` FOREIGN KEY (`preferences_id`) REFERENCES `preference_value` (`id`);

--
-- Constraints for table `user_role`
--
ALTER TABLE `user_role`
  ADD CONSTRAINT `FK143BF46AF9EECD7` FOREIGN KEY (`user_id`) REFERENCES `app_user` (`id`),
  ADD CONSTRAINT `FK143BF46A6A7428F7` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`);