package com.hunt.service;

import java.util.List;

import com.hunt.model.dto.PageInfo;
import com.hunt.model.entity.SysDevice;
import com.hunt.model.entity.SysDeviceRoleOrg;
import com.hunt.model.entity.SysUser;
import com.hunt.model.entity.SysUserInOrg;
import com.hunt.util.Result;
import com.hunt.util.StringUtil;
/**
 * 设备管理服务
 * @author williambai
 *
 */
public interface SystemDeviceService {
	
	
	/**
	 * 判断设备是否存在
	 * @param deviceSerial  序列号
	 * @return
	 */
	public boolean isExistDevice(String deviceSerial);
	
	
	/**
	 * 添加设备到角色机构表
	 * @param sysDeviceRoleOrg
	 * @return
	 */
	public Long insertDeviceRoleOrg(SysDeviceRoleOrg sysDeviceRoleOrg);
	
	
	/**
	 * 更新设备角色机构表
	 * @param sysDeviceRoleOrg
	 * @return
	 */
	public Long updateDeviceRoleOrg(SysDeviceRoleOrg sysDeviceRoleOrg);
	
	
	
	
	
	/**
	 * 查询该管理员所在的机构，下级部门的id，才能有权限查询
	 * @param page
	 * @param rows
	 * @return
	 */
	public PageInfo selectPage(int page,int rows,String sort,String order,String deviceName,String deviceSerial);

	/**
	 * 添加设备到设备表，有sysUserId,则添加到sys_device_role_org表中
	 * @param sysDevice
	 * @param sysUserId
	 * @param sysUserInOrg
	 * @return
	 */
	Long insertDevice(SysDevice sysDevice, Long sysUserId, SysUserInOrg sysUserInOrg);
	
	/**
	 * 修改设备 找到 这条记录后然后修改这条参数
	 * 该方法使用场景，先添加用户，
	 * @param sysDeviceRoleOrgId	设备角色机构表中的id主键
	 * @param sysRoleOrgId			角色机构id
	 * @param sysDevice				选中的当前设备
	 * @param sysUserId				要修改的用户id,选中的用户id
	 * @return -1					存在重复序列号
	 */
	public Long updateDevice(Long sysOrgId,SysDevice sysDevice, Long sysUserId);
	
	/**
	 * 自动绑定设备
	 * @param deviceSerial
	 * @param deviceName
	 * @param userName
	 * @param password
	 * @return
	 */
	public Result autoBind(String deviceSerial,String deviceName,SysUser sysUser,SysUserInOrg sysUserInOrg);
	/**
	 * 解除设备绑定
	 * @param sysOrgId
	 * @param sysDevice
	 * @param sysUserId
	 * @return
	 */
	public Result unBindDevice(Long sysOrgId,SysDevice sysDevice, Long sysUserId);
	
	
	

}
