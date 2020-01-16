package com.hunt.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hunt.model.dto.SysUserOrgDto;
import com.hunt.model.entity.SysContactUser;
import com.hunt.model.entity.SysOrganization;

/**
 * sys_contact_user 表操作
 * @author williambai
 *
 */
public interface SysContactUserMapper {

	Long insert(SysContactUser sysContactUser);
	
	List<SysContactUser> selectByUserId(@Param("sysUserId")Long sysUserId);
	
	
	List<Long> selectContactIdByUser(@Param("sysUserId") Long sysUserId);
	
	/**
	 * 查询该机构授权的 sys_contact_user
	 * @param sysOrgId
	 * @return
	 */
	List<SysContactUser> selectByOrgId(@Param("sysOrgId") Long sysOrgId); 
	
	/**
	 * 查询contactId
	 * @param sysOrgCode
	 * @return
	 */
	List<Long> selectContactLikeByOrgCode(@Param("sysOrgCode") String sysOrgCode);
	
	/**
	 * 查询contactId
	 * @param listOrgCode
	 * @return
	 */
	List<Long> selectContactByOrgCode(@Param("listOrgCode") List<String> listOrgCode);
	
	/**
	 * 查询已授权用户
	 * @param contactId
	 * @return
	 */
	List<SysUserOrgDto> selectUserOrgAuthByContactId(@Param("contactId") Long contactId);
	
	/**
	 * 查询已授权的机构id
	 * @param contactId
	 * @return
	 */
	List<Long> selectOrgIdByContactId(@Param("contactId") Long contactId);
	
	/**
	 * 查找已授权的用户
	 * @param contactId
	 * @return
	 */
	List<Long> selectUserIdByContactId(@Param("contactId") Long contactId);
	
	
	
	
	
	/**
	 * 确认上传，激活 
	 * @param sysContactUser
	 * @return
	 */
	Long update(SysContactUser sysContactUser);
	
	/**
	 * 同步更新orgCode
	 * @param sysOrgCode
	 * @param oldOrgCode
	 * @return
	 */
	Long updateByOrgCode(@Param("sysOrgCode")String sysOrgCode,@Param("oldOrgCode") String oldOrgCode);
	
	
	
	SysContactUser select(@Param("sysContactUser")SysContactUser sysContactUser);
	
	/**
	 * 查询未授权
	 * @param sysContactUser
	 * @param status 
	 * @return
	 */
	SysContactUser selectUnAuth(SysContactUser sysContactUser);
	
	/**
	 * 更新通讯录绑定状态
	 * @param sysContactUser
	 * @return
	 */
	Long updateStatusByUserIdContactId(@Param("sysContactUser")SysContactUser sysContactUser,@Param("updateStatus")Integer updateStatus);
} 
