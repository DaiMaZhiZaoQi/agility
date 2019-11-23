package com.hunt.service;

import com.hunt.model.dto.PageDto;
import com.hunt.model.dto.PageInfo;
import com.hunt.model.entity.SysDeviceRoleOrg;

/**
 * 设备管理
 * @author williambai
 *存在问题是本来是这个部门的应该绑定的设备，被其他部门操作绑定了
 */
public interface DeviceManageService {
	
	/**
	 * 查询没被绑定的设备
	 * @return
	 */
	PageInfo selectUnRegisDevice(PageDto pageDto);
	
	/**
	 * 查询未绑定的设备数量
	 * @return
	 */
	Long selectUnRegisCount();
	/**
	 * 查询已绑定的设备数量
	 * @return
	 */
	Long selectRegisCount(Long orgId);
	/**
	 * 查询已绑定的设备
	 * @param pageDto
	 * @param orgId 机构id
	 * @return
	 */
	PageInfo selectRegisDevice(PageDto pageDto,Long orgId);
	
	/**
	 * 根据序列号查找 SysDeviceRoleOrg
	 * @param deviceSerial 序列号
	 * @return
	 */
	SysDeviceRoleOrg selectBySerial(String deviceSerial);
}
