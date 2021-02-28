-- MariaDB dump 10.18  Distrib 10.5.8-MariaDB, for debian-linux-gnu (aarch64)
--
-- Host: localhost    Database: ExchangeApp
-- ------------------------------------------------------
-- Server version	10.5.8-MariaDB-1:10.5.8+maria~focal

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `assignment`
--

DROP TABLE IF EXISTS `assignment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `assignment` (
  `assignment_id` int(11) NOT NULL AUTO_INCREMENT,
  `submission_check_id` int(11) NOT NULL,
  `course_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `submission_check_url_id` int(11) DEFAULT NULL,
  `assignment_title` varchar(100) NOT NULL,
  `assignment_classroom_url` varchar(100) DEFAULT NULL,
  `assignment_description` text DEFAULT NULL,
  `start_date` timestamp NOT NULL DEFAULT utc_timestamp(),
  `end_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `max_submissions_total` int(11) DEFAULT NULL,
  `max_submissions_student` int(11) DEFAULT NULL,
  `coins_per_submission` int(11) NOT NULL,
  `coins_price` int(11) NOT NULL,
  `input_data_type` varchar(10) NOT NULL,
  `output_data_type` varchar(10) NOT NULL,
  `submission_notify` int(11) NOT NULL,
  `plagiarism_warning` int(11) DEFAULT NULL,
  `plagiarism_level` int(11) DEFAULT NULL,
  `visible` int(11) NOT NULL,
  `assignment_date_created` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `assignment_archived` int(11) NOT NULL DEFAULT 0,
  `assignment_deleted` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`assignment_id`),
  KEY `FK_assignment_author` (`user_id`),
  KEY `FK_assignment_submission_check` (`submission_check_id`),
  KEY `FK_assignment_submission_url` (`submission_check_url_id`),
  KEY `FK_course_assignments` (`course_id`),
  CONSTRAINT `FK_assignment_author` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `FK_assignment_submission_check` FOREIGN KEY (`submission_check_id`) REFERENCES `submission_check` (`submission_check_id`),
  CONSTRAINT `FK_assignment_submission_url` FOREIGN KEY (`submission_check_url_id`) REFERENCES `submission_check_url` (`submission_check_url_id`),
  CONSTRAINT `FK_course_assignments` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assignment`
--

