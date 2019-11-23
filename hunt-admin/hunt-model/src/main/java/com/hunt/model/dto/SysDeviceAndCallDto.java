package com.hunt.model.dto;


import java.util.List;
import java.util.Set;

import com.hunt.model.entity.BaseEntity;
import com.hunt.model.entity.SysDevice;
import com.hunt.model.entity.SysDeviceCallLog;
import com.hunt.model.entity.SysDeviceRecord;
import com.hunt.model.entity.SysDeviceTotal;
import com.hunt.model.entity.SysOrganization;
import com.hunt.model.entity.SysRoleOrganization;
import com.hunt.model.entity.SysUser;
/**
 * 设备和通话组合实体类
 * @author williambai
 *
 */
public class SysDeviceAndCallDto extends BaseEntity{
	
	private SysUser sysUser;
	
	private Set<SysOrganization> setSysOrganization;
	
	private Set<SysRoleOrganization> setSysRoleOrganization;
	
	private SysDevice sysDevice;
	
	private SysDeviceTotal sysDeviceTotal;
	
//	private List<SysDeviceCallLogAndRecordDto> listDeviceCallLogAndRecord;
	

	
	
	public SysUser getSysUser() {
		return sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}

	

	public Set<SysOrganization> getSetSysOrganization() {
		return setSysOrganization;
	}

	public void setSetSysOrganization(Set<SysOrganization> setSysOrganization) {
		this.setSysOrganization = setSysOrganization;
	}

	public Set<SysRoleOrganization> getSetSysRoleOrganization() {
		return setSysRoleOrganization;
	}

	public void setSetSysRoleOrganization(Set<SysRoleOrganization> setSysRoleOrganization) {
		this.setSysRoleOrganization = setSysRoleOrganization;
	}

	public SysDevice getSysDevice() {
		return sysDevice;
	}

	public void setSysDevice(SysDevice sysDevice) {
		this.sysDevice = sysDevice;
	}

	

	public SysDeviceTotal getSysDeviceTotal() {
		return sysDeviceTotal;
	}

	public void setSysDeviceTotal(SysDeviceTotal sysDeviceTotal) {
		this.sysDeviceTotal = sysDeviceTotal;
	}

/*	public List<SysDeviceCallLogAndRecordDto> getListDeviceCallLogAndRecord() {
		return listDeviceCallLogAndRecord;
	}

	public void setListDeviceCallLogAndRecord(List<SysDeviceCallLogAndRecordDto> listDeviceCallLogAndRecord) {
		this.listDeviceCallLogAndRecord = listDeviceCallLogAndRecord;
	}
	
	*/
	

}
