package com.hunt.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.hunt.model.dto.SysCallLogDeviceRecoDto;
import com.hunt.model.entity.SysDeviceTotal;

/**
 * 所有设备统计表
 * @author williambai
 *
 */
public interface SysDeviceTotalMapper {

	/**
	 * 添加设备
	 * @param sysDeviceTotal
	 * @return
	 */
	public Long insert(SysDeviceTotal sysDeviceTotal);
	
	/**
	 * 更新
	 * @param sysDeviceTotal
	 */
	public Long update(SysDeviceTotal sysDeviceTotal);
	
	/**
	 * 查询
	 * @param sysDeviceTotal
	 * @return
	 */
	public SysDeviceTotal select(SysDeviceTotal sysDeviceTotal);
	
	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	public SysDeviceTotal selectById(@Param("id") Long id);
	
	/**
	 * 通过设备id查询
	 * @param deviceId
	 * @return
	 */
	public List<SysDeviceTotal> selectByDeviceId(@Param("deviceId") Long deviceId);
	
	
	
	/**
	 * 查询今天该设备的统计
	 * @param deviceId
	 * @return
	 */
	public SysDeviceTotal selectByDevIdCreateTime(@Param("deviceId") Long deviceId);
	
	/**
	 * 查询指定日期的通话记录
	 * @param deviceId
	 * @param createTime
	 * @return
	 */
	public SysDeviceTotal selectByDevIdByCreateTime(@Param("deviceId") Long deviceId,@Param("createTime") Date createTime);
	
	/**
	 * 查询所有
	 * @return
	 */
	public List<SysDeviceTotal> selectAll();
	
	
	/**
	 * 通过机构id查询通话记录
	 * @param set
	 * @return
	 */
	public List<SysCallLogDeviceRecoDto>  selectCallLogByOrg(@Param("set")Set<Long> set,@Param("sort") String sort,@Param("order") String order,@Param("page") Integer page,@Param("rows") Integer rows);
	
	/**
	 * 按设备号搜索
	 * @param set
	 * @param sort
	 * @param order
	 * @param page
	 * @param rows
	 * @param deviceSerial  
	 * @return
	 */
	public List<SysCallLogDeviceRecoDto>  selectSearDevserCallLogByOrg(@Param("set")Set<Long> set,@Param("sort") String sort,@Param("order") String order,@Param("page") Integer page,@Param("rows") Integer rows,@Param("deviceSerial") String deviceSerial,@Param("beginTime")Long beginTime,@Param("endTime")Long endTime,@Param("callIsHaveRecord")Integer callIsHaveRecord);
	
	/**
	 * 按联系人名称搜索
	 * @param set
	 * @param sort
	 * @param order
	 * @param page
	 * @param rows
	 * @param deviceSerial
	 * @param callDate
	 * @param sTimeType
	 * @return
	 */
	public List<SysCallLogDeviceRecoDto>  selectSearCallNameCallLogByOrg(@Param("set")Set<Long> set,@Param("sort") String sort,@Param("order") String order,@Param("page") Integer page,@Param("rows") Integer rows,@Param("callName") String callName,@Param("beginTime")Long beginTime,@Param("endTime")Long endTime,@Param("callIsHaveRecord")Integer callIsHaveRecord);
	
	/**
	 * 按通话号码搜素
	 * @param set
	 * @param sort
	 * @param order
	 * @param page
	 * @param rows
	 * @param callNumber  通话号码
	 * @return
	 */
	public List<SysCallLogDeviceRecoDto>  selectSearCallNumCallLogByOrg(@Param("set")Set<Long> set,@Param("sort") String sort,@Param("order") String order,@Param("page") Integer page,@Param("rows") Integer rows,@Param("callNumber") String callNumber,@Param("beginTime")Long beginTime,@Param("endTime")Long endTime,@Param("callIsHaveRecord")Integer callIsHaveRecord);
	
	/**
	 *  按时间段查询
	 * @param set
	 * @param sort
	 * @param order
	 * @param page
	 * @param rows
	 * @param callDate  1,近三天  2，近 七天 3，近半月 4，近一月 5，近三月 6,近半年 7 今年内 8 一年前
	 * @param timeVarType day month
	 * @return
	 */
	public List<SysCallLogDeviceRecoDto>  selectSearCallDateCallLogByOrg(@Param("set")Set<Long> set,@Param("sort") String sort,@Param("order") String order,@Param("page") Integer page,@Param("rows") Integer rows,@Param("beginTime")Long beginTime,@Param("endTime")Long endTime,@Param("callIsHaveRecord")Integer callIsHaveRecord);
	
	
	
	/**
	 * 查看通话数量
	 * @param set 机构id集合
	 * @return
	 */
	public Long  selectCallCount(@Param("set")Set<Long> set);
	
