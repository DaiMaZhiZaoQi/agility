package com.hunt.model.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 任务组
 * @author williambai
 *
 */
public class SysTaskGroup extends BaseEntity {

	private Long id;
	/**
	 * 任务组名称
	 */
	private String taskGroupName;
	
	/**
	 * 任务码
	 */
	private String taskCode;
	
	/**
	 * 发布人用户id
	 */
	private Long taskPubUserId;
	
	/**
	 * 发布人用户名
	 */
	private String taskPubUserName;
	
	private String taskColumn;
	
	/**
	 * 任务总数
	 */
	private Integer taskSize;
	
	/**
	 * 任务数量
	 */
	private Integer taskFinish;
	
	/**
	 * 任务组状态（0，未进行，1，进行中，2，任务组已关闭）
	 */
	private Byte status;
	
	/**
	 * 任务发布时间
	 */
	private Date createTime;
	/**
	 * 任务更新时间
	 */
	private Date updateTime;
	
	private BigDecimal taskRate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public String getTaskGroupName() {
		return taskGroupName;
	}

	public void setTaskGroupName(String taskGroupName) {
		this.taskGroupName = taskGroupName;
	}

	public Long getTaskPubUserId() {
		return taskPubUserId;
	}

	public void setTaskPubUserId(Long taskPubUserId) {
		this.taskPubUserId = taskPubUserId;
	}

	public String getTaskPubUserName() {
		return taskPubUserName;
	}

	public void setTaskPubUserName(String taskPubUserName) {
		this.taskPubUserName = taskPubUserName;
	}

	public String getTaskColumn() {
		return taskColumn;
	}

	public void setTaskColumn(String taskColumn) {
		this.taskColumn = taskColumn;
	}

	public Integer getTaskSize() {
		return taskSize;
	}

	public void setTaskSize(Integer taskSize) {
		this.taskSize = taskSize;
	}
	
	
	

	public Integer getTaskFinish() {
		return taskFinish;
	}

	public void setTaskFinish(Integer taskFinish) {
		this.taskFinish = taskFinish;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
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

	public BigDecimal getTaskRate() {
		return taskRate;
	}

	public void setTaskRate(BigDecimal taskRate) {
		this.taskRate = taskRate;
	}
	
	
	
	
	
}
