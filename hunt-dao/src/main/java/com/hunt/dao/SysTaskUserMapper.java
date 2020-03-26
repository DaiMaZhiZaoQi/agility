package com.hunt.dao;

import org.apache.ibatis.annotations.Param;

import com.hunt.model.entity.SysTaskUser;

/**
 * 用户任务表
 * @author williambai
 *
 */
public interface SysTaskUserMapper {
	
	Long insert(SysTaskUser sysTaskUser);
	
	Boolean selectExistUserGroup(@Param("taskGroup")Long taskGroup,@Param("userId")Long userId);
	
	/**
	 * 查询任务用户
	 * @param taskGroup
	 * @param userId
	 * @return
	 */
	SysTaskUser selectTaskUser(@Param("taskGroup")Long taskGroup,@Param("userId")Long userId);
	
	/**
	 * 更新授权
	 * @param sysTaskUser
	 * @return
	 */
	Integer update(SysTaskUser sysTaskUser);
	
	/**
	 * 删除用户
	 * @param sysTaskUser
	 * @return
	 */
	Integer deleteTaskUser(@Param("taskUser")SysTaskUser sysTaskUser);
	
}
