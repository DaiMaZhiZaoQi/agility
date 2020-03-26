package com.hunt.model.dto;

import com.hunt.model.entity.BaseEntity;
import com.hunt.model.entity.SysDevice;
import com.hunt.model.entity.SysDeviceCallLog;
import com.hunt.model.entity.SysDeviceRecord;
import com.hunt.model.entity.SysOrganization;

/**
 * 通话记录，通话录音，设备，机构组合实体
 * @author williambai
 *
 */
public class SysCallLogDeviceRecoDto extends BaseEntity{
	private Integer callId;
	private String orgName;
	private Long orgId;
	private String devSerial;
	private Long deviceId;
	private Long devRolgId;
	private Long userId;
	private String userName;
	
	private SysDeviceCallLogAndRecordDto sysDeviceRecord;
	
	

	public Integer getCallId() {
		return callId;
	}

	public void setCallId(Integer callId) {
		this.callId = callId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	
	
	public String getDevSerial() {
		return devSerial;
	}

	public void setDevSerial(String devSerial) {
		this.devSerial = devSerial;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public Long getDevRolgId() {
		return devRolgId;
	}

	public void setDevRolgId(Long devRolgId) {
		this.devRolgId = devRolgId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public SysDeviceCallLogAndRecordDto getSysDeviceRecord() {
		return sysDeviceRecord;
	}

	public void setSysDeviceRecord(SysDeviceCallLogAndRecordDto sysDeviceRecord) {
		this.sysDeviceRecord = sysDeviceRecord;
	}
	


	
	

}
