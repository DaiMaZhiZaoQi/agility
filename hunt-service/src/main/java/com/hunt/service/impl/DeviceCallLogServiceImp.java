package com.hunt.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import com.hunt.dao.SysDeviceCallLogMapper;
import com.hunt.dao.SysDeviceMapper;
import com.hunt.dao.SysDeviceRecordMapper;
import com.hunt.dao.SysDeviceTotalMapper;
import com.hunt.model.dto.PageDto;
import com.hunt.model.dto.PageInfo;
import com.hunt.model.dto.SysCallLogDeviceRecoDto;
import com.hunt.model.dto.SysDeviceCallLogAndRecordDto;
import com.hunt.model.entity.SysDeviceCallLog;
import com.hunt.model.entity.SysDeviceRecord;
import com.hunt.model.entity.SysDeviceTotal;
import com.hunt.model.entity.SysOrganization;
import com.hunt.service.DeviceCallLogService;
import com.hunt.service.DeviceRecordService;
import com.hunt.service.SysOrganizationService;
import com.hunt.util.PlatformCode;
import com.hunt.util.ResponseCode;
import com.hunt.util.Result;
import com.hunt.util.StringUtil;
@Service
@Transactional
public class DeviceCallLogServiceImp implements DeviceCallLogService {

	@Autowired
	private SysDeviceCallLogMapper mSysDeviceCallLogMapper;
	
	@Autowired
	private SysDeviceRecordMapper mSysDeviceRecordMapper;
	
	@Autowired
	private SysDeviceTotalMapper mSysDeviceTotalMapper;
	
	@Autowired
	private DeviceRecordService mDeviceRecordService;
	
	@Autowired
	private SysOrganizationService mSysOrganization;
	
	@Override
	public Result insert(SysDeviceCallLog sysDeviceCallLog) {
//		Result validCallTypeResult = isValidCallType(sysDeviceCallLog);
//		if(validCallTypeResult!=null)return validCallTypeResult;
		Long deviceId = sysDeviceCallLog.getDeviceId();
		
//		String callEndTime = sysDeviceCallLog.getCallEndTime(); 
		Long callDate=sysDeviceCallLog.getCallDate();
		System.out.println("inset插入通话记录--->"+deviceId+"-->"+callDate);
		SysDeviceCallLog sysDeviceCallLogExist = mSysDeviceCallLogMapper.selectByCallDate(deviceId,callDate);
		if(sysDeviceCallLogExist!=null) {
			return Result.instance(ResponseCode.code_already_exist.getCode(), "通话记录已存在");
		}
		 
		
//		SysDeviceTotal sysDeviceTotal = mSysDeviceTotalMapper.selectByDeviceId(deviceId);    //  每天存一次，当天不存在则新建。
		Long sysOrgId = sysDeviceCallLog.getOrgId();
//		SysDeviceTotal sysDeviceTotal = mSysDeviceTotalMapper.selectByDevIdCreateTime(deviceId);    //  每天存一次，当天不存在则新建。
		SysDeviceTotal sysDeviceTotal = mSysDeviceTotalMapper.selectByOrgIdCreateTime(sysOrgId,deviceId);    //  每天存一次，当天不存在则新建。
		if(sysDeviceTotal==null) {		//设备统计不存在
			sysDeviceTotal=new SysDeviceTotal();
			sysDeviceTotal.setDeviceId(deviceId);
			sysDeviceTotal.setOrgId(sysOrgId);
			ResponseCode responseCode = fillCallType(sysDeviceTotal,sysDeviceCallLog);
			if(responseCode==ResponseCode.missing_parameter) {
				return Result.instance(responseCode.getCode(), "未知通话类型"); 
			}
			mSysDeviceCallLogMapper.insert(sysDeviceCallLog);
			Long insert = mSysDeviceTotalMapper.insert(sysDeviceTotal);
			if(insert<0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
				return Result.instance(ResponseCode.missing_parameter.getCode(), "参数错误");
			}
			return	Result.success(); 
			
		}else {
			ResponseCode responseCode = fillCallType(sysDeviceTotal, sysDeviceCallLog);
			if(responseCode==ResponseCode.missing_parameter) {
				return Result.instance(responseCode.getCode(), "未知通话类型");
			}
			mSysDeviceCallLogMapper.insert(sysDeviceCallLog);
			Long update = mSysDeviceTotalMapper.update(sysDeviceTotal);
			if(update<0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
				return Result.instance(ResponseCode.missing_parameter.getCode(), "参数错误");
			}
			return Result.success(sysDeviceCallLog);
		}
	}
	
	
	
	
	
