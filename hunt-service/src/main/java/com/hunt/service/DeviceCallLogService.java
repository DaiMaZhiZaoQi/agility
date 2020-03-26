package com.hunt.service;

import java.util.List;

import com.hunt.model.dto.PageDto;
import com.hunt.model.dto.PageInfo;
import com.hunt.model.dto.SysCallLogDeviceRecoDto;
import com.hunt.model.dto.SysDeviceCallLogAndRecordDto;
import com.hunt.model.entity.SysDeviceCallLog;
import com.hunt.model.entity.SysDeviceTotal;
import com.hunt.util.Result;

/**
 * 通话寄了Service
 * @author williambai
 *
 */
public interface DeviceCallLogService {
	
	/**
	 * 添加通话记录同时更新通话记录统计表
	 * @param sysDeviceCallLog
	 * @return
	 */
	public Result insert(SysDeviceCallLog sysDeviceCallLog);
	
	
	/**
	 * 分页查询设备通话数量
	 * @param deviceId
	 * @param sort
	 * @param order
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<SysDeviceCallLogAndRecordDto> select(Long deviceId,String sort,String order,Integer page,Integer rows);
	
	
	/**
	 * 查询该号码的通话记录
	 * @param pageDto
	 * @return
	 */
	public PageInfo selectByNum(PageDto pageDto);
	
	/**
	 * 查询通话记录
	 * @param orgId   部门id	或    个人id
	 * @param selectType	查询类型	0，查询个人1，查询部门
	 * @param sort	updete_time,更新时间	call_duration,通话时长		
	 * @param order 排序方式
	 * @param page	第几页
	 * @param rows	查询多少条
	 * @return
	 */
	public List<SysDeviceCallLogAndRecordDto> select(Long orgId,Integer selectType, String sort,String order,Integer page,Integer rows);
	
	
	
	/**
	 * 查询所有通话记录
	 * @param pageDto
	 * @return
	 */
	public PageInfo listCallRecord(PageDto pageDto);
	
	/**
	 * 更新通话记录
	 * @param sysDeviceCallLog
	 * @return
	 */
	public Long updateCallLog(SysDeviceCallLog sysDeviceCallLog);
	
	/**
	 * 更新通话记录 通话记录重传
	 * @param sysDeviceCallLog
	 * @return
	 */
	public Long updateAllCallLog(SysDeviceCallLog sysDeviceCallLog);
	
	/**
	 * 删除通话记录及录音
	 * @param sysDeviceCallLog
	 * @return
	 */
	public Long deleteCallLogAndRecord(SysDeviceCallLog sysDeviceCallLog);
	/**
	 * 根据机构id统计设备
	 * @param orgId  机构id 或者 userId
	 * @param optType 操作类型 0 机构查询
	 * @param sType 0 默认查询，1，查询 通话号码，2，查询设备号
	 * @param sTimeType 1,近三天  2，近 七天 3，近半月 4，近一月 5，近三月 6,近半年 7 今年内 	8 一年前之内
	 * @param sContent 查询内容
	 * @return
	 */
	public SysDeviceTotal selectDevTotalByRoleOrg(Long orgId,Integer optType,String sType,Long beginTime,Long endTime,String sContent);
	
	/**
	 * 查询通话统计
	 * @param pageDto
	 * @return
	 */
	public SysDeviceTotal selectDevTotalByPageDto(PageDto pageDto);
	
	
	
	
	/**
	 * 查询通话记录总数量
	 * @param orgId 机构id 或者 userId
	 * @param optType 操作类型0 机构id查询，1 用户id查询
	 * @return
	 */
	public Long selectTotalCount(Long orgId,Integer optType);
	
	/**
	 * 查询总数
	 * @param orgId
	 * @param optType	
	 * @param sType 0 默认查询，1，查询 通话号码，2，查询设备号
	 * @param sTimeType	  1,近三天  2，近 七天 3，近半月 4，近一月 5，近三月 6,近半年 7 今年内 	8 一年前之内
	 * @param sContent		查询内容
	 * @return
	 */
	@Deprecated
	public Long selectSearTotalCount(Long orgId,Integer optType,String sType,Long beginTime,Long endTime,String sContent,Integer callIsHaveRecord);
	
	/**
	 * 查询通话数量
	 * @param pageDto
	 * @return
	 */
	public Long selectTotalCountByPageDto(PageDto pageDto);
	

}
