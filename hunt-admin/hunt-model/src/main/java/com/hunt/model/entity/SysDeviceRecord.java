package com.hunt.model.entity;

import java.util.Date;

/**
 * 设备的通话录音
 * @author williambai
 *
 */
public class SysDeviceRecord extends BaseEntity{

	/**reco_id: 通话录音id*/
	private Long id;
	
	/**call_log_id: 通话记录id*/
	private Long callLogId;
	
	private Long deviceId;
	
	/**reco_file_path 录音文件保存路径*/
	private String recoFilePath;
	
	/**录音文件名*/
	private String recoRealFileName;
	
	/**reco_absolute_file_path 通话录音绝对路径*/
	private String recoAbsoluteFilePath;
	
	/**reco_audio_length 录音时长 单位 秒*/
	private Long recoAudioLength;
	
	/**reco_phone_size: 录音文件大小*/
	private Long recoPhoneSize;
	
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
	
	/**description:录音描述*/
	private String description;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCallLogId() {
		return callLogId;
	}

	public void setCallLogId(Long callLogId) {
		this.callLogId = callLogId;
	}

	
	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public String getRecoFilePath() {
		return recoFilePath;
	}

	public void setRecoFilePath(String recoFilePath) {
		this.recoFilePath = recoFilePath;
	}
	
	
	

	public String getRecoRealFileName() {
		return recoRealFileName;
	}

	public void setRecoRealFileName(String recoRealFileName) {
		this.recoRealFileName = recoRealFileName;
	}

	public String getRecoAbsoluteFilePath() {
		return recoAbsoluteFilePath;
	}

	public void setRecoAbsoluteFilePath(String recoAbsoluteFilePath) {
		this.recoAbsoluteFilePath = recoAbsoluteFilePath;
	}

	public Long getRecoAudioLength() {
		return recoAudioLength;
	}

	public void setRecoAudioLength(Long recoAudioLength) {
		this.recoAudioLength = recoAudioLength;
	}

	public Long getRecoPhoneSize() {
		return recoPhoneSize;
	}

	public void setRecoPhoneSize(Long recoPhoneSize) {
		this.recoPhoneSize = recoPhoneSize;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
