package com.hunt.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hunt.dao.SysDeviceRecordMapper;
import com.hunt.dao.SysDeviceTotalMapper;
import com.hunt.model.entity.SysDeviceCallLog;
import com.hunt.model.entity.SysDeviceRecord;
import com.hunt.model.entity.SysDeviceTotal;
import com.hunt.service.DeviceRecordService;
@Service
@Transactional
public class DeviceRecordServiceImp implements DeviceRecordService{

	
	@Autowired
	private SysDeviceRecordMapper mSysDeviceRecordMapper;
	
	@Autowired
	private SysDeviceTotalMapper mSysDeviceTotalMapper;
	
	@Override
	public Long insert(SysDeviceRecord sysDeviceRecord,Long orgId) {
		mSysDeviceRecordMapper.insert(sysDeviceRecord);
		Long deviceId = sysDeviceRecord.getDeviceId();
		
		Long recoAudioLength = sysDeviceRecord.getRecoAudioLength();
		Long recoPhoneSize = sysDeviceRecord.getRecoPhoneSize();
//		SysDeviceTotal sysDeviceTotal = mSysDeviceTotalMapper.selectByDeviceId(deviceId);
		
//		SysDeviceTotal sysDeviceTotal = mSysDeviceTotalMapper.selectByDevIdCreateTime(deviceId);
		SysDeviceTotal sysDeviceTotal = mSysDeviceTotalMapper.selectByOrgIdCreateTime(orgId,deviceId);
		if(sysDeviceTotal==null) {					
			sysDeviceTotal=new SysDeviceTotal();
			sysDeviceTotal.setOrgId(orgId);
			sysDeviceTotal.setRecoAudioLength(recoAudioLength);
			sysDeviceTotal.setRecoFileSize(recoPhoneSize);
			sysDeviceTotal.setDeviceId(deviceId);
			mSysDeviceTotalMapper.insert(sysDeviceTotal);
		}else {
			Long totalAudioLength = sysDeviceTotal.getRecoAudioLength();
			Long totalRecoFileSize = sysDeviceTotal.getRecoFileSize(); 
			totalAudioLength=totalAudioLength+(recoAudioLength==null?0:recoAudioLength);
			totalRecoFileSize=totalRecoFileSize+(recoPhoneSize==null?0:recoPhoneSize);
			sysDeviceTotal.setRecoAudioLength(totalAudioLength);
			sysDeviceTotal.setRecoFileSize(totalRecoFileSize); 
		}
		
		mSysDeviceTotalMapper.update(sysDeviceTotal);
		
		return sysDeviceRecord.getId();
	}
	
	
	

	@Override
	public SysDeviceRecord selectDeviceRecordByCallLogId(Long callLogId) {
		return mSysDeviceRecordMapper.selectByCallLogId(callLogId);
	}




	@Override
	public Long update(SysDeviceRecord sysDeviceRecord,Long orgId) {
		//		修改录音表
		mSysDeviceRecordMapper.update(sysDeviceRecord);
		//		修改统计表
		Long deviceId = sysDeviceRecord.getDeviceId(); 
		Long recoAudioLength = sysDeviceRecord.getRecoAudioLength();
		Long recoPhoneSize = sysDeviceRecord.getRecoPhoneSize();
//		SysDeviceTotal sysDeviceTotal = mSysDeviceTotalMapper.selectByDeviceId(deviceId);
		SysDeviceTotal sysDeviceTotal = mSysDeviceTotalMapper.selectByOrgIdCreateTime(orgId,deviceId);
		if(sysDeviceTotal==null) {					
			sysDeviceTotal=new SysDeviceTotal();
			sysDeviceTotal.setOrgId(orgId);
			sysDeviceTotal.setRecoAudioLength(recoAudioLength);
			sysDeviceTotal.setRecoFileSize(recoPhoneSize);
			sysDeviceTotal.setDeviceId(deviceId);
			mSysDeviceTotalMapper.insert(sysDeviceTotal);
		}else {
			Long totalAudioLength = sysDeviceTotal.getRecoAudioLength();
			Long totalRecoFileSize = sysDeviceTotal.getRecoFileSize(); 
			totalAudioLength=totalAudioLength+(recoAudioLength==null?0:recoAudioLength);
			totalRecoFileSize=totalRecoFileSize+(recoPhoneSize==null?0:recoPhoneSize);
			sysDeviceTotal.setRecoAudioLength(totalAudioLength);
			sysDeviceTotal.setRecoFileSize(totalRecoFileSize); 
		}
		
		Long update = mSysDeviceTotalMapper.update(sysDeviceTotal); 
		return update;
	}




