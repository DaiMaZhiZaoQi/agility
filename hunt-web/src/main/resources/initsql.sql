#INSERT INTO sys_permission_group_test(NAME,description,parent_id) VALUES("权限组一","权限组一",0),("权限组二","权限组二",0);



/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.17-log : Database - hunt
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
 
CREATE DATABASE IF NOT EXISTS `huntTest` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci; 
 


USE `huntTest`;

/*Table structure for table `sys_contact` */


CREATE TABLE IF NOT EXISTS `sys_contact` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '企业通讯录id',
  `org_name` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '机构简称',
  `contact_code` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '通讯录版本',
  `contact_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '企业通讯录名称yyyyMMddHHmmssSS.csv',
  `ori_file_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '原来的企业通讯录名称',
  `contact_sych_password` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '通讯录同步密码',
  `absolute_path` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '通讯录保存位置',
  `auth_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '文档作者',
  `create_by` bigint(20) DEFAULT '0' COMMENT '创建人id',
  `update_by` bigint(20) DEFAULT '0' COMMENT '更新人id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(4) DEFAULT '2' COMMENT '状态 1，正常 2，未激活 3，删除',
  `is_final` tinyint(4) DEFAULT '1' COMMENT '是否可修改 1，可删除2，不可删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='企业通讯录';

/*Table structure for table `sys_contact_user` */


CREATE TABLE IF NOT EXISTS `sys_contact_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '通讯录用户表id',
  `sys_contact_id` bigint(20) DEFAULT NULL COMMENT '通讯录人id',
  `sys_user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `sys_org_id` bigint(20) DEFAULT NULL COMMENT '机构id',
  `status` tinyint(4) DEFAULT '2' COMMENT '状态 1,正常2，未激活 3，删除',
  `is_auth` tinyint(4) DEFAULT '0' COMMENT '是否为文件作者0，不是；1，是',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通讯录联系人中间表，一个机构可能有多个通讯录，一个人员可能有多个通讯录';

/*Table structure for table `sys_data_group` */


CREATE TABLE IF NOT EXISTS `sys_data_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(256) DEFAULT NULL COMMENT '描述',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父级id',
  `name` varchar(256) DEFAULT NULL COMMENT '名称',
  `is_final` int(11) DEFAULT '1' COMMENT '是否可删除',
  `rank` bigint(20) DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT '0' COMMENT '创建人',
  `update_by` bigint(20) DEFAULT '0' COMMENT '更热人',
  `status` tinyint(4) DEFAULT '1' COMMENT '数据状态,1:正常,2:删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sys_data_item` */


CREATE TABLE IF NOT EXISTS `sys_data_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sys_data_group_id` bigint(20) DEFAULT NULL COMMENT '组id',
  `key_value` varchar(256) DEFAULT NULL COMMENT '值',
  `key_name` varchar(256) DEFAULT NULL COMMENT '名称',
  `is_final` int(11) DEFAULT '1' COMMENT '是否可删除',
  `rank` bigint(20) DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT '0' COMMENT '创建人',
  `update_by` bigint(20) DEFAULT '0' COMMENT '更热人',
  `status` tinyint(4) DEFAULT '1' COMMENT '数据状态,1:正常,2:删除,3:禁用账号',
  `description` varchar(256) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sys_device` */


CREATE TABLE IF NOT EXISTS `sys_device` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '设备id',
  `device_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '设备名称',
  `device_pwd` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '设备登录密码',
  `device_serial` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '设备序列号',
  `rank` bigint(20) DEFAULT '0' COMMENT '排序',
  `device_status` tinyint(4) DEFAULT '0' COMMENT '设备状态，0：关机，1：开机，2：在线，3，在线，话机故障，4，离线',
  `status` tinyint(4) DEFAULT '0' COMMENT '0，未绑定，1 已绑定，2, 已删除',
  `description` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '话机设备描述',
  `device_msg` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '话机状态信息，json信息，软件版本，设备ip，cpu，磁盘大小，内存大小',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT '0' COMMENT '创建人id',
  `update_by` bigint(20) DEFAULT '0' COMMENT '更新人id',
  `is_final` int(11) DEFAULT '1' COMMENT '是否可删除，1：可删除，0：不可删除',
  `device_time` datetime DEFAULT NULL COMMENT '话机设备时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `device_serial` (`device_serial`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理，话机设备状态，话机ip地址，cpu资源，内存资源，磁盘资源，通话记录的外键id，';

