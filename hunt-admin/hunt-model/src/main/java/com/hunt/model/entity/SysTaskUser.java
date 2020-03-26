package com.hunt.model.entity;
/**
 * 用户持有的任务
 * @author williambai
 *
 */

import java.util.Date;

public class SysTaskUser extends BaseEntity {

	private Long id;
	/**
	 * 用户id
	 */
	private Long sysUserId;
	/**
	 * 用户名称
	 */
	private String sysUserName;
	/**
	 * 用户的任务组
	 */
	private Long sysTaskGroup;
	/**
	 * 我的任务状态(0，未授权，1，已授权，2，任务已结束)
	 */
	private Byte status;
	
	private Date createTime;
	private Date updateTime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getSysUserId() {
		return sysUserId;
	}
	public void setSysUserId(Long sysUserId) {
		this.sysUserId = sysUserId;
	}
	public String getSysUserName() {
		return sysUserName;
	}
	public void setSysUserName(String sysUserName) {
		this.sysUserName = sysUserName;
	}
	public Long getSysTaskGroup() {
		return sysTaskGroup;
	}
	public void setSysTaskGroup(Long sysTaskGroup) {
		this.sysTaskGroup = sysTaskGroup;
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
	
}