	@Override
	public Long updateDeviceTotalRecordByDeviceId(Long deviceId, SysDeviceRecord sysDeviceRecord) {
		SysDeviceTotal sysDeviceTotal = mSysDeviceTotalMapper.selectByDevIdByCreateTime(deviceId,sysDeviceRecord.getCreateTime());
		Long id = sysDeviceTotal.getId(); 
		Long recoAudioLength = sysDeviceRecord.getRecoAudioLength();			//	录音时长
		Long recoPhoneSize = sysDeviceRecord.getRecoPhoneSize(); 				//	录音文件大小
		
		Long sysDeviceTotalRecoAudioLength = sysDeviceTotal.getRecoAudioLength(); 			
		sysDeviceTotalRecoAudioLength=sysDeviceTotalRecoAudioLength>recoAudioLength?sysDeviceTotalRecoAudioLength-recoAudioLength:0;
		
		Long callRecordCount = sysDeviceTotal.getCallRecordCount();
		callRecordCount=callRecordCount-1;
		
		Long sysDeviceTotalRecoFileSize = sysDeviceTotal.getRecoFileSize();
		sysDeviceTotalRecoFileSize=sysDeviceTotalRecoFileSize>recoPhoneSize?sysDeviceTotalRecoFileSize-recoPhoneSize:0;
		
		SysDeviceTotal newSysDeviceTotal=new SysDeviceTotal();
		newSysDeviceTotal.setId(id);
		newSysDeviceTotal.setRecoAudioLength(sysDeviceTotalRecoAudioLength);
		newSysDeviceTotal.setCallRecordCount(callRecordCount);
		newSysDeviceTotal.setRecoFileSize(sysDeviceTotalRecoFileSize);
		Long update = mSysDeviceTotalMapper.update(newSysDeviceTotal);
		return update;
	}




	@Override
	public Long updateDeviceTotalCallLogByDeviceId(Long deviceId, SysDeviceCallLog sysDeviceCallLog) {
		Date createTime = sysDeviceCallLog.getCreateTime();
//		SysDeviceTotal sysDeviceTotal = mSysDeviceTotalMapper.selectByDeviceId(deviceId);
		SysDeviceTotal sysDeviceTotal = mSysDeviceTotalMapper.selectByDevIdByCreateTime(deviceId,sysDeviceCallLog.getCreateTime());
		if(sysDeviceTotal==null) return -1l;
		Long id = sysDeviceTotal.getId();   
		Long callLogCount = sysDeviceTotal.getCallLogCount(); 
		SysDeviceTotal newSysDeviceTotal = new SysDeviceTotal();
		newSysDeviceTotal.setId(id); 
		Integer callType = sysDeviceCallLog.getCallType(); 
		switch (callType) {
			case 0:	{	// 	未接
				Long callNoAccept = sysDeviceTotal.getCallNoAccept();
				callNoAccept=callNoAccept>0?callNoAccept-1:0;
				newSysDeviceTotal.setCallNoAccept(callNoAccept); 
				break;
			}
			case 1:	{	//	呼入
				Long callAlreadyAcceptCount = sysDeviceTotal.getCallAlreadyAcceptCount();
				callAlreadyAcceptCount=callAlreadyAcceptCount>0?callAlreadyAcceptCount-1:0;
				newSysDeviceTotal.setCallAlreadyAcceptCount(callAlreadyAcceptCount); 
				break;
			}
			case 2:	{	//	呼出
				Long callCallOut = sysDeviceTotal.getCallCallOut();
				callCallOut=callCallOut>0?callCallOut-1:0;
				newSysDeviceTotal.setCallCallOut(callCallOut);
				break;
			}
			case 3:{		//	拒接
				Long callRefuseAccept = sysDeviceTotal.getCallRefuseAccept();
				callRefuseAccept=callRefuseAccept>0?callRefuseAccept-1:0;
				newSysDeviceTotal.setCallRefuseAccept(callRefuseAccept);
				break;
			}
			case 4:{		//	未接留言
				Long callNoAcceptLeave = sysDeviceTotal.getCallNoAcceptLeave();
				 callNoAcceptLeave=callNoAcceptLeave>0?callNoAcceptLeave-1:0;
				 newSysDeviceTotal.setCallNoAcceptLeave(callNoAcceptLeave); 
				break;
			}
			default:
				break;
		}
		Long callDuration = sysDeviceCallLog.getCallDuration();		
		Long sysDeviceTotalCallDuration = sysDeviceTotal.getCallDuration();
		sysDeviceTotalCallDuration=sysDeviceTotalCallDuration>callDuration?sysDeviceTotalCallDuration-callDuration:0;
		newSysDeviceTotal.setCallDuration(sysDeviceTotalCallDuration);
		 
		Integer callIsHaveRecord = sysDeviceCallLog.getCallHasRecord();
		Long callRecordCount = sysDeviceTotal.getCallRecordCount();
		callRecordCount=(callIsHaveRecord!=null&&callIsHaveRecord==1)?(callRecordCount-1):callRecordCount;
		callLogCount=callLogCount>0?(callLogCount-1):0;
		newSysDeviceTotal.setCallLogCount(callLogCount); 
		newSysDeviceTotal.setCallRecordCount(callRecordCount);
		Long update = mSysDeviceTotalMapper.update(newSysDeviceTotal); 
		return update;
	}
	
	

	
	




	
	
	

}
