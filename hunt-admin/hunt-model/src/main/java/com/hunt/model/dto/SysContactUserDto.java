package com.hunt.model.dto;

import com.hunt.model.entity.BaseEntity;
import com.hunt.model.entity.SysContact;
import com.hunt.model.entity.SysContactUser;
/**
 * 组合映射实体类
 * @author williambai
 *
 */
public class SysContactUserDto extends BaseEntity{
	
	private SysContact sysContact;
	private SysContactUser sysContactUser;
	public SysContact getSysContact() {
		return sysContact;
	}
	public void setSysContact(SysContact sysContact) {
		this.sysContact = sysContact;
	}
	public SysContactUser getSysContactUser() {
		return sysContactUser;
	}
	public void setSysContactUser(SysContactUser sysContactUser) {
		this.sysContactUser = sysContactUser;
	}
	

}
