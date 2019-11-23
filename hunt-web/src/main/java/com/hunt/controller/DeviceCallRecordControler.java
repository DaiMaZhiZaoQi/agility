package com.hunt.controller;

import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hunt.model.dto.PageInfo;
import com.hunt.model.dto.SysCallLogDeviceRecoDto;
import com.hunt.model.dto.SysDeviceAndCallDto;
import com.hunt.model.dto.SysDeviceCallLogAndRecordDto;
import com.hunt.model.entity.SysDevice;
import com.hunt.model.entity.SysDeviceTotal;
import com.hunt.model.entity.SysUser;
import com.hunt.service.DeviceCallLogService;
import com.hunt.service.impl.SysOrganizationServiceImpl;
import com.hunt.service.impl.SysUserServiceImpl;
import com.hunt.util.ResponseCode;
import com.hunt.util.Result;
import com.hunt.util.StringUtil;
//import com.sun.tools.classfile.Opcode.Set;
//import com.sun.xml.internal.xsom.impl.scd.Iterators.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
@Api(value="通话记录")
@Controller
@RequestMapping("callRecord")
public class DeviceCallRecordControler extends BaseController {
	
	  @Autowired
	  private DeviceCallLogService mDeviceCallLogService;
	  @Autowired 
	  private SysUserServiceImpl mSysUserServiceImpl;
	  @Autowired
	  private SysOrganizationServiceImpl mSysOrganizationServiceImpl;
	
	  @ApiOperation(value="查询通话记录",produces="text/html",httpMethod="GET")
	  @RequestMapping(value="callRecord",method=RequestMethod.GET)
	  public String callRecord() {
		  return "home/callRecord";
	  }
	  
	  @ApiOperation(value="查询该机构下的所有机构",produces="text/json",httpMethod="GET")
	  @ResponseBody
	  @RequestMapping(value="listOrgCount",method=RequestMethod.GET)
	  public PageInfo getOrgCallRecord(@RequestParam(value="optionType",required=false,defaultValue="0") Integer optionType,
									   @RequestParam(value="id") Long orgId) {
		  return  mSysOrganizationServiceImpl.selectOrgFromOrgId(0,0,orgId);
	  }
	  
	  
	/**
	 * 查询部门时显示部门下的所有通话记录，个人就显示个人通话记录，支持分页
	 * @param optType 0 查询部门 ,1 查询机构
	 * @param sort 排序字段，默认是 call_date
	 * @param sType 0 默认查询，1，查询 通话号码，2，查询设备号 
	 * @param sTimeType 1,近三天  2，近 七天 3，近半月 4，近一月 5，近三月 6,近半年 7 今年内 8 一年前 如今年是2019 就查询去年2018年，整年数据  默认查询七天内的
	 * @param beginTime 开始时间 默认时间0
	 * @param endTime
	 * @param sContent	搜索内容
	 * @return
	 */
	@ApiOperation(value="查询所有通话记录",produces="text/html",httpMethod="GET")
	@ResponseBody
	@RequestMapping(value="list",method=RequestMethod.GET)
	public PageInfo getAllCallRecord(@RequestParam(value="optType",required=false,defaultValue="0") Integer optType,
									@RequestParam(value="id") Long id,
									@RequestParam(value="page",defaultValue="1") Integer page,
									@RequestParam(value="row",defaultValue="15") Integer row,
		 							@RequestParam(value="sort",required=false,defaultValue="call_date") String sort,
									@RequestParam(value="order",required=false,defaultValue="DESC") String order,
//									@RequestParam(value="sTimeType",required=false,defaultValue="2") String sTimeType,
									@RequestParam(value="beginTime",defaultValue="0") Long beginTime,
									@RequestParam(value="endTime",defaultValue="0") Long endTime,
									@RequestParam(value="sContent",required=false) String sContent,
									@RequestParam(value="searchType",defaultValue="0") String searchType,
									@RequestParam(value="callIsHaveRecord",required=false,defaultValue="0") Integer callIsHaveRecord,
									HttpServletRequest request) {
		PageInfo pageInfo=null;
		String sType=getSType(sContent,searchType);
	
		beginTime=getBeginTime(beginTime);
		endTime=getEndTime(endTime);
		
		if(optType==0) {			//   查询机构，机构查询完毕后，汇总当前机构中的设备数据信息，汇总个人设备
										//	 查询该机构以及子机构中的人，通过该人，再查询设备
//			pageInfo = mSysOrganizationServiceImpl.selectDeviceCallRecordFromOrg(page, row, id);
//			pageInfo = mSysOrganizationServiceImpl.selectDeviceCallbyOrg(id,sort,order,page,row);
			pageInfo = mSysOrganizationServiceImpl.selectAllCallLogByOrg(id,sort,order,page,row,sType,beginTime,endTime,sContent,callIsHaveRecord);
//			List<SysDeviceAndCallDto> listDeviceAndCall= (List<SysDeviceAndCallDto>) pageInfo.getRows();
//			String jSon = JsonUtils.toJSon(listDeviceAndCall);
//			System.out.println("getAllCallRecord-->"+jSon);
			
//			for(SysDeviceAndCallDto sysDeviceAndCallDto:listDeviceAndCall) {
//				List<SysDeviceCallLogAndRecordDto> listCallRecordAndAudio = sysDeviceAndCallDto.getListDeviceCallLogAndRecord();
//				for(SysDeviceCallLogAndRecordDto dto:listCallRecordAndAudio) {
//					
//				}
//				 
//			}
			
		}else if(optType==1) {		//  查询个人  
//			SysUser sysUser = mSysUserServiceImpl.selectById(id);
//			List<SysDeviceAndCallDto> listUserAndDevice = mSysUserServiceImpl.listUserAndDevice(sysUser.getId()); 
			List<SysCallLogDeviceRecoDto> listUserAndDevice = mSysUserServiceImpl.listCallDevByUserId(id,sort,order,page,row,sType,beginTime,endTime,sContent,callIsHaveRecord); 
			   
		     pageInfo=new PageInfo(listUserAndDevice.size(), listUserAndDevice);
		}
//		request.setAttribute("listCallRecord", pageInfo.getRows());
		
//		mDeviceCallLogService.select(deviceId, sort, order, page, rows)
		
		return pageInfo;
//		return "home/callRecord";
	}