	@Override
	public PageInfo listCallRecord(PageDto pageDto) {
		Long id = pageDto.getId(); 			//  机构id
		List<Long> listOrgId=new ArrayList<>();
		mSysOrganization.selectSysOrgIdList(id, listOrgId, false);
		 List<SysCallLogDeviceRecoDto> listCallLog = mSysDeviceTotalMapper.selectSearCallLogByUserSetN(listOrgId,pageDto);
		
		return new PageInfo(listCallLog.size(), listCallLog); 
	}





	/**
	 * 判断呼入呼出类型参数是否正确
	 * @param sysDeviceCallLog
	 * @return	null  参数合法
	 */
	private Result isValidCallType(SysDeviceCallLog sysDeviceCallLog) {
		Integer callType = sysDeviceCallLog.getCallType();
		if(callType==0||callType==1||callType==3||callType==4) {		//  呼入
			String callInPhone = sysDeviceCallLog.getCallNumber();
			if(StringUtils.isEmpty(callInPhone)) {	
				return Result.instance(ResponseCode.missing_parameter.getCode(), "缺少呼入号码");
			}
			
		}else if(callType==2) {											// 呼出
			String callOutPhone = sysDeviceCallLog.getCallNumber();
			if(StringUtils.isEmpty(callOutPhone)) {
				return Result.instance(ResponseCode.missing_parameter.getCode(), "缺少呼出号码");
			}
		}
		
		return null;
	}
	
	/**
	 * 填充通话数量及类型
	 * @param sysDeviceTotal
	 * @param callType
	 */
	private ResponseCode fillCallType(SysDeviceTotal sysDeviceTotal,SysDeviceCallLog sysDeviceCallLog) {
		Integer callType = sysDeviceCallLog.getCallType();
		if(callType==null) {
			return ResponseCode.missing_parameter;
		}
		Long updateBy = sysDeviceCallLog.getUpdateBy();
		Long createBy = sysDeviceCallLog.getCreateBy();
		
		Long callLogCount = sysDeviceTotal.getCallLogCount(); 
		switch(callType) {
			case 0:{ 		//未接
				Long callNoAccept = sysDeviceTotal.getCallNoAccept();
				callNoAccept=callNoAccept==null?1:(callNoAccept+1);
				sysDeviceTotal.setCallNoAccept(callNoAccept);
				break;
			}
			case 1:{			//呼入已接
				Long callAlreadyAcceptCount = sysDeviceTotal.getCallAlreadyAcceptCount();
				callAlreadyAcceptCount=callAlreadyAcceptCount==null?1:(callAlreadyAcceptCount+1);
				sysDeviceTotal.setCallAlreadyAcceptCount(callAlreadyAcceptCount);
				break;
			}
			case 2:{			//呼出
				Long callCallOut = sysDeviceTotal.getCallCallOut(); 
				callCallOut=callCallOut==null?1:(callCallOut+1);
				sysDeviceTotal.setCallCallOut(callCallOut);
				break;
			}
			case 3:{			//未接
				Long callNoAccept = sysDeviceTotal.getCallNoAccept();
				callNoAccept=callNoAccept==null?1:(callNoAccept+1);
				sysDeviceTotal.setCallNoAccept(callNoAccept);
				break;
			}
			case 4:{			//未接留言
				Long callNoAcceptLeave = sysDeviceTotal.getCallNoAcceptLeave();
				callNoAcceptLeave=callNoAcceptLeave==null?1:(callNoAcceptLeave+1);
				sysDeviceTotal.setCallNoAcceptLeave(callNoAcceptLeave);
				break;
			}
			case 5:	{		// 拒接
				Long callRefuseAccept = sysDeviceTotal.getCallRefuseAccept();
				callRefuseAccept=callRefuseAccept==null?1:(callRefuseAccept+1);
				sysDeviceTotal.setCallRefuseAccept(callRefuseAccept);
				break;
			}
		}
		sysDeviceTotal.setCallLogCount(callLogCount==null?1:(callLogCount+1));
		sysDeviceTotal.setCreateBy(createBy);
		sysDeviceTotal.setUpdateBy(updateBy);
		
		Integer callHasRecord = sysDeviceCallLog.getCallHasRecord();		//是否有录音，有录音
		callHasRecord=(callHasRecord==null||callHasRecord<=0)?0:callHasRecord;
		Long callRecordCount = sysDeviceTotal.getCallRecordCount(); 
		sysDeviceTotal.setCallRecordCount(callRecordCount==null?(0+callHasRecord):(callHasRecord+callRecordCount));
		
		Long callDuration = sysDeviceCallLog.getCallDuration(); 
		
		callDuration=callDuration==null?0:callDuration;
		Long devTotalCallDuration = sysDeviceTotal.getCallDuration();
		sysDeviceTotal.setCallDuration(devTotalCallDuration==null?callDuration:(devTotalCallDuration+callDuration));
		return ResponseCode.success;
	}

