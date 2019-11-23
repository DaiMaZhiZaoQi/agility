package com.hunt.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import com.hunt.model.entity.BaseEntity;

public class MultilFileBodyDto extends BaseEntity{

	private String record;
	private String deviceSerial;
	private String sysDeviceCallLog;
	private MultipartFile multiPartFile;
	public String getRecord() {
		return record;
	}
	public void setRecord(String record) {
		this.record = record;
	}
	public String getDeviceSerial() {
		return deviceSerial;
	}
	public void setDeviceSerial(String deviceSerial) {
		this.deviceSerial = deviceSerial;
	}
	public String getSysDeviceCallLog() {
		return sysDeviceCallLog;
	}
	public void setSysDeviceCallLog(String sysDeviceCallLog) {
		this.sysDeviceCallLog = sysDeviceCallLog;
	}
	public MultipartFile getMultiPartFile() {
		return multiPartFile;
	}
	public void setMultiPartFile(MultipartFile multiPartFile) {
		this.multiPartFile = multiPartFile;
	}

	
	
	
}
