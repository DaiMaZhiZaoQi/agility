package com.hunt.controller;

import java.io.Serializable;

public class User implements Serializable{
	String partName;
	int deviceId;
	public String getPartName() {
		return partName;
	}
	public void setPartName(String partName) {
		this.partName = partName;
	}
	public int getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}
	

}