	@Override
	public List<SysDeviceCallLogAndRecordDto> select(Long deviceId,String sort,String order,Integer page,Integer rows) {
		String recoAudioLength=null;
		if("reco_audio_length".equals(sort)) {
			sort="call_date";
			recoAudioLength="reco_audio_length";
		}
		List<SysDeviceCallLogAndRecordDto> list=new ArrayList<SysDeviceCallLogAndRecordDto>();
		List<SysDeviceCallLog> listSysDeviceCallLog = mSysDeviceCallLogMapper.selectByDeviceId(deviceId,sort,order,page,rows);
		for(SysDeviceCallLog sysDeviceCallLog:listSysDeviceCallLog) {
			Integer callIsHavaRecord = sysDeviceCallLog.getCallHasRecord();
			SysDeviceCallLogAndRecordDto deviceCallLogRecord = new SysDeviceCallLogAndRecordDto();
			BeanUtils.copyProperties(sysDeviceCallLog, deviceCallLogRecord);
			if(callIsHavaRecord!=null&&callIsHavaRecord==1) {		//	有录音
				Long id = sysDeviceCallLog.getId(); 
				SysDeviceRecord sysDeviceRecord = mSysDeviceRecordMapper.selectByCallLogId(id);
				deviceCallLogRecord.setSysDeviceRecord(sysDeviceRecord);
			}
			list.add(deviceCallLogRecord);
			if("reco_audio_length".equals(recoAudioLength)) {		//	按录音时长排序
				sortByRecordLength(list,order);
			}
		}
		return list;
	}
	
	
	

	/**
	 * 查询设备及通话记录，
	 */
	@Override
	public List<SysDeviceCallLogAndRecordDto> select(Long orgId, Integer selectType, String sort, String order,
			Integer page, Integer rows) {
		// 根据机构id 查询到角色
		
		// 根据角色id 查询到职位和userId
		// 根据userId 查询到设备角色机构与设备id
		
		
		
//		mSysDeviceCallLogMapper.select(sysDeviceCallLog)
		return null;
	}



	private List<SysDeviceCallLogAndRecordDto> sortByRecordLength(List<SysDeviceCallLogAndRecordDto> list,String order){
		Collections.sort(list, new MyCompared(order)); 
		return list;
	}
	
	public class MyCompared implements Comparator<SysDeviceCallLogAndRecordDto>{

		private String order;
		public MyCompared(String order) {
			this.order=order;
		}
		@Override
		public int compare(SysDeviceCallLogAndRecordDto o1, SysDeviceCallLogAndRecordDto o2) {
			SysDeviceRecord sysDeviceRecord = o1.getSysDeviceRecord();
			SysDeviceRecord sysDeviceRecord2 = o2.getSysDeviceRecord();
			if(sysDeviceRecord==null||sysDeviceRecord2==null)return 1;
			if("desc".equals(order)) {
				return sysDeviceRecord.getRecoAudioLength().compareTo(sysDeviceRecord2.getRecoAudioLength());
			}else {
				return sysDeviceRecord2.getRecoAudioLength().compareTo(sysDeviceRecord.getRecoAudioLength());
			}
		}
		
		
		
		
	}
	@Override
	public Long updateCallLog(SysDeviceCallLog sysDeviceCallLog) {
		mSysDeviceCallLogMapper.update(sysDeviceCallLog);
		return	sysDeviceCallLog.getId();
	}
	
	

	
	
	@Override
	public Long updateAllCallLog(SysDeviceCallLog sysDeviceCallLog) {
		mSysDeviceCallLogMapper.update(sysDeviceCallLog);
		return	sysDeviceCallLog.getId();
	}