	private Long getBeginTime(Long beginTime) {
		if(beginTime==0) {
//			beginTime=StringUtil.getDayFirstMonth();
			beginTime=StringUtil.getDayFirstWeek();
		}
		return beginTime;
	}
	
	private Long getEndTime(Long endTime) {
		if(endTime==0) {
			endTime=System.currentTimeMillis();
		}
		return endTime;
	}
	
	/**
	 * 判断搜索内容  
	 * @param sContent
	 * @return sType=0,默认搜索方式,什么搜索内容为空，sType=1 查询通话号码，sType=2  查询设备号
	 */
	private String getSType(String sContent,String searchType) {
//		String sType="3";
		if(StringUtils.isEmpty(sContent)) {
			return "3";
		}else {
			return searchType;
		}
		/*if(!StringUtils.isEmpty(sContent)) {
			 Integer maResult=StringUtil.isNumber(sContent);
			 if(maResult==1) {
				 sType="1";
			 }else {
				 
				 sType="2";
			 }
		}
		return sType;*/
	}
	
	
	
	@ApiOperation(value="查询设备统计",produces="text/json",httpMethod="GET")
	@ResponseBody
	@RequestMapping(value="deviceTotal",method=RequestMethod.GET)
	public Result selectTotalByOrg(@RequestParam("id") Long id,
									@RequestParam(value="optType",defaultValue="0") Integer optType,
//									@RequestParam(value="sTimeType",required=false,defaultValue="2") String sTimeType,
									@RequestParam(value="sContent",required=false) String sContent,
									@RequestParam(value="beginTime",defaultValue="0") Long beginTime,
									@RequestParam(value="endTime",defaultValue="0") Long endTime,
									@RequestParam(value="searchType",defaultValue="0") String searchType) {
			String sType=getSType(sContent,searchType);
			beginTime=getBeginTime(beginTime);
			endTime=getEndTime(endTime);
			SysDeviceTotal sysDeviceTotal = mDeviceCallLogService.selectDevTotalByRoleOrg(id,optType,sType,beginTime,endTime,sContent); 
			return Result.success(sysDeviceTotal);   		 
	} 
	
	@ApiOperation(value="查询全部通话记录数量",produces="text/json",httpMethod="GET")
	@ResponseBody
	@RequestMapping(value="callTotal",method=RequestMethod.GET)
	public Result selectTotalCall(@RequestParam("id") Long id,
								@RequestParam(value="optType",defaultValue="0") Integer optType,
								@RequestParam(value="sTimeType",required=false,defaultValue="2") String sTimeType,
								@RequestParam(value="sContent",required=false) String sContent,
								@RequestParam(value="beginTime",defaultValue="0") Long beginTime,
								@RequestParam(value="endTime",defaultValue="0") Long endTime,
								@RequestParam(value="callIsHaveRecord",required=false,defaultValue="0")Integer callIsHaveRecord,
								@RequestParam(value="searchType",defaultValue="0") String searchType) {
		String sType=getSType(sContent,searchType);  
		beginTime=getBeginTime(beginTime); 
		endTime=getEndTime(endTime);
//		Long selectCallCount = mDeviceCallLogService.selectTotalCount(id,optType);
		Long selectCallCount = mDeviceCallLogService.selectSearTotalCount(id,optType,sType,beginTime,endTime,sContent,callIsHaveRecord);
		 
		return Result.success(selectCallCount==null?0:selectCallCount);
	}
	
	
	
	
}
	
	


