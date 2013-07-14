CREATE DATABASE  IF NOT EXISTS `opennote` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `opennote`;
-- MySQL dump 10.13  Distrib 5.5.31, for debian-linux-gnu (i686)
--
-- Host: 127.0.0.1    Database: opennote
-- ------------------------------------------------------
-- Server version	5.5.31-0ubuntu0.13.04.1

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
-- Table structure for table `GroupRole`
--

DROP TABLE IF EXISTS `GroupRole`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GroupRole` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GroupRole`
--

LOCK TABLES `GroupRole` WRITE;
/*!40000 ALTER TABLE `GroupRole` DISABLE KEYS */;
/*!40000 ALTER TABLE `GroupRole` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Invite`
--

DROP TABLE IF EXISTS `Invite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Invite` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user` int(11) NOT NULL,
  `userGroup` int(11) NOT NULL,
  `groupRole` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_Invitations_User1` (`user`),
  KEY `fk_Invitations_UserGroup1` (`userGroup`),
  KEY `fk_Invitations_GroupRole1` (`groupRole`),
  CONSTRAINT `fk_Invitations_GroupRole1` FOREIGN KEY (`groupRole`) REFERENCES `GroupRole` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Invitations_User1` FOREIGN KEY (`user`) REFERENCES `User` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Invitations_UserGroup1` FOREIGN KEY (`userGroup`) REFERENCES `UserGroup` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Invite`
--

LOCK TABLES `Invite` WRITE;
/*!40000 ALTER TABLE `Invite` DISABLE KEYS */;
/*!40000 ALTER TABLE `Invite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Session`
--

DROP TABLE IF EXISTS `Session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Session` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user` int(11) NOT NULL,
  `ip` varchar(40) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_Session_User1` (`user`),
  CONSTRAINT `fk_Session_User1` FOREIGN KEY (`user`) REFERENCES `User` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Session`
--

LOCK TABLES `Session` WRITE;
/*!40000 ALTER TABLE `Session` DISABLE KEYS */;
INSERT INTO `Session` VALUES (37,12,'192.168.0.101'),(38,12,'192.168.0.101');
/*!40000 ALTER TABLE `Session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login` varchar(24) NOT NULL,
  `fullName` varchar(36) NOT NULL,
  `passwordHash` varchar(124) NOT NULL,
  `email` varchar(45) DEFAULT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `login_UNIQUE` (`login`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES (2,'test1','name1','pass1','mail1','2013-07-13'),(3,'test2','name2','pass2','mail2','2013-07-13'),(4,'test3','name2','pass2','mail2','2013-07-13'),(7,'test4','name2','pass2','mail2','2013-07-13'),(8,'qqqtest6','qqqqname2','677ff264d07336b7ade9bfcfe58b9872','qqqqmail2','2013-07-13'),(11,'userLogin2','qqqqname2','677ff264d07336b7ade9bfcfe58b9872','qqqqmail2','2013-07-14'),(12,'ololo1','qqqqname2','677ff264d07336b7ade9bfcfe58b9872','qqqqmail2','2013-07-14');
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `UserGroup`
--

DROP TABLE IF EXISTS `UserGroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UserGroup` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user` int(11) NOT NULL,
  `group` int(11) NOT NULL,
  `groupRole` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_UserGroup_Groups1` (`group`),
  KEY `fk_UserGroup_GroupRole1` (`groupRole`),
  KEY `fk_UserGroup_User1` (`user`),
  CONSTRAINT `fk_UserGroup_GroupRole1` FOREIGN KEY (`groupRole`) REFERENCES `GroupRole` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_UserGroup_Groups1` FOREIGN KEY (`group`) REFERENCES `Group` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_UserGroup_User1` FOREIGN KEY (`user`) REFERENCES `User` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `UserGroup`
--

LOCK TABLES `UserGroup` WRITE;
/*!40000 ALTER TABLE `UserGroup` DISABLE KEYS */;
/*!40000 ALTER TABLE `UserGroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SimpleNote`
--

DROP TABLE IF EXISTS `SimpleNote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SimpleNote` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(45) NOT NULL,
  `body` varchar(250) DEFAULT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SimpleNote`
--

LOCK TABLES `SimpleNote` WRITE;
/*!40000 ALTER TABLE `SimpleNote` DISABLE KEYS */;
/*!40000 ALTER TABLE `SimpleNote` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Group`
--

DROP TABLE IF EXISTS `Group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `slug` varchar(20) NOT NULL,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Group`
--

LOCK TABLES `Group` WRITE;
/*!40000 ALTER TABLE `Group` DISABLE KEYS */;
/*!40000 ALTER TABLE `Group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `UserSimpleNote`
--

DROP TABLE IF EXISTS `UserSimpleNote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UserSimpleNote` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userGroup` int(11) NOT NULL,
  `simpleNote` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_UsersSimpleNotes_UserGroup1` (`userGroup`),
  KEY `fk_UsersSimpleNotes_SimpleNotes1` (`simpleNote`),
  CONSTRAINT `fk_UsersSimpleNotes_SimpleNotes1` FOREIGN KEY (`simpleNote`) REFERENCES `SimpleNote` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_UsersSimpleNotes_UserGroup1` FOREIGN KEY (`userGroup`) REFERENCES `UserGroup` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `UserSimpleNote`
--

LOCK TABLES `UserSimpleNote` WRITE;
/*!40000 ALTER TABLE `UserSimpleNote` DISABLE KEYS */;
/*!40000 ALTER TABLE `UserSimpleNote` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-07-15 20:10:55
