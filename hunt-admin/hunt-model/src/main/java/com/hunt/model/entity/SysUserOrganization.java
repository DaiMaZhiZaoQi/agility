package com.hunt.model.entity;
/**
 * 用户机构表
 * @author williambai
 *
 */

import java.util.Date;

public class SysUserOrganization extends BaseEntity{

	private Long id;
	private Long sysUserId;
	private Long sysOrgId;
	private Long sysPermissionOrgId;
	private String sysOrgCode;
	private Integer rank;
	private Date createTime;
	private Date updateTime;
	private Long createBy;
	private Long updateBy;
	private Integer status;
	private Integer isFinal;
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
	
	
	
	public Long getSysPermissionOrgId() {
		return sysPermissionOrgId;
	}
	public void setSysPermissionOrgId(Long sysPermissionOrgId) {
		this.sysPermissionOrgId = sysPermissionOrgId;
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
