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
  `en_location` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_location`
--

LOCK TABLES `tbl_location` WRITE;
/*!40000 ALTER TABLE `tbl_location` DISABLE KEYS */;
INSERT INTO `tbl_location` VALUES (1,'Quang trung',NULL,'',NULL),(2,'Bùi Viện','Quận 1','\0',NULL),(3,'Trần Hưng Đạo','Quận 5','',NULL),(4,'Phan Huy Ích','Gò Vấp','',NULL),(5,'Phạm Văn Chiêu','Gò Vấp','',NULL),(6,'Nguyễn Kiệm','Gò Vấp','',NULL);
/*!40000 ALTER TABLE `tbl_location` ENABLE KEYS */;
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
  `tbl_vehicle_vehicle_number` varchar(12) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_tbl_order_tbl_location1_idx` (`tbl_location_id`),
  KEY `FKcew8wssc7f6nr5jev20ltjwu8` (`tbl_order_status_id`),
  KEY `FKpewslmsot0yad4neixn3v2qqc` (`tbl_user_id`),
  KEY `fk_tbl_order_tbl_vehicle1_idx` (`tbl_vehicle_vehicle_number`),
  CONSTRAINT `FKcew8wssc7f6nr5jev20ltjwu8` FOREIGN KEY (`tbl_order_status_id`) REFERENCES `tbl_order_status` (`id`),
  CONSTRAINT `FKpewslmsot0yad4neixn3v2qqc` FOREIGN KEY (`tbl_user_id`) REFERENCES `tbl_user` (`id`),
  CONSTRAINT `fk_tbl_order_tbl_location1` FOREIGN KEY (`tbl_location_id`) REFERENCES `tbl_location` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tbl_order_tbl_vehicle1` FOREIGN KEY (`tbl_vehicle_vehicle_number`) REFERENCES `tbl_vehicle` (`vehicle_number`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=210 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_order`
--

LOCK TABLES `tbl_order` WRITE;
/*!40000 ALTER TABLE `tbl_order` DISABLE KEYS */;
INSERT INTO `tbl_order` VALUES (199,12,1542415327963,1542415561980,234017,84600000,37800000,1,6,36,1,'95D3-123.66'),(200,1064,1542415565756,1542503504503,87938747,84600000,37800000,1,6,36,1,'95D3-123.66'),(201,7695,1542603405614,1543118797605,515391991,84600000,37800000,1,6,36,1,'95D3-123.66'),(202,12,1543118988159,1543119019918,31759,84600000,37800000,1,4,36,1,'95D3-123.66'),(203,12,1543119068223,1543119099604,31381,84600000,37800000,1,4,36,1,'95D3-123.66'),(204,12,1543930452861,1543930547990,95129,84600000,60800000,1,4,73,1,'51A1-12345'),(205,533,1543930586335,1543973927035,43340700,84600000,60800000,1,4,73,1,'51A1-12345'),(206,12,1543975197195,1543975323263,126068,84600000,60800000,1,4,73,1,'51A1-12345'),(207,12,1543975532163,1543975619519,87356,84600000,60800000,1,4,73,1,'51A1-12345'),(208,12,1543978436257,1543980516080,2079823,84600000,60800000,1,4,36,1,'95D3-123.66'),(209,NULL,1544151600000,NULL,NULL,1544137200,1544158800000,1,3,36,1,'78A3-12302');
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
) ENGINE=InnoDB AUTO_INCREMENT=390 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_order_pricing`
--

LOCK TABLES `tbl_order_pricing` WRITE;
/*!40000 ALTER TABLE `tbl_order_pricing` DISABLE KEYS */;
INSERT INTO `tbl_order_pricing` VALUES (368,0,12,22,199),(369,3,30,55,199),(370,0,12,22,200),(371,3,30,55,200),(372,0,12,22,201),(373,3,30,55,201),(374,0,12,22,202),(375,3,30,55,202),(376,0,12,22,203),(377,3,30,55,203),(378,0,12,22,204),(379,3,30,55,204),(380,0,12,22,205),(381,3,30,55,205),(382,0,12,22,206),(383,3,30,55,206),(384,0,12,22,207),(385,3,30,55,207),(386,0,12,22,208),(387,3,30,55,208),(388,0,12,22,209),(389,3,30,55,209);
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_order_status`
--

