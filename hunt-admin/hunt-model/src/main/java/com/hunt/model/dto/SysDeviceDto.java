package com.hunt.model.dto;

import java.util.Date;
import java.util.List;

import com.hunt.model.entity.BaseEntity;
import com.hunt.model.entity.SysDeviceRoleOrg;

public class SysDeviceDto extends BaseEntity{

	/**id*/
	private Long id;
	
	/**device_name :  设备名称*/
	private String deviceName;
	
	/**device_serial: 设备序列号*/
	private String deviceSerial;
	
	/**device_pwd:设备密码*/
	private String devicePwd;
	
	/**rank: 排序*/
	private Long rank;
	
	/**device_status:  设备状态，0：关机，1：开机，2：在线，3，在线，话机故障，4，离线   */
	private Integer deviceStatus;
	
	/**status: 数据状态,1:正常,2:删除',*/
	private Integer status;
	
	/**create_time 创建时间*/
	private Date createTime;
	
	/**update_Time 更新时间*/
	private Date updateTime;
	
	/**create_by  创建人*/
	private Long createBy;
	
	/**update_by 更新人*/
	private Long updateBy;
	
	/**是否可删除，1：可删除，0：不可删除*/
	private Integer isFinal;
	
	/**description: 描述*/
	private String description;
	
	/**device_msg 话机状态信息，json信息，软件版本，设备ip，cpu，磁盘大小，内存大小  */
	private String deviceMsg;
	
	/**device_time:  设备时间*/
	private Date deviceTime;
	
	private List<SysDeviceRoleOrg> sysDeviceRoleOrg;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceSerial() {
		return deviceSerial;
	}

	public void setDeviceSerial(String deviceSerial) {
		this.deviceSerial = deviceSerial;
	}

	public String getDevicePwd() {
		return devicePwd;
	}

	public void setDevicePwd(String devicePwd) {
		this.devicePwd = devicePwd;
	}

	public Long getRank() {
		return rank;
	}

	public void setRank(Long rank) {
		this.rank = rank;
	}

	public Integer getDeviceStatus() {
		return deviceStatus;
	}

	public void setDeviceStatus(Integer deviceStatus) {
		this.deviceStatus = deviceStatus;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public Integer getIsFinal() {
		return isFinal;
	}

	public void setIsFinal(Integer isFinal) {
		this.isFinal = isFinal;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDeviceMsg() {
		return deviceMsg;
	}

	public void setDeviceMsg(String deviceMsg) {
		this.deviceMsg = deviceMsg;
	}

	public Date getDeviceTime() {
		return deviceTime;
	}

	public void setDeviceTime(Date deviceTime) {
		this.deviceTime = deviceTime;
	}

	public List<SysDeviceRoleOrg> getSysDeviceRoleOrg() {
		return sysDeviceRoleOrg;
	}

	public void setSysDeviceRoleOrg(List<SysDeviceRoleOrg> sysDeviceRoleOrg) {
		this.sysDeviceRoleOrg = sysDeviceRoleOrg;
	}


	
	
	
	
}
