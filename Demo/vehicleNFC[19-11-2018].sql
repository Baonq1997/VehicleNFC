CREATE DATABASE  IF NOT EXISTS `vehiclenfc` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `vehiclenfc`;
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
) ENGINE=InnoDB AUTO_INCREMENT=201 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_order`
--

LOCK TABLES `tbl_order` WRITE;
/*!40000 ALTER TABLE `tbl_order` DISABLE KEYS */;
INSERT INTO `tbl_order` VALUES (199,12,1542415327963,1542415561980,234017,84600000,37800000,1,6,36,1,'95D3-123.66'),(200,1064,1542415565756,1542503504503,87938747,84600000,37800000,1,6,36,1,'95D3-123.66');
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
) ENGINE=InnoDB AUTO_INCREMENT=372 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_order_pricing`
--

LOCK TABLES `tbl_order_pricing` WRITE;
/*!40000 ALTER TABLE `tbl_order_pricing` DISABLE KEYS */;
INSERT INTO `tbl_order_pricing` VALUES (368,0,12,22,199),(369,3,30,55,199),(370,0,12,22,200),(371,3,30,55,200);
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
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_policy`
--

LOCK TABLES `tbl_policy` WRITE;
/*!40000 ALTER TABLE `tbl_policy` DISABLE KEYS */;
INSERT INTO `tbl_policy` VALUES (4,84600000,37800000,1),(11,84600000,37800000,3),(12,84600000,37800000,4),(13,84600000,37800000,5),(14,84600000,37800000,6);
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
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_policy_has_tbl_vehicle_type`
--