/*Table structure for table `sys_device_calllog` */


CREATE TABLE IF NOT EXISTS `sys_device_calllog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '通话记录id',
  `device_id` bigint(20) DEFAULT '0' COMMENT '数据来源那一台设备',
  `call_type` tinyint(4) DEFAULT '-1' COMMENT '''通话类型，0：未接电话，1：呼入，2：呼出，3：拒接，4，未接留言'',',
  `call_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '称呼',
  `call_number` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '通话号码',
  `call_date` bigint(20) DEFAULT '0' COMMENT '通话时间以开始时间为准',
  `call_subscription_id` int(11) DEFAULT '-1' COMMENT '拨号方式',
  `call_has_record` int(11) DEFAULT '-1' COMMENT '是否有录音',
  `call_record_ms` bigint(20) DEFAULT '0' COMMENT '录音时长',
  `call_description` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '通话描述',
  `call_duration` bigint(20) DEFAULT '0' COMMENT '通话时长',
  `call_address` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '归属地',
  `call_iscollect` tinyint(4) DEFAULT '0' COMMENT '是否收藏0，未收藏，1，已收藏',
  `call_ishave_record` tinyint(4) DEFAULT '-1' COMMENT '是否有录音，有录音则查询录音文件0，无录音，1，有录音',
  `call_other` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '其他信息',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT '0' COMMENT '创建来源由谁创建 userId',
  `update_by` bigint(20) DEFAULT '0' COMMENT '更新来源',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态1，正常，2删除',
  `is_final` tinyint(4) DEFAULT '1' COMMENT '是否可修改1，可修改，2，不可修改',
  `description` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '通话记录描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='记录电话机的每一条通话记录通话记录';

/*Table structure for table `sys_device_record` */


CREATE TABLE IF NOT EXISTS `sys_device_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '录音id',
  `call_log_id` bigint(20) DEFAULT '0' COMMENT '通话记录id',
  `device_id` bigint(20) DEFAULT '0' COMMENT '设备id',
  `reco_file_path` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '录音保存路径',
  `reco_real_file_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '录音文件真实名称',
  `reco_absolute_file_path` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '通话录音绝对路径',
  `reco_audio_length` bigint(20) DEFAULT '0' COMMENT ' 录音时长单位秒',
  `reco_phone_size` bigint(20) DEFAULT '0' COMMENT '录音文件大小',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT '0' COMMENT '创建人',
  `update_by` bigint(20) DEFAULT '0' COMMENT '更新人',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态 ：1，正常；2，删除',
  `is_final` int(11) DEFAULT '1' COMMENT '是否可删除,1：可删除，2：不可删除',
  `description` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '通话描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通话语音保存文件路径，大小，最后通话时间，联系人信息';

/*Table structure for table `sys_device_role_org` */


CREATE TABLE IF NOT EXISTS `sys_device_role_org` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sys_role_org_id` bigint(20) DEFAULT '0' COMMENT '角色机构表id',
  `sys_org_id` bigint(20) DEFAULT '0' COMMENT '机构id',
  `sys_device_id` bigint(20) DEFAULT '0' COMMENT '设备id',
  `sys_user_id` bigint(20) DEFAULT '0' COMMENT '用户id',
  `is_final` tinyint(4) DEFAULT '1' COMMENT '是否可删除 1，可删除，0，不可删除',
  `rank` bigint(20) DEFAULT '0' COMMENT '排序字段',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT '0' COMMENT '创建人',
  `update_by` bigint(20) DEFAULT '0' COMMENT '创建人',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态 ，1：正常，2：删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='一个角色可以有多个设备，一个设备被多个角色使用';

/*Table structure for table `sys_device_total` */


CREATE TABLE IF NOT EXISTS `sys_device_total` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `device_id` bigint(20) DEFAULT NULL COMMENT '话机设备表id',
  `call_log_count` bigint(20) DEFAULT '0' COMMENT '通话记录数量',
  `call_record_count` bigint(20) DEFAULT '0' COMMENT '通话录音数量',
  `reco_audio_length` bigint(20) DEFAULT '0' COMMENT '通话录音时长',
  `call_duration` bigint(20) DEFAULT '0' COMMENT '通话时长,接通时长',
  `reco_file_size` bigint(20) DEFAULT '0' COMMENT '通话录音文件大小',
  `call_already_accept_count` bigint(20) DEFAULT '0' COMMENT '已接数量',
  `call_no_accept` bigint(20) DEFAULT '0' COMMENT '未接数量',
  `call_refuse_accept` bigint(20) DEFAULT '0' COMMENT '拒接数量',
  `call_call_out` bigint(20) DEFAULT '0' COMMENT '呼出数量',
  `call_no_accept_leave` bigint(20) DEFAULT '0' COMMENT '未接留言数量',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT '0' COMMENT '创建人',
  `update_by` bigint(20) DEFAULT '0' COMMENT '更新人',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态 1，正常  2，删除',
  `is_final` tinyint(4) DEFAULT '1' COMMENT '是否可删除，1：可删除，2：不可删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='设备管理统计表';

/*Table structure for table `sys_ip_forbidden` */


CREATE TABLE IF NOT EXISTS `sys_ip_forbidden` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `is_final` int(11) DEFAULT '1' COMMENT '是否可删除',
  `rank` bigint(20) DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT '0' COMMENT '创建人',
  `update_by` bigint(20) DEFAULT '0' COMMENT '更热人',
  `status` tinyint(4) DEFAULT '1' COMMENT '数据状态,1:正常,2:删除',
  `expire_time` datetime DEFAULT NULL COMMENT '到期时间',
  `description` varchar(256) DEFAULT NULL COMMENT '说明',
  `ip` varchar(256) DEFAULT NULL COMMENT 'IP地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sys_log` */


