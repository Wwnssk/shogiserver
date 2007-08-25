-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.0.38-Ubuntu_0ubuntu1-log


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO,ANSI_QUOTES' */;


--
-- Create schema shogi_test_db
--

CREATE DATABASE IF NOT EXISTS shogi_test_db;
USE shogi_test_db;

DROP TABLE IF EXISTS "shogi_test_db"."Users";
CREATE TABLE  "shogi_test_db"."Users" (
  "user_id" int(10) unsigned NOT NULL auto_increment,
  "user_name" varchar(25) character set utf8 NOT NULL,
  "user_password" varchar(25) character set ascii NOT NULL,
  PRIMARY KEY  ("user_id","user_name")
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
INSERT INTO "shogi_test_db"."Users" ("user_id","user_name","user_password") VALUES 
 (1,'admin','admin');



/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
