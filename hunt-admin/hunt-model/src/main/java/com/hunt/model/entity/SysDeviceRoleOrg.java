package com.hunt.model.entity;

import java.util.Date;

public class SysDeviceRoleOrg extends BaseEntity{ 
		private Long id;
		/**sys_role_org_id: 角色机构id*/
//		private Long sysRoleOrgId;
		/**机构id*/
		private Long sysOrgId;
		/**sys_device_id:设备id*/
		private Long sysDeviceId;
		/**sys_user_id:用户id*/
		private Long sysUserId;
		
		private Integer isFinal;
		private Long rank;
		private Date createTime;
		private Date updateTime;
		private Long createBy;
		private Long updateBy;
		/**  状态 ，1：正常，2：删除 */
		private Integer status;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
//		public Long getSysRoleOrgId() {
//			return sysRoleOrgId;
//		}
//		public void setSysRoleOrgId(Long sysRoleOrgId) {
//			this.sysRoleOrgId = sysRoleOrgId;
//		}
		
		
		
		public Long getSysOrgId() {
			return sysOrgId;
		}
		public void setSysOrgId(Long sysOrgId) {
			this.sysOrgId = sysOrgId;
		}
		public Long getSysDeviceId() {
			return sysDeviceId;
		}
		public void setSysDeviceId(Long sysDeviceId) {
			this.sysDeviceId = sysDeviceId;
		}
		public Long getSysUserId() {
			return sysUserId;
		}
		public void setSysUserId(Long sysUserId) {
			this.sysUserId = sysUserId;
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
	
		
		
		
}
