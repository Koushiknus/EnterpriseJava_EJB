CREATE DATABASE IF NOT EXISTS `authdb` DEFAULT CHARACTER SET utf8;

USE `authdb`;

/*Table structure for table `category` */

DROP TABLE IF EXISTS `category`;

CREATE TABLE `category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cat_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `category` */

insert  into `category`(`id`,`cat_name`) values (1,'Social'),(2,'For Sale'),(3,'Jobs'),(4,'Tuition');

/*Table structure for table `notes` */

DROP TABLE IF EXISTS `notes`;

CREATE TABLE `notes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userid` varchar(32) NOT NULL,
  `catid` bigint(20) NOT NULL,
  `title` varchar(255) NOT NULL,
  `content` varchar(3000) NOT NULL,
  `postedDate` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_notes` (`catid`),
  CONSTRAINT `FK_notes` FOREIGN KEY (`catid`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