	/**
	 * 查询通话总数
	 * @param set
	 * @param callNumber
	 * @param deviceSerial
	 * @param sTimeType	 	1,近三天 2，近 七天 3，近半月 4，近一月 5，近三月 6,近半年 7 今年内 8 一年前之内
	 * @param callDate		与sTimeType对应的时间间隔
	 * @return
	 */
	public Long  selectSearCallCount(@Param("set")Set<Long> set,@Param("callNumber") String callNumber,@Param("callName")String callName,@Param("deviceSerial") String deviceSerial,@Param("beginTime")Long beginTime,@Param("endTime")Long endTime,@Param("callIsHaveRecord")Integer callIsHaveRecord);
	
	/**
	 * 查看通话数量
	 * @param userId  用户名
	 * @return
	 */
	public Long  selectCountByUserId(@Param("userId") Long userId);
	
	/**
	 * 搜索通话记录数量
	 * @param userId
	 * @param callNumber
	 * @param deviceSerial
	 * @param sTimeType
	 * @param callDate
	 * @return
	 */
	public Long  selectSearCountByUserId(@Param("userId") Long userId,@Param("callNumber") String callNumber,@Param("deviceSerial") String deviceSerial,@Param("callName")String callName,@Param("beginTime")Long beginTime,@Param("endTime")Long endTime,@Param("callIsHaveRecord")Integer callIsHaveRecord);
	
	/**
	 * 查询通话记录通过用户名
	 * @param userId
	 * @param sort
	 * @param order
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<SysCallLogDeviceRecoDto>  selectCallLogByUser(@Param("userId")Long userId, @Param("sort") String sort,@Param("order") String order,@Param("page") Integer page,@Param("rows") Integer rows);
	
	/**
	 * 搜索用户通话记录
	 * @param userId
	 * @param sort
	 * @param order
	 * @param page
	 * @param rows
	 * @param callNumber	电话号码
	 * @param deviceSerial	序列号
	 * @param sTimeType		1,近三天 2，近 七天 3，近半月 4，近一月 5，近三月 6,近半年 7 今年内 8 一年前之内
	 * @param callDate		与时间类型对应的时间码
	 * @return
	 */
	public List<SysCallLogDeviceRecoDto>  selectSearCallLogByUser(@Param("userId")Long userId, @Param("sort") String sort,@Param("order") String order,@Param("page") Integer page,@Param("rows") Integer rows,@Param("callNumber") String callNumber,@Param("deviceSerial") String deviceSerial,@Param("callName")String callName,@Param("beginTime")Long beginTime,@Param("endTime")Long endTime,@Param("callIsHaveRecord") Integer callIsHaveRecord);
	
	/**
	 * 查询用户集合id
	 * @param userId
	 * @param sort
	 * @param order
	 * @param page
	 * @param rows
	 * @param callNumber
	 * @param deviceSerial
	 * @param callName
	 * @param beginTime
	 * @param endTime
	 * @param callIsHaveRecord
	 * @return
	 */
	public List<SysCallLogDeviceRecoDto>  selectSearCallLogByUserSet(@Param("userSetId")Set<Long> userId, @Param("sort") String sort,@Param("order") String order,@Param("page") Integer page,@Param("rows") Integer rows,@Param("callNumber") String callNumber,@Param("deviceSerial") String deviceSerial,@Param("callName")String callName,@Param("beginTime")Long beginTime,@Param("endTime")Long endTime,@Param("callIsHaveRecord") Integer callIsHaveRecord);
	
	/**
	 * 查询该角色所存在的机构
	 * @param roleOrgId
	 * @return
	 */
	public SysCallLogDeviceRecoDto selectRogIdByRoleOrgId(@Param("roleOrgId") Long roleOrgId);
	
	/**
	 * 查询设备统计
	 * @param set
	 * @return
	 */
	public List<SysDeviceTotal>  selectTotalByOrg(@Param("set")Set<Long> set);
	
	/**
	 * 搜索统计
	 * @param set
	 * @param callNumber	电话号码 
	 * @param deviceSerial	序列号
	 * @param sTimeType		1,近三天 2，近 七天 3，近半月 4，近一月 5，近三月 6,近半年 7 今年内 8 一年前之内
	 * @param callDate		与时间类型对应的时间码
	 * @return
	 */
	public List<SysDeviceTotal>  selectSearTotalByOrg(@Param("set")Set<Long> set,@Param("callNumber") String callNumber,@Param("callName")String callName,@Param("deviceSerial") String deviceSerial,@Param("beginTime")Long beginTime,@Param("endTime")Long endTime);
	
	
	/**
	 * 查询该用户的设备统计
	 * @param set
	 * @return
	 */
	public List<SysDeviceTotal>  selectTotalByUserId(@Param("userId")Long set);
	
	/**
	 * 
	 * @param set
	 * @param callNumber
	 *  @param deviceSerial	序列号
	 * @param sTimeType		1,近三天 2，近 七天 3，近半月 4，近一月 5，近三月 6,近半年 7 今年内 8 一年前之内
	 * @param callDate		与时间类型对应的时间码
	 * @return
	 */
	public List<SysDeviceTotal>  selectSerTotalByUserId(@Param("userId")Long set,@Param("callNumber") String callNumber,@Param("callName")String callName,@Param("deviceSerial") String deviceSerial,@Param("beginTime")Long beginTime,@Param("endTime")Long endTime);
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