LOCK TABLES `tbl_order_status` WRITE;
/*!40000 ALTER TABLE `tbl_order_status` DISABLE KEYS */;
INSERT INTO `tbl_order_status` VALUES (3,'Open',NULL),(4,'Close',NULL),(5,'Refund requested',NULL),(6,'Refunded',NULL);
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
  `tbl_location_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_idx` (`tbl_location_id`),
  CONSTRAINT `id` FOREIGN KEY (`tbl_location_id`) REFERENCES `tbl_location` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_policy`
--

LOCK TABLES `tbl_policy` WRITE;
/*!40000 ALTER TABLE `tbl_policy` DISABLE KEYS */;
INSERT INTO `tbl_policy` VALUES (4,84600000,60800000,1),(11,84600000,37800000,3),(12,84600000,37800000,4),(13,84600000,37800000,5),(14,84600000,37800000,6),(18,84600000,60800000,6);
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
  `min_hour` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_tbl_policy_instance_has_tbl_vehicle_type_tbl_vehicle_typ_idx` (`tbl_vehicle_type_id`),
  KEY `fk_tbl_policy_instance_has_tbl_vehicle_type_tbl_policy_inst_idx` (`tbl_policy_id`),
  CONSTRAINT `fk_tbl_policy_instance_has_tbl_vehicle_type_tbl_policy_instan1` FOREIGN KEY (`tbl_policy_id`) REFERENCES `tbl_policy` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tbl_policy_instance_has_tbl_vehicle_type_tbl_vehicle_type1` FOREIGN KEY (`tbl_vehicle_type_id`) REFERENCES `tbl_vehicle_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_policy_has_tbl_vehicle_type`
--

LOCK TABLES `tbl_policy_has_tbl_vehicle_type` WRITE;
/*!40000 ALTER TABLE `tbl_policy_has_tbl_vehicle_type` DISABLE KEYS */;
INSERT INTO `tbl_policy_has_tbl_vehicle_type` VALUES (2,4,3,1),(3,4,9,3),(4,4,10,2),(5,4,11,1),(24,11,3,1),(25,11,9,3),(26,11,10,2),(27,11,11,1),(28,12,3,1),(29,12,9,3),(30,12,10,2),(31,12,11,1),(32,13,3,1),(33,13,9,3),(34,13,10,2),(35,13,11,1),(36,14,3,1),(37,14,9,3),(38,14,10,2),(39,14,11,1),(44,18,3,1),(45,18,9,3),(46,18,10,2),(47,18,11,1);
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
  `from_date` bigint(20) DEFAULT NULL,
  `tbl_policy_has_tbl_vehicle_type_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `tbl_policy_has_tbl_vehicle_type_id_idx` (`tbl_policy_has_tbl_vehicle_type_id`),
  CONSTRAINT `tbl_policy_has_tbl_vehicle_type_id` FOREIGN KEY (`tbl_policy_has_tbl_vehicle_type_id`) REFERENCES `tbl_policy_has_tbl_vehicle_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_pricing`
--

