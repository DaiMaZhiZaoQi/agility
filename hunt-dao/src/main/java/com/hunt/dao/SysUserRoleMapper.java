package com.hunt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hunt.model.entity.SysUserRole;

/**
 * 角色用户
 * @author williambai
 *
 */
public interface SysUserRoleMapper {
	
	public Long insert(SysUserRole sysUserRole);
	
	public Long deleteBy(SysUserRole sysUserRole);
	
	/**
	 * 查询用户角色
	 * @param sysUserId
	 * @return
	 */
	public List<SysUserRole> selectByUserId(@Param("sysUserId")Long sysUserId);
	
	/**
	 * 查询用户角色id
	 * @param sysUserId
	 * @return
	 */
	public List<Long> selectRoleIdByUserId(@Param("sysUserId")Long sysUserId);
	
	/**
	 * 查询用户的角色权限
	 * @param sysUserId
	 * @return
	 */
	public List<Long> selectPerIdByUserId(@Param("sysUserId")Long sysUserId);
	
	
	/**
	 * 查询用户的机构码
	 * @param sysUserId
	 * @return 权限机构码
	 */
	public List<String> selectPerCodeByUserId(@Param("sysUserId")Long sysUserId);
	  
	
//	public Integer deleteByUserIdRoleId(@Param("sysUserId")Long sysUserId,@Param("sysRoleId")Long sysRoleId);
	/**
	 * 修改用户角色
	 * @param sysUserRole
	 * @return
	 */
	public Integer updateByUserIdRoleId(@Param("sysUserId")Long sysUserId,@Param("sysRoleId")Long sysRoleId);
	
	
	
	
	
	
	
	
}
