package com.hunt.dao;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.hunt.model.entity.SysUser;
import com.hunt.model.entity.SysUserDto2;
import com.hunt.model.entity.SysUserInOrg;

/**
 * 用户所在机构
 * @author williambai
 *
 */
public interface SysUserInOrgMapper {

	/**
	 * 查询用户所在的机构
	 * @param userId
	 * @return
	 */
	SysUserInOrg selectByUserId(@Param("userId") Long userId);
	
	Long insert(SysUserInOrg sysUserInOrg);
	
	Long update(SysUserInOrg sysUserInOrg);
	
	/**
	 * 同步更新orgCode
	 * @param sysOrgCode
	 * @param oldOrgCode
	 * @return
	 */
	Long updateByOrgCode(@Param("sysOrgCode")String sysOrgCode,@Param("oldOrgCode") String oldOrgCode);
	
	/**
	 * 查询用户
	 * @param listOrgId 机构id集合
	 * @return
	 */
	public List<SysUserDto2> selectUserByListOrgId(@Param("listOrgId")List<Long> listOrgId);
	
	/**
	 * 查询用户id
	 * @param listOrgId
	 * @return
	 */
	public Set<Long> selectUserIdByListOrgId(@Param("listOrgId")List<Long> listOrgId);
	
	/**
	 * 查询用户id
	 * @param orgId 机构id
	 * @return
	 */
	public List<Long> selectUserIdByOrgId(@Param("sysOrgId") Long sysOrgId);
	
	
}