LOCK TABLES `tbl_policy_has_tbl_vehicle_type` WRITE;
/*!40000 ALTER TABLE `tbl_policy_has_tbl_vehicle_type` DISABLE KEYS */;
INSERT INTO `tbl_policy_has_tbl_vehicle_type` VALUES (2,4,3,1),(3,4,9,3),(4,4,10,2),(5,4,11,1),(24,11,3,1),(25,11,9,3),(26,11,10,2),(27,11,11,1),(28,12,3,1),(29,12,9,3),(30,12,10,2),(31,12,11,1),(32,13,3,1),(33,13,9,3),(34,13,10,2),(35,13,11,1),(36,14,3,1),(37,14,9,3),(38,14,10,2),(39,14,11,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_pricing`
--

LOCK TABLES `tbl_pricing` WRITE;
/*!40000 ALTER TABLE `tbl_pricing` DISABLE KEYS */;
INSERT INTO `tbl_pricing` VALUES (59,0,12,22,NULL,2),(60,3,30,55,NULL,2),(61,0,5,15,NULL,3),(62,2,20,50,NULL,3),(63,3,50,60,NULL,3);
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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_refund_request`
--

LOCK TABLES `tbl_refund_request` WRITE;
/*!40000 ALTER TABLE `tbl_refund_request` DISABLE KEYS */;
INSERT INTO `tbl_refund_request` VALUES (1,123,'staff','manager',2,199,1542462299055,1542462318451,'hello'),(2,180,'staff','manager',2,199,1542502343028,1542502361676,'yes please'),(3,1000,'staff','manager',3,200,1542503526516,1542543796274,'te'),(4,520,'staff','manager',3,200,1542544112344,1542544311568,'please refund me'),(5,250,'staff','manager',3,200,1542544711768,1542544902961,'please'),(6,200,'staff','manager',3,200,1542545016508,1542545031072,'hello'),(7,2000,'staff','manager',3,200,1542593156326,1542593376598,''),(8,120,'staff','manager',2,200,1542593401530,1542593462176,'');
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
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_user`
--

LOCK TABLES `tbl_user` WRITE;
/*!40000 ALTER TABLE `tbl_user` DISABLE KEYS */;
INSERT INTO `tbl_user` VALUES (36,'+84899168485','123456',-92,'jim','raynor','\0','','95D3-123.66'),(44,'123','123456',1000,'edited','men','','','51A1-12345'),(45,'81763128','3217836',0,'321836128','321836',NULL,'\0','3218746128'),(46,'4891274219','32193721987',0,'21937129','3218937129',NULL,'\0','32198371298'),(48,'5432545325','432534253',0,'43254325','43253245324',NULL,'\0','43253245432'),(51,'3514325325','35432543',0,'34253245','43254',NULL,'\0','554325'),(52,'32432423432','324234',0,'4324324324','324324324',NULL,'\0','324324324'),(53,'435325342543','43254325',0,'43253245','43253245',NULL,'\0','34254325'),(54,'5346543643','45325324',0,'324324','46236432',NULL,'\0','4632436532'),(56,'+84938169174','123456',0,'Thang','Nguyen','\0','\0','B316-23-457'),(57,'+84938169175','123456',0,'Thang','Nguyen','\0','\0','B316-23-458'),(58,'+84938169176','123456',0,'Thang','Nguyen','\0','\0','B316-23-459'),(59,'+84938169177','123456',0,'Thang','Nguyen','\0','\0','B316-23-460'),(60,'312832189828','123456',0,'cuong','mai','\0','\0','48SA213982'),(61,'312832189829','123456',0,'cuong','mai','\0','\0','48SA213955'),(62,'312832189833','123456',0,'cuong','mai','\0','\0','48SA213952'),(63,'3921874219','123456',0,'cuong','mai','\0','\0','3149172291'),(64,'984732959382','123456',0,'cuong','mai 2','\0','\0','23181888'),(65,'2131248721','123456',0,'cuong','mai 3','\0','\0','213123219'),(66,'4593298438','123456',0,'cuong','mai 4','\0','\0','ido3920jow'),(67,'213128376827','213456',0,'cuong','mai 5','\0','\0','2131289462'),(68,'278137291','123456',0,'cuong','mai 6','\0','\0','1249012210'),(69,'921383288888','123456',0,'cuogn','mai 7','\0','\0','1893217494'),(70,'453276199928','123456',0,'cuong','mai 9','\0','\0','3249871982'),(71,'982139543','123456',0,'32512893481','cuong','\0','\0','1498351985'),(72,'4522229019','123456',0,'cuong','mai 10','\0','\0','3041230999'),(73,'+84902739291','123456',140,'quoc','bao','\0','\0',NULL);
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
INSERT INTO `tbl_vehicle` VALUES ('123858848','214631277','hello','edited',1541091600000,3,'','\0',NULL),('12412123','24123123','xyz','1300mm',1542906000000,3,'','\0',NULL),('1249012210','0932109449',NULL,NULL,NULL,NULL,'\0','',NULL),('1498351985','3980157901',NULL,NULL,NULL,NULL,'\0','',NULL),('1893217494','1431549057',NULL,NULL,NULL,NULL,'\0','',NULL),('213123123','3213123213',NULL,NULL,NULL,NULL,'\0','',NULL),('213123219','3829137129',NULL,NULL,NULL,NULL,'\0','',NULL),('213123478','321123443',NULL,NULL,NULL,NULL,'\0','',NULL),('21312412','213123123','abc','321412',1542474000000,3,'','',NULL),('213124124','2312312',NULL,NULL,NULL,NULL,'\0','',NULL),('2131289462','2901381204',NULL,NULL,NULL,NULL,'\0','',NULL),('21321371','32183126','32183216','231837129',1542387600000,3,'','',NULL),('213214712','24491822',NULL,NULL,NULL,NULL,'\0','',NULL),('21931238','21932139',NULL,NULL,NULL,NULL,'\0','',NULL),('21A5-45733','72891786','honda','1100mm',1543510800000,3,'','\0',NULL),('2261357125','237153122',NULL,NULL,NULL,NULL,'\0','',NULL),('23181888','321424213',NULL,NULL,NULL,NULL,'\0','',NULL),('3041230999','3104929029',NULL,NULL,NULL,NULL,'\0','',NULL),('3149172291','238171282',NULL,NULL,NULL,NULL,'\0','',NULL),('3214215831','4329842394','2483204032','4204830284',1538845200000,3,'','',NULL),('3214888','21214124',NULL,NULL,NULL,NULL,'\0','',NULL),('3218746128','321836312',NULL,NULL,NULL,NULL,'\0','',NULL),('32198371298','32193798',NULL,NULL,NULL,NULL,'\0','',NULL),('324324324','432432432',NULL,NULL,NULL,NULL,'\0','',NULL),('3249871982','8291372950',NULL,NULL,NULL,NULL,'\0','',NULL),('34254325','3425342543',NULL,NULL,NULL,NULL,'\0','',NULL),('37D3-728.24','38818457','honda','3000mm',1543338000000,3,'','\0',NULL),('4324234324','432432432','3123','21312',1540573200000,3,'','\0',NULL),('43253245432','3425324532',NULL,NULL,NULL,NULL,'\0','',NULL),('4632436532','432532455',NULL,NULL,NULL,NULL,'\0','',NULL),('48SA213952','394832348',NULL,NULL,NULL,NULL,'\0','',NULL),('48SA213955','394832348',NULL,NULL,NULL,NULL,'\0','',NULL),('48SA213982','394832348',NULL,NULL,NULL,NULL,'\0','',NULL),('51A1-12345','12345678','hondo','2000m',1543078800000,3,'','',NULL),('554325','54325554',NULL,NULL,NULL,NULL,'\0','',NULL),('77A7-72.888','72881889','honda','1000mm',1543510800000,3,'','\0',NULL),('7K88-82.388','12345666',NULL,NULL,NULL,NULL,'','',NULL),('85A2-712.11','83819922',NULL,NULL,NULL,NULL,'\0','',NULL),('95D3-123.66','83817754','toyota','1500mm',1543510800000,3,'','',36),('99K1-123.56','72847287','toyota','2000mm',1542474000000,3,'','\0',NULL),('B316-23-457','722183728',NULL,NULL,NULL,NULL,'\0','',NULL),('B316-23-458','722183728',NULL,NULL,NULL,NULL,'\0','',NULL),('B316-23-459','722183728',NULL,NULL,NULL,NULL,'\0','',NULL),('B316-23-460','722183728',NULL,NULL,NULL,NULL,'\0','',NULL),('ido3920jow','3290339023',NULL,NULL,NULL,NULL,'\0','',NULL);
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
INSERT INTO `tbl_vehicle_type` VALUES (3,'4 chỗ',NULL),(4,'7 chỗ',NULL),(9,'xe ben',NULL),(10,'xe tải nhỏ',NULL),(11,'xe tải vừa',NULL);
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

-- Dump completed on 2018-11-19 11:07:25
