package com.hunt.model.dto;

import java.util.List;

import com.hunt.model.entity.BaseEntity;
import com.hunt.model.entity.SysDeviceTotal;

/**
 * 返回的通话记录大集合
 * @author williambai
 *
 */
public class SysCallLogTotal extends BaseEntity{
	
	private SysDeviceTotal sysDeviceTotal;
	private List<SysCallLogDeviceRecoDto> listCallLog;
	public SysDeviceTotal getSysDeviceTotal() {
		return sysDeviceTotal;
	}
	public void setSysDeviceTotal(SysDeviceTotal sysDeviceTotal) {
		this.sysDeviceTotal = sysDeviceTotal;
	}
	public List<SysCallLogDeviceRecoDto> getListCallLog() {
		return listCallLog;
	}
	public void setListCallLog(List<SysCallLogDeviceRecoDto> listCallLog) {
		this.listCallLog = listCallLog;
	}

	
}
