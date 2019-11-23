package com.hunt.model.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 设备统计
 * @author williambai
 *
 */
public class SysDeviceTotal extends BaseEntity{
	
	/**record_total_id */
	private Long id;

	/**device_id*/
	private Long deviceId;
	
	/***call_log_count 通话记录数量*/
	private Long callLogCount;
	
	/***call_record_count 通话录音数量*/
	private Long callRecordCount;
	
	/***reco_audio_length:通话录音时长*/
	private Long recoAudioLength;
	
	/***call_duration:通话时长，接通时长*/
	private Long callDuration;
	
	/***reco_file_size: 通话录音文件大小*/
	private Long recoFileSize;
	
	/**call_already_accept_count：已接数量*/
	private Long callAlreadyAcceptCount;
	
	/**call_no_accept： 未接数量*/
	private Long callNoAccept;
	
	/**call_refuse_accept 拒接数量*/
	private Long callRefuseAccept;
	
	/**call_call_out 呼出数量*/
	private Long callCallOut;
	
	/**call_no_accept_leave:未接留言数量*/
	private Long callNoAcceptLeave;
	
	
	/**create_time: 创建时间*/
	private Date createTime;
	
	/**update_time：更新时间*/
	private Date updateTime;
	
	/**create_by：创建来源*/
	private Long createBy;
	
	/**update_by: 更新来源*/
	private Long updateBy;
	
	/**status: 状态：1，正常；2，删除*/
	private Integer status;
	
	/**is_final:是否可删除1，可删除；2，不可删除*/
	private Integer isFinal;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public Long getCallLogCount() {
		return callLogCount;
	}

	public void setCallLogCount(Long callLogCount) {
		this.callLogCount = callLogCount;
	}

	public Long getCallRecordCount() {
		return callRecordCount;
	}

	public void setCallRecordCount(Long callRecordCount) {
		this.callRecordCount = callRecordCount;
	}

	public Long getRecoAudioLength() {
		return recoAudioLength;
	}

	public void setRecoAudioLength(Long recoAudioLength) {
		this.recoAudioLength = recoAudioLength;
	}

	
	


	public Long getCallDuration() {
		return callDuration;
	}

	public void setCallDuration(Long callDuration) {
		this.callDuration = callDuration;
	}

	public Long getRecoFileSize() {
		return recoFileSize;
	}

	public void setRecoFileSize(Long recoFileSize) {
		this.recoFileSize = recoFileSize;
	}

	public Long getCallAlreadyAcceptCount() {
		return callAlreadyAcceptCount;
	}

	public void setCallAlreadyAcceptCount(Long callAlreadyAcceptCount) {
		this.callAlreadyAcceptCount = callAlreadyAcceptCount;
	}

	public Long getCallNoAccept() {
		return callNoAccept;
	}

	public void setCallNoAccept(Long callNoAccept) {
		this.callNoAccept = callNoAccept;
	}

	public Long getCallRefuseAccept() {
		return callRefuseAccept;
	}

	public void setCallRefuseAccept(Long callRefuseAccept) {
		this.callRefuseAccept = callRefuseAccept;
	}

	public Long getCallCallOut() {
		return callCallOut;
	}

	public void setCallCallOut(Long callCallOut) {
		this.callCallOut = callCallOut;
	}

	public Long getCallNoAcceptLeave() {
		return callNoAcceptLeave;
	}

	public void setCallNoAcceptLeave(Long callNoAcceptLeave) {
		this.callNoAcceptLeave = callNoAcceptLeave;
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
