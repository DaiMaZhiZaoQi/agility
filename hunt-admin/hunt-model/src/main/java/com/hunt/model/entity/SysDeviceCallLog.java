package com.hunt.model.entity;


import java.util.Date;
/**
 * 通话记录
 * @author williambai
 *
 */
public class SysDeviceCallLog extends BaseEntity{
	/**call_logId: 通话记录id*/
	private Long id;
	private Long deviceRoleOrgId;
	/**device_id 设备id 该条通话记录属于那一台设备*/
	private Long deviceId;
	
	/**通话名称*/
	private String callName;
	/**call_type: '通话类型，0：未接电话，1：呼入，2：呼出，3：拒接，4，未接留言',  */
	private Integer callType;
	/**call_duration 通话时长*/
	private Long callDuration;
	/**通话号码，呼入和呼出都使用该号码*/
	private String callNumber;
	/**通话时间*/
	private Long callDate;
	/**通话方式*/
	private Integer callSubscriptionId;
	/**是否有录音，有录音则查询录音文件0，无录音，1，有录音 */
	private Integer callHasRecord;
	/**录音时长*/
	private Long callRecordMs;
	/**通话描述*/
	private String callDescription;
	/**call_address 电话归属地*/
	private String callAddress;
	
	/**call_iscollect  是否为收藏关注的号码*/
	private Integer callIsCollect;
	
	
	/**call_other 其他信息*/
	private String callOther;
	
	/**create_time 创建时间*/
	private Date createTime;
	
	/**update_time 更新时间*/
	private Date updateTime;
	
	/**create_by  创建来源*/
	private Long createBy;
	
	/**update_by  更新来源，由谁更新*/
	private Long updateBy;
	
	/**status 状态1，正常，2，删除*/
	private Integer status;
	
	/**是否可修改1，可修改，2，不可修改*/
	private Integer isFinal;
	
	private Long orgId;
	private String orgName;
	private String orgCode;
	private Long userId;
	private String devSerial;
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	
	
	public Long getDeviceRoleOrgId() {
		return deviceRoleOrgId;
	}

	public void setDeviceRoleOrgId(Long deviceRoleOrgId) {
		this.deviceRoleOrgId = deviceRoleOrgId;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
	/**call_type: '通话类型，0：未接电话，1：呼入，2：呼出，3：拒接，4，未接留言',  */
	public Integer getCallType() {
		return callType;
	}

	public void setCallType(Integer callType) {
		this.callType = callType;
	}




	public Long getCallDuration() {
		return callDuration;
	}

	public void setCallDuration(Long callDuration) {
		this.callDuration = callDuration;
	}

	public String getCallAddress() {
		return callAddress;
	}

	public void setCallAddress(String callAddress) {
		this.callAddress = callAddress;
	}

	public Integer getCallIsCollect() {
		return callIsCollect;
	}

	public void setCallIsCollect(Integer callIsCollect) {
		this.callIsCollect = callIsCollect;
	}

	

	

	public String getCallOther() {
		return callOther;
	}

	public void setCallOther(String callOther) {
		this.callOther = callOther;
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

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
	

	public Long getUpdateBy() {
		return updateBy;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsFinal() {
		return isFinal;
	}

	public void setIsFinal(Integer isFinal) {
		this.isFinal = isFinal;
	}


	
	/***********************************/
	
	public String getCallName() {
		return callName;
	}

	public void setCallName(String callName) {
		this.callName = callName;
	}

	public String getCallNumber() {
		return callNumber;
	}

	public void setCallNumber(String callNumber) {
		this.callNumber = callNumber;
	}

	public Long getCallDate() {
		return callDate;
	}

	public void setCallDate(Long callDate) {
		this.callDate = callDate;
	}

	public Integer getCallSubscriptionId() {
		return callSubscriptionId;
	}

	public void setCallSubscriptionId(Integer callSubscriptionId) {
		this.callSubscriptionId = callSubscriptionId;
	}

	

	public Integer getCallHasRecord() {
		return callHasRecord;
	}

	public void setCallHasRecord(Integer callHasRecord) {
		this.callHasRecord = callHasRecord;
	}

	public Long getCallRecordMs() {
		return callRecordMs;
	}

	public void setCallRecordMs(Long callRecordMs) {
		this.callRecordMs = callRecordMs;
	}

	public String getCallDescription() {
		return callDescription;
	}

	public void setCallDescription(String callDescription) {
		this.callDescription = callDescription;
	}
	
	
	
	

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getDevSerial() {
		return devSerial;
	}

	public void setDevSerial(String devSerial) {
		this.devSerial = devSerial;
	}

	
	
	
	
	
	

	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
