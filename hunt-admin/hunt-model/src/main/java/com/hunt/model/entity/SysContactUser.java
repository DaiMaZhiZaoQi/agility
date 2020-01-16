package com.hunt.model.entity;
/**
 * 通讯录用户实体类
 * @author williambai
 *
 */

import java.util.Date;

public class SysContactUser extends BaseEntity{
	
	private Long id;
	private Long sysContactId;
	private Long sysUserId;
	private Long sysOrgId;
	private String sysOrgCode;
	private Integer status;
	private Integer isAuth;
	private Date createTime;
	private Date updateTime;
	
	
	
	public SysContactUser() {
		super();
	}
	
	public SysContactUser(Long sysContactId, Long sysOrgId) {
		super();
		this.sysContactId = sysContactId;
		this.sysOrgId = sysOrgId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getSysContactId() {
		return sysContactId;
	}
	public void setSysContactId(Long sysContactId) {
		this.sysContactId = sysContactId;
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

	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Integer getIsAuth() {
		return isAuth;
	}

	public void setIsAuth(Integer isAuth) {
		this.isAuth = isAuth;
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