CREATE TABLE IF NOT EXISTS `sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `rank` bigint(20) DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT '0' COMMENT '创建人',
  `update_by` bigint(20) DEFAULT '0' COMMENT '更热人',
  `status` tinyint(4) DEFAULT '1' COMMENT '数据状态,1:正常,2:删除',
  `ip` varchar(256) DEFAULT NULL COMMENT '请求ip',
  `user_id` varchar(256) DEFAULT NULL COMMENT '操作用户id',
  `method` varchar(2048) DEFAULT NULL COMMENT '请求方法',
  `param` text COMMENT '请求参数',
  `result` text COMMENT '请求结果',
  `duration` bigint(20) DEFAULT NULL COMMENT '持续时间',
  `url` varchar(512) DEFAULT NULL COMMENT '请求url',
  `user_agent` varchar(512) DEFAULT NULL COMMENT '请求ua',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sys_login_status` */


CREATE TABLE IF NOT EXISTS `sys_login_status` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sys_user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `session_id` varchar(256) DEFAULT NULL COMMENT 'session id',
  `session_expires` datetime DEFAULT NULL COMMENT 'session过期时间',
  `sys_user_login_name` varchar(256) DEFAULT NULL COMMENT '登录名',
  `sys_user_zh_name` varchar(256) DEFAULT NULL COMMENT '中文名',
  `last_login_time` datetime DEFAULT NULL COMMENT '上一次登录时间',
  `platform` tinyint(4) DEFAULT NULL COMMENT '登录平台 1:web 2:android 3:ios',
  `rank` bigint(20) DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT '0' COMMENT '创建人',
  `update_by` bigint(20) DEFAULT '0' COMMENT '更热人',
  `status` tinyint(4) DEFAULT '1' COMMENT '数据状态,1:正常,2:删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sys_organization` */


