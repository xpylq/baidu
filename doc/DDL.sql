DROP TABLE IF EXISTS `automation_proxy`;
CREATE TABLE `automation_proxy` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `ip` VARCHAR(32) NOT NULL,
  `port` INT NOT NULL,
  `speed` DOUBLE(16,2) NOT NULL DEFAULT '0' COMMENT '相应速度(秒)',
  `location` VARCHAR(32)  NOT NULL DEFAULT '' COMMENT '位置',
   PRIMARY KEY (`id`),
   KEY `idx_ip_port` (`ip`,`port`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='代理表';