	@Override
	public Long deleteCallLogAndRecord(SysDeviceCallLog sysDeviceCallLog) {
//		for(SysDeviceCallLog sysDeviceCallLog:listSysDeviceCallLog) {
			sysDeviceCallLog.setStatus(2);
			
			Long sysDeviceCallLogId = mSysDeviceCallLogMapper.deleteOrNoDeleteDeviceCallLog(sysDeviceCallLog);
			
			// 	一条通话记录最多仅有一条语音消息
			SysDeviceRecord sysDeviceRecord = new SysDeviceRecord();
			sysDeviceRecord.setCallLogId(sysDeviceCallLogId);
			sysDeviceRecord.setStatus(2);
			mSysDeviceRecordMapper.update(sysDeviceRecord);
			Long result = mDeviceRecordService.updateDeviceTotalCallLogByDeviceId(sysDeviceCallLog.getDeviceId(), sysDeviceCallLog);
			if(result<=0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
			return result; 
			
//		}
		
	}
	



	@Override
	public SysDeviceTotal selectDevTotalByRoleOrg(Long orgId,Integer optType,String sType, Long beginTime,Long endTime,
			String sContent) {
		String callNumber="";
		String deviceSerial="";
		String callName="";
		switch (sType) {
			case "0":		// 查询号码
				callNumber=sContent;
				break;
			case "1":
				callName=sContent;
				break;
			case "2":		// 查询设备号
				deviceSerial=sContent;
				break;
		}
//		String callDate=StringUtil.getSRange(sTimeType);
		if(optType==0) {
			List<SysOrganization> listOrg=new ArrayList<>();
			List<SysOrganization> listOrganization = mSysOrganization.selectSysOrgList(orgId,listOrg,false);
			Set<Long> setId=new HashSet<>();
			for(SysOrganization sysOrganization:listOrganization) {
				setId.add(sysOrganization.getId()); 
			}
//			List<SysDeviceTotal> listDeviceTo = mSysDeviceTotalMapper.selectTotalByOrg(setId);
			List<SysDeviceTotal> listDeviceTo = mSysDeviceTotalMapper.selectSearTotalByOrg(setId,callNumber,callName,deviceSerial,beginTime,endTime);
			return calcu(listDeviceTo);
		}else {
//			List<SysDeviceTotal> listSysDeviceTotal = mSysDeviceTotalMapper.selectTotalByUserId(orgId);
			List<SysDeviceTotal> listSysDeviceTotal = mSysDeviceTotalMapper.selectSerTotalByUserId(orgId,callNumber,callName,deviceSerial,beginTime,endTime);
			 
			return calcu(listSysDeviceTotal);
		}
		
	}

	@Override
	public SysDeviceTotal selectDevTotalByPageDto(PageDto pageDto) {
		Long id = pageDto.getId(); 			//  机构id
		List<Long> listOrgId=new ArrayList<>();
		mSysOrganization.selectSysOrgIdList(id, listOrgId, false);
		String callNumber = pageDto.getCallNumber();
		String orgName = pageDto.getOrgName();
		if(!StringUtils.isEmpty(callNumber)||!StringUtils.isEmpty(orgName)||pageDto.getBeginTime()==null) {
			List<SysCallLogDeviceRecoDto> listDeviceDto=null;
			if(pageDto.getBeginTime()==null) {
				listDeviceDto = mSysDeviceTotalMapper.selectTotalByUserSetNN(listOrgId,pageDto);
			}else {
				listDeviceDto = mSysDeviceTotalMapper.selectTotalByUserSetN(listOrgId,pageDto);
			}
		
			SysDeviceTotal sysDeviceTotal = new SysDeviceTotal();
			Long callLogCount=0l;		// 通话数量
			Long callRecordCount=0l;	// 通话录音数量
			Long callAlreadyAcceptCount=0l;	// 已接
			Long callNoAccept=0l;		// 未接
			Long callRefuseAccept=0l;	// 拒接
			Long callCallOut=0l;		// 拨出
			for(SysCallLogDeviceRecoDto dto:listDeviceDto) {
				callLogCount++;
				SysDeviceCallLogAndRecordDto sysDeviceRecord = dto.getSysDeviceRecord();
				callRecordCount+= sysDeviceRecord.getCallHasRecord(); 
				Integer callType = sysDeviceRecord.getCallType();
				switch (callType) {
				case 1:		// 呼入已接
					callAlreadyAcceptCount++;
					break;
				case 2:		// 呼出
					callCallOut++;
					break;
				case 3:		// 拒接
					callRefuseAccept++;
					break;
				case 4:
					callNoAccept++;
					break;
				case 5:
					callNoAccept++;
					break;
				default:
					break;
				}
			}
			sysDeviceTotal.setCallLogCount(callLogCount);
			sysDeviceTotal.setCallRecordCount(callRecordCount);
			sysDeviceTotal.setCallAlreadyAcceptCount(callAlreadyAcceptCount);
			sysDeviceTotal.setCallCallOut(callCallOut);
			sysDeviceTotal.setCallRefuseAccept(callRefuseAccept);
			sysDeviceTotal.setCallNoAccept(callNoAccept);
			return sysDeviceTotal;
		}
//			List<SysDeviceTotal> listDeviceTo = mSysDeviceTotalMapper.selectTotalByPageDto(listOrgId, pageDto);
			List<SysDeviceTotal> listDeviceTo = mSysDeviceTotalMapper.selectTotalByPageDtoN(listOrgId, pageDto);
		 return calcu(listDeviceTo);
	}





	@Override
	public Long selectTotalCount(Long orgId, Integer optType) {
		if(optType==0) {
			List<SysOrganization> listOrg=new ArrayList<>();
			List<SysOrganization> listOrganization = mSysOrganization.selectSysOrgList(orgId,listOrg,false);
			Set<Long> setId=new HashSet<>();
			for(SysOrganization sysOrganization:listOrganization) {
				setId.add(sysOrganization.getId());
			}
			return  mSysDeviceTotalMapper.selectCallCount(setId);
			
		}else {
			return mSysDeviceTotalMapper.selectCountByUserId(orgId);
		}
	}

	
	


	@Override
	public Long selectSearTotalCount(Long orgId, Integer optType, String sType, Long beginTime,Long endTime, String sContent,Integer callIsHaveRecord) {
		String callNumber="";
		String deviceSerial="";
		String callName="";
		switch (sType) {
			case "0":		// 查询号码
				callNumber=sContent;
				break;
			case "1":
				callName=sContent;
				break;
			case "2":		// 查询设备号
				deviceSerial=sContent;
				break;
		}
		if(optType==0) {
			List<SysOrganization> listOrg=new ArrayList<>();
			List<SysOrganization> listOrganization = mSysOrganization.selectSysOrgList(orgId,listOrg,false);
			Set<Long> setId=new HashSet<>();
			for(SysOrganization sysOrganization:listOrganization) {
				setId.add(sysOrganization.getId());
			} 
			return  mSysDeviceTotalMapper.selectSearCallCount(setId,callNumber,callName,deviceSerial,beginTime,endTime,callIsHaveRecord);
			
		}else {
			return mSysDeviceTotalMapper.selectSearCountByUserId(orgId,callNumber,deviceSerial,callName,beginTime,endTime,callIsHaveRecord);
		}
	}
	
	@Override
	public Long selectTotalCountByPageDto(PageDto pageDto) {
		Long id = pageDto.getId(); 			//  机构id
		List<Long> listOrgId=new ArrayList<>();
		mSysOrganization.selectSysOrgIdList(id, listOrgId, false);
		if(pageDto.getBeginTime()==null) {   
			return mSysDeviceTotalMapper.selectSearCallCountN(listOrgId,pageDto);
		}else {
			return mSysDeviceTotalMapper.selectSearCallCountNN(listOrgId,pageDto);
		}
	}





	private SysDeviceTotal calcu(List<SysDeviceTotal> list) {
    	Long callLogCount=0l;		//  通话记录
    	Long callRecordCount=0l;	//  通话录音数量
    	Long recoFileSize=0l;		//  录音文件大小
    	Long callAlAcceptCount=0l;	//  已接电话
    	Long callNoAccept=0l;		// 未接电话
    	Long callRefuAcc=0l;		//  拒接
    	Long callCallOut=0l;		//  呼出
    	SysDeviceTotal sysDeviceTotal = new SysDeviceTotal();
    	for(SysDeviceTotal to:list) {
    		 callLogCount += to.getCallLogCount();
    		 callRecordCount+=to.getCallRecordCount();
    		 recoFileSize+=to.getRecoFileSize();
    		 callAlAcceptCount+=to.getCallAlreadyAcceptCount();
    		 callNoAccept+=to.getCallNoAccept();
    		 callRefuAcc+=to.getCallRefuseAccept();
    		 callCallOut+=to.getCallCallOut();
    	}
    	sysDeviceTotal.setCallLogCount(callLogCount);
    	sysDeviceTotal.setCallRecordCount(callRecordCount);
    	sysDeviceTotal.setRecoFileSize(recoFileSize);
    	sysDeviceTotal.setCallAlreadyAcceptCount(callAlAcceptCount);
    	sysDeviceTotal.setCallNoAccept(callNoAccept);
    	sysDeviceTotal.setCallRefuseAccept(callRefuAcc);
    	sysDeviceTotal.setCallCallOut(callCallOut);
    	
    	return sysDeviceTotal;
    }


	/**
	 * 构建通话记录统计实体类
	 * @param sysDeviceCallLog
	 * @param sysDeviceRecord
	 */
	private void createDeviceTotal(SysDeviceCallLog sysDeviceCallLog,SysDeviceRecord sysDeviceRecord) {
		Integer callType = sysDeviceCallLog.getCallType();
		switch (callType) {
		case 0:
			
			break;

		default:
			break;
		}
	}
	
	
	
	

}