LOCK TABLES `tbl_pricing` WRITE;
/*!40000 ALTER TABLE `tbl_pricing` DISABLE KEYS */;
INSERT INTO `tbl_pricing` VALUES (59,0,12,22,NULL,2),(60,3,30,55,NULL,2),(61,0,5,15,NULL,3),(62,2,20,50,NULL,3),(63,3,50,60,NULL,3),(64,0,12,22,NULL,44),(65,3,30,55,NULL,44),(66,0,5,15,NULL,45),(67,2,20,50,NULL,45),(68,3,50,60,NULL,45);
/*!40000 ALTER TABLE `tbl_pricing` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_refund_request`
--

DROP TABLE IF EXISTS `tbl_refund_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_refund_request` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `amount` double DEFAULT NULL,
  `tbl_staft_username` varchar(25) NOT NULL,
  `tbl_manager_username` varchar(25) DEFAULT NULL,
  `tbl_refund_status_id` int(11) NOT NULL,
  `tbl_order_id` int(11) NOT NULL,
  `create_date` bigint(20) DEFAULT NULL,
  `close_date` bigint(20) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_tbl_refund_request_tbl_staft_idx` (`tbl_staft_username`),
  KEY `fk_tbl_refund_request_tbl_staft1_idx` (`tbl_manager_username`),
  KEY `fk_tbl_refund_request_tbl_order1_idx` (`tbl_order_id`),
  KEY `fk_tbl_refund_request_tbl_refund_status_idx` (`tbl_refund_status_id`),
  CONSTRAINT `fk_tbl_refund_request_tbl_order1` FOREIGN KEY (`tbl_order_id`) REFERENCES `tbl_order` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tbl_refund_request_tbl_refund_status` FOREIGN KEY (`tbl_refund_status_id`) REFERENCES `tbl_refund_status` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tbl_refund_request_tbl_staft` FOREIGN KEY (`tbl_staft_username`) REFERENCES `tbl_staff` (`username`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tbl_refund_request_tbl_staft1` FOREIGN KEY (`tbl_manager_username`) REFERENCES `tbl_staff` (`username`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_refund_request`
--

LOCK TABLES `tbl_refund_request` WRITE;
/*!40000 ALTER TABLE `tbl_refund_request` DISABLE KEYS */;
INSERT INTO `tbl_refund_request` VALUES (1,123,'staff','manager',2,199,1542462299055,1542462318451,'hello'),(2,180,'staff','manager',2,199,1542502343028,1542502361676,'yes please'),(3,1000,'staff','manager',3,200,1542503526516,1542543796274,'te'),(4,520,'staff','manager',3,200,1542544112344,1542544311568,'please refund me'),(5,250,'staff','manager',3,200,1542544711768,1542544902961,'please'),(6,200,'staff','manager',3,200,1542545016508,1542545031072,'hello'),(7,2000,'staff','manager',3,200,1542593156326,1542593376598,''),(8,120,'staff','manager',2,200,1542593401530,1542593462176,''),(9,10000,'staff','manager',2,201,1543118881339,1543118888757,'PLEASE'),(10,10000,'staff','manager',2,200,1543974683401,1543974700587,'abc');
/*!40000 ALTER TABLE `tbl_refund_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_refund_status`
--

DROP TABLE IF EXISTS `tbl_refund_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_refund_status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_refund_status`
--

LOCK TABLES `tbl_refund_status` WRITE;
/*!40000 ALTER TABLE `tbl_refund_status` DISABLE KEYS */;
INSERT INTO `tbl_refund_status` VALUES (1,'Open',NULL),(2,'Approved',NULL),(3,'Rejected',NULL);
/*!40000 ALTER TABLE `tbl_refund_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_staff`
--

DROP TABLE IF EXISTS `tbl_staff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_staff` (
  `username` varchar(25) NOT NULL,
  `password` varchar(45) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `is_manager` bit(1) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_staff`
--

LOCK TABLES `tbl_staff` WRITE;
/*!40000 ALTER TABLE `tbl_staff` DISABLE KEYS */;
INSERT INTO `tbl_staff` VALUES ('cuncun','123','',''),('cuong','mai','','\0'),('manager','456','',''),('staff','123','','\0');
/*!40000 ALTER TABLE `tbl_staff` ENABLE KEYS */;
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
  `tbl_vehicle_vehicle_number` varchar(12) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `phone_number_UNIQUE` (`phone_number`),
  KEY `fk_tbl_user_tbl_vehicle1_idx` (`tbl_vehicle_vehicle_number`),
  CONSTRAINT `fk_tbl_user_tbl_vehicle1` FOREIGN KEY (`tbl_vehicle_vehicle_number`) REFERENCES `tbl_vehicle` (`vehicle_number`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_user`
--

LOCK TABLES `tbl_user` WRITE;
/*!40000 ALTER TABLE `tbl_user` DISABLE KEYS */;
INSERT INTO `tbl_user` VALUES (36,'+84899168485','123456',12477,'jim','raynor','\0','','78A3-12302'),(44,'093407758','123456',1000,'Thanh','Truong','','',NULL),(73,'+84902739291','123456',671,'quoc','bao','\0','','51A1-12345'),(76,'1224093586','123456',0,'dang','trung','\0','','52A-98765'),(77,'1234567','123456',0,'dang','trung','\0','','52A-123.45'),(78,'0987654321','123456',0,'trung','ne','\0','','52W-123.45'),(79,'24747483833','123456',0,'avd','whhsh','\0','','52A-98471'),(80,'738372888444','1223456',0,'cuong','mai','\0','','46A2-72888'),(81,'474849595432','1235689',0,'av','djrh','\0','','24A-123.55'),(82,'123456543','123456',0,'ttung','ne','\0','','83A-121.57'),(83,'12345567288','123456',0,'test','ter','\0','','88A6-882.33'),(85,'4687556776','123456',0,'fhjuh','ghkk','\0','','67H6-467.77');
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
  `is_active` bit(1) NOT NULL DEFAULT b'1',
  `tbl_user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`vehicle_number`),
  KEY `fk_tbl_vehicle_tbl_vehicle_type_idx` (`tbl_vehicle_type_id`),
  KEY `fk_tbl_vehicle_tbl_user1_idx` (`tbl_user_id`),
  CONSTRAINT `fk_tbl_vehicle_tbl_user1` FOREIGN KEY (`tbl_user_id`) REFERENCES `tbl_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tbl_vehicle_tbl_vehicle_type` FOREIGN KEY (`tbl_vehicle_type_id`) REFERENCES `tbl_vehicle_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_vehicle`