LOCK TABLES `assignment` WRITE;
/*!40000 ALTER TABLE `assignment` DISABLE KEYS */;
INSERT INTO `assignment` VALUES (1,1,25,10,NULL,'Ti boš izbrisan',NULL,NULL,'2021-02-18 18:02:43','2021-02-18 18:02:59',NULL,NULL,1,1,'.txt','.txt',0,NULL,NULL,1,'2021-02-18 18:02:59',0,1),(2,1,25,10,NULL,'Ti pa ne boš izbrisan',NULL,NULL,'2021-02-19 09:20:04','2021-02-19 09:20:16',NULL,NULL,1,1,'.java','.java',0,NULL,NULL,1,'2021-02-19 09:20:16',0,0),(3,1,23,10,NULL,'as','google.si',NULL,'2021-02-19 19:27:15','2021-02-28 00:00:00',NULL,NULL,1,1,'.txt','.txt',0,NULL,NULL,0,'2021-02-19 19:27:21',0,0),(4,1,24,10,NULL,'lol',NULL,NULL,'2021-02-25 19:36:35','2021-02-25 19:39:50',NULL,NULL,1,1,'kjh','kh',0,80,95,1,'2021-02-25 19:39:50',0,0),(5,1,23,10,NULL,'Naloga',NULL,NULL,'2021-02-26 12:19:04','2021-04-30 00:00:00',NULL,NULL,1,1,'.txt','.txt',0,NULL,NULL,1,'2021-02-26 12:19:19',0,0);
/*!40000 ALTER TABLE `assignment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `course` (
  `course_id` int(11) NOT NULL AUTO_INCREMENT,
  `access_password_id` int(11) DEFAULT NULL,
  `access_level_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `course_title` varchar(100) NOT NULL,
  `course_description` text DEFAULT NULL,
  `course_classroom_url` varchar(100) DEFAULT NULL,
  `initial_coins` int(11) NOT NULL,
  `course_created` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `course_archived` int(11) NOT NULL DEFAULT 0,
  `course_deleted` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`course_id`),
  KEY `FK_course_access_policy` (`access_level_id`),
  KEY `FK_course_passwod` (`access_password_id`),
  KEY `FK_guardian_main` (`user_id`),
  CONSTRAINT `FK_course_access_policy` FOREIGN KEY (`access_level_id`) REFERENCES `course_access_level` (`access_level_id`),
  CONSTRAINT `FK_course_passwod` FOREIGN KEY (`access_password_id`) REFERENCES `course_access_password` (`access_password_id`),
  CONSTRAINT `FK_guardian_main` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES (23,NULL,1,15,'Javni predmet jao','Ta predmet je javno odprt za vse registrirane uporabnike, razen za tiste, ki jih je izvajalec predmeta blokiral (dodal na blacklist)',NULL,2,'2021-02-10 13:14:54',0,0),(24,21,3,16,'Predmet zaklenjen z geslom','Do tega predmeta imajo dostop samo tisti, ki poznajo geslo. Geslo potrebujejo vpisati pri prvem   vpisu v predmet. Geslo tega predmeta je geslo','https://google.com',20,'2021-02-10 13:24:00',0,0),(25,NULL,3,10,'Predmet z omejenim dostopom 66','Do tega predmeta imajo dostop samo tisti, ki jih je skrbnik določil. Med drugim ima dostop do predmeta uporabnik student, uporabnik prof pa ne.',NULL,15,'2021-02-19 17:36:50',0,0);
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course_access_level`
--

DROP TABLE IF EXISTS `course_access_level`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `course_access_level` (
  `access_level_id` int(11) NOT NULL AUTO_INCREMENT,
  `access_level_description` varchar(100) NOT NULL,
  PRIMARY KEY (`access_level_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course_access_level`
--

LOCK TABLES `course_access_level` WRITE;
/*!40000 ALTER TABLE `course_access_level` DISABLE KEYS */;
INSERT INTO `course_access_level` VALUES (1,'PUBLIC'),(2,'PASSWORD'),(3,'WHITELIST');
/*!40000 ALTER TABLE `course_access_level` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course_access_password`
--

DROP TABLE IF EXISTS `course_access_password`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `course_access_password` (
  `access_password` varchar(100) NOT NULL,
  `access_password_id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`access_password_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course_access_password`
--

LOCK TABLES `course_access_password` WRITE;
/*!40000 ALTER TABLE `course_access_password` DISABLE KEYS */;
INSERT INTO `course_access_password` VALUES ('geslo',15),('geslo',16),('geslo',17),('geslo',18),('geslo',19),('geslo',20),('geslo',21);
/*!40000 ALTER TABLE `course_access_password` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `guardian`
--

DROP TABLE IF EXISTS `guardian`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `guardian` (
  `user_id` int(11) NOT NULL,
  `course_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`course_id`),
  KEY `FK_guardian2` (`course_id`),
  CONSTRAINT `FK_guardian` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `FK_guardian2` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guardian`
--

LOCK TABLES `guardian` WRITE;
/*!40000 ALTER TABLE `guardian` DISABLE KEYS */;
/*!40000 ALTER TABLE `guardian` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notification` (
  `notification_id` int(11) NOT NULL AUTO_INCREMENT,
  `notification_title` varchar(128) NOT NULL,
  `notification_body` varchar(512) NOT NULL,
  `notification_created` timestamp NOT NULL DEFAULT utc_timestamp(),
  `course_id` int(11) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `notification_deleted` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`notification_id`),
  KEY `FK_notification_author` (`user_id`),
  KEY `FK_notification_course` (`course_id`),
  CONSTRAINT `FK_notification_author` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `FK_notification_course` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
INSERT INTO `notification` VALUES (7,'Testni sistem','Nahajate se na testne sistemu aplikacije ExchangeApp.','2021-02-10 13:13:55',NULL,15,0),(8,'Začetno stanje žetonov','Začetno stanje žetonov na tem predmetu je enako 2 - to pomeni, da vsak, ki se v predmet prijavi pridobi 2 žetona.','2021-02-10 13:17:21',23,15,0),(10,'Ti pa ne boš izbrisan','Oddaja je zares tukaj','2021-02-19 17:46:33',25,10,0),(11,'Ti boš izbrisan','','2021-02-19 17:48:36',25,10,1),(12,'Ti boš izbrisan','','2021-02-19 17:51:32',NULL,10,1),(13,'Naslov objave','To je telo objave, ki naj se shrani v bazo','2021-02-20 18:31:14',NULL,15,1),(14,'Naslov objave','To je telo objave, ki naj se shrani v bazo','2021-02-20 18:31:47',NULL,15,1),(15,'Naslov objave','To je telo objave, ki naj se shrani v bazo','2021-02-20 18:32:07',NULL,15,1),(16,'Naslov objave','To je telo objave, ki naj se shrani v bazo','2021-02-20 18:35:46',NULL,15,1),(17,'Naslov objave','To je telo objave, ki naj se shrani v bazo','2021-02-20 18:37:34',NULL,15,1),(18,'Naslov objave','To je telo objave, ki naj se shrani v bazo','2021-02-20 18:39:29',NULL,15,1),(19,'tolu','toilkaz','2021-02-20 18:42:09',NULL,15,1),(20,'Sap hufsi gon ceh sarbuswo wobpe po volohmal mogov sose ewivu voposej ha jos.','Pefmodnu sabgit ho iw hi ram seilisu gikjol bula boru oru maijeuv awhakag wihof cetih em. Nuzbebaz zot mo depabal luwho tiave osnoli geh mizwi go apjacrig tetkueh volejaka ijumi eje os uhpice. Rolubuv womus bo lugsirzik owojagu ojakuf panrap ba su lad bahode to navopag sof av duv. Rulnowak kiebu ace rioddom moihoco onu damkifi li dutbu loowuhof rezjomi jofivese kevfon da ezla inasehur guzciz ni. Fi to ifete kak ehkolum be kaog ereazuweg kibnodar laplo gockipabi cecavpa conbamu dibajra afubuahe cibe.','2021-02-20 18:44:01',NULL,15,1);
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase`
--

DROP TABLE IF EXISTS `purchase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `purchase` (
  `purchase_id` int(11) NOT NULL AUTO_INCREMENT,
  `submission_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `purchase_date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`purchase_id`),
  KEY `FK_submission_bought` (`submission_id`),
  KEY `FK_user_bought` (`user_id`),
  CONSTRAINT `FK_submission_bought` FOREIGN KEY (`submission_id`) REFERENCES `submission` (`submission_id`),
  CONSTRAINT `FK_user_bought` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase`
--

LOCK TABLES `purchase` WRITE;
/*!40000 ALTER TABLE `purchase` DISABLE KEYS */;
/*!40000 ALTER TABLE `purchase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_blacklist`
--

DROP TABLE IF EXISTS `student_blacklist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `student_blacklist` (
  `user_id` int(11) NOT NULL,
  `course_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`course_id`),
  KEY `FK_student_blacklist2` (`course_id`),
  CONSTRAINT `FK_student_blacklist` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `FK_student_blacklist2` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_blacklist`
--

LOCK TABLES `student_blacklist` WRITE;
/*!40000 ALTER TABLE `student_blacklist` DISABLE KEYS */;
INSERT INTO `student_blacklist` VALUES (16,25);
/*!40000 ALTER TABLE `student_blacklist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_signed_in`
--

DROP TABLE IF EXISTS `student_signed_in`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `student_signed_in` (
  `user_id` int(11) NOT NULL,
  `course_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`course_id`),
  KEY `FK_student_signed_in2` (`course_id`),
  CONSTRAINT `FK_student_signed_in` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `FK_student_signed_in2` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_signed_in`
--

LOCK TABLES `student_signed_in` WRITE;
/*!40000 ALTER TABLE `student_signed_in` DISABLE KEYS */;
INSERT INTO `student_signed_in` VALUES (10,23),(10,24),(15,23),(15,24),(16,23);
/*!40000 ALTER TABLE `student_signed_in` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_whitelist`
--

DROP TABLE IF EXISTS `student_whitelist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `student_whitelist` (
  `user_id` int(11) NOT NULL,
  `course_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`course_id`),
  KEY `FK_student_whitelist2` (`course_id`),
  CONSTRAINT `FK_student_whitelist` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `FK_student_whitelist2` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_whitelist`
--

LOCK TABLES `student_whitelist` WRITE;
/*!40000 ALTER TABLE `student_whitelist` DISABLE KEYS */;
INSERT INTO `student_whitelist` VALUES (17,25);
/*!40000 ALTER TABLE `student_whitelist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `submission`
--

DROP TABLE IF EXISTS `submission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `submission` (
  `submission_id` int(11) NOT NULL AUTO_INCREMENT,
  `submission_status_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `assignment_id` int(11) NOT NULL,
  `file_key` varchar(100) NOT NULL,
  `submission_created` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `submission_deleted` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`submission_id`),
  KEY `FK_assignment_submission` (`assignment_id`),
  KEY `FK_status_of_submission` (`submission_status_id`),
  KEY `FK_submission_author` (`user_id`),
  CONSTRAINT `FK_assignment_submission` FOREIGN KEY (`assignment_id`) REFERENCES `assignment` (`assignment_id`),
  CONSTRAINT `FK_status_of_submission` FOREIGN KEY (`submission_status_id`) REFERENCES `submission_status` (`submission_status_id`),
  CONSTRAINT `FK_submission_author` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `submission`
--

LOCK TABLES `submission` WRITE;
/*!40000 ALTER TABLE `submission` DISABLE KEYS */;
INSERT INTO `submission` VALUES (1,1,10,3,'1Z1UrOSJ','2021-02-26 12:06:27',0),(2,1,10,3,'OImnK7bi','2021-02-26 12:06:27',0),(3,1,10,3,'QRnTVHIG','2021-02-26 12:06:27',0),(4,1,10,3,'KxkJVSUk','2021-02-26 12:06:27',0),(5,1,10,3,'SYHvxplP','2021-02-26 12:06:28',0),(6,1,10,3,'8yS8ukJ4','2021-02-26 12:06:28',0),(7,1,10,3,'IFau8GhR','2021-02-26 12:06:28',0),(8,1,10,3,'ruXpF55E','2021-02-26 12:06:28',0),(9,1,10,3,'fNYBtCAf','2021-02-26 12:06:28',0),(10,1,10,3,'ijEPhGeU','2021-02-26 12:06:28',0),(11,1,10,3,'MARxNrrJ','2021-02-26 12:14:46',0),(12,1,10,3,'rd9meG1L','2021-02-26 12:14:46',0),(13,1,10,3,'LE44Rf5q','2021-02-26 12:14:47',0),(14,1,10,3,'MqhRrg8u','2021-02-26 12:14:47',0),(15,1,10,3,'F5ltipIT','2021-02-26 12:14:47',0),(16,1,10,3,'KkpBO448','2021-02-26 12:14:47',0),(17,1,10,3,'8sOG3kzu','2021-02-26 12:14:47',0),(18,1,10,3,'m08zJXHv','2021-02-26 12:14:47',0),(19,1,10,3,'p95ik14o','2021-02-26 12:14:47',0),(20,1,10,3,'cIxinUHn','2021-02-26 12:14:47',0),(21,1,10,5,'WnAbInsX','2021-02-26 12:19:53',0),(22,1,10,5,'QI8ti7ox','2021-02-26 12:19:53',0),(23,1,10,5,'kmFIRxiw','2021-02-26 12:19:53',0),(24,1,10,5,'jcCyTCel','2021-02-26 12:19:53',0),(25,1,10,5,'tZVxlTcY','2021-02-26 12:19:53',0),(26,1,10,5,'xfDiCBWf','2021-02-26 12:19:54',0),(27,1,10,5,'U9RAb7BE','2021-02-26 12:19:54',0),(28,1,10,5,'ndXoSG2L','2021-02-26 12:19:54',0),(29,1,10,5,'3WSsIzWa','2021-02-26 12:19:54',0),(30,1,10,5,'C7449xin','2021-02-26 12:19:54',0);
/*!40000 ALTER TABLE `submission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `submission_check`
--

DROP TABLE IF EXISTS `submission_check`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `submission_check` (
  `submission_check_id` int(11) NOT NULL AUTO_INCREMENT,
  `submission_check_description` varchar(100) NOT NULL,
  PRIMARY KEY (`submission_check_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `submission_check`
--

LOCK TABLES `submission_check` WRITE;
/*!40000 ALTER TABLE `submission_check` DISABLE KEYS */;
INSERT INTO `submission_check` VALUES (1,'NONE'),(2,'MANUAL'),(3,'AUTOMATIC');
/*!40000 ALTER TABLE `submission_check` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `submission_check_url`
--

DROP TABLE IF EXISTS `submission_check_url`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `submission_check_url` (
  `submission_check_url_id` int(11) NOT NULL AUTO_INCREMENT,
  `check_url` varchar(100) NOT NULL,
  PRIMARY KEY (`submission_check_url_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `submission_check_url`
--

LOCK TABLES `submission_check_url` WRITE;
/*!40000 ALTER TABLE `submission_check_url` DISABLE KEYS */;
/*!40000 ALTER TABLE `submission_check_url` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `submission_status`
--

DROP TABLE IF EXISTS `submission_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `submission_status` (
  `submission_status_id` int(11) NOT NULL AUTO_INCREMENT,
  `submission_status_description` varchar(100) NOT NULL,
  PRIMARY KEY (`submission_status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `submission_status`
--

LOCK TABLES `submission_status` WRITE;
/*!40000 ALTER TABLE `submission_status` DISABLE KEYS */;
INSERT INTO `submission_status` VALUES (1,'PENDING_REVIEW'),(2,'PLAGIARISM'),(3,'OK'),(4,'NOK');
/*!40000 ALTER TABLE `submission_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_type_id` int(11) NOT NULL,
  `registration_status_id` int(11) NOT NULL,
  `email` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL,
  `surname` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `personal_number` char(8) NOT NULL,
  `user_created` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `user_deleted` int(11) NOT NULL DEFAULT 0,
  `confirmation_string` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_confirmation_string_uindex` (`confirmation_string`),
  KEY `FK_type_of_user` (`user_type_id`),
  KEY `FK_registration_status` (`registration_status_id`),
  CONSTRAINT `FK_registration_status` FOREIGN KEY (`registration_status_id`) REFERENCES `user_registration_status` (`registration_status_id`),
  CONSTRAINT `FK_type_of_user` FOREIGN KEY (`user_type_id`) REFERENCES `user_type` (`user_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (10,1,2,'karleto.spacapan','Karleto','Špacapan','geslo','a_000001','2021-02-05 17:41:38',0,NULL),(15,1,2,'admin','Admin','Adminko','admin','a_000002','2021-02-10 13:11:21',0,NULL),(16,2,2,'prof','Janez','Novak','prof','p_000001','2021-02-10 13:11:21',0,NULL),(17,3,2,'student','Peter','Novak','student','63180122','2021-02-10 13:11:21',0,NULL),(22,2,1,'ime.surname@fri1.uni-lj.si','Ime','Surname','pass123','p_000002','2021-02-20 15:04:43',0,NULL),(25,4,1,'admin123','Ime-test','Priimek-test','admin123','63170145','2021-02-21 10:03:37',0,NULL),(37,3,2,'jakob.marusic@student.uni-lj.si','Jakob','Marušič','123','63170196','2021-02-24 20:06:41',0,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_password_reset`
--

DROP TABLE IF EXISTS `user_password_reset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_password_reset` (
  `reset_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `reset_key` varchar(64) NOT NULL,
  `reset_key_created` timestamp NOT NULL DEFAULT utc_timestamp(),
  `reset_key_used` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`reset_id`),
  UNIQUE KEY `user_password_reset_reset_key_uindex` (`reset_key`),
  KEY `user_password_reset_user_id_fk` (`user_id`),
  CONSTRAINT `user_password_reset_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_password_reset`
--

LOCK TABLES `user_password_reset` WRITE;
/*!40000 ALTER TABLE `user_password_reset` DISABLE KEYS */;
INSERT INTO `user_password_reset` VALUES (2,37,'bpoiOPPO4rBBe15o3VTBzouHq52RYFw7gKRnQ8BtvJ7mA1yGcQAoLBfetK8g77cX','2021-02-24 20:07:33',0),(3,37,'Tb34xjNkl2ivXo4w8dIubEQ9tWw44TmC0i3Hq0MIQEELVWm7jRav2EpAQq6PjBAN','2021-02-24 20:09:04',1),(4,37,'p2vgGHb2xYScvuoIFBb06Szd6g9MUm2Pu0szfh1dMLkVaw8L7NLafA6ssBgEubdm','2021-02-25 20:21:35',0),(5,37,'y5f6yj4u2EA4S9WHHMTnS2XV5xEswYxN2k5vw7Yzo1Lk3EsvrjcijlrY1Sjnbsv7','2021-02-25 20:38:36',0),(6,37,'w8YIHZX6Bq47tjDL22vsdnQxKCrUDDe2HtEvPUftx2pgMVfT8zMfjqxI8PYT7MWx','2021-02-25 20:40:08',0),(7,37,'IgE63vr0DOHFwo3IV4JDYrY2bPGvpVLwUoM6XxbuqLnGXJf6ohDHuvMTDonQiCZ9','2021-02-25 20:49:08',0),(8,37,'gIGFWJtajSQcJxLenB2rY2vTT5AhrLDNgCIqYdjcD2pzM8mYlnkxYcGDbXii6bAH','2021-02-26 08:14:37',0),(9,37,'5BOfL2xoZyWRPunXrvmN0Jmp3TrWPXUtA2Suw4wsw0Uhlvsi5XiANf0hxivNbt35','2021-02-26 08:52:56',0),(10,37,'H3wNVBKR26Yc307iidHRB66GfbUvFjE6WKVcYGcb6W5LfdPix2RdpCgApFCy03kV','2021-02-26 09:34:50',0),(11,37,'TCm8kPeKys9wguTBj8ZlAET2mgwj8Ncs0bcWtJfkjbz1KGC2yAALZ7ELMnHllyl6','2021-02-26 09:54:49',1);
/*!40000 ALTER TABLE `user_password_reset` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_registration_status`
--

DROP TABLE IF EXISTS `user_registration_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_registration_status` (
  `registration_status_id` int(11) NOT NULL AUTO_INCREMENT,
  `registration_status` varchar(128) NOT NULL,
  PRIMARY KEY (`registration_status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_registration_status`
--

LOCK TABLES `user_registration_status` WRITE;
/*!40000 ALTER TABLE `user_registration_status` DISABLE KEYS */;
INSERT INTO `user_registration_status` VALUES (1,'PENDING_EMAIL'),(2,'REGISTERED');
/*!40000 ALTER TABLE `user_registration_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_type`
--

DROP TABLE IF EXISTS `user_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_type` (
  `user_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_type_description` varchar(100) NOT NULL,
  PRIMARY KEY (`user_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_type`
--

LOCK TABLES `user_type` WRITE;
/*!40000 ALTER TABLE `user_type` DISABLE KEYS */;
INSERT INTO `user_type` VALUES (1,'ADMIN'),(2,'PROFESSOR'),(3,'STUDENT'),(4,'OTHER');
/*!40000 ALTER TABLE `user_type` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-02-26 13:35:44
