package com.hunt.dao;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.hunt.model.dto.PageDto;
import com.hunt.model.dto.SysDeviceOrgDto;
import com.hunt.model.entity.SysDevice;

/**
 * 设备dao
 * @author williambai
 *
 */
public interface SysDeviceMapper {

	/**
	 * 添加
	 * @param sysDevice
	 * @return
	 */
	public Long insert(SysDevice sysDevice);
	
	/**
	 * 更新
	 * @param sysDevice
	 */
	public void update(SysDevice sysDevice);
	
	/**
	 * 更新设备时间，用于心跳上报
	 * @param deviceSerial
	 * @return
	 */
	public Long updateDeviceTimeById(@Param("deviceSerial")String deviceSerial,@Param("deviceMsg")String deviceMsg);
	
	/**
	 * 查询
	 * @param sysDevice
	 * @return
	 */
	public SysDevice select(SysDevice sysDevice);
	
	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	public SysDevice selectById(@Param("id") Long id);
	
	/**
	 * 查询所有
	 * @return
	 */
	public List<SysDevice> selectAll(); 
	
	/**
	 * 查询所有设备，排序
	 * @param deviceName
	 * @return
	 */
	public List<SysDevice> selectAll(@Param("sort") String sort,@Param("order") String order, @Param("deviceName") String deviceName,@Param("deviceSerial")String deviceSerial);
	 

	/**
	 * 通过序列号查询设备数量，sys_device 不允许存在序列号相同的设备
	 * @param deviceSerial
	 * @return
	 */
	public SysDevice selectByDeviceSerial(@Param("deviceSerial") String deviceSerial);
	
	/**
	 * 查询已绑定的设备
	 * @param deviceSerial 序列号
	 * @return
	 */
	public SysDevice selectBindByDeviceSerial(@Param("deviceSerial") String deviceSerial);
	
	/**
	 * 查询未绑定的设备
	 * @return
	 */
	public List<SysDevice> selectUnRegisDevice(@Param("pageDto")PageDto pageDto);
	
	
	public Long selectUnRegisCount();
	
	/**
	 * 查询该机构下的所有设备
	 * 
	 * @param set	机构id
	 * @return
	 */
	public Long selectRegisCount(@Param("set")Set<Long> set);
	
	public List<SysDeviceOrgDto> selectDevRegis(@Param("set")Set<Long> set,@Param("pageDto")PageDto pageDto);
//	public List<SysDeviceOrgDto> selectDevRegis(PageDto pageDto);
	
	/**
	 * 查询数量
	 * @return
	 */
	public int selectCounts();
	
	
	
	/**
	 * 判断该序列号的设备是否存在
	 * @param deviceSerial  序列号
	 * @return
	 */
	public boolean isExistDevice(@Param("deviceSerial") String deviceSerial);
	
	
	
	
	
	
	
	
	
	
}
