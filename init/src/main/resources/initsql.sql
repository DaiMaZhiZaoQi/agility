#INSERT INTO sys_permission_group_test(NAME,description,parent_id) VALUES("权限组一","权限组一",0),("权限组二","权限组二",0);



/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.17-log : Database - zicoo_record
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
 
CREATE DATABASE IF NOT EXISTS `zicoo_record` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci; 
#CREATE DATABASE IF NOT EXISTS `hunt` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci; 
 


USE `zicoo_record`;
#USE `hunt`;

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
  `sys_org_code` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `status` tinyint(4) DEFAULT '2' COMMENT '状态 1,正常2，未激活 3，删除',
  `is_auth` tinyint(4) DEFAULT '0' COMMENT '是否为文件作者0，不是；1，是',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通讯录联系人中间表，一个机构可能有多个通讯录，一个人员可能有多个通讯录';



/*Table structure for table `sys_data_group` */


CREATE TABLE IF NOT EXISTS `sys_data_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父级id',
  `name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `is_final` int(11) DEFAULT '1' COMMENT '是否可删除',
  `rank` bigint(20) DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT '0' COMMENT '创建人',
  `update_by` bigint(20) DEFAULT '0' COMMENT '更热人',
  `status` tinyint(4) DEFAULT '1' COMMENT '数据状态,1:正常,2:删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '字典组';

/*Table structure for table `sys_data_item` */

CREATE TABLE IF NOT EXISTS `sys_data_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sys_data_group_id` bigint(20) DEFAULT NULL COMMENT '组id',
  `key_value` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '值',
  `key_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `is_final` int(11) DEFAULT '1' COMMENT '是否可删除',
  `rank` bigint(20) DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT '0' COMMENT '创建人',
  `update_by` bigint(20) DEFAULT '0' COMMENT '更热人',
  `status` tinyint(4) DEFAULT '1' COMMENT '数据状态,1:正常,2:删除,3:禁用账号',
  `description` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述',
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
  `sys_org_id` bigint(20) DEFAULT NULL,
  `sys_org_code` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT '',
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
  `org_id` bigint(20) DEFAULT NULL,
  `org_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `org_code` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `user_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '话机使用人员',
  `dev_serial` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `orgId` (`org_id`),
  KEY `call_number` (`call_number`),
  KEY `org_name` (`org_name`)
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
  `org_id` bigint(20) DEFAULT NULL COMMENT '机构id',
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
  `description` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '说明',
  `ip` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'IP地址',
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
  `ip` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '请求ip',
  `user_id` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '操作用户id',
  `method` varchar(2048) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '请求方法',
  `param` text COLLATE utf8mb4_unicode_ci COMMENT '请求参数',
  `result` text COLLATE utf8mb4_unicode_ci COMMENT '请求结果',
  `duration` bigint(20) DEFAULT NULL COMMENT '持续时间',
  `url` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '请求url',
  `user_agent` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '请求ua',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sys_login_status` */


CREATE TABLE IF NOT EXISTS `sys_login_status` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sys_user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `session_id` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'session id',
  `session_expires` datetime DEFAULT NULL COMMENT 'session过期时间',
  `sys_user_login_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '登录名',
  `sys_user_zh_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '中文名',
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
  `name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `description` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述',
  `is_final` int(11) DEFAULT '1' COMMENT '是否可删除',
  `parent_id` bigint(20) DEFAULT '0',
  `rank` bigint(20) DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT '0' COMMENT '创建人id',
  `update_by` bigint(20) DEFAULT '0' COMMENT '更新人id',
  `status` bigint(20) DEFAULT '1' COMMENT '数据状态 1，正常，其他删除，年月日时分秒',
  `full_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '全称',
  `org_code` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '机构代码',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_status` (`org_code`,`status`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sys_permission` */

CREATE TABLE IF NOT EXISTS `sys_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `description` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述',
  `code` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '编码',
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
  `name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `description` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父级id',
  `is_final` int(11) DEFAULT '1' COMMENT '是否可删除',
  `rank` bigint(20) DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT '0' COMMENT '创建人id',
  `update_by` bigint(20) DEFAULT '0' COMMENT '更新人id',
  `status` tinyint(4) DEFAULT '1' COMMENT '数据状态,1:正常,2:删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '权限组';

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
  `description` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `rank` bigint(20) DEFAULT '0' COMMENT '排序',
  `is_final` int(11) DEFAULT '1' COMMENT '是否可删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT '0' COMMENT '创建人id',
  `update_by` bigint(20) DEFAULT '0' COMMENT '更新人id',
  `status` tinyint(4) DEFAULT '1' COMMENT '数据状态,1:正常,2:删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色';


