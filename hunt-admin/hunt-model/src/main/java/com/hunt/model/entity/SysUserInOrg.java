package com.hunt.model.entity;

import java.util.Date;

public class SysUserInOrg extends BaseEntity{
	
	private Long id;
	private Long sysUserId;
	private Long sysOrgId;
	private String sysOrgCode;
	private Integer rank;
	private Date createTime;
	private Date updateTime;
	private Long createBy;
	private Long updateBy;
	private Long status;
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
	public Long getSysOrgId() {
		return sysOrgId;
	}
	public void setSysOrgId(Long sysOrgId) {
		this.sysOrgId = sysOrgId;
	}
	public String getSysOrgCode() {
		return sysOrgCode;
	}
	public void setSysOrgCode(String sysOrgCode) {
		this.sysOrgCode = sysOrgCode;
	}
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
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
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
	

}
