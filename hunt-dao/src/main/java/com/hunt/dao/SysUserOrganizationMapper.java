package com.hunt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hunt.model.entity.SysUser;
import com.hunt.model.entity.SysUserOrganization;
import com.hunt.model.entity.SysUserRole;

/**
 * 用户机构
 * @author williambai
 *
 */
public interface SysUserOrganizationMapper {
	
	public Long insert(SysUserOrganization sysUserOrganization);

	/**
	 * 同步更新orgCode
	 * @param sysOrgCode
	 * @param oldOrgCode
	 * @return
	 */
	Long updateByOrgCode(@Param("sysOrgCode")String sysOrgCode,@Param("oldOrgCode") String oldOrgCode);
	
	public List<SysUserOrganization> selectByUserId(@Param("sysUserId")Long sysUserId);
	
	public List<SysUserOrganization> selectByUserIdOrderByOrgCode(@Param("sysUserId") Long sysUserId);
	
	List<SysUserOrganization> selectLikeOrgCode(@Param("sysUserId")Long sysUserId,@Param("sysOrgCode") String sysOrgCode);
	
	public List<SysUserOrganization> selectByOrgId(@Param("listSysOrgId")List<String> listSysOrgId,@Param("sysUserId")Long sysUserId);
	

	
	/**
	 * 查询结构id通过用户id
	 * @param sysUserId
	 * @return
	 */
	public List<Long> selectOrgIdByUserId(@Param("sysUserId") Long sysUserId);
	
	public Integer updateByUserIdOrgId(@Param("sysUserId") Long sysUserId,@Param("sysOrgId")Long sysOrgId);
	
	/**
	 * 查询最小的机构码
	 * @param sysUserId
	 * @return
	 */
	public String selectMinOrgCode(@Param("sysUserId") Long sysUserId);
	
	/**
	 * 查询机构码
	 * @param sysUserId  用户id
	 * @return
	 */
	public List<String> selectOrgCodeByUserId(@Param("sysUserId") Long sysUserId);
	
}
