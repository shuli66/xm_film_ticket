-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: localhost    Database: xm_film_ticket
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `actor`
--

DROP TABLE IF EXISTS `actor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `actor` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `film_id` int DEFAULT NULL COMMENT '电影ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '人员姓名',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='演职人员表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actor`
--

LOCK TABLES `actor` WRITE;
/*!40000 ALTER TABLE `actor` DISABLE KEYS */;
INSERT INTO `actor` VALUES (1,5,'张梓宸','导演','http://localhost:9090/files/download/1732024293546-张梓宸.jpg'),(2,5,'范欣','编剧','http://localhost:9090/files/download/1732024553012-范欣.jpg'),(3,15,'屈楚萧','主演','http://localhost:9090/files/download/1732157901749-屈楚萧.jpg'),(4,15,'张子枫','主演','http://localhost:9090/files/download/1732157926152-张子枫.png'),(5,15,'刘德华','主演','http://localhost:9090/files/download/1732157944743-刘德华.jpg'),(6,15,'彭顺','导演','http://localhost:9090/files/download/1732157966152-彭顺.jpg'),(7,1,'成龙','主演','http://localhost:9090/files/download/1732157986946-成龙.jpg'),(8,1,'张栾','导演','http://localhost:9090/files/download/1732158002220-张栾.png'),(9,16,'凯莉·马塞尔','导演','http://localhost:9090/files/download/1732158036170-凯莉·马塞尔.jpg'),(10,16,'汤姆·哈迪','编剧','http://localhost:9090/files/download/1732158052381-汤姆·哈迪.jpg'),(11,16,'汤姆·哈迪','主演','http://localhost:9090/files/download/1732158075390-汤姆·哈迪.jpg'),(12,16,'贾冰','演员','http://localhost:9090/files/download/1732158090614-贾冰.jpg'),(13,11,'陆川','导演','http://localhost:9090/files/download/1732158113931-陆川.jpg'),(14,11,'陆川','编剧','http://localhost:9090/files/download/1732158123553-陆川.jpg'),(15,11,'任敏','演员','http://localhost:9090/files/download/1732158153965-任敏.jpg'),(16,11,'郑恺','演员','http://localhost:9090/files/download/1732158199495-郑凯.jpg'),(17,11,'苗苗','主演','http://localhost:9090/files/download/1732158191093-苗苗.jpg'),(18,18,'饺子','导演','http://localhost:9090/files/download/1739085020477-微信图片_20250209151009.png'),(19,20,'陈思诚','导演','http://localhost:9090/files/download/1739085093772-微信图片_20250209151127.png'),(20,20,'王宝强','主演','http://localhost:9090/files/download/1739085134591-微信图片_20250209151207.png'),(21,20,'刘昊然','主演','http://localhost:9090/files/download/1739085180914-微信图片_20250209151253.png'),(22,21,'乌尔善','导演','http://localhost:9090/files/download/1739085227395-微信图片_20250209151341.png'),(23,19,'徐克','导演','http://localhost:9090/files/download/1739085273989-微信图片_20250209151421.png'),(24,19,'肖战','主演','http://localhost:9090/files/download/1739085311428-微信图片_20250209151504.png'),(25,22,'林超贤','导演','http://localhost:9090/files/download/1739085361385-微信图片_20250209151554.png'),(26,22,'黄轩','主演','http://localhost:9090/files/download/1739085397779-微信图片_20250209151631.png'),(27,24,'甘剑宇','导演','http://localhost:9090/files/download/1739085453262-微信图片_20250209151720.png'),(28,24,'肖央','主演','http://localhost:9090/files/download/1739085498376-微信图片_20250209151807.png'),(29,18,'吕艳婷','演员','http://localhost:9090/files/download/1739086004752-微信图片_20250209152637.png');
/*!40000 ALTER TABLE `actor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '姓名',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电话',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='管理员表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (1,'admin','admin','管理员','http://localhost:9090/files/download/1721114905635-柴犬.jpeg','ADMIN','18899990011','admin2@xm.com'),(2,'wsy','123456','黍离666','http://localhost:9090/files/download/1745495596209-微信头像.jpg','ADMIN','1875645821','2285556422@qq.com');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `area`
--

DROP TABLE IF EXISTS `area`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `area` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '区域名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='电影区域表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `area`
--

LOCK TABLES `area` WRITE;
/*!40000 ALTER TABLE `area` DISABLE KEYS */;
INSERT INTO `area` VALUES (1,'其他'),(2,'波兰'),(3,'德国'),(4,'西班牙'),(5,'意大利'),(6,'印度'),(7,'日本'),(8,'韩国'),(9,'泰国'),(10,'中国澳门'),(11,'中国香港'),(12,'俄罗斯'),(13,'法国'),(14,'英国'),(15,'美国'),(16,'中国台湾'),(17,'中国大陆');
/*!40000 ALTER TABLE `area` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cinema`
--

DROP TABLE IF EXISTS `cinema`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cinema` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电话',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地址',
  `leader` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '负责人',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '身份证号',
  `front` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '身份证正面',
  `back` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '身份证反面',
  `certificate` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '营业执照',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '审批状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='影院信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cinema`
--

LOCK TABLES `cinema` WRITE;
/*!40000 ALTER TABLE `cinema` DISABLE KEYS */;
INSERT INTO `cinema` VALUES (1,'wxc','123456','http://localhost:9090/files/download/1731572848469-万象城影城.jpg','CINEMA','万象城影城','18899990000','wxc@163.com','合肥市蜀山区创新大道万象城B座5楼','李小璐','341223199008099011','http://localhost:9090/files/download/1731572912799-身份证正面.png','http://localhost:9090/files/download/1731573034914-身份证反面.png','http://localhost:9090/files/download/1731573101771-经营许可证.png','审核通过'),(2,'wd','123456','http://localhost:9090/files/download/1731573320394-万达影城.jpg','CINEMA','万达影城','19900001212','xiaoming@xm.com','河南省郑州市惠济区创新路','李小明','341225199908099044','http://localhost:9090/files/download/1731573355718-身份证正面.png','http://localhost:9090/files/download/1731573358109-身份证反面.png','http://localhost:9090/files/download/1731573360759-经营许可证.png','审核通过'),(3,'asks','123456','http://localhost:9090/files/download/1731573387527-奥斯卡赛影城.jpg','CINEMA','奥斯卡赛影城','18056806885','2053449517@qq.com','合肥市蜀山区京东商城五楼奥斯卡赛影城','李小明','341225199908099044','http://localhost:9090/files/download/1731573416656-身份证正面.png','http://localhost:9090/files/download/1731573419282-身份证反面.png','http://localhost:9090/files/download/1731573421995-经营许可证.png','审核通过'),(4,'dd','123456','http://localhost:9090/files/download/1731594191339-丁丁影城.jpg','CINEMA','丁丁影城（乐客来店）','18866664444','dd@xm.com','合肥市蜀山区京东商城五楼奥斯卡赛影城','王大锤','341225199908099046','http://localhost:9090/files/download/1732093866808-身份证正面.png','http://localhost:9090/files/download/1732093871413-身份证反面.png','http://localhost:9090/files/download/1732093875859-经营许可证.png','审核通过'),(5,'gxy','0712','http://localhost:9090/files/download/1739439125448-21BB0BA4AFDB3F4F78AF2361F23476A9.png','CINEMA','gxy','15617231992','215263526@qq.com','北京万达影城石景山万达广场店','王少毅','415856485648561','http://localhost:9090/files/download/1739438208541-原神.jpg','http://localhost:9090/files/download/1739438214102-原神.jpg','http://localhost:9090/files/download/1739438220514-微信头像.jpg','审核通过'),(8,'dadi','135796','http://localhost:9090/files/download/1744352997279-微信图片_20250411142927.png','CINEMA','大地影城','1763432092','222233@qq.com','郑州惠济区万科','王少毅','233333333333','http://localhost:9090/files/download/1744275856679-1731573358109-身份证反面.png','http://localhost:9090/files/download/1744275861368-1731573355718-身份证正面.png','http://localhost:9090/files/download/1744275865491-1731573360759-经营许可证.png','审核通过'),(9,'huoli','123456','http://localhost:9090/files/download/1744554460119-images.png','CINEMA','活力天宝国际影城','12515565666','265666@qq.com','国家体育场1层基座BC口地下停车场8号电梯入口','李子柒','45453543453522','http://localhost:9090/files/download/1744284668673-1731573358109-身份证反面.png','http://localhost:9090/files/download/1744284672669-1731573355718-身份证正面.png','http://localhost:9090/files/download/1744284676186-1731573360759-经营许可证.png','审核通过');
/*!40000 ALTER TABLE `cinema` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `collect`
--

DROP TABLE IF EXISTS `collect`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `collect` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `film_id` int DEFAULT NULL COMMENT '电影ID',
  `user_id` int DEFAULT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='想看信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `collect`
--

LOCK TABLES `collect` WRITE;
/*!40000 ALTER TABLE `collect` DISABLE KEYS */;
INSERT INTO `collect` VALUES (6,5,1),(7,6,1),(10,8,1),(14,1,2),(15,7,1),(19,18,5),(20,9,7),(21,7,8),(22,1,9),(28,16,5),(29,23,5),(31,1,5),(32,5,5),(34,22,1),(36,15,6),(37,10,6),(38,15,5),(39,5,6),(41,13,6),(42,20,6),(43,4,6),(44,2,6),(45,8,5),(46,39,5),(47,26,5),(48,34,6),(49,39,6),(50,4,17),(52,6,5),(53,1,20),(54,1,21);
/*!40000 ALTER TABLE `collect` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `film_id` int NOT NULL COMMENT '电影ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `comment_text` text NOT NULL COMMENT '评论内容',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
  PRIMARY KEY (`id`),
  KEY `film_id` (`film_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`film_id`) REFERENCES `film` (`id`) ON DELETE CASCADE,
  CONSTRAINT `comment_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (3,4,5,'这就是烂片！','2025-02-09 10:57:49'),(4,4,5,'非常难看！','2025-02-09 11:00:53'),(7,4,6,'挺好的啊','2025-02-08 11:31:20'),(8,18,6,'好看到超出预期，打破了对哪吒闹海神话传说的既定想象，为故事塑造了符合时代的全新内核。第一部讲打破宿命，第二部是推翻规则，善恶无关种族，申公豹有自己的坚守与珍视，仙人有私心。跳出陈规，就会发现新的“真相”。无论是妖是魔、成人成仙，即便全世界挡在面前，也不要忘了那个心中有火、想与世界斗上一斗的自己。面上是魔童闹海，暗含的却是整个时代命题，细细品，其中有千滋百味的情感共震。','2025-02-08 23:21:16'),(9,1,6,'一般','2025-02-08 23:23:35'),(13,14,5,'业务点','2025-02-09 06:46:05'),(14,16,6,'挺好看的我喜欢','2025-02-09 09:07:48'),(15,24,7,'冲着老怪来的，没失望，徐克这次格局整的有点大，保家卫国戏份多过江湖武侠，战争场面很震撼、很写实，全片深刻诠释了“侠之大者，为国为民”。','2025-02-09 09:12:07'),(18,1,7,'就这啊','2025-02-09 09:29:16'),(19,1,7,'11','2025-02-09 09:37:06'),(20,11,7,'难看','2025-02-09 10:43:14'),(22,14,5,'2323623','2025-02-10 05:56:52'),(23,23,6,'给小孩子看的吧','2025-02-10 10:02:23'),(24,10,5,'感觉一般','2025-02-12 10:08:57'),(25,4,5,'666','2025-02-12 10:43:08'),(26,15,5,'88','2025-02-12 10:43:18'),(27,24,9,'侠之大者为国为民','2025-02-13 01:23:06'),(28,18,5,'66666','2025-02-17 04:33:19'),(29,4,9,'666','2025-02-19 07:12:15'),(31,18,5,'666','2025-02-19 08:18:32'),(33,23,5,'666演都不演了','2025-03-27 19:49:27'),(36,17,13,'垃圾烂片','2025-04-08 02:03:33'),(37,15,5,'66','2025-04-08 08:42:46'),(38,23,5,'熊大快开门啊，我是熊二啊','2025-04-10 06:30:41'),(39,20,5,'嘿嘿','2025-04-10 06:35:35'),(40,11,6,'烂到好笑，精神健全/真人实境/环保主义/无脑正能量版《EVA》，王俊凯碇真嗣、苗苗葛城美里、任敏绫波丽。想成为庵野秀明，结果拍了坨《北京堡垒》。铺张浪费、自负空洞、东拼西凑，毫无原创性可言，最基本的流畅度也不达标，陆川可以息影了','2025-04-10 09:13:20'),(41,1,5,'看到成龙大哥这么卖力，也不想给差评。只是这些年选女主角的品味真是越来越差，这次连花瓶都算不上，更别说演技了。至于笑点来说，不好意思，实在get不到。只能看看大哥卖力演自己的落魄……','2025-04-10 09:14:44'),(42,33,5,'这部电影有点抽象','2025-04-10 21:59:04'),(43,39,5,'好期待啊','2025-04-11 04:14:39'),(44,4,5,'这个就是爱情！','2025-04-11 21:36:43'),(45,26,5,'就这啊','2025-04-13 01:46:19'),(46,9,5,'太好笑了\n','2025-04-13 06:11:13'),(47,1,5,'111','2025-04-13 19:07:23'),(48,6,5,'怎么没人看啊，这电影','2025-05-08 01:56:16'),(49,1,21,'看起来还不错','2025-05-08 02:38:58'),(50,12,5,'还行吧','2026-01-05 00:50:26'),(51,2,5,'你好','2026-02-02 23:42:49');
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `film`
--

DROP TABLE IF EXISTS `film`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `film` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电影名称',
  `english` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '英文名称',
  `start` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '上映日期',
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电影时长',
  `type_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '分类IDs',
  `language` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电影语言',
  `resolution` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '分辨率',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '电影简介',
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电影封面',
  `employ` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '制作公司',
  `area_id` int DEFAULT NULL COMMENT '区域ID',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电影状态',
  `score` double(10,1) DEFAULT '0.0' COMMENT '电影评分',
  `total` double(10,2) DEFAULT '0.00' COMMENT '电影票房',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='电影信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `film`
--

LOCK TABLES `film` WRITE;
/*!40000 ALTER TABLE `film` DISABLE KEYS */;
INSERT INTO `film` VALUES (1,'熊猫计划','PANDA PLAN','2024-11-01','90','[17,18]','普通话','2DIMAX','太太太太太好笑太解压啦！全民爱看的熊猫喜剧全国欢乐热映中！软萌可爱又极富松弛感的熊猫幼崽呼呼堪称能够治愈一切烦恼、终止精神内耗的超强解药！成龙大哥本色出演自己，七旬老汉守国宝，惊喜笑料真不少！ 超萌超可爱的主人公熊猫幼崽（呼呼 饰）被国际功夫巨星Jackie（成龙 饰）认养，满心期待开启新生活。然而，呼呼独有的“大小眼”特别基因却被国际犯罪组织盯上了，组织首领不惜悬赏雇佣兵一亿美金发起了一场熊猫抢夺战。面对突如其来的危机，新晋奶爸Jackie怎能袖手旁观？联手金牌经纪人（魏翔 饰）与熊猫保育员（史策 饰）全力展开营救！万万没想到……这群反派好像不太聪明，一场惊险刺激却又荒诞可笑的守“呼”行动就此开启！偷国宝遇上成龙，这群笨贼算是踢到钢板啦！ 温馨提示：熊猫计划全程高能，谁看谁迷“呼”，小心笑到岔气！','http://localhost:9090/files/download/1731931650829-熊猫计划.jpg','峨眉电影集团有限公司',17,'已上映',9.0,384.00),(2,'那个不为人知的故事','The Untold Story','2024-11-09','103','[10,19]','普通话','普通','近百万人千呼万唤的超人气同名IP首登大银幕，心痛与希望交织，为国捐躯的缉毒警察顶级凄美虐恋爆泪片，带你大哭一场! 神秘的出租车司机陈铭生(邱泽 饰)与性格清冷的文物修复师杨昭(郎月婷 饰)偶然相遇，在一场暧昧、试探、你进我退的极致拉扯中，陈铭生渐渐卸下心防。在一次旅行中，陈铭生的卧底缉毒警身份暴露，二人的感情再次面临选择，这份汹涌悸动的爱能否抵挡住命运的捉弄？卧底缉毒警陈铭⽣孤勇奋战为国牺牲，又有多少⽆名英雄日日夜夜负重前⾏，守护着万家灯⽕？ 无名，却被无数人铭记； 陈铭生，我们永远都记得。','http://localhost:9090/files/download/1731933799990-那个不为人知的故事.jpg','一抹悠蓝传媒（成都）有限公司',17,'已上映',8.0,77.00),(3,'最后的里程','Last mile','2024-11-09','128','[14,19]','日语','普通','11月，即将到来的全球最大购物活动之一“黑色星期五”前夜，发生了一起快递箱爆炸事件。很快，这起事件演变成了让整个日本陷入恐慌的连续爆炸事件。物流仓库的新任主管·舟渡艾蕾娜（满岛光 饰）和团队经理·梨本孔（冈田将生 饰）一同应对这场前所未有的危机！ 是谁？为什么安放炸弹? 剩下的炸弹还有多少？现在在哪里? 物流是现代社会的生命线，如何才能阻止这个连续爆破事件？当谜题被解开时，这个世界所隐藏真面目将浮出水面。','http://localhost:9090/files/download/1731935365485-最后的里程.jpg','日本东宝株式会社',7,'已上映',4.0,0.00),(4,'爱情神话','B for Busy','2024-10-30','112','[19,10]','普通话','普通','老白是一位来自上海的离异退休绘画教师，他爱上了一位同样离异的李小姐。他的前妻蓓蓓和他的一个学生的介入让整个事情变得复杂起来。在上海的弄堂里，一个男人和三个女人的故事徐徐拉开序幕。','http://localhost:9090/files/download/1731935504069-爱情神话.jpg','麦特影业（湖北）有限公司',17,'已上映',10.0,160.00),(5,'伟大征程','THE GREAT JOURNEY','2024-10-31','106','[1,8,12]','普通话','普通','国内首部4K全景声大型情景史诗舞台艺术电影。','http://localhost:9090/files/download/1731935563310-伟大征程.jpg','中央新闻纪录电影制片厂（集团）',17,'已上映',8.0,0.00),(6,'红色一号：冬日行动','RED ONE','2024-11-08','123','[17,18,19]','英语','普通','圣诞老人（J·K·西蒙斯 饰）被神秘绑架，北极安保主管（道恩·强森 饰）不得已与全世界最臭名昭著的赏金猎人（克里斯·埃文斯 饰）展开合作，他们寻遍全球、打遍天下，力求拯救圣诞节。','http://localhost:9090/files/download/1731935619979-红色一号：冬日行动.jpg','中国电影股份有限公司北京电影发行分',15,'已上映',0.0,65.00),(7,'海绵宝宝：拯救比奇堡','Saving Bikini Bottom: The Sandy Cheeks Movie','2024-10-30','83','[7,9,5]','英语','普通','为了制造出适合陪伴人类的海洋伙伴宠物，由海啸女士带领的靴子实验室抓走了比奇堡的所有居民，海绵宝宝和松鼠珊迪·奇克斯为了拯救比奇堡决心前往人类世界，开启了惊心动魄的冒险旅程。海绵宝宝和珊迪在得州被响尾蛇攻击，珊迪的家人们挺身而出帮二人脱险，并护送二人到了靴子实验室。 在实验室里众人目睹了海啸女士的邪恶计划，海绵宝宝作为邪恶克隆计划的牺牲品被抓走命悬一线。在危急关头，珊迪发挥自己的聪明才智，在章鱼哥、派大星、蟹老板等比奇堡朋友们和奇克斯家人们的帮助下，排除万难成功阻止了海啸女士的克隆计划。','http://localhost:9090/files/download/1731935708082-海绵宝宝.jpg','美国维亚康姆国际集团',15,'已上映',8.0,0.00),(8,'哈利·波特与凤凰社','Harry Potter and the Order of the Phoenix','2007-08-10','138','[5,6]','英语','普通','叛乱开始！伏地魔已经复活，但魔法部正在采取一切手段阻止魔法界了解真相——包括任命魔法部官员多洛雷斯·乌姆里奇担任霍格沃茨的新一任黑魔法防御术教授。当乌姆里奇拒绝传授实用的防御魔法时，罗恩和赫敏说服哈利为即将来临的巫师战争秘密挑选并训练了一批学生。这部电影改编自J.K. 罗琳的“哈利·波特”系列第五部小说，引人入胜的故事，善恶之间的惊人对决蓄势待发。准备好战斗吧！','http://localhost:9090/files/download/1731935784434-哈利·波特与凤凰社.jpg','英国盛日影业公司',15,'已上映',7.0,0.00),(9,'这个杀手不太冷','LEON','2024-11-01','133','[17,14,19]','法语','普通','住在纽约贫民区的意大利人里昂（让·雷诺 饰）是一名孤独的职业杀手。一天，邻居家的小姑娘玛蒂尔达（娜塔莉·波特曼 饰）敲开了他的房门，央求在他家里暂避杀身之祸。小姑娘的家人是警方缉毒组的眼线，因为贪污了一小包毒品而遭到黑警斯坦菲尔德（加里·奥德曼 饰）残忍杀害。玛蒂尔达得到里昂的救助之后，在他那里学会了使用枪支，里昂也从玛蒂尔达那里学会了法文，在这个无依无靠的世界从彼此身上寻找到了温暖。 然而他们的人生注定不平静，玛蒂尔达想要报仇，跟踪黑警后反而被抓住，里昂及时赶到将她救回，这次行动也让黑警关注到了他们的动向。狡猾的斯坦菲尔德紧盯二人，想要将他们逮捕，冲突升级让里昂和玛蒂尔达不得不分开逃跑，没想到这次分开竟然是诀别......','http://localhost:9090/files/download/1731935870269-这个杀手不太冷.jpg','Les Films du Dauphin',15,'已上映',0.0,15.60),(10,'变形金刚：起源','Transformers One 冒险 动作 动画','2024-09-01','104','[7,9,17]','英语','3DIMAX','变形金刚系列焕新之作，“含刚量100%”感受极致变形盛宴！故事发生在变形金刚的母星塞伯坦，聚焦变形金刚的青年时代，讲述汽车人领袖擎天柱与霸天虎领袖威震天从手足走向反目的故事，打响旷日持久的塞伯坦之战。英雄集结踏上全新冒险，燃炸特效引爆变形狂潮，大银幕开启变形金刚新纪元！','http://localhost:9090/files/download/1731935925435-变形金刚：起源.jpg','美国派拉蒙动画',15,'已上映',7.8,0.00),(11,'749局','Bureau749','2024-10-20','123','[10,17,16]','普通话','2DIMAX','故事发生在近未来，因为未知神秘生物的出现，导致整个城市面临前所未有的危机。一桩被隐藏多年的秘密计划浮出水面。少年马山被带入749局，进入层层迷宫，开启一程冒险之旅，并在这一旅程中完成了一次自我的成长与救赎。影片既有紧张刺激的神秘元素、又有热血澎湃的动作场面、也展现了热血青春的浪漫情怀。','http://localhost:9090/files/download/1731936007291-749局.jpg','北京猿动力泛娱文化发展有限公司',17,'已上映',0.0,181.50),(12,' 爱你很久很久','I Am the Secret in Your Heart','2024-11-01','113','[10,18,19]','港语','普通','你的青春里有没有一个忘不掉的人？阳光明媚的高中生王晓夏（李沐 饰）在一次偶然中对天才转校生程奕（曹佑宁 饰）一见钟情，无法克制的心跳，让晓夏开启高调追爱模式。而从小一起长大默默守护着她的柚子（娄峻硕 饰）也决定为自己争取一次。一边是天降男神，一边是竹马守护，十七岁的少年都怀揣着最炙热的爱意，晓夏又该如何抉择？ 爱是给勇敢者的奖励，2024年冬天第一部爱情电影，一定要和最最最爱的人一起看……','http://localhost:9090/files/download/1731936107657-爱你很久很久.jpg','百薇国际娱乐股份有限公司',16,'已上映',0.0,0.00),(13,'蓦然回首','Look back suddenly','2026-03-12','75','[9,19]','日语','2DIMAX','这是一个令人怦然心碎的感人至深的有关于青春、梦想和友谊的故事。自信的小学生藤野一直以来都在校报上连载四格漫画，也因此享受着同学们的追捧赞美，直到她意外得知一个“家里蹲”不上学的同学京本，也想要尝试在校报上刊登漫画。这两个截然不同的女孩，她们之间唯一的联系就是对漫画的热爱。然而，有一天，在她们之间发生了一些事情，让一切都改变了……','http://localhost:9090/files/download/1731936171933-暮然回首.jpg','日本集英社',7,'待上映',0.0,45.00),(14,'哈利·波特与火焰杯','Harry Potter and the Goblet of Fire','2024-11-01','157','[7,14,5]','英语','2DIMAX','这是一个不同寻常的学期，魔法学校的盛事：三强争霸赛在霍格沃茨举行。学生们争先报名，希翼获得永恒无上的荣誉。火焰杯在选出三所学校的三名勇士后，竟将未足年龄的哈利也挑选出来。哈利的当选引起其他学生的嫉妒连连，就连铁哥们罗恩也首次和他闹起了别扭，哈利感到孤立无援，还好有教父小天狼星和赫敏一直支持他。可总有些好事者如记者丽塔等着看他的好戏。天知道这场比赛是何等艰辛危险，在龙的眼皮下偷蛋，潜入湖底救人，在迷宫中探险……一不小心，甚至可能付出生命的代价。黑魔王伏地魔的威胁渐渐显现，每一天都险象环生，动人心弦。','http://localhost:9090/files/download/1731936246284-哈利·波特与火焰杯.jpg','美国华纳兄弟影片公司',15,'已上映',0.0,245.00),(15,'危机航线','High Forces','2024-11-01','119','[2,13,17]','港语','2DIMAX','疯上天！万米高空癫狂犯罪大片！和刘德华、张子枫、屈楚萧、刘涛一起体验极“疯”失重，心跳狂飙！ “这是航班HY688，我们遭遇劫机！劫匪要价35亿人民币！目前飞机处于失控状态，请求支援！！！”一架五星级超豪华A380客机在国际首航途中遭遇暴力劫机，整架飞机被杀意笼罩。机舱内，国际安保专家高皓军（刘德华 饰）正在和疯批悍匪团周旋，他的女儿小军（张子枫 饰）被绑为人质，性命岌岌可危。劫匪头目Mike（屈楚萧 饰）要价35亿人民币，全机乘客沦为他的筹码和玩具。女儿和前妻傅源（刘涛 饰）先后落入魔爪，暴躁杀神高晧军开启极限反杀模式，一场高空追杀就此展开。此时机舱内已是浓烟滚滚，坠机只是时间问题！全机乘客能否安全降落，这架摇摇欲坠的豪华客机究竟是最后的栖身之所还是坠入地狱的幽灵航班？','http://localhost:9090/files/download/1731936303137-危机航线.jpg','广州市英明文化传播有限公司',17,'已上映',10.0,0.00),(16,' 毒液：最后一舞','VENOM:THE LAST DANCE','2024-11-01','109','[15,16,17]','英语','3DIMAX','超级IP“毒液”迎来系列终章，10月23日全国大银幕上映。被军方神秘研究组织抓捕，刚踏上逃亡之路的毒液与埃迪，又遭遇“共生体之神”纳尔麾下外星异噬体们猎杀，劲敌环伺，毒液百变附体极限突围。毒液与埃迪背水一战，生死难料，而牺牲自己是否会成为拯救对方的唯一方式？','http://localhost:9090/files/download/1731936355598-毒液：最后一舞.jpg','美国哥伦比亚影片公司',15,'已上映',8.5,0.00),(17,' 志愿军：存亡之战','The Volunteers:The Battle of Life and Death','2024-11-01','144','[12,19]','普通话','3DIMAX','陈凯歌执导，《志愿军》三部曲第二部《志愿军：存亡之战》将聚焦铁原阻击战。震撼巨制还原铁原硝烟，铁血不灭谱写英雄故事，一场存亡之战，一支英雄铁军！ 抗美援朝战争第五次战役的转移阶段，铁原阻击战正式打响，中国人民志愿军第63军2.5万将士与近5万“联合国军”展开激战。志愿军将士们浴血奋战，终于把敌军打上了谈判桌。 面前，武装到牙齿的敌军步步迫近；身后，是数十万正在转移的主力部队，生死存亡，在此一战。 1个小时，敌军的4500吨炮弹向我军一个师的阵地倾泻而至，12昼夜，步坦炮协同的敌军数次冲锋……志愿军战士们发扬“钉子战术”，把自己变成一根钉子，狠狠地钉在阵地上！暗藏机巧的防御工事，神出鬼没的地道突袭，招招见血的贴身肉搏，“人在阵地在”的誓言响彻铁原战场，63军全体将士一起，在铁原构成了一道冲不破的“铁长城”。','http://localhost:9090/files/download/1731936401500-志愿军.jpeg','中国电影股份有限公司',17,'已上映',7.0,0.00),(18,'哪吒之魔童闹海 (2025)','','2025-01-29','144','[19,18,9]','普通话','3DIMAX','天劫之后，哪吒、敖丙的灵魂虽保住了，但肉身很快会魂飞魄散。太乙真人打算用七色宝莲给二人重塑肉身。但是在重塑肉身的过程中却遇到重重困难，哪吒、敖丙的命运将走向何方？','http://localhost:9090/files/download/1739081872720-微信图片_20250209141704.png','成都可可豆动画影视有限公司',17,'已上映',10.0,612.00),(19,'射雕英雄传：侠之大者 (2025)','The Legend of the Condor Heroes: The Greatest Hero (2025)','2025-01-29','146','[20]','普通话','3DIMAX','恩怨情仇的江湖，权势角力的战乱时代，郭靖（肖战 饰）童年离别家乡，逐渐炼就可改变局面和命运的庞大力量。虽受高人赏识和器重，得传天下绝世武功“九阴真经”和“降龙十八掌”，却惹来各方嫉忌，成为众矢之的。\n　　郭靖，不亢不卑，怀赤子之心，与黄蓉（庄达菲 饰）在铁骑箭雨和硝烟旌旗中，力挽狂澜，保护南宋边关。\n　　雕海苍穹，勇者无惧，侠之大者，力拔山河，成就武林传说。\n　　本片主要改编自金庸同名原著第34至40章。','http://localhost:9090/files/download/1739082169263-微信图片_20250209142206.png',NULL,17,'已上映',0.0,0.00),(20,'唐探1900','Detective Chinatown 1900','2025-01-29','136','[18,17,14]','普通话','3DIMAX','1900年，在美国旧金山唐人街，华裔印第安猎人阿鬼（王宝强 饰）与留美青年秦福（刘昊然 饰）因一场凶杀案偶然结识，误打误撞组成“唐人街神探”组合。开启了一场笑闹探案之旅……','http://localhost:9090/files/download/1739082278428-微信图片_20250209142431.png',NULL,17,'已上映',9.0,0.00),(21,'封神第二部：战火西岐','Conferred Gods Part II: War in Xiqi','2025-01-29','144','[12,16,17]','普通话','3DIMAX','姜子牙、姬发带队坚守西岐，家园保卫战一触即发！邓婵玉、闻仲奉商王殷寿之命，率魔家四将等殷商大军征伐西岐，西岐一方得殷郊、雷震子、杨戬、哪吒等相助，更聚全民之力守卫家园。兵戈相对、法术交锋，两大阵营掀起强强对决，关于“封神榜” 的争夺正在继续......','http://localhost:9090/files/download/1739082415624-微信图片_20250209142640.png',NULL,17,'已上映',9.0,136.00),(22,'蛟龙行动','Operation Jiaolong','2025-01-29','146','[19,17,12]','普通话','2DIMAX','未来某日，中国海军截获关键情报——一艘超级潜艇正密谋一项针对我国东南沿海城市的绝密军事计划。我方派出最新型潜艇“龙鲸号”，与蛟龙小队协同作战。海底战场险象环生，蛟龙遭遇重重陷阱，潜艇与敌多番埋伏周旋，更驶入海底火山口遍布的广袤深海，一场惊心动魄、攸关生死存亡的深海决战拉开帷幕……\n　　该影片为《红海行动》续作。','http://localhost:9090/files/download/1739082536550-微信图片_20250209142829.png',NULL,17,'已上映',9.0,68.00),(23,'熊出没·重启未来','Boonie Bears: Restart the Future','2025-01-29','108','[19,18,9]','普通话','3DIMAX','熊大、熊二、光头强意外地和来自未来世界的小亮一起穿越到100年后：世界发生巨大灾变，孢子植物全面侵占，人类在末日中艰难求生，整个地球危在旦夕！而这一切的罪魁祸首竟是......光头强？！\n　　100年前究竟发生了什么？小亮是敌是友？光头强为何背负恶名？人类命运最终会走向何方？熊强三人组能否穿透这层层迷雾，重启未来？','http://localhost:9090/files/download/1739082634597-微信图片_20250209143024.png',NULL,17,'已上映',6.0,204.00),(24,'误杀3','Manslaughter 3','2024-12-28','109','[19,14,13]','普通话','3DIMAX','郑炳睿（肖央 饰）的宝贝女儿婷婷众目睽睽之下，遭神秘绑匪绑架，始终陪伴父女左右的李慧萍（佟丽娅 饰）与他一同展开救女行动。但狡诈绑匪轻松躲避警方负责人张景贤（段奕宏 饰）的密集追捕，更将救女心切的郑、李二人玩弄于股掌之间。绝望间，郑炳睿选择走上了一条通往地狱的救女之路，纵使化身“修罗”也不能放过伤害自己女儿的恶徒！殊不知一场更大的阴谋正在悄然展开，所有卷入其中的人都无法逃脱。','http://localhost:9090/files/download/1739082723349-微信图片_20250209143155.png',NULL,17,'已上映',0.0,68.00),(25,'名侦探柯南：迷宫的十字路口','名探偵コナン 迷宮の十字路','2024-12-27','107','[9,14,5]','日语','普通','京、大阪、京都相继发生杀人事件，5名死者皆是被头戴面具的黑影人用武士刀和弓箭瞬间杀害。警方介入调查，发现死者都是出没于3地专门偷窃有名的佛像及美术品的偷盗集团“源氏萤”成员。该集团共有成员8名，以义经的家臣名字互相称呼，且都持有一本《义经记》，被害5人手中的《义经记》均被拿走。\n　　这日京都古寺山能寺收到奇怪书信，说若能解开信中所附的画谜，便能找到8年前被窃的如来佛像。柯南、小兰随毛利小五郎来到该寺，均对画谜一筹莫展。5名被杀者之一曾对与新一不相上下的大阪高中生服部平次照料有加，服部也来到京都对案件秘密展开调查，并借机找寻自己的初恋情人。柯南与服部相遇联手调查时，发现有人想置服部于死地，紧接“源氏萤”另一成员被害，而这一切，似乎又都与画谜有关','http://localhost:9090/files/download/1739082863419-微信图片_20250209143413.png',NULL,7,'已上映',0.0,11.00),(26,'小小的我','Little me','2024-12-27','131','[19,7]','普通话','3DIMAX','患有脑瘫的刘春和（易烊千玺 饰）勇敢冲破身心的枷锁，为外婆（林晓杰 饰）圆梦舞台的同时，也弥合了与妈妈（蒋勤勤 饰）的关系，并努力寻求着自己人生的坐标。在经历一个盛夏的蜕变后，他终于踏上了新的旅程。','http://localhost:9090/files/download/1739083050882-微信图片_20250209143706.png',NULL,17,'已上映',8.5,204.00),(27,'花样年华','In the Mood for Love','2025-02-14','103','[18,10]','港语','3DIMAX','1960年代的香港，报馆编辑周慕云（梁朝伟 饰）与太太搬进一间住户多是上海人的公寓，和某家日资公司的贸易代表陈先生与太太苏丽珍（张曼玉 饰）成了邻居。因为发现各自在外工作的配偶背着他们有了婚外情，周慕云和苏丽珍开始见面商讨未来可能发生的事情以及相应对策。\n　　起初两人是君子之交淡如水就事谈事，可是日子一日接一日过去后，在周围一帮上海邻居的闲言碎语中，他们发现配偶的事早空出了他们的脑袋，彼此眼里只剩下了对方，而刻意回避已生出的感情的结果，是更加刻骨的相思','http://localhost:9090/files/download/1739083261791-微信图片_20250209144042.png',NULL,11,'已上映',0.0,0.00),(28,'火锅艺术家 ','Hot Pot Artist','2025-01-13','98','[18]','普通话','3DIMAX','东北小伙李如意（崔志佳 饰）的导演事业遭逢变故，还被债主七哥（乔杉 饰）上门催债，无奈之下找到好兄弟张磊（魏翔 饰）、贾总（艾伦 饰）借钱却遭戏精式婉拒，就在穷途末路之时李如意施展厨艺天赋，摇身一变成为火锅届“扛把子”，带领一众小弟誓要赢得“火锅争霸赛”的百万奖金！在登顶之路上，李如意与老丈人袁飞（张琪 饰）、竞争对手陈华柳（于洋 饰）、倒霉伙计小天（宋小宝 饰）开展了一系列啼笑皆非的花式PK，最终冠军究竟花落谁家？李如意是否会继续在火锅届大展拳脚？抓马人生等待解锁…','http://localhost:9090/files/download/1739466797437-微信图片_20250209142431.png',NULL,17,'已上映',0.0,0.00),(29,'成为一只珍珠鸡','On Becoming a Guinea Fowl','2024-05-16','99','[18,19]','英语','3DIMAX','一夜狂欢过后，女孩戴着面具从派对返家，却撞见躺在地上一命呜呼的舅舅。迟迟等不到父亲危机处理，还遇上表姊妹酒醉突袭，然而比这个荒唐之夜更漫长的，却是那场悼念舅舅的家族葬礼。当恶棍一夕成了天使，寡妇成了众矢之的，阿姨们吟唱着爱与家庭，长久以来被肮脏掩埋的，还有那个所有人都不敢直视的秘密。','http://localhost:9090/files/download/1739467699859-7DAC04DF233C3707FA684B1FD4C029FE.png',NULL,14,'已上映',0.0,33.00),(30,'“骗骗”喜欢你','Honey Money Phony','2024-12-31','114','[19,10]','普通话','3DIMAX','打工女孩林晴朗（金晨 饰）被渣男张子骏（王皓 饰）欺骗，一夜之间甜蜜誓言变成201314元巨额债务。诈骗头子欧阳晖（孙阳 饰）的意外出现，给林晴朗的生活带来新的转机，两人联合“碰碰车车神”董晓惠（李雪琴 饰）、“骗场老炮”柏仕通（王耀庆 饰）设局开团，与张子骏斗智斗勇。一对“欢喜冤家”，一队“骗子天团”，一场“骗爱游戏”，结局将何去何从……','http://localhost:9090/files/download/1739468014768-C91D60B70F492BF29EE0B267CF0F1184.png',NULL,17,'已上映',0.0,0.00),(31,'穿越时空的少女','時をかける少女','2025-01-11','98','[9,16,19]','日语','3DIMAX','17岁少女绀野真琴是一个活泼跳脱的少女。在一次普通值日工作中，真琴不小心摔倒了，自此她发现自己拥有了穿越时空的能力。她兴奋不已，掌握了穿越时空的方法后，她经常用这个方法去解决生活中许多令她困扰十分的事。\n　　喜欢棒球的真琴与同班的两名男生千昭与功介有着共同爱好。一天，与她本是好朋友的千昭突然向她表达爱意，手足无措了她再次穿越时空希望可以躲避这件事的发生。但没想到，千昭会跟同学友梨在一起了，而友梨也是一直喜欢千昭的。此时，暗恋功介的果穗误以为真琴是功介的女友。\n　　为了帮忙解决朋友的问题，真琴不断以同样的方法回到过去。她突然发现自己手臂上的数字原来是穿越时空的次数，次数变得越来越少，与此同时她也发现了千昭的秘密……','http://localhost:9090/files/download/1739468254096-01F1CD840A928EDB19080DA44557E5B7.png','xxxx',7,'已上映',0.0,0.00),(32,'杀死比尔3 Kill Bill','Kill Beatrix','2030-03-01','90','[17,15,13]','英语','3DIMAX','新娘（乌玛·瑟曼饰）曾经是致命毒蛇暗杀小组（D．I．V．A．S）的一员，企图通过结婚脱离血腥的生活。但是她的前同僚（汉纳、刘玉玲、薇薇卡·A·福克斯、迈克尔·马德森等人扮演）以及所有人的老板比尔（大卫·卡拉丁饰）的到来破坏了这一切。“比尔，”新娘请求说，“我怀孕了，是你的孩子。”但是回答她的是“砰”的一声枪响！4年后她在一家医院醒来，就立刻开始着手一次从得克萨斯到冲绳、东京以及墨西哥的复仇之旅，为了一个目标她要大开杀戒。“当我到达目的地之后，我将杀死比尔。”','http://localhost:9090/files/download/1744301791167-微信图片_20250411001608.png',NULL,15,'待上映',0.0,0.00),(33,'忍者蝙蝠侠大战极道联盟',' Batman Ninja vs. Yakuza League','2025-03-18','89','[17,9,5]','日语','3DIMAX','蝙蝠侠与盟友从过去归来仅一天，就发现世界已非离开时的模样。整个日本岛不复存在，正义联盟的英雄们也集体消失。然而他们还没来得及理清头绪，更离奇的变故接踵而至——黑帮成员开始从天上纷纷坠落。','http://localhost:9090/files/download/1744302027791-微信图片_20250411001951.png','神风动画工作室',7,'已上映',7.0,0.00),(34,'戏台','xitai','2025-11-13','91','[19,18]','普通话','普通','继2015年执导电视剧《好大一个家》之后，陈佩斯时隔多年再执导筒。影片由陈佩斯执导的同名话剧改编而来，原作以荒诞幽默的戏剧冲突和针砭时弊的辛辣嘲讽带出了战乱年代的艺术传承和小人物的生存之道。\n\n话剧上演期间曾场场爆满、一票难求，文本经历了市场的长期检验，让人非常安心。此次改编成电影，最大的看点非陈佩斯、黄渤两代喜剧笑匠的强强联合莫属。二人能在片中擦出怎样的火花，令人期待。','http://localhost:9090/files/download/1744302355348-微信图片_20250411002444.png','xxxx',17,'待上映',0.0,0.00),(35,'无名之辈2','wumingzhibei2','2026-03-13','90','[18,19]','普通话','3DIMAX','饶晓志的成名作《无名之辈》将在问世七年后迎来续集。故事继续围绕小人物展开，讲述义乌小老板的生意经。目前该片释出的信息不多，只知道它于2023年立项并公布了概念海报，2024年开拍，预计今年上映。但愿这一次，饶晓志能延续前作的喜剧风格，拍出小人物的荒腔走板。','http://localhost:9090/files/download/1744302462330-微信图片_20250411002726.png','xxxx',17,'待上映',0.0,0.00),(36,'刺杀小说家2',' Cìshā xiǎoshuō jiā 2 6 / 5,000 Assassination of Novelist 2','2026-04-09','90','[16,17]','普通话','2DIMAX','路阳在导演《风起陇西》《天启异闻录》两部剧集后，预计将于今年携《刺杀小说家2》回归大银幕。该片继续由原著作者双雪涛担任文学顾问，延续了前作的热血、冒险风格。现实与小说世界的双线叙事、精益求精的视觉特效，依旧是该片的两大看点。\n\n只不过，雷佳音退居男三，由邓超饰演男一，董子健番位前移为男二。所以这部续集很可能会揭示出更多的背景故事，比如解开赤发鬼的人物前史之类。但鉴于前作只取得了6.5的豆瓣评分，这一部能否完成口碑逆袭，目前还是一个未知数。','http://localhost:9090/files/download/1744302571387-微信图片_20250411002908.png','xxxxx',17,'待上映',0.0,0.00),(37,'入型入格','Fit into the shape','2026-05-06','90','[19,17,20]','普通话','3DIMAX','继《门前宝地》之后，徐浩峰再次与弟弟徐骏峰联手推出武侠新作，改编自徐浩峰创作的同名小说，原作曾获得过《上海文学》评选的中篇小说奖。\n\n电影故事发生在民国年间，围绕陈坤、吴磊饰演的两兄弟展开，两人一个是在小学教武术的武人，一个是表面经营理发店实则身怀绝技的武林高手。相较《门前宝地》来说，该片从文本到演员阵容都上了一个新台阶。\n\n片名“入型入格”在北京话里是“把事儿办漂亮”的意思，寄托了徐浩峰“拍出一部漂亮电影”的美好愿景，但愿这部作品不要再重复《门前宝地》的闹剧。','http://localhost:9090/files/download/1744302733970-微信图片_20250411003140.png','xxxxx',17,'待上映',0.0,0.00),(38,'狂野时代','Wild Times','2026-04-09','90','[6,14,16]','普通话','普通','毕赣导演的新作，就现有物料可以看出，这是一部融合了超现实、侦探、科幻等多种元素的电影，讲一个仿生人在时空漫游中复活的故事，充满了新奇感和想象力。\n\n电影班底也十分扎实，由易烊千玺和舒淇领衔主演。据悉，本片已于2024年4月开拍，很可能将于2025年上映。不出意外的话，这又将是一部冲奖力作！','http://localhost:9090/files/download/1744302841692-微信图片_20250411003335.png','xxxxxxx',17,'待上映',0.0,0.00),(39,'转念花开','Change of mind, flowers bloom','2026-07-17','90','[19,13]','普通话','3DIMAX','《转念花开》是贾玲指导的首部非喜剧题材电影。影片讲一位曾经误入传销组织的中年家庭妇女为了让更多人远离传销，决定用自己的经验来帮助大家，通过深入虎穴，协助警方破获大案。\n\n片中，贾玲将与张小斐再次合作，两人饰演的角色有怎样的关系，瘦身成功后的贾玲将以怎样的形象露面，新作又会带来怎样的突破，这些都构成了本片的看点。','http://localhost:9090/files/download/1744302963985-微信图片_20250411003547.png','xxxxxxxx',17,'待上映',0.0,0.00);
/*!40000 ALTER TABLE `film` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `film_show`
--

DROP TABLE IF EXISTS `film_show`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `film_show` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `film_id` int DEFAULT NULL COMMENT '电影ID',
  `room_id` int DEFAULT NULL COMMENT '影厅ID',
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '放映时间',
  `price` double(10,2) DEFAULT NULL COMMENT '电影票价',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '状态',
  `cinema_id` int DEFAULT NULL COMMENT '影院ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='放映记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `film_show`
--

LOCK TABLES `film_show` WRITE;
/*!40000 ALTER TABLE `film_show` DISABLE KEYS */;
INSERT INTO `film_show` VALUES (1,1,1,'2024-11-30 09:00:00',36.00,'购票中',1),(2,4,8,'2024-11-30 17:00:00',40.00,'购票中',4),(3,1,9,'2024-11-30 16:00:00',40.00,'购票中',4),(4,14,8,'2024-11-30 19:00:00',49.00,'购票中',4),(5,20,17,'2025-02-09 00:00:00',68.50,'已放映',2),(6,25,15,'2025-02-09 00:00:00',38.00,'已放映',2),(7,21,16,'2025-02-10 00:00:00',68.00,'已放映',2),(8,24,14,'2025-02-09 00:00:00',68.00,'已放映',2),(9,22,2,'2025-02-09 00:00:00',68.00,'购票中',1),(10,18,3,'2025-02-09 00:00:00',68.00,'购票中',1),(11,26,4,'2025-02-09 00:00:00',68.00,'购票中',1),(12,21,5,'2025-02-09 00:00:00',68.00,'购票中',1),(13,23,10,'2025-02-09 15:06:40',68.00,'购票中',3),(14,19,11,'2025-02-09 15:06:53',68.00,'购票中',3),(15,25,12,'2025-02-09 00:00:00',38.00,'购票中',3),(16,16,13,'2025-02-09 15:07:35',68.00,'购票中',3),(17,11,18,'2025-02-20 00:00:00',45.00,'购票中',5),(18,25,19,'2025-02-20 00:00:00',44.00,'购票中',5),(19,16,16,'2025-02-20 00:00:00',33.00,'购票中',2),(20,28,14,'2025-02-21 00:00:00',33.00,'购票中',2),(22,11,16,'2025-02-21 00:00:00',1.50,'购票中',2),(25,25,8,'2025-04-07 00:00:00',11.00,'购票中',4),(26,21,6,'2025-03-31 00:00:00',14.00,'购票中',4),(33,2,22,'2025-04-18 00:00:00',51.00,'购票中',8),(34,6,23,'2025-04-25 00:00:00',43.50,'购票中',8),(35,9,24,'2025-04-25 00:00:00',15.60,'购票中',9),(36,20,25,'2025-04-23 00:00:00',55.00,'购票中',9),(37,8,22,'2025-04-30 00:00:00',46.00,'购票中',8),(38,9,18,'2025-04-24 00:00:00',44.00,'购票中',1),(39,12,19,'2025-04-19 00:00:00',122.00,'购票中',5),(40,10,23,'2025-04-22 02:00:00',33.00,'购票中',8),(41,9,19,'2025-04-11 01:05:39',33.00,'购票中',5),(42,29,5,'2025-04-23 00:00:00',33.00,'购票中',1),(43,28,25,'2025-04-23 00:00:00',66.00,'购票中',9),(44,33,16,'2025-04-11 13:50:14',55.00,'购票中',2),(45,33,14,'2025-05-01 00:00:00',33.00,'购票中',2),(46,2,14,'2025-04-25 00:00:00',45.00,'购票中',2);
/*!40000 ALTER TABLE `film_show` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notice`
--

DROP TABLE IF EXISTS `notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notice` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '公告标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '公告内容',
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发布时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='系统公告表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notice`
--

LOCK TABLES `notice` WRITE;
/*!40000 ALTER TABLE `notice` DISABLE KEYS */;
INSERT INTO `notice` VALUES (4,'新增上映影片 《哪吒之魔童闹海》：','《哪吒之魔童闹海》：作为《哪吒之魔童降世》的续集，原班人马回归，讲述天劫之后，哪吒与敖丙在太乙真人用七色宝莲给二人重塑肉身过程中遭遇重重困难，命运走向未知的故事。','2025-02-09 15:00:24'),(5,'新增上映影片  《封神第二部：战火西岐》','故事紧接第一部，讲述西伯侯之子姬发反出朝歌，闻太师率领殷商大军进攻周都西岐，封神榜争夺战愈演愈烈的故事。','2025-02-09 15:01:50'),(6,'我今天心情好','我今天心情好','2025-02-11 02:03:49'),(7,'今天修复了已知bug','修复了选座异常的问题','2025-02-14 01:39:12'),(9,'电影购票管理系统测试上线','本系统测试上线，正在逐步完善','2025-04-13 22:19:29');
/*!40000 ALTER TABLE `notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单编号',
  `user_id` int DEFAULT NULL COMMENT '用户ID',
  `film_id` int DEFAULT NULL COMMENT '电影ID',
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '放映时间',
  `cinema_id` int DEFAULT NULL COMMENT '影院ID',
  `room_id` int DEFAULT NULL COMMENT '影厅ID',
  `seat` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '座位',
  `create_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '下单时间',
  `price` double(10,2) DEFAULT NULL COMMENT '总价格',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=88 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='订单信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (9,'20241127092848',1,1,'2024-11-30 16:00:00',4,9,'[{\"row\":8,\"col\":7},{\"row\":8,\"col\":8}]','2024-11-27 09:28:48',80.00,'已退票'),(10,'20241127094515',1,1,'2024-11-30 16:00:00',4,9,'[{\"row\":8,\"col\":8}]','2024-11-26 09:45:15',40.00,'待取票'),(13,'20241127201637',1,1,'2024-11-30 16:00:00',4,9,'[{\"row\":1,\"col\":8},{\"row\":1,\"col\":7}]','2024-11-27 20:16:37',80.00,'待取票'),(14,'20250207014740',5,1,'2024-11-30 16:00:00',4,9,'[{\"row\":5,\"col\":4}]','2025-02-07 01:47:40',40.00,'待取票'),(15,'20250209004408',5,4,'2024-11-30 17:00:00',4,8,'[{\"row\":4,\"col\":8}]','2025-02-09 00:44:08',40.00,'待取票'),(16,'20250209151919',5,18,'2025-02-09 00:00:00',1,3,'[{\"row\":3,\"col\":5}]','2025-02-09 15:19:19',68.00,'已取票'),(17,'20250209152151',6,18,'2025-02-09 00:00:00',1,3,'[{\"row\":3,\"col\":6}]','2025-02-09 15:21:51',68.00,'已取票'),(18,'20250209152232',6,18,'2025-02-09 00:00:00',1,3,'[{\"row\":4,\"col\":5}]','2025-02-09 15:22:32',68.00,'待取票'),(19,'20250209155237',1,24,'2025-02-09 00:00:00',2,14,'[{\"row\":4,\"col\":6}]','2025-02-09 15:52:37',68.00,'待取票'),(20,'20250209155250',1,23,'2025-02-09 15:06:40',3,10,'[{\"row\":4,\"col\":7},{\"row\":4,\"col\":8}]','2025-02-09 15:52:50',136.00,'待取票'),(21,'20250209155300',1,18,'2025-02-09 00:00:00',1,3,'[{\"row\":7,\"col\":2}]','2025-02-09 15:53:00',68.00,'待取票'),(23,'20250209183054',5,18,'2025-02-09 00:00:00',1,3,'[{\"row\":5,\"col\":7}]','2025-02-09 18:30:54',68.00,'待取票'),(24,'20250209201453',1,18,'2025-02-09 00:00:00',1,3,'[{\"row\":7,\"col\":4}]','2025-02-09 20:14:53',68.00,'待取票'),(25,'20250210175428',5,26,'2025-02-09 00:00:00',1,4,'[{\"row\":4,\"col\":8}]','2025-02-10 17:54:28',68.00,'待取票'),(26,'20250210215733',5,21,'2025-02-09 00:00:00',1,5,'[{\"row\":3,\"col\":3}]','2025-02-10 21:57:33',68.00,'待取票'),(27,'20250211222745',8,22,'2025-02-09 00:00:00',1,2,'[{\"row\":4,\"col\":8}]','2025-02-11 22:27:45',68.00,'待取票'),(28,'20250213024238',5,18,'2025-02-09 00:00:00',1,3,'[{\"row\":4,\"col\":8}]','2025-02-13 02:42:38',68.00,'待取票'),(29,'20250213164439',1,1,'2024-11-30 09:00:00',1,1,'[{\"row\":4,\"col\":8}]','2025-02-13 16:44:39',36.00,'待取票'),(31,'20250213172814',9,4,'2024-11-30 17:00:00',4,8,'[{\"row\":2,\"col\":6}]','2025-02-13 17:28:14',40.00,'待取票'),(32,'20250217203654',6,18,'2025-02-09 00:00:00',1,3,'[{\"row\":5,\"col\":3}]','2025-02-17 20:36:54',68.00,'已取票'),(33,'20250219195735',5,23,'2025-02-09 15:06:40',3,10,'[{\"row\":4,\"col\":4}]','2025-02-19 19:57:35',68.00,'待取票'),(34,'20250219195822',5,1,'2024-11-30 16:00:00',4,9,'[{\"row\":4,\"col\":7}]','2025-02-19 19:58:22',40.00,'待取票'),(50,'20250220001422',5,18,'2025-02-09 00:00:00',1,3,'[{\"row\":5,\"col\":6}]','2025-02-20 00:14:22',68.00,'待取票'),(52,'20250220003246',5,26,'2025-02-09 00:00:00',1,4,'[{\"row\":5,\"col\":3},{\"row\":5,\"col\":4}]','2025-02-20 00:32:46',136.00,'待取票'),(57,'20250328113823',5,6,'2025-03-29 13:00:00',4,9,'[{\"row\":8,\"col\":1}]','2025-03-28 11:38:23',65.00,'已退票'),(59,'20250328115010',5,13,'2025-03-24 00:00:00',4,6,'[{\"row\":8,\"col\":1}]','2025-03-28 11:50:10',45.00,'待取票'),(63,'20250401205016',5,11,'2025-02-20 00:00:00',5,18,'[{\"row\":6,\"col\":7}]','2025-04-01 20:50:15',45.00,'待取票'),(64,'20250402150206',5,14,'2024-11-30 19:00:00',4,8,'[{\"row\":4,\"col\":5}]','2025-04-02 15:02:06',49.00,'待取票'),(65,'20250402150817',5,11,'2025-02-20 00:00:00',5,18,'[{\"row\":8,\"col\":2},{\"row\":8,\"col\":3},{\"row\":8,\"col\":4}]','2025-04-02 15:08:17',135.00,'已取票'),(67,'20250408222050',15,18,'2025-02-09 00:00:00',1,3,'[{\"row\":8,\"col\":8}]','2025-04-08 22:20:50',68.00,'待支付'),(68,'20250409140150',1,1,'2024-11-30 09:00:00',1,1,'[{\"row\":4,\"col\":3}]','2025-04-09 14:01:50',36.00,'待支付'),(69,'20250410184842',6,1,'2024-11-30 09:00:00',1,1,'[{\"row\":8,\"col\":1}]','2025-04-10 18:48:42',36.00,'已取票'),(70,'20250410200746',5,11,'2025-02-21 00:00:00',2,16,'[{\"row\":8,\"col\":1}]','2025-04-10 20:07:46',1.50,'已取票'),(73,'20250411141108',5,33,'2025-05-01 00:00:00',2,14,'[{\"row\":5,\"col\":5}]','2025-04-11 14:11:08',33.00,'待支付'),(75,'20250412133715',5,9,'2025-04-25 00:00:00',9,24,'[{\"row\":5,\"col\":8}]','2025-04-12 13:37:15',15.60,'待取票'),(76,'20250413124835',5,2,'2025-04-25 00:00:00',2,14,'[{\"row\":5,\"col\":6}]','2025-04-13 12:48:35',45.00,'待支付'),(77,'20250413174550',5,29,'2025-04-23 00:00:00',1,5,'[{\"row\":3,\"col\":7}]','2025-04-13 17:45:50',33.00,'待支付'),(78,'20250413221208',5,9,'2025-04-25 00:00:00',9,24,'[{\"row\":8,\"col\":7}]','2025-04-13 22:12:08',15.60,'待支付'),(80,'20250414110802',5,21,'2025-02-09 00:00:00',1,5,'[{\"row\":5,\"col\":6},{\"row\":5,\"col\":8}]','2025-04-14 11:08:02',136.00,'待支付'),(81,'20250420144416',5,29,'2025-04-23 00:00:00',1,5,'[{\"row\":8,\"col\":7}]','2025-04-20 14:44:16',33.00,'待取票'),(83,'20250420231407',5,1,'2024-11-30 09:00:00',1,1,'[{\"row\":8,\"col\":2}]','2025-04-20 23:14:07',36.00,'已取票'),(84,'20250508182006',20,21,'2025-02-09 00:00:00',1,5,'[{\"row\":5,\"col\":5}]','2025-05-08 18:20:06',68.00,'已取票'),(85,'20250508182111',20,25,'2025-04-07 00:00:00',4,8,'[{\"row\":8,\"col\":1}]','2025-05-08 18:21:11',11.00,'待取票'),(86,'20250508183921',21,1,'2024-11-30 09:00:00',1,1,'[{\"row\":8,\"col\":3}]','2025-05-08 18:39:21',36.00,'已取票'),(87,'20250508194750',21,18,'2025-02-09 00:00:00',1,3,'[{\"row\":3,\"col\":4}]','2025-05-08 19:47:50',68.00,'待支付');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `cinema_id` int DEFAULT NULL COMMENT '影院ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '影厅名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='影厅信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (1,1,'一号厅'),(2,1,'二号厅'),(3,1,'三号厅'),(4,1,'四号厅'),(5,1,'五号厅'),(6,4,'浪漫厅'),(7,4,'青春厅'),(8,4,'激情厅'),(9,4,'未来厅'),(10,3,'1号厅'),(11,3,'2号厅'),(12,3,'3号厅'),(13,3,'4号厅'),(14,2,'一号厅'),(15,2,'二号厅'),(16,2,'三号厅'),(17,2,'四号厅'),(18,5,'一号厅'),(19,5,'二号厅'),(20,1,'一号厅'),(21,1,'二号厅'),(22,8,'激光厅'),(23,8,'激光厅Max'),(24,9,'普通厅'),(25,9,'超级厅'),(26,2,'五号厅');
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `score`
--

DROP TABLE IF EXISTS `score`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `score` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` int DEFAULT NULL COMMENT '用户ID',
  `film_id` int DEFAULT NULL COMMENT '电影ID',
  `score` double(10,1) DEFAULT NULL COMMENT '评分',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='电影评分表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `score`
--

LOCK TABLES `score` WRITE;
/*!40000 ALTER TABLE `score` DISABLE KEYS */;
INSERT INTO `score` VALUES (5,2,7,8.0),(6,2,1,9.0),(7,2,10,8.0),(8,1,10,9.0),(9,1,1,10.0),(10,1,4,10.0),(11,1,16,9.0),(12,5,1,8.0),(13,5,15,10.0),(14,6,10,8.0),(15,5,18,10.0),(16,6,18,10.0),(17,6,21,10.0),(18,6,22,9.0),(19,7,21,8.0),(20,7,23,6.0),(21,7,17,7.0),(23,5,23,6.0),(24,5,16,8.0),(25,5,20,9.0),(26,6,5,8.0),(27,5,33,7.0),(28,5,8,7.0),(29,5,26,8.0),(30,5,3,4.0),(31,20,26,9.0),(32,20,2,8.0);
/*!40000 ALTER TABLE `score` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type`
--

DROP TABLE IF EXISTS `type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `type` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '分类名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='电影分类表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type`
--

LOCK TABLES `type` WRITE;
/*!40000 ALTER TABLE `type` DISABLE KEYS */;
INSERT INTO `type` VALUES (1,'纪录片'),(2,'灾难'),(3,'伦理'),(4,'体育'),(5,'冒险'),(6,'奇幻'),(7,'家庭'),(8,'纪录'),(9,'动画'),(10,'爱情'),(11,'历史'),(12,'战争'),(13,'犯罪'),(14,'悬疑'),(15,'恐怖'),(16,'科幻'),(17,'动作'),(18,'喜剧'),(19,'剧情'),(20,'武侠');
/*!40000 ALTER TABLE `type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '姓名',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电话',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `account` double(10,2) DEFAULT '0.00' COMMENT '余额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'zhangsan','123456','张三','USER','http://localhost:9090/files/download/1731589177928-柴犬.jpeg','18899990000','zhangsan@xm.com',16674.00),(2,'lisi','123456','李四','USER','http://localhost:9090/files/download/1731589198135-拉布拉多.jpeg','18877776666','lisi@xm.com',344.00),(3,'wangwu','123456','王五','USER','http://localhost:9090/files/download/1731589214705-柯基.jpeg','18877778888','wangwu@xm.com',0.00),(4,'zhaoliu','123456','赵六','USER','http://localhost:9090/files/download/1731594522034-金毛.jpeg','18822223333','zhaoliu@xm.com',0.00),(5,'wsy','123456','黍离','USER','http://localhost:9090/files/download/1739096642670-头像素材5.png','17634732092','2285637422@qq.com',5656823.00),(6,'wjh','123456','王锦辉','USER','http://localhost:9090/files/download/1739085686753-头像素材4.png','13345678912','2285637422@qq.com',196.00),(7,'make','123456','make','USER','http://localhost:9090/files/download/1739121142189-头像素材3.png','16355345512','225645622@qq.com',0.00),(8,'纪念','123456','离黍','USER','http://localhost:9090/files/download/1739283989416-头像素材2.jpg','17634732092','2285637422@qq.com',32.00),(9,'css','123456','css','USER','http://localhost:9090/files/download/1739438595269-头像素材2.jpg','17312312343','2112321@',60.00),(10,'刻晴','123456','刻晴','USER','http://localhost:9090/files/download/1744104491930-头像素材1.jpg','45455455555','511535@',0.00),(11,'刘邦','123456','刘邦','USER','https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png','15589999966','151545@',0.00),(12,'坤坤','123456','坤坤','USER','https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png','17666666666',NULL,0.00),(13,'xxx','123456','xxx','USER','/files/download/1744704136523-头像素材1.jpg','15322222312','5645@qq.com',0.00),(14,'王壮','123456','王壮','USER','/files/download/1744554207369-images.png','15923568512','56565656565@qq.com',0.00),(15,'原神','123456','原神','USER','/files/download/1744542454383-原神.jpg','13465625563','233342@qq.com',0.00),(16,'gzttg','123456','呜呜呜','USER','http://localhost:9090/files/download/1744180205453-原神.jpg','14524569822','23@qq.com',0.00),(18,'kkk','111111','kkk','USER','/files/download/1746699960435-头像素材1.jpg','15335536336',NULL,0.00),(19,'xin','123456','心儿','USER','https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png','15655635635','3388538938@qq.com',0.00),(20,'wangan','123456','王铵','USER','http://localhost:9090/files/download/1745650930599-E8E0A00BAB162B129EA86C106BFF04E7.jpg','15234568561','222@qq.com',0.00),(21,'ks','123456','昆山','USER','http://localhost:9090/files/download/1746700698622-images.png','17634732092','hzpkwtp6971@163.com',0.00);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `video`
--

DROP TABLE IF EXISTS `video`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `video` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `film_id` int DEFAULT NULL COMMENT '电影ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '视频名称',
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '视频封面',
  `video` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '视频文件',
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发布时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='电影预告片表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `video`
--

LOCK TABLES `video` WRITE;
/*!40000 ALTER TABLE `video` DISABLE KEYS */;
INSERT INTO `video` VALUES (1,15,'危机航线最新预告视频','http://localhost:9090/files/download/1732089157986-危机航线02.jpg','http://localhost:9090/files/download/1732089166236-危机航线宣传视频01.mp4','2024-11-20 20:22:24'),(2,16,'《毒液：最后一舞》《毒液：最后一舞》今日上映 新水墨动画上演百变毒液见证终章','http://localhost:9090/files/download/1732089439291-毒液01.jpg','http://localhost:9090/files/download/1732089445887-毒液宣传视频01.mp4','2024-11-20 20:22:24'),(3,10,'《变形金刚：起源》动画电影《变形金刚：起源》宣布确认引进中国内地，待定档','http://localhost:9090/files/download/1732089596260-变形金刚01.jpg','http://localhost:9090/files/download/1732089602154-变形金刚宣传片.mp4','2024-11-20 20:22:24'),(7,1,'预告片1：“‘功夫’熊猫”版 (中文字幕)','http://localhost:9090/files/download/1744196921997-微信图片_20250409190716.png','http://localhost:9090/files/download/1744196936930-703180068.mp4','2025-04-09 18:59:09'),(8,18,'哪吒之魔童闹海 预告片1 (中文字幕）','http://localhost:9090/files/download/1744197253610-微信图片_20250409191127.png','http://localhost:9090/files/download/1744197266755-703190454.mp4','2025-04-09 19:14:31');
/*!40000 ALTER TABLE `video` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'xm_film_ticket'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-15 11:40:19
