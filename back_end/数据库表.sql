-- phpMyAdmin SQL Dump
-- version 4.8.4
-- https://www.phpmyadmin.net/
--
-- 主机： 127.0.0.1
-- 生成日期： 2020-02-12 04:49:13
-- 服务器版本： 10.1.37-MariaDB
-- PHP 版本： 7.3.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 数据库： `eye`
--
CREATE DATABASE eye DEFAULT CHARACTER SET utf8;
USE eye;

-- --------------------------------------------------------

--
-- 表的结构 `eyesight`
--

CREATE TABLE `eyesight` (
  `id` int(11) NOT NULL,
  `leftsight` varchar(10) DEFAULT NULL,
  `rightsight` varchar(10) DEFAULT NULL,
  `uid` int(11) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `phone` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `email`, `phone`) VALUES
(1, '泽', '12', '1234567891@qq.com', '12345678901'),
(2, '林', '12345678', '12345@qq.com', '3457898989898'),
(3, '解决', '12345678', '12222@qq.com', '12345123451'),
(4, '人', '987654321', '1408881222@qq.com', '12121212121'),
(5, '秒', '12345671', '1234111@qq.com', '12367576765'),
(6, '金', '1234567890', '122211@qq.com', '12343432124'),
(7, '林斯', '123456789', '18888@qq.com', '19128319383');

--
-- 转储表的索引
--

--
-- 表的索引 `eyesight`
--
ALTER TABLE `eyesight`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_users` (`uid`);

--
-- 表的索引 `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`) USING BTREE;

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `eyesight`
--
ALTER TABLE `eyesight`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- 限制导出的表
--

--
-- 限制表 `eyesight`
--
ALTER TABLE `eyesight`
  ADD CONSTRAINT `fk_users` FOREIGN KEY (`uid`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
