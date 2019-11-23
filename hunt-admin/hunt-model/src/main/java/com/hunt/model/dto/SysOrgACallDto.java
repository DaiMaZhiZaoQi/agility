package com.hunt.model.dto;
/**
 * 查询机构及其该机构下的通话
 * @author williambai
 *
 */

import java.util.List;

import com.hunt.model.entity.BaseEntity;
import com.hunt.model.entity.SysDevice;
import com.hunt.model.entity.SysDeviceTotal;
import com.hunt.model.entity.SysOrganization;

public class SysOrgACallDto extends BaseEntity{
	private SysDeviceTotal listDeviceTotal;
	private SysOrganization mOrganization;
	
	private List<SysDeviceCallLogAndRecordDto> list;
	
	
	
	
	public SysDeviceTotal getListDeviceTotal() {
		return listDeviceTotal;
	}
	public void setListDeviceTotal(SysDeviceTotal listDeviceTotal) {
		this.listDeviceTotal = listDeviceTotal;
	}
	public SysOrganization getmOrganization() {
		return mOrganization;
	}
	public void setmOrganization(SysOrganization mOrganization) {
		this.mOrganization = mOrganization;
	}
	public List<SysDeviceCallLogAndRecordDto> getList() {
		return list;
	}
	public void setList(List<SysDeviceCallLogAndRecordDto> list) {
		this.list = list;
	}
	
	
}
