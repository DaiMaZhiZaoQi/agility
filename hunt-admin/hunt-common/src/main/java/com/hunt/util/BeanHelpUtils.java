package com.hunt.util;

import java.util.List;

import com.hunt.model.entity.SysDeviceTotal;

/**
 * 实体类帮助工具
 * @author williambai
 *
 */
public class BeanHelpUtils {

	/**
	 * 通话统计
	 * @param list
	 * @return
	 */
	public static SysDeviceTotal calcu(List<SysDeviceTotal> list) {
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
}
