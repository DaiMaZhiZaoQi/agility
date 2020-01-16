package com.hunt.model.dto;
/**
 * 机构权限
 * @author williambai
 *
 */

import java.util.Date;
import java.util.List;

import com.hunt.model.entity.BaseEntity;
import com.hunt.model.entity.SysUserOrganization;

public class SysPermissionOrgDto extends BaseEntity{

	private Long id;
	private String name;
	private String description;
	private String code;
	private Long sysOrgId;
	private Integer isFinal;
	private Long rank;
	private Date createTime;
	private Date updateTime;
	private String permGroupName;
	private Long perGroupId;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	
	
	public Long getSysOrgId() {
		return sysOrgId;
	}
	public void setSysOrgId(Long sysOrgId) {
		this.sysOrgId = sysOrgId;
	}
	public Integer getIsFinal() {
		return isFinal;
	}
	public void setIsFinal(Integer isFinal) {
		this.isFinal = isFinal;
	}
	public Long getRank() {
		return rank;
	}
	public void setRank(Long rank) {
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
	public String getPermGroupName() {
		return permGroupName;
	}
	public void setPermGroupName(String permGroupName) {
		this.permGroupName = permGroupName;
	}
	public Long getPerGroupId() {
		return perGroupId;
	}
	public void setPerGroupId(Long perGroupId) {
		this.perGroupId = perGroupId;
	}

	
	
	
	
	
}
