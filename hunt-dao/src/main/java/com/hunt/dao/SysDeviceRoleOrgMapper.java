package com.hunt.dao;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.hunt.model.entity.SysDeviceRoleOrg;

/**
 * 设备角色机构mapper
 * @author williambai
 *
 */
public interface SysDeviceRoleOrgMapper {
			
	/**
	 * 新增
	 * @param sysDeviceRoleOrg
	 * @return
	 */
	public Long insert(SysDeviceRoleOrg sysDeviceRoleOrg);
	
	/**
	 * 修改
	 * @param sysDeviceRoleOrg
	 */
	public Long update(SysDeviceRoleOrg sysDeviceRoleOrg);
	
	/**
	 * 查询设备id通过角色id
	 * @param roleOrgId
	 * @return
	 */
	public List<Long> selectDeviceIdByRoleOrgId(@Param("roleOrgId") Long roleOrgId);
	
	
	
	/**
	 * 根据设备id查找SysDeviceRoleOrg  同一个设备被同一个用户只能添加一次
	 * 
	 * @param sysDeviceId	设备id
	 * @param sysUserId		用户id
	 * @return
	 */
	public List<SysDeviceRoleOrg> selectByDeviceIdAndUserId(@Param("sysDeviceId") Long sysDeviceId,@Param("sysUserId") Long sysUserId);
	 
	
	/**
	 * 通过设备id,查询DeviceRoleOrg
	 * @param sysDeviceId
	 * @return
	 */
	public List<SysDeviceRoleOrg> selectByDeviceId(@Param("sysDeviceId") Long sysDeviceId);
	
	/**
	 * 已绑定的设备
	 * @param sysDeviceId
	 * @return
	 */
	public List<SysDeviceRoleOrg> selectOnByDeviceId(@Param("sysDeviceId") Long sysDeviceId);
	
	/**
	 * 获取使用该设备的用户,一个设备只能由一个用户绑定
	 * @param deviceId
	 * @return set size>1 不合法，被多个用户使用，size<1,合法
	 */
	public Set<Long> selectUserIdByDeviceId(@Param("deviceId") Long deviceId);
	
	
	/**
	 * 通过用户id查找DeviceRoleOrg
	 * @param userId
	 * @return
	 */
	public List<SysDeviceRoleOrg> selectByUserId(@Param("userId") Long userId);
	
	public SysDeviceRoleOrg select(SysDeviceRoleOrg sysDeviceRoleOrg);
	
	public SysDeviceRoleOrg selectById(@Param("id") Long id);
	
	public List<SysDeviceRoleOrg> selectAll();
	
	
	
}
