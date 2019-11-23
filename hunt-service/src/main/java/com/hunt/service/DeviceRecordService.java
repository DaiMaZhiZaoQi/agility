package com.hunt.service;

import java.util.List;

import com.hunt.model.entity.SysDeviceCallLog;
import com.hunt.model.entity.SysDeviceRecord;

public interface DeviceRecordService {
	
	/**
	 * 上传录音
	 * @param sysDeviceRecord
	 * @return
	 */
	public Long insert(SysDeviceRecord sysDeviceRecord);
	
	/**
	 * 
	 * @param sysDeviceRecord
	 * @return
	 */
	public Long update(SysDeviceRecord sysDeviceRecord);
	
	/**
	 * 通过通话记录id查找录音
	 * @param callLogId
	 * @return
	 */
	public SysDeviceRecord selectDeviceRecordByCallLogId(Long callLogId);
	
	
	/**
	 * 减通话录音时长  修改设备统计表，当录音文件被删除，通话记录被删除，恢复都可以调用该方法修改设备统计表
	 * @param deviceId
	 * @return
	 */
	public Long updateDeviceTotalRecordByDeviceId(Long deviceId,SysDeviceRecord sysDeviceRecord);
	
	/**
	 * 减通话记录，在删除通话记录时用到
	 * @param deviceId
	 * @param sysDeviceCallLog
	 * @return
	 */
	public Long updateDeviceTotalCallLogByDeviceId(Long deviceId,SysDeviceCallLog sysDeviceCallLog);
	
	
}
