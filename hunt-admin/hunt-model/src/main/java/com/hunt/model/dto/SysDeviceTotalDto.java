package com.hunt.model.dto;

import com.hunt.model.entity.BaseEntity;

/**
 * 所有设备状态
 * @author williambai
 *
 */
public class SysDeviceTotalDto extends BaseEntity {
	private Integer allDevice;
	private Integer onLineDevice;
	private Integer	allAcount;
	private Integer	onLineAccount;
	private String userName;
	public Integer getAllDevice() {
		return allDevice;
	}
	public void setAllDevice(Integer allDevice) {
		this.allDevice = allDevice;
	}
	public Integer getOnLineDevice() {
		return onLineDevice;
	}
	public void setOnLineDevice(Integer onLineDevice) {
		this.onLineDevice = onLineDevice;
	}
	public Integer getAllAcount() {
		return allAcount;
	}
	public void setAllAcount(Integer allAcount) {
		this.allAcount = allAcount;
	}
	public Integer getOnLineAccount() {
		return onLineAccount;
	}
	public void setOnLineAccount(Integer onLineAccount) {
		this.onLineAccount = onLineAccount;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
