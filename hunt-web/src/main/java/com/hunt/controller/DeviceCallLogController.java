package com.hunt.controller;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hunt.dao.SysDeviceCallLogMapper;
import com.hunt.dao.SysDeviceMapper;
import com.hunt.model.dto.LoginInfo;
import com.hunt.model.dto.PageInfo;
import com.hunt.model.dto.SysDeviceCallLogAndRecordDto;
import com.hunt.model.entity.SysDevice;
import com.hunt.model.entity.SysDeviceCallLog;
import com.hunt.service.DeviceCallLogService;
import com.hunt.service.impl.DeviceCallLogServiceImp;
import com.hunt.util.ResponseCode;
import com.hunt.util.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value="设备通话记录")
@Controller
@RequestMapping("deviceCallLog") 
public class DeviceCallLogController extends BaseController{
	
	@Autowired
	private DeviceCallLogService mDeviceCallLogService;
	
	@Autowired
	private SysDeviceMapper mSysDeviceMapper;
	
	@Autowired
	private SysDeviceCallLogMapper mSysDeviceCallLogMapper;
	
	@RequestMapping(value="callLog",method=RequestMethod.GET)
	public String callLog(@RequestParam(value="deviceId",required=true) Long deviceId,HttpServletRequest request) {
		List<SysDeviceCallLogAndRecordDto> list = mDeviceCallLogService.select(deviceId,"update_time","desc",1,6);
		request.setAttribute("listCallLog", list);
		return "home/deviceCallLog";
	}
	
	
	
	/**
	 * 可删除，全部交个DeviceCallRecordController 处理
	 * @param sysDeviceCallLogJson
	 * @param deviceSerial
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="insert",method=RequestMethod.POST)
	public Result insert(@RequestParam("sysDeviceCallLog")String sysDeviceCallLogJson,@RequestParam("deviceSerial") String deviceSerial) {
		SysDeviceCallLog sysDeviceCallLog=JsonUtils.readValue(sysDeviceCallLogJson, SysDeviceCallLog.class);
		if(sysDeviceCallLog==null) {
			return Result.instance(ResponseCode.missing_parameter.getCode(), "缺少参数"); 
		}
	    
		SysDevice sysDevice = mSysDeviceMapper.selectByDeviceSerial(deviceSerial);
		
		if(sysDevice==null) {
			return Result.error("设备不存在,请添加");
		}else {
			Long deviceId = sysDevice.getId();
			sysDeviceCallLog.setDeviceId(deviceId);
		}
		 
		Subject subject = SecurityUtils.getSubject();
        LoginInfo loginInfo=(LoginInfo)subject.getSession().getAttribute("loginInfo");
        System.out.println("loginInfo-->"+loginInfo.toString());
        if (loginInfo!=null) {
        	Long createId = loginInfo.getId(); 
        	sysDevice.setCreateBy(createId);
        	sysDevice.setUpdateBy(createId);
        	sysDeviceCallLog.setCreateBy(createId);
        	sysDeviceCallLog.setUpdateBy(createId);
        	
		}
        
		Result result= mDeviceCallLogService.insert(sysDeviceCallLog);
		
		return result;
	}
	
	
	/**
	 * 加载通话列表
	 * @param deviceId	设备id
	 * @param sort		排序方式，按修改时间，通话时长，录音时长
	 * @param order		排序方式，正序还是倒序
	 * @param isFirst	1,首次加载,0,再次加载。 首次加载，加载全部通话数量
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="list",method=RequestMethod.GET)
	public PageInfo list(@RequestParam(value="deviceId",required=true) Long deviceId,
						@RequestParam(value="sort",required=false,defaultValue="call_date") String sort,
						@RequestParam(value="order",required=false,defaultValue="desc") String order,
						@RequestParam(value="isFirst",defaultValue="1") Integer isFirst,
						@RequestParam(value="page",defaultValue="1") Integer page,
						@RequestParam(value="rows",defaultValue="6") Integer rows) {
		int pageCount=0;
		if(isFirst==1) {
			Long callLogCount = mSysDeviceCallLogMapper.selectCountsByDeviceId(deviceId);
			pageCount=Integer.parseInt(callLogCount+"");
		}
		List<SysDeviceCallLogAndRecordDto> list = mDeviceCallLogService.select(deviceId,sort,order,page,rows);
		if(pageCount==0) {
			pageCount=list.size();
		}
		PageInfo pageInfo = new PageInfo(pageCount, list);
		return pageInfo;
		
	}
	@ApiOperation(value="修改通话记录",httpMethod="POST",produces="text/json")
	@ResponseBody
	@RequestMapping(value="update",method=RequestMethod.POST)
	public Result update(SysDeviceCallLog sysDeviceCallLog) {
		if(sysDeviceCallLog==null) {
			return Result.instance(ResponseCode.missing_parameter.getCode(), "通话记录不存在");
		}
		Long id = mDeviceCallLogService.updateCallLog(sysDeviceCallLog);
		if(id>0) {
			return Result.success();
		}
		return Result.instance(ResponseCode.missing_parameter.getCode(), "没有找到该通话记录");
	}
	
	@ApiOperation(value="删除通话记录及录音",httpMethod="POST",produces="text/json")
	@ResponseBody
	@RequestMapping(value="delete",method=RequestMethod.POST)
	public Result delete(@RequestBody SysDeviceCallLog sysDeviceCallLog) { //  @RequestParam(value="sysDeviceCallLog")
//		SysDeviceCallLog sysDeviceCallLog=JsonUtils.readValue(sysDeviceCallLogJson, SysDeviceCallLog.class);
		System.out.println("deleteSysDeviceCallLog-->"+sysDeviceCallLog.toEntityString());
		Long updateCallLog = mDeviceCallLogService.deleteCallLogAndRecord(sysDeviceCallLog);
		if(updateCallLog==-1) { 
			return Result.instance(ResponseCode.data_not_exist.getCode(), "没有找到通话记录");
		}
		if(updateCallLog<=0) {
			return Result.error("参数错误");
		}
		return Result.success();
	}
	
	

}
