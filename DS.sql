-- MySQL dump 10.13  Distrib 5.6.16, for osx10.7 (x86_64)
--
-- Host: localhost    Database: wm
-- ------------------------------------------------------
-- Server version	5.6.16

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
-- Table structure for table `mannschaft`
--

DROP TABLE IF EXISTS `mannschaft`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mannschaft` (
  `mid` int(11) NOT NULL AUTO_INCREMENT,
  `bezeichnung` varchar(45) NOT NULL,
  `gruppe` varchar(45) NOT NULL,
  `anzahlspiele` int(10) unsigned NOT NULL DEFAULT '0',
  `siege` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `unentschieden` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `niederlagen` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `tore` int(10) unsigned NOT NULL DEFAULT '0',
  `gegentore` int(10) unsigned NOT NULL DEFAULT '0',
  `punkte` int(11) NOT NULL DEFAULT '0',
  `status` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`mid`)
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mannschaft`
--

LOCK TABLES `mannschaft` WRITE;
/*!40000 ALTER TABLE `mannschaft` DISABLE KEYS */;
INSERT INTO `mannschaft` VALUES (1,'Brasilien','A',1,1,0,0,3,1,3,NULL),(2,'Kroatien','A',0,0,0,0,0,0,0,NULL),(3,'Mexiko','A',0,0,0,0,0,0,0,NULL),(4,'Kamerun','A',0,0,0,0,0,0,0,NULL),(5,'Spanien','B',0,0,0,0,0,0,0,NULL),(6,'Niederlande','B',0,0,0,0,0,0,0,NULL),(7,'Chile','B',0,0,0,0,0,0,0,NULL),(8,'Australien','B',0,0,0,0,0,0,0,NULL),(9,'Kolumbien','C',0,0,0,0,0,0,0,NULL),(10,'Griechenland','C',0,0,0,0,0,0,0,NULL),(11,'Elfenbeinkueste','C',0,0,0,0,0,0,0,NULL),(12,'Japan','C',0,0,0,0,0,0,0,NULL),(13,'Uruguay','D',0,0,0,0,0,0,0,NULL),(14,'Costa Rica','D',0,0,0,0,0,0,0,NULL),(15,'England','D',0,0,0,0,0,0,0,NULL),(16,'Italien','D',0,0,0,0,0,0,0,NULL),(17,'Schweiz','E',0,0,0,0,0,0,0,NULL),(18,'Ecuador','E',0,0,0,0,0,0,0,NULL),(19,'Frankreich','E',0,0,0,0,0,0,0,NULL),(20,'Honduras','E',0,0,0,0,0,0,0,NULL),(21,'Argentinien','F',0,0,0,0,0,0,0,NULL),(22,'Bosnien-H.','F',0,0,0,0,0,0,0,NULL),(23,'Iran','F',0,0,0,0,0,0,0,NULL),(24,'Nigeria','F',0,0,0,0,0,0,0,NULL),(25,'Deutschland','G',0,0,0,0,0,0,0,NULL),(26,'Portugal','G',0,0,0,0,0,0,0,NULL),(27,'Ghana','G',0,0,0,0,0,0,0,NULL),(28,'USA','G',0,0,0,0,0,0,0,NULL),(29,'Belgien','H',0,0,0,0,0,0,0,NULL),(30,'Algerien','H',0,0,0,0,0,0,0,NULL),(31,'Russland','H',0,0,0,0,0,0,0,NULL),(32,'Korea Republik','H',0,0,0,0,0,0,0,NULL),(33,'Sieger A','AF',0,0,0,0,0,0,0,NULL),(34,'Zweiter B','AF',0,0,0,0,0,0,0,NULL),(35,'Sieger C','AF',0,0,0,0,0,0,0,NULL),(36,'Zweiter D','AF',0,0,0,0,0,0,0,NULL),(37,'Sieger B','AF',0,0,0,0,0,0,0,NULL),(38,'Zweiter A','AF',0,0,0,0,0,0,0,NULL),(39,'Sieger D','AF',0,0,0,0,0,0,0,NULL),(40,'Zweiter C','AF',0,0,0,0,0,0,0,NULL),(41,'Sieger E','AF',0,0,0,0,0,0,0,NULL),(42,'Zweiter F','AF',0,0,0,0,0,0,0,NULL),(43,'Sieger G','AF',0,0,0,0,0,0,0,NULL),(44,'Zweiter H','AF',0,0,0,0,0,0,0,NULL),(45,'Sieger F','AF',0,0,0,0,0,0,0,NULL),(46,'Zweiter E','AF',0,0,0,0,0,0,0,NULL),(47,'Sieger H','AF',0,0,0,0,0,0,0,NULL),(48,'Zweiter G','AF',0,0,0,0,0,0,0,NULL),(49,'Sieger AF5','VF',0,0,0,0,0,0,0,NULL),(50,'Sieger AF6','VF',0,0,0,0,0,0,0,NULL),(51,'Sieger AF1','VF',0,0,0,0,0,0,0,NULL),(52,'Sieger AF2','VF',0,0,0,0,0,0,0,NULL),(53,'Sieger AF7','VF',0,0,0,0,0,0,0,NULL),(54,'Sieger AF8','VF',0,0,0,0,0,0,0,NULL),(55,'Sieger AF3','VF',0,0,0,0,0,0,0,NULL),(56,'Sieger AF4','VF',0,0,0,0,0,0,0,NULL),(57,'Sieger VF1','HF',0,0,0,0,0,0,0,NULL),(58,'Sieger VF2','HF',0,0,0,0,0,0,0,NULL),(59,'Sieger VF3','HF',0,0,0,0,0,0,0,NULL),(60,'Sieger VF4','HF',0,0,0,0,0,0,0,NULL),(61,'Verlierer HF1','SP3',0,0,0,0,0,0,0,NULL),(62,'Verlierer HF2','SP3',0,0,0,0,0,0,0,NULL),(63,'Sieger HF1','FI',0,0,0,0,0,0,0,NULL),(64,'Sieger HF2','FI',0,0,0,0,0,0,0,NULL);
/*!40000 ALTER TABLE `mannschaft` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `spiel`
--

DROP TABLE IF EXISTS `spiel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `spiel` (
  `sid` int(11) NOT NULL AUTO_INCREMENT,
  `fk_midheim` int(11) NOT NULL,
  `fk_midgast` int(11) NOT NULL,
  `toreheim` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `toregast` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `ort` varchar(45) NOT NULL,
  `beginn` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `ende` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `checked` tinyint(4) DEFAULT '0',
  `bezeichnung` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`sid`),
  KEY `midgast_idx` (`fk_midgast`),
  KEY `midheim_idx` (`fk_midheim`),
  CONSTRAINT `midgast` FOREIGN KEY (`fk_midgast`) REFERENCES `mannschaft` (`mid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `midheim` FOREIGN KEY (`fk_midheim`) REFERENCES `mannschaft` (`mid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spiel`
--

LOCK TABLES `spiel` WRITE;
/*!40000 ALTER TABLE `spiel` DISABLE KEYS */;
INSERT INTO `spiel` VALUES (1,1,2,0,0,'Sao Paulo','2014-06-12 20:00:00','2014-06-12 22:00:00',0,'gg1'),(2,3,4,0,0,'Natal','2014-06-13 16:00:00','2014-06-13 18:00:00',0,'gg2'),(3,1,3,0,0,'Fortaleza','2014-06-17 19:00:00','2014-06-17 21:00:00',0,'gg3'),(4,4,2,0,0,'Manaus','2014-06-18 22:00:00','2014-06-19 00:00:00',0,'gg4'),(5,4,1,0,0,'Brasilia','2014-06-23 20:00:00','2014-06-23 22:00:00',0,'gg5'),(6,2,3,0,0,'Recife','2014-06-23 20:00:00','2014-06-23 22:00:00',0,'gg6'),(7,5,6,0,0,'Salvador','2014-06-13 19:00:00','2014-06-13 21:00:00',0,'gg7'),(8,7,8,0,0,'Cuiaba','2014-06-13 22:00:00','2014-06-14 00:00:00',0,'gg8'),(9,8,6,0,0,'Porto Alegre','2014-06-18 16:00:00','2014-06-18 18:00:00',0,'gg9'),(10,5,7,0,0,'Rio de Janeiro','2014-06-18 19:00:00','2014-06-18 21:00:00',0,'gg10'),(11,8,5,0,0,'Curitiba','2014-06-23 16:00:00','2014-06-23 18:00:00',0,'gg11'),(12,6,7,0,0,'Sao Paolo','2014-06-23 16:00:00','2014-06-23 18:00:00',0,'gg12'),(13,9,10,0,0,'Belo Horizonte','2014-06-14 16:00:00','2014-06-14 18:00:00',0,'gg13'),(14,11,12,0,0,'Recife','2014-06-15 01:00:00','2014-06-15 03:00:00',0,'gg14'),(15,9,11,0,0,'Brasilia','2014-06-19 16:00:00','2014-06-19 18:00:00',0,'gg15'),(16,12,10,0,0,'Natal','2014-06-19 22:00:00','2014-06-20 00:00:00',0,'gg16'),(17,12,9,0,0,'Cuiaba','2014-06-24 20:00:00','2014-06-24 22:00:00',0,'gg17'),(18,10,11,0,0,'Fortaleza','2014-06-24 20:00:00','2014-06-24 22:00:00',0,'gg18'),(19,13,14,0,0,'Fortaleza','2014-06-14 19:00:00','2014-06-14 21:00:00',0,'gg19'),(20,15,16,0,0,'Manaus','2014-06-14 22:00:00','2014-06-15 00:00:00',0,'gg20'),(21,13,15,0,0,'Sao Paulo','2014-06-19 19:00:00','2014-06-19 21:00:00',0,'gg21'),(22,16,14,0,0,'Recife','2014-06-20 16:00:00','2014-06-20 18:00:00',0,'gg22'),(23,16,13,0,0,'Natal','2014-06-24 16:00:00','2014-06-24 18:00:00',0,'gg23'),(24,14,15,0,0,'Belo Horizonte','2014-06-24 16:00:00','2014-06-24 18:00:00',0,'gg24'),(25,17,18,0,0,'Brasilia','2014-06-15 16:00:00','2014-06-15 18:00:00',0,'gg25'),(26,19,20,0,0,'Porto Alegre','2014-06-15 19:00:00','2014-06-15 21:00:00',0,'gg26'),(27,17,19,0,0,'Salvador','2014-06-20 19:00:00','2014-06-20 21:00:00',0,'gg27'),(28,20,18,0,0,'Curitiba','2014-06-20 22:00:00','2014-06-21 00:00:00',0,'gg28'),(29,20,17,0,0,'Manaus','2014-06-25 20:00:00','2014-06-25 22:00:00',0,'gg29'),(30,18,19,0,0,'Rio de Janeiro','2014-06-25 20:00:00','2014-06-25 22:00:00',0,'gg30'),(31,21,22,0,0,'Rio de Janeiro','2014-06-15 22:00:00','2014-06-16 00:00:00',0,'gg31'),(32,23,24,0,0,'Curitiba','2014-06-16 19:00:00','2014-06-16 21:00:00',0,'gg32'),(33,21,23,0,0,'Belo Horizonte','2014-06-21 16:00:00','2014-06-21 18:00:00',0,'gg33'),(34,24,22,0,0,'Cuiaba','2014-06-21 22:00:00','2014-06-22 00:00:00',0,'gg34'),(35,24,21,0,0,'Porto Alegre','2014-06-25 16:00:00','2014-06-25 18:00:00',0,'gg35'),(36,22,23,0,0,'Salvador','2014-06-25 16:00:00','2014-06-25 18:00:00',0,'gg36'),(37,25,26,0,0,'Salvador','2014-06-16 16:00:00','2014-06-16 18:00:00',0,'gg37'),(38,27,28,0,0,'Natal','2014-06-16 22:00:00','2014-06-17 00:00:00',0,'gg38'),(39,25,27,0,0,'Fortaleza','2014-06-21 19:00:00','2014-06-21 21:00:00',0,'gg39'),(40,28,26,0,0,'Manaus','2014-06-22 22:00:00','2014-06-23 00:00:00',0,'gg40'),(41,28,25,0,0,'Recife','2014-06-26 16:00:00','2014-06-26 18:00:00',0,'gg41'),(42,26,27,0,0,'Brasilia','2014-06-26 16:00:00','2014-06-26 18:00:00',0,'gg42'),(43,29,30,0,0,'Belo Horizonte','2014-06-17 16:00:00','2014-06-17 18:00:00',0,'gg43'),(44,31,32,0,0,'Cuiaba','2014-06-17 22:00:00','2014-06-18 00:00:00',0,'gg44'),(45,29,31,0,0,'Porto Alegre','2014-06-22 16:00:00','2014-06-22 18:00:00',0,'gg45'),(46,32,30,0,0,'Rio de Janeiro','2014-06-22 19:00:00','2014-06-22 21:00:00',0,'gg46'),(47,32,29,0,0,'Sao Paulo','2014-06-26 20:00:00','2014-06-26 22:00:00',0,'gg47'),(48,30,31,0,0,'Curitiba','2014-06-26 20:00:00','2014-06-26 22:00:00',0,'gg48'),(64,33,34,0,0,'Belo Horizonte','2014-06-28 16:00:00','2014-06-28 18:00:00',0,'af1'),(65,35,36,0,0,'Rio de Janeiro','2014-06-28 20:00:00','2014-06-28 22:00:00',0,'af2'),(66,37,38,0,0,'Fortaleza','2014-06-29 16:00:00','2014-06-29 18:00:00',0,'af3'),(67,39,40,0,0,'Recife','2014-06-29 20:00:00','2014-06-29 22:00:00',0,'af4'),(68,41,42,0,0,'Brasilia','2014-06-30 16:00:00','2014-06-30 18:00:00',0,'af5'),(69,43,44,0,0,'Porto Alegre','2014-06-30 20:00:00','2014-06-30 22:00:00',0,'af6'),(70,45,46,0,0,'Sao Paulo','2014-07-01 16:00:00','2014-07-01 18:00:00',0,'af7'),(71,47,48,0,0,'Salvador','2014-07-01 20:00:00','2014-07-01 22:00:00',0,'af8'),(72,49,50,0,0,'Rio de Janeiro','2014-07-04 16:00:00','2014-07-04 18:00:00',0,'vf1'),(73,51,52,0,0,'Fortaleza','2014-07-04 20:00:00','2014-07-04 22:00:00',0,'vf2'),(74,53,54,0,0,'Brasilia','2014-07-05 16:00:00','2014-07-05 18:00:00',0,'vf3'),(75,55,56,0,0,'Salvador','2014-07-05 20:00:00','2014-07-05 22:00:00',0,'vf4'),(76,57,58,0,0,'Belo Horizonte','2014-07-08 20:00:00','2014-07-08 22:00:00',0,'hf1'),(77,59,60,0,0,'Sao Paulo','2014-07-09 20:00:00','2014-07-09 22:00:00',0,'hf2'),(78,61,62,0,0,'Brasilia','2014-07-12 20:00:00','2014-07-12 22:00:00',0,'sp3'),(79,63,64,0,0,'Rio de Janeiro','2014-07-13 19:00:00','2014-07-13 21:00:00',0,'fi');
/*!40000 ALTER TABLE `spiel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipp`
--

DROP TABLE IF EXISTS `tipp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipp` (
  `tid` int(11) NOT NULL AUTO_INCREMENT,
  `fk_uid` int(11) NOT NULL,
  `fk_sid` int(11) NOT NULL,
  `toreheim` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `toregast` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `checked` tinyint(3) DEFAULT '0',
  PRIMARY KEY (`tid`),
  KEY `fkUID_idx` (`fk_uid`),
  KEY `fkSID_idx` (`fk_sid`),
  CONSTRAINT `fkSID` FOREIGN KEY (`fk_sid`) REFERENCES `spiel` (`sid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fkUID` FOREIGN KEY (`fk_uid`) REFERENCES `user` (`uid`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipp`
--

LOCK TABLES `tipp` WRITE;
/*!40000 ALTER TABLE `tipp` DISABLE KEYS */;
/*!40000 ALTER TABLE `tipp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trunde`
--

DROP TABLE IF EXISTS `trunde`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `trunde` (
  `trid` int(11) NOT NULL AUTO_INCREMENT,
  `bezeichnung` varchar(45) NOT NULL,
  `fk_admin` int(11) DEFAULT NULL,
  PRIMARY KEY (`trid`),
  KEY `fkadmin_idx` (`fk_admin`),
  CONSTRAINT `fkadmin` FOREIGN KEY (`fk_admin`) REFERENCES `user` (`uid`) ON DELETE SET NULL ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trunde`
--

LOCK TABLES `trunde` WRITE;
/*!40000 ALTER TABLE `trunde` DISABLE KEYS */;
/*!40000 ALTER TABLE `trunde` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `uid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `pw` char(76) NOT NULL,
  `punkte` int(11) NOT NULL DEFAULT '0',
  `admin` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (34,'admin','$2a$10$mAwZBqIQW8DqkPqqOhTLR.8qGpJtzLfSeUzNnyGDAoe8UGleMljoy',0,1),(35,'user','$2a$10$Icg8ysl4avuzHhY5nrwVhuGVG882i.OhkplBllbbv.X7zSm9kHZgm',0,0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_trunde`
--

DROP TABLE IF EXISTS `user_trunde`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_trunde` (
  `utrid` int(11) NOT NULL AUTO_INCREMENT,
  `fk_uid` int(11) NOT NULL,
  `fk_trid` int(11) NOT NULL,
  PRIMARY KEY (`utrid`),
  KEY `uid_idx` (`fk_uid`),
  KEY `trid_idx` (`fk_trid`),
  KEY `fktrid_idx` (`fk_trid`),
  CONSTRAINT `fktrid` FOREIGN KEY (`fk_trid`) REFERENCES `trunde` (`trid`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `user_trunde_ibfk_1` FOREIGN KEY (`fk_uid`) REFERENCES `user` (`uid`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=112 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_trunde`
--

LOCK TABLES `user_trunde` WRITE;
/*!40000 ALTER TABLE `user_trunde` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_trunde` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-03-31 17:19:47
