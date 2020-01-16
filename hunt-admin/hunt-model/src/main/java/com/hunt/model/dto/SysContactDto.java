package com.hunt.model.dto;

import java.util.Date;
import java.util.List;

import com.hunt.model.entity.BaseEntity;

/**
 * 企业通讯录
 * @author williambai
 *
 */


public class SysContactDto extends BaseEntity{

	private Long id;
	private String contactCode;
	private String orgName;
	private String contactName;
	private String oriFileName;
	private String contactSychPassword;
	private String absolutePath;
	private String authName;
	private Long createBy;
	private Long updateBy;
	private Date createTime;
	private Date updateTime;
	private Integer status;
	private Integer isFinal;
	private List<Long> listOrgId;
	private List<Long> listUserId;
	
	public SysContactDto() {
		super();
	}
	
	public SysContactDto(Long id,String contactCode,String contactName,String oriFileName, String contactSychPassword, String absolutePath, Long createBy,
			Long updateBy) {
		super();
		this.id=id;
		this.contactCode = contactCode;
		this.contactName=contactName;
		this.oriFileName=oriFileName;
		this.contactSychPassword = contactSychPassword;
		this.absolutePath = absolutePath;
		this.createBy = createBy;
		this.updateBy = updateBy;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getContactCode() {
		return contactCode;
	}
	public void setContactCode(String contactCode) {
		this.contactCode = contactCode;
	}
	
	
	
	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	
	

	public String getOriFileName() {
		return oriFileName;
	}

	public void setOriFileName(String oriFileName) {
		this.oriFileName = oriFileName;
	}

	public String getContactSychPassword() {
		return contactSychPassword;
	}
	public void setContactSychPassword(String contactSychPassword) {
		this.contactSychPassword = contactSychPassword;
	}
	public String getAbsolutePath() {
		return absolutePath;
	}
	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}
	

	public String getAuthName() {
		return authName;
	}

	public void setAuthName(String authName) {
		this.authName = authName;
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

	public List<Long> getListOrgId() {
		return listOrgId;
	}

	public void setListOrgId(List<Long> listOrgId) {
		this.listOrgId = listOrgId;
	}

	public List<Long> getListUserId() {
		return listUserId;
	}

	public void setListUserId(List<Long> listUserId) {
		this.listUserId = listUserId;
	}
	
	
	
	
}
