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
INSERT INTO `tbl_location` VALUES (1,'Quang trung',NULL,'');
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
) ENGINE=InnoDB AUTO_INCREMENT=165 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_order`
--

LOCK TABLES `tbl_order` WRITE;
/*!40000 ALTER TABLE `tbl_order` DISABLE KEYS */;
INSERT INTO `tbl_order` VALUES (129,10,1540972757601,1540973257109,499508,0,1900000000000000,1,4,36,1,3),(130,10,1540973302722,1540973380301,77579,0,1900000000000000,1,4,36,1,3),(131,10,1540973384331,1540974046586,662255,0,1900000000000000,1,4,36,1,3),(132,10,1540974055480,1540974062115,6635,0,1900000000000000,1,4,36,1,3),(133,10,1540974078667,1540974084264,5597,0,1900000000000000,1,4,36,1,3),(134,10,1540974220472,1540974224894,4422,0,1900000000000000,1,4,36,1,3),(135,10,1541037472580,1541038010961,538381,0,1900000000000000,1,4,36,1,3),(137,10,1541237040025,1541237065368,25343,0,1900000000000000,1,4,36,1,3),(138,109,1541216992170,1541237276305,20284135,0,1900000000000000,1,4,36,1,3),(139,119,1541216992170,1541238526279,21534109,0,1900000000000000,1,4,36,1,3),(140,122,1541216992170,1541238775100,21782930,0,1900000000000000,1,4,36,1,3),(141,246,1541216992170,1541241922094,24929924,1541289600000,1541307600000,1,4,36,1,3),(143,10,1541324705543,1541324721377,15834,1541264400000,1541347200000,1,4,36,1,3),(144,10,1541326633554,1541326963643,330089,1541264400000,1541347200000,1,4,36,1,3),(145,10,1541327097051,1541327107901,10850,1541264400000,1541347200000,1,4,36,1,3),(146,10,1541330237227,1541330383878,146651,1541264400000,1541347200000,1,4,36,1,3),(148,10,1541330615957,1541330743851,127894,1541264400000,1541347200000,1,4,36,1,3),(149,1826,1541331039226,1541472582289,141543063,1541264400000,1541347200000,1,4,36,1,3),(160,10,1541470878604,1541471641312,762708,1541264400000,1541347200000,1,4,36,1,3),(161,10,1541471653015,1541472297641,644626,1541264400000,1541347200000,1,4,36,1,3),(162,10,1541474339781,1541474488237,148456,1541264400000,1541347200000,1,4,36,1,3),(163,10,1541474606430,1541474734909,128479,1541264400000,1541347200000,1,4,36,1,3),(164,10,1541474760727,1541474798226,37499,1541264400000,1541347200000,1,4,36,1,3);
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
  `late_fee_per_hour` double DEFAULT NULL,
  `tbl_order_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1t04nclcvnhje9954ib04bnng` (`tbl_order_id`),
  CONSTRAINT `FK1t04nclcvnhje9954ib04bnng` FOREIGN KEY (`tbl_order_id`) REFERENCES `tbl_order` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=301 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_order_pricing`
--

