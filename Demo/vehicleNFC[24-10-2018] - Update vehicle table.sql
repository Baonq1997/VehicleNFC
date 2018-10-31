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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_order`
--

LOCK TABLES `tbl_order` WRITE;
/*!40000 ALTER TABLE `tbl_order` DISABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_order_pricing`
--

LOCK TABLES `tbl_order_pricing` WRITE;
/*!40000 ALTER TABLE `tbl_order_pricing` DISABLE KEYS */;
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
  PRIMARY KEY (`id`,`tbl_vehicle_vehicle_number`),
  UNIQUE KEY `phone_number_UNIQUE` (`phone_number`),
  KEY `fk_tbl_user_tbl_vehicle1_idx` (`tbl_vehicle_vehicle_number`),
  CONSTRAINT `fk_tbl_user_tbl_vehicle1` FOREIGN KEY (`tbl_vehicle_vehicle_number`) REFERENCES `tbl_vehicle` (`vehicle_number`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_user`
--

LOCK TABLES `tbl_user` WRITE;
/*!40000 ALTER TABLE `tbl_user` DISABLE KEYS */;
INSERT INTO `tbl_user` VALUES (26,'123','123456',1000,'bao','quoc','\0','','30A-969.32T');
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
INSERT INTO `tbl_vehicle` VALUES ('30A-969.32T','KC-6156692','KIA','3595x1595x1490 mm',1582304400,1,''),('31A-969.32T','KC-6156693','TOYOTA',NULL,NULL,NULL,'\0');
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

-- Dump completed on 2018-10-24 10:24:42