/*Table structure for table `sys_role_organization` */

CREATE TABLE IF NOT EXISTS `sys_role_organization` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sys_organization_id` bigint(20) DEFAULT NULL COMMENT '组织id',
  `sys_role_id` bigint(20) DEFAULT NULL COMMENT '角色id',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父级id',
  `name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `full_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '角色权限';


/*Table structure for table `sys_user` */

CREATE TABLE IF NOT EXISTS `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '登陆名',
  `zh_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '中文名',
  `en_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '英文名',
  `sex` int(11) DEFAULT '0' COMMENT '性别',
  `birth` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '生日',
  `email` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电话',
  `address` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地址',
  `password` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码',
  `password_salt` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码盐',
  `rank` bigint(20) DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT '0' COMMENT '创建人',
  `update_by` bigint(20) DEFAULT '0' COMMENT '更热人',
  `status` tinyint(4) DEFAULT '1' COMMENT '数据状态,1:正常,2:删除,3:禁用账号',
  `is_final` tinyint(4) DEFAULT NULL COMMENT '是否能修改',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sys_user_in_org` */

CREATE TABLE IF NOT EXISTS `sys_user_in_org` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sys_user_id` bigint(20) DEFAULT NULL,
  `sys_org_id` bigint(20) DEFAULT NULL,
  `sys_org_code` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `rank` bigint(20) DEFAULT '0',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_by` bigint(20) DEFAULT '0',
  `update_by` bigint(20) DEFAULT '0',
  `status` bigint(20) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `userOrg` (`sys_user_id`,`sys_org_id`,`status`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sys_user_organization` */

CREATE TABLE IF NOT EXISTS `sys_user_organization` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sys_user_id` bigint(20) DEFAULT NULL,
  `sys_org_id` bigint(20) DEFAULT NULL,
  `sys_org_code` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '机构码',
  `rank` tinyint(4) DEFAULT '0',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_by` bigint(20) DEFAULT '0',
  `update_by` bigint(20) DEFAULT '0',
  `STATUS` tinyint(4) DEFAULT '1' COMMENT '数据状态,1:正常,2:删除',
  `is_final` tinyint(4) DEFAULT '1' COMMENT '是否能修改，1，能，2，不能',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '用户机构权限';




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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '用户权限';

/*Table structure for table `sys_user_role` */

CREATE TABLE IF NOT EXISTS `sys_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sys_user_id` bigint(20) DEFAULT NULL,
  `sys_role_id` bigint(20) DEFAULT NULL,
  `rank` tinyint(4) DEFAULT '0',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_by` bigint(20) DEFAULT '0',
  `update_by` bigint(20) DEFAULT '0',
  `status` tinyint(4) DEFAULT '1' COMMENT '数据状态,1:正常,2:删除',
  `is_final` tinyint(4) DEFAULT '1' COMMENT '是否能修改，1，能，2，不能',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '用户角色';


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

/*Table structure for table `sys_task_group` */
CREATE TABLE IF NOT EXISTS `sys_task_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务组id',
  `task_group_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '任务组名称',
  `task_code` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '任务文件码（文件名和文件码组成唯一标识）',
  `task_pub_user_id` bigint(20) DEFAULT NULL COMMENT '任务发布人id',
  `task_pub_user_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '任务发布人名称',
  `task_column` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '任务栏',
  `task_size` int(11) DEFAULT '0' COMMENT '任务发布总数',
  `task_finish` int(11) DEFAULT '0' COMMENT '已完成数量',
  `status` tinyint(4) DEFAULT '0' COMMENT '任务组状态（0，未进行，1，进行中，2，任务组已关闭）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '任务发布时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '任务更新时间',
  `task_rate` decimal(5,2) DEFAULT '0.00' COMMENT '任务进度比率',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务组';