LOCK TABLES `tbl_order_pricing` WRITE;
/*!40000 ALTER TABLE `tbl_order_pricing` DISABLE KEYS */;
INSERT INTO `tbl_order_pricing` VALUES (230,0,10,20,129),(231,3,30,50,129),(232,0,10,20,130),(233,3,30,50,130),(234,0,10,20,131),(235,3,30,50,131),(236,0,10,20,132),(237,3,30,50,132),(238,0,10,20,133),(239,3,30,50,133),(240,0,10,20,134),(241,3,30,50,134),(242,0,10,20,135),(243,3,30,50,135),(246,0,10,20,137),(247,3,30,50,137),(248,0,10,20,138),(249,3,30,50,138),(250,0,10,20,139),(251,3,30,50,139),(252,0,10,20,140),(253,3,30,50,140),(254,0,10,20,141),(255,3,30,50,141),(257,0,10,20,143),(258,3,30,50,143),(259,0,10,20,144),(260,3,30,50,144),(261,0,10,20,145),(262,3,30,50,145),(263,0,10,20,146),(264,3,30,50,146),(267,0,10,20,148),(268,3,30,50,148),(269,0,10,20,149),(270,3,30,50,149),(291,0,10,20,160),(292,3,30,50,160),(293,0,10,20,161),(294,3,30,50,161),(295,0,10,20,162),(296,3,30,50,162),(297,0,10,20,163),(298,3,30,50,163),(299,0,10,20,164),(300,3,30,50,164);
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_order_status`
--

LOCK TABLES `tbl_order_status` WRITE;
/*!40000 ALTER TABLE `tbl_order_status` DISABLE KEYS */;
INSERT INTO `tbl_order_status` VALUES (3,'Open',NULL),(4,'Close',NULL);
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
  `tbl_time_id` int(11) NOT NULL,
  `tbl_vehicle_type_id` int(11) NOT NULL,
  `min_hour` int(11) DEFAULT NULL,
  `tbl_location_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_tbl_policy_instance_has_tbl_vehicle_type_tbl_vehicle_typ_idx` (`tbl_vehicle_type_id`),
  KEY `fk_tbl_policy_instance_has_tbl_vehicle_type_tbl_policy_inst_idx` (`tbl_time_id`),
  KEY `fk_tbl_policy_instance_has_tbl_vehicle_type_tbl_location1_idx` (`tbl_location_id`),
  CONSTRAINT `fk_tbl_policy_instance_has_tbl_vehicle_type_tbl_location1` FOREIGN KEY (`tbl_location_id`) REFERENCES `tbl_location` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tbl_policy_instance_has_tbl_vehicle_type_tbl_policy_instan1` FOREIGN KEY (`tbl_time_id`) REFERENCES `tbl_time` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tbl_policy_instance_has_tbl_vehicle_type_tbl_vehicle_type1` FOREIGN KEY (`tbl_vehicle_type_id`) REFERENCES `tbl_vehicle_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_policy`
--

LOCK TABLES `tbl_policy` WRITE;
/*!40000 ALTER TABLE `tbl_policy` DISABLE KEYS */;
INSERT INTO `tbl_policy` VALUES (2,4,3,1,1),(3,4,4,2,1);
/*!40000 ALTER TABLE `tbl_policy` ENABLE KEYS */;
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
  `late_fee_per_hour` double DEFAULT NULL,
  `tbl_policy_id` int(11) NOT NULL,
  PRIMARY KEY (`id`,`tbl_policy_id`),
  KEY `fk_tbl_pricing_tbl_policy_instance_has_tbl_vehicle_type1_idx` (`tbl_policy_id`),
  CONSTRAINT `fk_tbl_pricing_tbl_policy_instance_has_tbl_vehicle_type1` FOREIGN KEY (`tbl_policy_id`) REFERENCES `tbl_policy` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_pricing`
--

LOCK TABLES `tbl_pricing` WRITE;
/*!40000 ALTER TABLE `tbl_pricing` DISABLE KEYS */;
INSERT INTO `tbl_pricing` VALUES (5,0,10,30,2),(6,3,20,50,2);
/*!40000 ALTER TABLE `tbl_pricing` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_refund_request`
--

DROP TABLE IF EXISTS `tbl_refund_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_refund_request` (
  `id` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `tbl_staft_username` varchar(25) NOT NULL,
  `tbl_manager_username` varchar(25) DEFAULT NULL,
  `tbl_order_id` int(11) NOT NULL,
  `tbl_refund_status_id` int(11) NOT NULL,
  `amount` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_tbl_refund_tbl_staft_idx` (`tbl_staft_username`),
  KEY `fk_tbl_refund_tbl_staft1_idx` (`tbl_manager_username`),
  KEY `fk_tbl_refund_tbl_order1_idx` (`tbl_order_id`),
  CONSTRAINT `fk_tbl_refund_tbl_order1` FOREIGN KEY (`tbl_order_id`) REFERENCES `tbl_order` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tbl_refund_tbl_staft` FOREIGN KEY (`tbl_staft_username`) REFERENCES `tbl_staft` (`username`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tbl_refund_tbl_staft1` FOREIGN KEY (`tbl_manager_username`) REFERENCES `tbl_staft` (`username`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_refund_request`
--

LOCK TABLES `tbl_refund_request` WRITE;
/*!40000 ALTER TABLE `tbl_refund_request` DISABLE KEYS */;
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_refund_status`
--

LOCK TABLES `tbl_refund_status` WRITE;
/*!40000 ALTER TABLE `tbl_refund_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_refund_status` ENABLE KEYS */;
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
  `is_activate` bit(1) NOT NULL DEFAULT b'1',
  `is_manager` bit(1) DEFAULT NULL,
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
-- Table structure for table `tbl_time`
--

DROP TABLE IF EXISTS `tbl_time`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_time` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `allowed_parking_from` bigint(20) NOT NULL,
  `allowed_parking_to` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_time`
--

LOCK TABLES `tbl_time` WRITE;
/*!40000 ALTER TABLE `tbl_time` DISABLE KEYS */;
INSERT INTO `tbl_time` VALUES (4,1541264400000,1541347200000);
/*!40000 ALTER TABLE `tbl_time` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_user`
--

