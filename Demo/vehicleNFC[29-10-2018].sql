-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: vehiclenfc
-- ------------------------------------------------------
-- Server version	5.7.21-log

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
-- Table structure for table `tbl_location`
--

DROP TABLE IF EXISTS `tbl_location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_location` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `location` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `is_activated` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_location`
--

LOCK TABLES `tbl_location` WRITE;
/*!40000 ALTER TABLE `tbl_location` DISABLE KEYS */;
INSERT INTO `tbl_location` VALUES (1,'Quang Trung',NULL,'');
/*!40000 ALTER TABLE `tbl_location` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_location_has_tbl_policy`
--

DROP TABLE IF EXISTS `tbl_location_has_tbl_policy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_location_has_tbl_policy` (
  `tbl_location_id` int(11) NOT NULL,
  `tbl_policy_id` int(11) NOT NULL,
  PRIMARY KEY (`tbl_location_id`,`tbl_policy_id`),
  KEY `fk_tbl_location_has_tbl_policy_tbl_policy1_idx` (`tbl_policy_id`),
  KEY `fk_tbl_location_has_tbl_policy_tbl_location_idx` (`tbl_location_id`),
  CONSTRAINT `fk_tbl_location_has_tbl_policy_tbl_location` FOREIGN KEY (`tbl_location_id`) REFERENCES `tbl_location` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tbl_location_has_tbl_policy_tbl_policy1` FOREIGN KEY (`tbl_policy_id`) REFERENCES `tbl_policy` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_location_has_tbl_policy`
--

LOCK TABLES `tbl_location_has_tbl_policy` WRITE;
/*!40000 ALTER TABLE `tbl_location_has_tbl_policy` DISABLE KEYS */;
INSERT INTO `tbl_location_has_tbl_policy` VALUES (1,1);
/*!40000 ALTER TABLE `tbl_location_has_tbl_policy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_order`
--

DROP TABLE IF EXISTS `tbl_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `total` double DEFAULT NULL,
  `check_in_date` bigint(20) NOT NULL,
  `check_out_date` bigint(20) DEFAULT NULL,
  `duration` bigint(20) DEFAULT NULL,
  `allowed_parking_from` bigint(20) DEFAULT NULL,
  `allowed_parking_to` bigint(20) DEFAULT NULL,
  `min_hour` int(11) DEFAULT '0',
  `tbl_order_status_id` int(11) NOT NULL,
  `tbl_user_id` int(11) NOT NULL,
  `tbl_location_id` int(11) NOT NULL,
  `tbl_vehicle_type_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_tbl_order_tbl_location1_idx` (`tbl_location_id`),
  KEY `FKcew8wssc7f6nr5jev20ltjwu8` (`tbl_order_status_id`),
  KEY `FKpewslmsot0yad4neixn3v2qqc` (`tbl_user_id`),
  KEY `fk_tbl_order_tbl_vehicle_type_idx` (`tbl_vehicle_type_id`),
  CONSTRAINT `FKcew8wssc7f6nr5jev20ltjwu8` FOREIGN KEY (`tbl_order_status_id`) REFERENCES `tbl_order_status` (`id`),
  CONSTRAINT `FKpewslmsot0yad4neixn3v2qqc` FOREIGN KEY (`tbl_user_id`) REFERENCES `tbl_user` (`id`),
  CONSTRAINT `fk_tbl_order_tbl_location1` FOREIGN KEY (`tbl_location_id`) REFERENCES `tbl_location` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tbl_order_tbl_vehicle_type` FOREIGN KEY (`tbl_vehicle_type_id`) REFERENCES `tbl_vehicle_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=107 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_order`
--

LOCK TABLES `tbl_order` WRITE;
/*!40000 ALTER TABLE `tbl_order` DISABLE KEYS */;
INSERT INTO `tbl_order` VALUES (1,1000,10,10,10,10,10,0,2,1,1,1),(2,NULL,0,NULL,NULL,NULL,NULL,0,2,1,1,1),(3,NULL,0,NULL,NULL,123,123,0,2,1,1,1),(4,NULL,0,NULL,NULL,123,123,0,2,1,1,1),(7,NULL,0,NULL,NULL,123,123,0,2,1,1,1),(8,NULL,0,NULL,NULL,123,123,0,2,1,1,1),(9,NULL,0,NULL,NULL,123,123,0,2,1,1,1),(10,NULL,0,NULL,NULL,123,123,0,2,1,1,1),(11,NULL,0,NULL,NULL,123,123,0,2,1,1,1),(12,NULL,0,NULL,NULL,123,123,0,2,1,1,1),(13,NULL,0,NULL,NULL,123,123,0,2,1,1,1),(14,NULL,0,NULL,NULL,123,123,0,2,1,1,1),(15,NULL,0,NULL,NULL,123,123,0,2,2,1,1),(16,NULL,0,NULL,NULL,123,123,0,2,2,1,1),(17,205,1539016763487,1539057672657,NULL,123,123,0,2,2,1,1),(18,0,1539058248033,1539058333723,NULL,123,123,0,2,2,1,1),(19,0,1539058529562,1539058595305,NULL,123,123,0,2,2,1,1),(20,225,1539016763487,1539061537016,NULL,123,123,0,2,2,1,1),(23,0,1539062094351,1539062369980,NULL,123,123,0,2,2,1,1),(24,0,1539062437046,1539062462513,NULL,123,123,0,2,2,1,1),(25,0,1539063511109,1539063532709,NULL,123,123,0,2,2,1,1),(26,0,1539064679991,1539064739012,NULL,123,123,0,2,2,1,1),(27,NULL,1539227936618,NULL,NULL,123,123,0,2,2,1,1),(28,15,1539230257491,1539234893572,4560000,123,123,0,2,1,1,1),(29,0,1539236128304,1539236145779,1020000,123,123,0,2,1,1,1),(30,NULL,1539353153438,NULL,NULL,123,123,0,2,1,1,1),(31,1,1539353281978,1539353345058,180000,123,123,0,2,1,1,1),(32,8.666666666666668,1539353374616,1539353400620,1560000,123,123,0,2,1,1,1),(33,6,1539399271021,1539399409612,1080000,123,123,0,2,1,1,1),(34,0,1539399448410,1539399569141,0,123,123,0,2,1,1,1),(35,6,1539399621414,1539399641149,1140000,123,123,0,2,1,1,1),(36,13,1539400091347,1539401512089,2400000,123,123,0,2,1,1,1),(37,6,1539401560602,1539401577871,1020000,123,123,0,2,1,1,1),(38,10,1539401637978,1539401726985,1740000,123,123,0,2,1,1,1),(39,NULL,1539401761829,NULL,NULL,123,123,0,2,1,1,1),(40,NULL,1539401986725,NULL,NULL,123,123,0,2,1,1,1),(41,NULL,1539402328919,NULL,NULL,123,123,0,2,1,1,1),(42,8,1539402415723,1539402439578,1380000,123,123,0,2,1,1,1),(43,NULL,1539402526186,NULL,NULL,123,123,0,2,1,1,1),(44,NULL,1539402620577,NULL,NULL,123,123,0,2,1,1,1),(45,NULL,1539402793457,NULL,NULL,123,123,0,2,1,1,1),(46,NULL,1539402967574,NULL,NULL,123,123,0,2,1,1,1),(47,NULL,1539403482151,NULL,NULL,123,123,0,2,1,1,1),(48,NULL,1539404705972,NULL,NULL,123,123,0,2,1,1,1),(49,NULL,1539404848857,NULL,NULL,123,123,0,2,2,1,1),(50,1403,1539405198967,1539657431975,255180000,123,123,1,2,2,1,1),(53,15,1539658083294,1539658147011,180000,1539402328919,1559402328919,1,2,1,1,1),(54,15,1539667968346,1539668214716,360000,1539402328919,1559402328919,1,2,1,1,1),(55,15,1539669919119,1539669980836,60000,1539402328919,1559402328919,1,2,1,1,1),(56,15,1539753960890,1539753962190,60000,1539402328919,1559402328919,1,2,1,1,1),(57,NULL,1539753981252,NULL,NULL,1539402328919,1559402328919,1,2,1,1,1),(58,NULL,1539754368513,NULL,NULL,1539402328919,1559402328919,1,2,1,1,1),(59,NULL,1539754738695,NULL,NULL,1539402328919,1559402328919,1,2,1,1,1),(60,15,1539754796780,1539754797834,60000,1539402328919,1559402328919,1,2,1,1,1),(61,15,1539755187259,1539755878171,1800000,1539402328919,1559402328919,1,2,1,1,1),(62,15,1539755909395,1539755919520,600000,1539402328919,1559402328919,1,2,1,1,1),(63,15,1539787690354,1539787692847,120000,1539402328919,1559402328919,1,2,1,1,1),(64,15,1539831862846,1539831879239,960000,1539402328919,1559402328919,1,2,1,1,1),(65,15,1539833308474,1539833309136,0,1539402328919,1559402328919,1,2,1,1,1),(66,15,1539833351875,1539833535457,180000,1539402328919,1559402328919,1,2,2,1,1),(67,15,1539833536710,1539833601988,300000,1539402328919,1559402328919,1,2,2,1,1),(68,15,1539833668242,1539833711541,2580000,1539402328919,1559402328919,1,2,2,1,1),(69,15,1539833818670,1539833827496,480000,1539402328919,1559402328919,1,2,1,1,1),(70,15,1539836000225,1539836006386,360000,1539402328919,1559402328919,1,2,1,1,1),(71,15,1539836170394,1539836176358,300000,1539402328919,1559402328919,1,2,1,1,1),(72,15,1539836256230,1539836266060,540000,1539402328919,1559402328919,1,2,1,1,1),(73,15,1539836382592,1539838639469,2160000,1539402328919,1559402328919,1,2,1,1,1),(74,17,1539836487683,1539842195239,40200,1539402328919,1559402328919,1,2,2,1,1),(75,15,1539838643973,1539838762560,3480000,1539402328919,1559402328919,1,2,1,1,1),(76,15,1539838774560,1539838785597,660000,1539402328919,1559402328919,1,2,1,1,1),(77,15,1539838943172,1539838947776,2400,1539402328919,1559402328919,1,2,1,1,1),(78,15,1539842196862,1539842200717,1800,1539402328919,1559402328919,1,2,2,1,1),(79,15,1539842502031,1539842663313,24600,1539402328919,1559402328919,1,2,2,1,1),(80,1449,1539842722382,1540105533505,263460,1539402328919,1559402328919,1,2,2,1,1),(81,15,1539843119501,1539843210613,18600,1539402328919,1559402328919,1,2,1,1,1),(82,16,1539864356420,1539870061146,38400,1539402328919,1559402328919,1,2,1,1,1),(83,15,1539870290743,1539870308569,10200,1539402328919,1559402328919,1,2,1,1,1),(84,15,1539870620084,1539870732452,31200,1539402328919,1559402328919,1,2,1,1,1),(85,15,1539870825454,1539870861114,2100,1539402328919,1559402328919,1,2,1,1,1),(86,15,1539919161840,1539919299617,1020,1539402328919,1559402328919,1,2,1,1,1),(87,15,1539919878467,1539919915738,2220,1539402328919,1559402328919,1,2,1,1,1),(88,15,1540016216631,1540016229050,720,1539402328919,1559402328919,1,2,1,1,1),(89,15,1540104851764,1540104926091,840,1539402328919,1559402328919,1,2,1,1,1),(90,15,1540105010297,1540105332514,1320,1539402328919,1559402328919,1,2,1,1,1),(91,15,1540105400384,1540106078829,1080,1539402328919,1559402328919,1,2,1,1,1),(92,15,1540105597675,1540105617557,1140,1539402328919,1559402328919,1,2,2,1,1),(93,15,1540105821512,1540105844867,1380,1539402328919,1559402328919,1,2,2,1,1),(94,748,1540005864452,1540105910607,100046155,1539402328919,1559402328919,1,2,1,1,1),(95,15,1540106121447,1540106176837,3300,1539402328919,1559402328919,1,2,2,1,1),(96,15,1540106384618,1540106397501,720,1539402328919,1559402328919,1,2,2,1,1),(97,15,1540616824347,1540616903290,1080,1539402328919,1559402328919,1,2,1,1,1),(98,15,1540616935723,1540617152692,2160,1539402328919,1559402328919,1,2,1,1,1),(99,20,1540641449842,1540645665536,4500,1539402328919,1559402328919,1,2,1,1,1),(100,15,1540645850634,1540645895368,2640,1539402328919,1559402328919,1,2,1,1,1),(101,15,1540645991677,1540646102462,3000,1539402328919,1559402328919,1,2,1,1,1),(102,15,1540646193455,1540646265039,660,1539402328919,1559402328919,1,2,1,1,1),(103,15,1540646296979,1540646431145,840,1539402328919,1559402328919,1,2,1,1,1),(104,15,1540646527071,1540647034208,1620,1539402328919,1559402328919,1,2,1,1,1),(105,770,1540647155517,1540790270001,141240,1539402328919,1559402328919,1,2,1,1,1),(106,15,1540790286749,1540790292596,300,1539402328919,1559402328919,1,2,1,1,1);
/*!40000 ALTER TABLE `tbl_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_order_pricing`
--

DROP TABLE IF EXISTS `tbl_order_pricing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_order_pricing` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `from_hour` int(11) NOT NULL,
  `price_per_hour` double NOT NULL,
  `late_fee_per_hour` int(11) DEFAULT NULL,
  `tbl_order_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1t04nclcvnhje9954ib04bnng` (`tbl_order_id`),
  CONSTRAINT `FK1t04nclcvnhje9954ib04bnng` FOREIGN KEY (`tbl_order_id`) REFERENCES `tbl_order` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=188 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_order_pricing`
--

LOCK TABLES `tbl_order_pricing` WRITE;
/*!40000 ALTER TABLE `tbl_order_pricing` DISABLE KEYS */;
INSERT INTO `tbl_order_pricing` VALUES (1,1,10,24,1),(2,3,25,33,1),(3,1,15,30,7),(4,3,20,25,7),(5,1,15,30,8),(6,3,20,25,8),(7,1,15,30,9),(8,3,20,25,9),(9,1,15,30,10),(10,3,20,25,10),(11,1,15,30,12),(12,3,20,25,12),(13,1,15,30,13),(14,3,20,25,13),(15,1,15,30,14),(16,3,20,25,14),(17,1,15,30,15),(18,3,20,25,15),(19,1,15,30,16),(20,3,20,25,16),(21,0,15,30,20),(22,3,20,25,20),(23,0,15,30,23),(24,3,20,25,23),(25,0,15,30,24),(26,3,20,25,24),(27,0,15,30,25),(28,3,20,25,25),(29,0,15,30,26),(30,3,20,25,26),(31,0,15,30,27),(32,3,20,25,27),(33,0,15,30,28),(34,3,20,25,28),(35,0,15,30,29),(36,3,20,25,29),(37,0,15,30,30),(38,3,20,25,30),(39,0,15,30,31),(40,3,20,25,31),(41,0,15,30,32),(42,3,20,25,32),(43,0,15,30,33),(44,3,20,25,33),(45,0,15,30,34),(46,3,20,25,34),(47,0,15,30,35),(48,3,20,25,35),(49,0,15,30,36),(50,3,20,25,36),(51,0,15,30,37),(52,3,20,25,37),(53,0,15,30,38),(54,3,20,25,38),(55,0,15,30,39),(56,3,20,25,39),(57,0,15,30,40),(58,3,20,25,40),(59,0,15,30,41),(60,3,20,25,41),(61,0,15,30,42),(62,3,20,25,42),(63,0,15,30,43),(64,3,20,25,43),(65,0,15,30,44),(66,3,20,25,44),(67,0,15,30,45),(68,3,20,25,45),(69,0,15,30,46),(70,3,20,25,46),(71,0,15,30,47),(72,3,20,25,47),(73,0,15,30,48),(74,3,20,25,48),(75,0,15,30,49),(76,3,20,25,49),(77,0,15,30,50),(78,3,20,25,50),(79,0,15,30,53),(80,3,20,25,53),(81,0,15,30,54),(82,3,20,25,54),(83,0,15,30,55),(84,3,20,25,55),(85,0,15,30,56),(86,3,20,25,56),(87,0,15,30,57),(88,3,20,25,57),(89,0,15,30,58),(90,3,20,25,58),(91,0,15,30,59),(92,3,20,25,59),(93,0,15,30,60),(94,3,20,25,60),(95,0,15,30,61),(96,3,20,25,61),(97,0,15,30,62),(98,3,20,25,62),(99,0,15,30,63),(100,3,20,25,63),(101,0,15,30,64),(102,3,20,25,64),(103,0,15,30,65),(104,3,20,25,65),(105,0,15,30,66),(106,3,20,25,66),(107,0,15,30,67),(108,3,20,25,67),(109,0,15,30,68),(110,3,20,25,68),(111,0,15,30,69),(112,3,20,25,69),(113,0,15,30,70),(114,3,20,25,70),(115,0,15,30,71),(116,3,20,25,71),(117,0,15,30,72),(118,3,20,25,72),(119,0,15,30,73),(120,3,20,25,73),(121,0,15,30,74),(122,3,20,25,74),(123,0,15,30,75),(124,3,20,25,75),(125,0,15,30,76),(126,3,20,25,76),(127,0,15,30,77),(128,3,20,25,77),(129,0,15,30,78),(130,3,20,25,78),(131,0,15,30,79),(132,3,20,25,79),(133,0,15,30,80),(134,3,20,25,80),(135,0,15,30,81),(136,3,20,25,81),(137,0,15,30,82),(138,3,20,25,82),(139,0,15,30,83),(140,3,20,25,83),(141,0,15,30,84),(142,3,20,25,84),(143,0,15,30,85),(144,3,20,25,85),(145,0,15,30,86),(146,3,20,25,86),(147,0,15,30,87),(148,3,20,25,87),(149,0,15,30,88),(150,3,20,25,88),(151,0,15,30,89),(152,3,20,25,89),(153,0,15,30,90),(154,3,20,25,90),(155,0,15,30,91),(156,3,20,25,91),(157,0,15,30,92),(158,3,20,25,92),(159,0,15,30,93),(160,3,20,25,93),(161,0,15,30,94),(162,3,20,25,94),(163,0,15,30,95),(164,3,20,25,95),(165,0,15,30,96),(166,3,20,25,96),(167,7,30,35,94),(168,0,15,30,97),(169,3,20,25,97),(170,0,15,30,98),(171,3,20,25,98),(172,0,15,30,99),(173,3,20,25,99),(174,0,15,30,100),(175,3,20,25,100),(176,0,15,30,101),(177,3,20,25,101),(178,0,15,30,102),(179,3,20,25,102),(180,0,15,30,103),(181,3,20,25,103),(182,0,15,30,104),(183,3,20,25,104),(184,0,15,30,105),(185,3,20,25,105),(186,0,15,30,106),(187,3,20,25,106);
/*!40000 ALTER TABLE `tbl_order_pricing` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_order_status`
--

DROP TABLE IF EXISTS `tbl_order_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_order_status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_order_status`
--

LOCK TABLES `tbl_order_status` WRITE;
/*!40000 ALTER TABLE `tbl_order_status` DISABLE KEYS */;
INSERT INTO `tbl_order_status` VALUES (1,'open',NULL),(2,'close',NULL);
/*!40000 ALTER TABLE `tbl_order_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_policy`
--

DROP TABLE IF EXISTS `tbl_policy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_policy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `allowed_parking_from` bigint(20) NOT NULL,
  `allowed_parking_to` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_policy`
--

LOCK TABLES `tbl_policy` WRITE;
/*!40000 ALTER TABLE `tbl_policy` DISABLE KEYS */;
INSERT INTO `tbl_policy` VALUES (1,1539402328919,1559402328919),(2,1539402328919,1559402328919);
/*!40000 ALTER TABLE `tbl_policy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_policy_has_tbl_vehicle_type`
--

DROP TABLE IF EXISTS `tbl_policy_has_tbl_vehicle_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_policy_has_tbl_vehicle_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tbl_policy_id` int(11) NOT NULL,
  `tbl_vehicle_type_id` int(11) NOT NULL,
  `min_hour` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_tbl_policy_has_tbl_vehicle_type_tbl_policy1_idx` (`tbl_policy_id`),
  KEY `fk_tbl_policy_has_tbl_vehicle_type_tbl_vehicle_type1_idx` (`tbl_vehicle_type_id`),
  CONSTRAINT `fk_tbl_policy_has_tbl_vehicle_type_tbl_policy1` FOREIGN KEY (`tbl_policy_id`) REFERENCES `tbl_policy` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tbl_policy_has_tbl_vehicle_type_tbl_vehicle_type1` FOREIGN KEY (`tbl_vehicle_type_id`) REFERENCES `tbl_vehicle_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_policy_has_tbl_vehicle_type`
--

LOCK TABLES `tbl_policy_has_tbl_vehicle_type` WRITE;
/*!40000 ALTER TABLE `tbl_policy_has_tbl_vehicle_type` DISABLE KEYS */;
INSERT INTO `tbl_policy_has_tbl_vehicle_type` VALUES (1,1,1,1),(2,1,2,1);
/*!40000 ALTER TABLE `tbl_policy_has_tbl_vehicle_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_pricing`
--

DROP TABLE IF EXISTS `tbl_pricing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_pricing` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `from_hour` int(11) NOT NULL,
  `price_per_hour` double NOT NULL,
  `late_fee_per_hour` int(11) DEFAULT NULL,
  `tbl_policy_has_tbl_vehicle_type_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_tbl_pricing_tbl_policy_has_tbl_vehicle_type1_idx` (`tbl_policy_has_tbl_vehicle_type_id`),
  CONSTRAINT `fk_tbl_pricing_tbl_policy_has_tbl_vehicle_type1` FOREIGN KEY (`tbl_policy_has_tbl_vehicle_type_id`) REFERENCES `tbl_policy_has_tbl_vehicle_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_pricing`
--

LOCK TABLES `tbl_pricing` WRITE;
/*!40000 ALTER TABLE `tbl_pricing` DISABLE KEYS */;
INSERT INTO `tbl_pricing` VALUES (1,0,15,30,1),(2,3,20,25,1);
/*!40000 ALTER TABLE `tbl_pricing` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_staft`
--

DROP TABLE IF EXISTS `tbl_staft`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_staft` (
  `username` varchar(25) NOT NULL,
  `password` varchar(45) DEFAULT NULL,
  `is_deactivated` bit(1) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_staft`
--

LOCK TABLES `tbl_staft` WRITE;
/*!40000 ALTER TABLE `tbl_staft` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_staft` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_user`
--

DROP TABLE IF EXISTS `tbl_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone_number` varchar(15) NOT NULL,
  `password` varchar(60) NOT NULL,
  `money` double NOT NULL,
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `sms_noti` bit(1) DEFAULT b'0',
  `is_activated` bit(1) DEFAULT b'0',
  `tbl_vehicle_vehicle_number` varchar(12) NOT NULL,
  `confirm_code` varchar(7) DEFAULT NULL,
  PRIMARY KEY (`id`,`tbl_vehicle_vehicle_number`),
  UNIQUE KEY `phone_number_UNIQUE` (`phone_number`),
  KEY `fk_tbl_user_tbl_vehicle1_idx` (`tbl_vehicle_vehicle_number`),
  CONSTRAINT `fk_tbl_user_tbl_vehicle1` FOREIGN KEY (`tbl_vehicle_vehicle_number`) REFERENCES `tbl_vehicle` (`vehicle_number`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_user`
--

LOCK TABLES `tbl_user` WRITE;
/*!40000 ALTER TABLE `tbl_user` DISABLE KEYS */;
INSERT INTO `tbl_user` VALUES (1,'123','123456',2865.3333333333335,'bao','quoc','','','30A-969.32T',NULL),(2,'47281678','123',123,'cuong','mai','\0','\0','31A-969.32T',NULL),(28,'388268881','1234567',0,'cuong','abc',NULL,'\0','51X2-932.34',NULL),(29,'9876545','123456',0,'trung','ne','\0','','52A-12345',NULL),(30,'1234567890','123456',0,'trung','ne','\0','','52A2-12345',NULL),(31,'88889999113','123456',300,'super','hot','\0','','55X1-728.11',NULL),(32,'+84899168485','play4fun',0,'jim','raynor','\0','','66H8-288.28',NULL),(34,'+84902739291','123455',0,'cua ','ha ha',NULL,'\0','86D6-835.28',NULL);
/*!40000 ALTER TABLE `tbl_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_vehicle`
--

DROP TABLE IF EXISTS `tbl_vehicle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_vehicle` (
  `vehicle_number` varchar(12) NOT NULL,
  `license_plate_id` varchar(10) DEFAULT NULL,
  `brand` varchar(45) DEFAULT NULL,
  `size` varchar(255) DEFAULT NULL,
  `expire_date` bigint(20) DEFAULT NULL,
  `tbl_vehicle_type_id` int(11) DEFAULT NULL,
  `is_verified` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`vehicle_number`),
  KEY `fk_tbl_vehicle_tbl_vehicle_type_idx` (`tbl_vehicle_type_id`),
  CONSTRAINT `fk_tbl_vehicle_tbl_vehicle_type` FOREIGN KEY (`tbl_vehicle_type_id`) REFERENCES `tbl_vehicle_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_vehicle`
--

LOCK TABLES `tbl_vehicle` WRITE;
/*!40000 ALTER TABLE `tbl_vehicle` DISABLE KEYS */;
INSERT INTO `tbl_vehicle` VALUES ('14124214','424214','214214','214214',1539277200000,1,''),('14214124214','2141243',NULL,NULL,NULL,NULL,'\0'),('20A291','1238083012','dákh','2180',1539104400000,1,''),('30A-969.32T','KC-3415132','trgfrebgertg','wergewgrew',1539450000000,1,''),('31A-969.32T','KC-6156693','FORD','3400x1600x1400 mm',1551459600000,1,''),('325f234fr32','12321ed21e','21312321','3213213',1539190800000,1,''),('32A-929.32T','KC-6156699','KIA','3595x1595x1490 mm',1539062369980,1,''),('332432432','432432','4324324','432432',1538672400000,1,''),('40A-969.32T','KC-6156696','KIA','3595x1595x1490 mm',1539062369980,1,''),('431243213','432423526',NULL,NULL,NULL,NULL,'\0'),('43243242','324324324','324324324','234324',1538845200000,1,''),('51X2-932.34','1375827455',NULL,NULL,NULL,NULL,'\0'),('52A-12345','123456','ABC','sapdkfp',1540054800000,1,''),('52A2-12345','12345678','hello','its me',1539450000000,2,''),('55X1-728.11','11118888','sms','hello',1539363600000,1,''),('66H8-288.28','85829888','hello men','19900.210mm',1549990800000,2,''),('72A-88278','8829981845','wqernewo`','dsownfoqwn',1539622800000,2,''),('78A12349','213448u','KAI','12398mm',1539968400000,2,''),('82D7-728.82','758288','KIA','sfasd',1538845200000,1,''),('85A-969.32T','KC-6156697','FORD','1300mm x 700mm',1540659600000,1,'\0'),('86D6-835.28','84882882',NULL,NULL,NULL,NULL,'\0'),('90XX-12492','13249219','KIA','124932mm',1539277200000,1,'');
/*!40000 ALTER TABLE `tbl_vehicle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_vehicle_type`
--

DROP TABLE IF EXISTS `tbl_vehicle_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_vehicle_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_vehicle_type`
--

LOCK TABLES `tbl_vehicle_type` WRITE;
/*!40000 ALTER TABLE `tbl_vehicle_type` DISABLE KEYS */;
INSERT INTO `tbl_vehicle_type` VALUES (1,'4 chỗ','',NULL,NULL,NULL,NULL,NULL,0),(2,'7 chỗ','',NULL,NULL,NULL,NULL,NULL,0);
/*!40000 ALTER TABLE `tbl_vehicle_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'vehiclenfc'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-10-29 12:56:55
