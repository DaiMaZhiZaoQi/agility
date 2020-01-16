package com.hunt.util;
/**
 * 权限码枚举
 * @author williambai
 *
 */
public enum PermissionCode {
	
		 USER_MANAGE(-100,"user:manage", "您没有用户管理权限"),
	
	
		ROLE_MANAGE(-101,"role:manage","您没有角色管理权限"),
		
		
		ORG_MANAGE(-102,"org:manage","您没有机构管理权限"),
		
	
		SYSTEM_MANAGE(-103,"system:manage","您没有系统管理权限"),
	
	
	
		DEVICE_INSERT(-104,"device:insert","您没有设备注册权限"),
		DEVICE_SELECT(-105,"device:select","您没有设备查询权限"),
		DEVICE_MANAGE(-106,"device:manage","您没有设备维护权限"),
	
	
		CALLLOG_INSERT(-107,"callLog:insert","您没有添加通话记录权限"),
		CALLLOG_SELECT(-108,"callLog:select","您没有通话记录查询权限"),
		CALLLOG_DELETE(-109,"callLog:delete","您没有通话记录维护权限"),
	
	
		RECORD_INSERT(-110,"record:insert","您没有添加录音权限"),
		RECORD_SELECT(-111,"record:select","您没有录音查询权限"),
		RECORD_DELETE(-112,"record:delete","您没有录音维护权限"),
		
	
	
		CONTACT_INSERT(-113,"contact:insert","您没有通讯录发布权限"),
		CONTACT_SELECT(-114,"contact:select","您没有通讯录查询权限"),
		CONTACT_DELETE(-115,"contact:delete","您没有通讯录维护权限");
			
		 public Integer pCode;
		 public String pName;
		 public String pMsg;
		 PermissionCode(Integer pCode, String pName, String pMsg) {
			 this.pCode=pCode;
			this.pName=pName;
			this.pMsg=pMsg;
		}
	
	
	
	
	
	
	

}
