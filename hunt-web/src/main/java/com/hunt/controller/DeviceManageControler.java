package com.hunt.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hunt.model.dto.PageDto;
import com.hunt.model.dto.PageInfo;
import com.hunt.service.DeviceManageService;
import com.hunt.util.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.HttpMethod;
@Api(value="设备管理")
@Controller
@RequestMapping("devManager")
public class DeviceManageControler extends BaseController{
	
	@Autowired
	private DeviceManageService mDeviceManageService;
	
	
	@ApiOperation(value="设备管理",httpMethod="GET",produces="text/html") 
	@RequestMapping("devManager")
	public String devManager() {
		return "home/deviceManage";
	}
	
	@ApiOperation(value="查询未绑定的设备",httpMethod="GET",produces="text/json")
	@ResponseBody
	@RequestMapping(value="listUnRegis",method=RequestMethod.GET)
	public PageInfo listUnRegis(@RequestParam(value="sort",defaultValue="create_time") String sort,
								@RequestParam(value="order",defaultValue="DESC") String order,
								@RequestParam(value="page",defaultValue="1") Integer page,
								@RequestParam(value="rows",defaultValue="15") Integer rows) {
		PageDto pageDto = new PageDto(sort,order,page,rows);
		PageInfo pageInfo = mDeviceManageService.selectUnRegisDevice(pageDto); 
		return pageInfo;
	}
	
	@ApiOperation(value="查询未绑定设备的总数",httpMethod="GET",produces="text/json")  
	@ResponseBody
	@RequestMapping(value="unRegisCount",method=RequestMethod.GET)
	public Result getTotalUnRegisCount() {
		Long count= mDeviceManageService.selectUnRegisCount();
		return Result.success(count);
	}
	
	@ApiOperation(value="查询该用户下已绑定的设备总数",httpMethod="GET",produces="text/json")
	@ResponseBody
	@RequestMapping(value="regisCount",method=RequestMethod.GET)
	public Result getTotalRegisCount(@RequestParam(value="orgId") Long orgId) {
		Long count=mDeviceManageService.selectRegisCount(orgId);
		return Result.success(count);
	}
	
	
	
	@ApiOperation(value="查询该用户下已绑定的设备",produces="application/json",httpMethod="GET")
	@ResponseBody
	@RequestMapping(value="listRegis",method=RequestMethod.GET)
	public PageInfo listRegis(@RequestParam(value="sort",defaultValue="create_time") String sort,
							@RequestParam(value="order",defaultValue="DESC") String order,
							@RequestParam(value="page",defaultValue="1") Integer page,
							@RequestParam(value="rows",defaultValue="15") Integer rows,
							@RequestParam(value="orgId") Long orgId) {
		PageDto pageDto=new PageDto(sort,order,page,rows);
		return mDeviceManageService.selectRegisDevice(pageDto, orgId);
	}

}
