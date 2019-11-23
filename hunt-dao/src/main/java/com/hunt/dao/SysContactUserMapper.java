package com.hunt.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hunt.model.dto.SysUserOrgDto;
import com.hunt.model.entity.SysContactUser;

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
	 * 查询已授权用户
	 * @param contactId
	 * @return
	 */
	List<SysUserOrgDto> selectUserOrgAuthByContactId(@Param("contactId") Long contactId);
	/**
	 * 确认上传，激活 
	 * @param sysContactUser
	 * @return
	 */
	Long update(SysContactUser sysContactUser);
	
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
