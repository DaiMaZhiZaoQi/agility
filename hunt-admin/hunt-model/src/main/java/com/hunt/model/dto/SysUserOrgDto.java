package com.hunt.model.dto;

import com.hunt.model.entity.BaseEntity;
import com.hunt.model.entity.SysUser;
/**
 * 用户所在机构
 * @author williambai
 *
 */
public class SysUserOrgDto extends BaseEntity{
	private Long orgId;
	private String orgName;
    // id :
    private Long sysUserId;

    private Integer isAuth;
    // login_name :登陆名
    private String loginName;

    // zh_name :中文名
    private String zhName;

    // en_name :英文名
    private String enName;
    
  
    
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
	
	public Long getSysUserId() {
		return sysUserId;
	}
	public void setSysUserId(Long sysUserId) {
		this.sysUserId = sysUserId;
	}
	public Integer getIsAuth() {
		return isAuth;
	}
	public void setIsAuth(Integer isAuth) {
		this.isAuth = isAuth;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getZhName() {
		return zhName;
	}
	public void setZhName(String zhName) {
		this.zhName = zhName;
	}
	public String getEnName() {
		return enName;
	}
	public void setEnName(String enName) {
		this.enName = enName;
	}

	
	
	
	
}
