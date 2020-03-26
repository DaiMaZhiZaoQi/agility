package com.hunt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hunt.model.dto.PageDto;
import com.hunt.model.dto.SysCallLogDeviceRecoDto;
import com.hunt.model.entity.SysDeviceCallLog;

/**
 * 通话记录
 * @author williambai
 *
 */
public interface SysDeviceCallLogMapper {

	/**
	 * 添加
	 * @param sysDeviceCallLog
	 * @return
	 */
	public Long insert(SysDeviceCallLog sysDeviceCallLog);
	
	/**
	 * 更新
	 * @param sysDeviceCallLog
	 */
	public void update(SysDeviceCallLog sysDeviceCallLog);
	

	/**
	 * 同步更新orgCode
	 * @param sysOrgCode
	 * @param oldOrgCode
	 * @return
	 */
	Long updateByOrgCode(@Param("sysOrgCode")String sysOrgCode,@Param("oldOrgCode") String oldOrgCode);
	
	/**
	 * 通话记录，删除或恢复正常
	 * @param sysDeviceCallLog
	 * @return
	 */
	public Long deleteOrNoDeleteDeviceCallLog(SysDeviceCallLog sysDeviceCallLog);
	/**
	 * 查询某一条
	 * @param sysDeviceCallLog
	 * @return
	 */
	public SysDeviceCallLog select(SysDeviceCallLog sysDeviceCallLog);
	
	/**
	 * 查询该设备的通话记录数量
	 * @param deviceId
	 * @return
	 */
	public Long selectCountsByDeviceId(@Param("deviceId") Long deviceId);
	
	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	public SysDeviceCallLog selectById(@Param("id") Long id);
	
	/**
	 * 根据设备id查通话记录
	 * @param deviceId
	 * @return
	 */
	public List<SysDeviceCallLog> selectByDeviceId(@Param("deviceId") Long deviceId,@Param("sort") String sort,@Param("order") String order,@Param("page") Integer page,@Param("rows") Integer rows);
	
	/**
	 * 根据号码查询通话记录
	 * @param pageDto
	 * @return
	 */
	public List<SysCallLogDeviceRecoDto> selectByPageDto(@Param("pageDto")PageDto pageDto);
	
	/**
	 * 查询通话记录数量
	 * @param pageDto
	 * @return
	 */
	public Integer selectByPageDtoCount(@Param("pageDto")PageDto pageDto);
	
	/**
	 * 查询通话记录
	 * @param sysUserId	用户id
	 * @param sort		排序字段
	 * @param order		排序方式
	 * @param page		页码
	 * @param rows		查询行数
	 * @return 
	 */
	public List<SysDeviceCallLog> selectByUserId(@Param("sysUserId") Long sysUserId,@Param("sort") String sort,@Param("order") String order,@Param("page") Integer page,@Param("rows") Integer rows);
	
	/**
	 * 查询通话记录
	 * @param sysUserId
	 * @param sort
	 * @param order
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<SysDeviceCallLog> selectByRoleOrgId(@Param("sysRoleOrgId") Long sysUserId,@Param("sort") String sort,@Param("order") String order,@Param("page") Integer page,@Param("rows") Integer rows);
	
	
	
	public List<SysDeviceCallLog> selectTotalByDeviceId(@Param("deviceId") Long deviceId);
	
	/**
	 * 查询所有SysDeviceCallLog
	 * @return
	 */
	public List<SysDeviceCallLog> selectAll();
	
	/**
	 * 
	 * @param deviceId	设备id
	 * @param callEndTime 客户端通话时间
	 * @return 
	 */
	public SysDeviceCallLog selectByCallDate(@Param("deviceId") Long deviceId,@Param("callDate") Long callDate);
	
	/**
	 * 通过机构id查找通话记录
	 * @param orgId
	 * @return
	 */
	public List<SysDeviceCallLog> selectDeviceCallLogByOrgId(@Param("orgId") Long orgId);
	
	
	
	
	
}