--

LOCK TABLES `tbl_vehicle` WRITE;
/*!40000 ALTER TABLE `tbl_vehicle` DISABLE KEYS */;
INSERT INTO `tbl_vehicle` VALUES ('21A5-45733','72891786','honda','1100mm',1543510800000,3,'','\0',NULL),('24A-123.55','47474848',NULL,NULL,0,NULL,'\0','',81),('46A2-72888','88748288',NULL,NULL,0,NULL,'\0','',80),('51A1-12345','12345678','hondo','2000m',1543078800000,3,'','',73),('52A-123.45','12345678',NULL,NULL,0,NULL,'\0','',77),('52A-98471','474748382',NULL,NULL,0,NULL,'\0','',79),('52A-98765','12345678',NULL,NULL,0,NULL,'\0','',76),('52W-123.45','12345566',NULL,NULL,0,NULL,'\0','',78),('67H6-467.77','676557887',NULL,NULL,0,NULL,'\0','',85),('78A3-12302','7281998289','toyota','1000mm x 2000mm',1546102800000,3,'','',36),('83A-121.57','3738383',NULL,NULL,0,NULL,'\0','',82),('88A6-882.33','399382888',NULL,NULL,0,NULL,'\0','',83),('95D3-123.66','83817754','toyota','1500mm',1543510800000,3,'','\0',NULL);
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
  `en_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_vehicle_type`
--

LOCK TABLES `tbl_vehicle_type` WRITE;
/*!40000 ALTER TABLE `tbl_vehicle_type` DISABLE KEYS */;
INSERT INTO `tbl_vehicle_type` VALUES (3,'4 chỗ','4-seat car'),(4,'7 chỗ','7-seat car'),(9,'16 chỗ','16-seat car'),(10,'xe tải nhỏ','small truck'),(11,'xe tải vừa','medium truck');
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

-- Dump completed on 2018-12-07 16:18:54