LOCK TABLES `tbl_user` WRITE;
/*!40000 ALTER TABLE `tbl_user` DISABLE KEYS */;
INSERT INTO `tbl_user` VALUES (36,'+84899168485','123456',1124,'jim','raynor','','','4324234324'),(44,'2142142131','243241412',0,'42151412','2121412',NULL,'\0','12412123'),(45,'81763128','3217836',0,'321836128','321836',NULL,'\0','3218746128'),(46,'4891274219','32193721987',0,'21937129','3218937129',NULL,'\0','32198371298'),(48,'5432545325','432534253',0,'43254325','43253245324',NULL,'\0','43253245432'),(51,'3514325325','35432543',0,'34253245','43254',NULL,'\0','554325'),(52,'32432423432','324234',0,'4324324324','324324324',NULL,'\0','324324324'),(53,'435325342543','43254325',0,'43253245','43253245',NULL,'\0','34254325'),(54,'5346543643','45325324',0,'324324','46236432',NULL,'\0','4632436532');
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
INSERT INTO `tbl_vehicle` VALUES ('123858848','214631277','hello','edited',1541091600000,3,'',''),('12412123','24123123','xyz','1300mm',1542906000000,3,'',''),('213123123','3213123213',NULL,NULL,NULL,NULL,'\0',''),('213123478','321123443',NULL,NULL,NULL,NULL,'\0',''),('21312412','213123123','abc','321412',1542474000000,3,'',''),('213124124','2312312',NULL,NULL,NULL,NULL,'\0',''),('21321371','32183126','32183216','231837129',1542387600000,3,'',''),('213214712','24491822',NULL,NULL,NULL,NULL,'\0',''),('21931238','21932139',NULL,NULL,NULL,NULL,'\0',''),('2261357125','237153122',NULL,NULL,NULL,NULL,'\0',''),('3214215831','4329842394','2483204032','4204830284',1538845200000,3,'',''),('3214888','21214124',NULL,NULL,NULL,NULL,'\0',''),('3218746128','321836312',NULL,NULL,NULL,NULL,'\0',''),('32198371298','32193798',NULL,NULL,NULL,NULL,'\0',''),('324324324','432432432',NULL,NULL,NULL,NULL,'\0',''),('34254325','3425342543',NULL,NULL,NULL,NULL,'\0',''),('4324234324','432432432','3123','21312',1540573200000,3,'',''),('43253245432','3425324532',NULL,NULL,NULL,NULL,'\0',''),('4632436532','432532455',NULL,NULL,NULL,NULL,'\0',''),('554325','54325554',NULL,NULL,NULL,NULL,'\0','');
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_vehicle_type`
--

LOCK TABLES `tbl_vehicle_type` WRITE;
/*!40000 ALTER TABLE `tbl_vehicle_type` DISABLE KEYS */;
INSERT INTO `tbl_vehicle_type` VALUES (3,'4 chỗ'),(4,'7 chỗ');
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

-- Dump completed on 2018-11-07 12:08:25