CREATE TABLE IF NOT EXISTS `sys_organization` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) DEFAULT NULL COMMENT '名称',
  `description` varchar(1024) DEFAULT NULL COMMENT '描述',
  `is_final` int(11) DEFAULT '1' COMMENT '是否可删除',
  `parent_id` bigint(20) DEFAULT '0',
  `rank` bigint(20) DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT '0' COMMENT '创建人id',
  `update_by` bigint(20) DEFAULT '0' COMMENT '更新人id',
  `status` tinyint(4) DEFAULT '1' COMMENT '数据状态,1:正常,2:删除',
  `full_name` varchar(256) DEFAULT NULL COMMENT '全称',
  `org_code` varchar(200) DEFAULT NULL COMMENT '机构代码',
  PRIMARY KEY (`id`),
  UNIQUE KEY `org_code` (`org_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sys_permission` */


CREATE TABLE IF NOT EXISTS `sys_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) DEFAULT NULL COMMENT '名称',
  `description` varchar(256) DEFAULT NULL COMMENT '描述',
  `code` varchar(256) DEFAULT NULL COMMENT '编码',
  `sys_permission_group_id` bigint(20) DEFAULT NULL COMMENT '分组id',
  `is_final` int(11) DEFAULT '1' COMMENT '是否可删除',
  `rank` bigint(20) DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT '0' COMMENT '创建人id',
  `update_by` bigint(20) DEFAULT '0' COMMENT '更新人id',
  `status` tinyint(4) DEFAULT '1' COMMENT '数据状态,1:正常,2:删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sys_permission_group` */


CREATE TABLE IF NOT EXISTS `sys_permission_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) DEFAULT NULL COMMENT '名称',
  `description` varchar(256) DEFAULT NULL COMMENT '描述',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父级id',
  `is_final` int(11) DEFAULT '1' COMMENT '是否可删除',
  `rank` bigint(20) DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT '0' COMMENT '创建人id',
  `update_by` bigint(20) DEFAULT '0' COMMENT '更新人id',
  `status` tinyint(4) DEFAULT '1' COMMENT '数据状态,1:正常,2:删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sys_record_total` */


CREATE TABLE IF NOT EXISTS `sys_record_total` (
  `record_total_id` bigint(20) NOT NULL COMMENT '通话记录统计表id',
  `device_id` bigint(20) DEFAULT NULL COMMENT '话机设备表id',
  `call_log_count` bigint(20) DEFAULT '0' COMMENT '通话记录数量',
  `call_record_count` bigint(20) DEFAULT '0' COMMENT '通话录音数量',
  `reco_audio_length` bigint(20) DEFAULT '0' COMMENT '通话录音时长',
  `reco_file_size` bigint(20) DEFAULT '0' COMMENT '通话录音文件大小',
  `call_already_accept_count` bigint(20) DEFAULT '0' COMMENT '已接数量',
  `call_no_accept` bigint(20) DEFAULT '0' COMMENT '未接数量',
  `call_refuse_accept` bigint(20) DEFAULT '0' COMMENT '拒接数量',
  `call_call_out` bigint(20) DEFAULT '0' COMMENT '呼出数量',
  `call_no_accept_leave` bigint(20) DEFAULT '0' COMMENT '未接留言数量',
  PRIMARY KEY (`record_total_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='设备管理统计表';

/*Table structure for table `sys_role` */


CREATE TABLE IF NOT EXISTS `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(1024) DEFAULT NULL,
  `name` varchar(256) DEFAULT NULL,
  `rank` bigint(20) DEFAULT '0' COMMENT '排序',
  `is_final` int(11) DEFAULT '1' COMMENT '是否可删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT '0' COMMENT '创建人id',
  `update_by` bigint(20) DEFAULT '0' COMMENT '更新人id',
  `status` tinyint(4) DEFAULT '1' COMMENT '数据状态,1:正常,2:删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sys_role_organization` */


CREATE TABLE IF NOT EXISTS `sys_role_organization` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sys_organization_id` bigint(20) DEFAULT NULL COMMENT '组织id',
  `sys_role_id` bigint(20) DEFAULT NULL COMMENT '角色id',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父级id',
  `name` varchar(256) DEFAULT NULL,
  `full_name` varchar(256) DEFAULT NULL,
  `description` varchar(256) DEFAULT NULL,
  `rank` bigint(20) DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT '0' COMMENT '创建人id',
  `update_by` bigint(20) DEFAULT '0' COMMENT '更新人id',
  `status` tinyint(4) DEFAULT '1' COMMENT '数据状态,1:正常,2:删除',
  `is_final` tinyint(4) DEFAULT NULL COMMENT '是否能修改',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sys_role_permission` */


CREATE TABLE IF NOT EXISTS `sys_role_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sys_permission_id` bigint(20) DEFAULT NULL COMMENT '权限id',
  `sys_role_id` bigint(20) DEFAULT NULL COMMENT '角色id',
  `rank` bigint(20) DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT '0' COMMENT '创建人id',
  `update_by` bigint(20) DEFAULT '0' COMMENT '更新人id',
  `status` tinyint(4) DEFAULT '1' COMMENT '数据状态,1:正常,2:删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sys_user` */


CREATE TABLE IF NOT EXISTS `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(256) DEFAULT NULL COMMENT '登陆名',
  `zh_name` varchar(256) DEFAULT NULL COMMENT '中文名',
  `en_name` varchar(256) DEFAULT NULL COMMENT '英文名',
  `sex` int(11) DEFAULT '0' COMMENT '性别',
  `birth` varchar(256) DEFAULT NULL COMMENT '生日',
  `email` varchar(256) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(256) DEFAULT NULL COMMENT '电话',
  `address` varchar(1024) DEFAULT NULL COMMENT '地址',
  `password` varchar(256) DEFAULT NULL COMMENT '密码',
  `password_salt` varchar(256) DEFAULT NULL COMMENT '密码盐',
  `rank` bigint(20) DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT '0' COMMENT '创建人',
  `update_by` bigint(20) DEFAULT '0' COMMENT '更热人',
  `status` tinyint(4) DEFAULT '1' COMMENT '数据状态,1:正常,2:删除,3:禁用账号',
  `is_final` tinyint(4) DEFAULT NULL COMMENT '是否能修改',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sys_user_permission` */


CREATE TABLE IF NOT EXISTS `sys_user_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sys_user_id` bigint(20) DEFAULT NULL,
  `sys_permission_id` bigint(20) DEFAULT NULL,
  `is_final` int(11) DEFAULT '1',
  `rank` bigint(20) DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT '0' COMMENT '创建人id',
  `update_by` bigint(20) DEFAULT '0' COMMENT '更新人id',
  `status` tinyint(4) DEFAULT '1' COMMENT '数据状态,1:正常,2:删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sys_user_role_organization` */


CREATE TABLE IF NOT EXISTS `sys_user_role_organization` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sys_user_id` bigint(20) DEFAULT NULL,
  `sys_role_organization_id` bigint(20) DEFAULT NULL,
  `rank` bigint(20) DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT '0' COMMENT '创建人id',
  `update_by` bigint(20) DEFAULT '0' COMMENT '更新人id',
  `status` tinyint(4) DEFAULT '1' COMMENT '数据状态,1:正常,2:删除',
  `is_final` tinyint(4) DEFAULT NULL COMMENT '是否能修改',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

insert  into `sys_permission_group`(`id`,`name`,`description`,`parent_id`,`is_final`,`create_by`) 
values 
(1,'用户管理','用户数据增删改查',0,2,1),
(2,'权限管理','权限数据增删改查',0,2,1),
(3,'角色管理','角色数据增删改查',0,2,1),
(4,'组织机构管理','组织机构数据增删改查',0,2,1),
(5,'职位管理','职位数据增删改查',0,2,1),
(6,'字典管理','字典数据增删改查',0,2,1),
(7,'数据库','数据库监控',0,2,1),
(8,'安全','安全',0,2,1),
(9,'日志','日志',0,2,1),
(10,'其他','其他',0,2,1),
(11,'话机列表','话机列表',0,2,1),
(12,'#1super的设备状态','#1super的设备状态',0,2,1);


insert  into `sys_permission`
(`id`,`name`,`description`,`code`,`sys_permission_group_id`,`is_final`) 
values 
(1,'新增','新增用户','user:insert',1,2),
(2,'删除','删除用户','user:delete',1,2),
(3,'更新','更新用户','user:update',1,2),
(4,'查询','查询用户','user:select',1,2),
(5,'列表','查询用户列表','user:list',1,2),
(6,'新增','新增权限','permission:insert',2,2),
(7,'删除','删除权限','permission:delete',2,2),
(8,'更新','更新权限','permission:update',2,2),
(9,'查询','查询权限','permission:select',2,2),
(10,'列表','查询权限列表','permission:list',2,2),
(11,'新增','新增角色','role:insert',3,2),
(12,'删除','删除角色','role:delete',3,2),
(13,'更新','更新角色','role:update',3,2),
(14,'查询','查询角色','role:select',3,2),
(15,'列表','查询角色列表','role:list',3,2),
(16,'新增','新增组织机构','organization:insert',4,2),
(17,'删除','删除组织机构','organization:delete',4,2),
(18,'更新','更新组织机构','organization:update',4,2),
(19,'查询','查询组织机构','organization:select',4,2),
(20,'列表','查询组织机构列表','organization:list',4,2),
(21,'新增','新增职位','job:insert',5,2),
(22,'删除','删除职位','job:delete',5,2),
(23,'更新','更新职位','job:update',5,2),
(24,'查询','查询职位','job:select',5,2),
(25,'列表','查询职位列表','job:list',5,2),
(26,'新增','新增字典','data:insert',6,2),
(27,'删除','删除字典','data:delete',6,2),
(28,'更新','更新字典','data:update',6,2),
(29,'查询','查询字典','data:select',6,2),
(30,'列表','查询字典列表','data:list',6,2),
(31,'启用','启用用户','user:enable',1,2),
(32,'禁用','禁用用户','user:forbidden',1,2),
(33,'密码','修改密码','user:updatePassword',1,2),
(34,'查看数据库监控','查看数据库监控','db:select',7,2),
(35,'下线','下线用户','user:loginout',1,2),
(36,'用户在线列表','用户在线列表','user:loginStatu:list',1,2),
(37,'新建权限组','新建权限组','permission:group:insert',2,2),
(38,'权限组列表','权限组列表','permission:group:list',2,2),
(39,'新增','新增IP','ip:insert',8,2),
(40,'更新','更新ip','ip:update',8,2),
(41,'删除','删除ip','ip:delete',8,2),
(42,'查看','查看ip','ip:select',8,2),
(43,'列表','ip列表','ip:list',8,2),
(44,'字典组列表','字典组列表','data:group:list',6,2),
(45,'新增字典组','新增字典组','data:group:insert',6,2),
(46,'列表','列表','log:list',9,2),
(47,'引导','引导界面','system:index',10,1),
(48,'话机列表','查看话机列表','device:list',11,1),

(49,'查询设备','查看该用户的所有状态','device:1:list',12,1),
(50,'修改设备','修改该设备数据','device:1:update',12,1),
(51,'删除设备','删除该设备数据','device:1:delete',12,1);



insert into `sys_user_permission` (`id`, `sys_user_id`, `sys_permission_id`) 
values
(1,1,1),
(2,1,2),
(3,1,3),
(4,1,4),
(5,1,5),
(6,1,31),
(7,1,32),
(8,1,33),
(9,1,35),
(10,1,36),
(11,1,6),
(12,1,7),
(13,1,8),
(14,1,9),
(15,1,10),
(16,1,37),
(17,1,38),
(18,1,11),
(19,1,12),
(20,1,13),
(21,1,14),
(22,1,15),
(23,1,16),
(24,1,17),
(25,1,18),
(26,1,19),
(27,1,20),
(28,1,21),
(29,1,22),
(30,1,23),
(31,1,24),
(32,1,25),
(33,1,26),
(34,1,27),
(35,1,28),
(36,1,29),
(37,1,30),
(38,1,44),
(39,1,45),
(40,1,34),
(41,1,39),
(42,1,40),
(43,1,41),
(44,1,42),
(45,1,43),
(46,1,46),
(47,1,47),
(48,1,48),
(49,5,49),
(50,5,50),
(51,5,51);

insert  into `sys_data_group`(`id`,`description`,`parent_id`,`name`,`is_final`) 
values 
(1,'极限验证',0,'极限验证',2),
(2,'日志输出控制',0,'日志',2),
(3,'是否开启Ip拦截',0,'IP拦截',2),
(4,'系统配置信息',0,'系统',2);

insert  into `sys_data_item`(`id`,`sys_data_group_id`,`key_value`,`key_name`,`is_final`,`description`) 
values 
(1,1,'796c2461adf8051c835e4a758a6091f6','geetest_id',2,'geetest_id'),
(2,1,'0edad631bed761ab039d8391dd3103ff','geetest_key',2,'geetest_key'),
(3,2,'true','error_detail',2,'是否输出错误日志详情'),
(4,3,'true','ip_forbidden',2,'是否开启ip拦截'),
(5,4,'/hunt-admin','basePath',2,'系统root路径');

insert into `sys_organization` (`id`, `name`, `description`, `is_final`,`full_name`, `org_code`) 
values('1','系统','系统','2','系统','100');

insert into `sys_role_organization` 
(`id`, `sys_organization_id`, `sys_role_id`, `parent_id`, `name`, `full_name`,`description`,`is_final`) 
values
('1','1','0','0','系统','系统','系统','2');

insert  into `sys_user`
(`id`,`login_name`,`zh_name`,`en_name`,`sex`,`password`,`password_salt`,`create_by`,`is_final`) 
values 
(1,'super','super','super',1,'f893d078cee0c79c90e8747e1df8f54b','0e1e5f9114dc4d60a7ea9e13c60bdff8',1,2);


insert into `sys_user_role_organization` 
(`id`,`sys_user_id`,`sys_role_organization_id`,`create_by`,`is_final`) 
values('1','1','1','1','1');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
