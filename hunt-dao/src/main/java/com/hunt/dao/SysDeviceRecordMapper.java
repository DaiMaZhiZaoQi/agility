package com.hunt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hunt.model.entity.SysDeviceRecord;

/**
 * 通话录音
 * @author williambai
 *
 */
public interface SysDeviceRecordMapper {

	
	/**
	 * 添加
	 * @param sysDeviceRecord
	 * @return
	 */
	public Long insert(SysDeviceRecord sysDeviceRecord);
	
	/**
	 * 更新
	 * @param sysDeviceRecord
	 */
	public void update(SysDeviceRecord sysDeviceRecord);
	
	
	
	/**
	 * 查询
	 * @param sysDeviceRecord
	 * @return
	 */
	public SysDeviceRecord select(SysDeviceRecord sysDeviceRecord);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public SysDeviceRecord selectById(@Param("id") Long id);
	
	/**
	 * 通过通话记录id查找通话语音，一条通话记录对应一条语音信息
	 * @param callLogId
	 * @return
	 */
	public SysDeviceRecord selectByCallLogId(@Param("callLogId") Long callLogId);
	/**
	 * 查询所有
	 * @return
	 */
	public List<SysDeviceRecord> selectAll();
	
	
	
	
	
}