/*Table structure for table `sys_task_user` */
CREATE TABLE IF NOT EXISTS `sys_task_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户任务表id',
  `sys_user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `sys_user_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '用户名称',
  `sys_task_group` bigint(20) DEFAULT NULL COMMENT '用户所属任务组',
  `status` tinyint(4) DEFAULT '0' COMMENT '我的任务状态(0，未授权，1，已授权，2，任务已结束)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户任务，用户存在该表中，则用户能查看任务';

/*Table structure for table `sys_task` */
CREATE TABLE IF NOT EXISTS `sys_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务id',
  `task_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '任务名称',
  `task_number` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '任务号码',
  `task_msg` text COLLATE utf8mb4_unicode_ci COMMENT '任务信息',
  `task_group_id` bigint(20) DEFAULT NULL COMMENT '任务所属任务组id',
  `task_user_id` bigint(20) DEFAULT NULL COMMENT '任务所属的用户id',
  `task_user_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '任务所属用户名称',
  `task_count` bigint(20) DEFAULT '0' COMMENT '任务执行次数，执行一次即任务已开始',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '任务发布时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '任务更新时间',
  `task_time_chain` text COLLATE utf8mb4_unicode_ci COMMENT '任务更新时间链',
  `status` tinyint(4) DEFAULT '-1' COMMENT '任务状态(-1，任务未分配，0，未开始，1，正在进行，2，已完成，3，任务已重新分配,4,任务已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `num_group` (`task_number`,`task_group_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务详情';





insert  into `sys_permission_group`(`id`,`name`,`description`,`parent_id`,`is_final`,`create_by`) 
values 
(1,'系统管理权限','系统管理权限（用户管理，角色管理，部门管理，系统维护）',0,2,1),
(2,'业务权限','业务权限（设备管理，通话记录管理，录音管理，通讯录管理）',0,2,1);


insert  into `sys_permission`
(`id`,`name`,`description`,`code`,`sys_permission_group_id`,`is_final`) 
values 
(1,'用户管理','用户管理(对用户的增删改查)','user:manage',1,2),
(2,'角色管理','角色管理(对角色的增删改查)','role:manage',1,2),
(3,'部门管理','部门管理(对部门的增删改查)','org:manage',1,2),
(4,'系统维护','系统维护(对系统的增删改查)','system:manage',1,2),
(5,'设备注册','设备注册对用户的鉴权，对设备的添加','device:insert',2,2),
(6,'设备查询','设备查询','device:select',2,2),
(7,'设备维护(Web远程管理、删除设备)','设备管理(Web远程管理、删除设备)','device:manage',2,2),
(8,'添加通话记录','添加通话记录','callLog:insert',2,2),
(9,'通话记录查询','通话记录查询','callLog:select',2,2),
(10,'通话记录维护','通话记录维护(删除)','callLog:delete',2,2),
(11,'添加录音','添加录音','record:insert',2,2),
(12,'录音查询','录音查询','record:select',2,2),
(13,'录音维护','录音维护(删除)','record:delete',2,2),
(14,'通讯录发布','通讯录发布','contact:insert',2,2),
(15,'通讯录查询','通讯录查询','contact:select',2,2),
(16,'通讯录维护','通讯录维护(删除)','contact:delete',2,2);


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
(5,4,'/zicoo','basePath',2,'系统root路径');

/*添加机构*/
insert into `sys_organization` (`id`, `name`, `description`, `is_final`,`full_name`, `org_code`) 
values('1','系统','系统','2','系统','01');

/*添加用户*/
insert  into `sys_user`
(`id`,`login_name`,`zh_name`,`en_name`,`sex`,`password`,`password_salt`,`create_by`,`is_final`) 
values 
(1,'super','super','super',1,'f893d078cee0c79c90e8747e1df8f54b','0e1e5f9114dc4d60a7ea9e13c60bdff8',1,2);

/*添加用户所在机构*/
INSERT INTO sys_user_in_org(id,sys_user_id,sys_org_id,sys_org_code) VALUES(1,1,1,'01');

/*添加用户机构权限*/
INSERT INTO sys_user_organization(id,sys_user_id,sys_org_id,sys_org_code,is_final) VALUES(1,1,1,'01',2);


/*添加角色*/
INSERT INTO sys_role(description,NAME,is_final)
VALUES
('系统管理员','系统管理员','2'),('业务管理员','业务管理员','1'),('业务员','业务员','1'),('查询用户','查询用户','1');


/*添加角色权限*/
INSERT INTO sys_role_permission(sys_permission_id,sys_role_id)
VALUES
(1,1),(2,1),(3,1),(4,1),
(9,2),(10,2),(12,2),(13,2),(6,2),(7,2),(15,2),(16,2),
(8,3),(9,3),(11,3),(12,3),(5,3),(6,3),(14,3),(15,3),
(9,4),(12,4),(6,4),(15,4);

/*添加用户角色*/
INSERT INTO sys_user_role(sys_user_id,sys_role_id,is_final)
VALUES(1,1,2),(1,2,1),(1,3,1),(1,4,1);





/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
