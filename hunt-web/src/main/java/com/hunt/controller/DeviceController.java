package com.hunt.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hunt.dao.SysDeviceMapper;
import com.hunt.dao.SysUserInOrgMapper;
import com.hunt.dao.SysUserMapper;
import com.hunt.model.dto.PageInfo;
import com.hunt.model.dto.SysDeviceAndCallDto;
import com.hunt.model.entity.SysDevice;
import com.hunt.model.entity.SysUser;
import com.hunt.model.entity.SysUserInOrg;
import com.hunt.service.SysOrganizationService;
import com.hunt.service.impl.SysOrganizationServiceImpl;
import com.hunt.service.impl.SysUserServiceImpl;
import com.hunt.util.PermissionCode;
import com.hunt.util.ResponseCode;
import com.hunt.util.Result;

import io.swagger.annotations.Api;
/**
 * 设备管理模块
 * 1、查看所有设备
 * 2、 设备新增
 * 3、上传设备录音
 * 4、删除录音
 * 5，查看录音文件
 * @author williambai
 *
 */
import io.swagger.annotations.ApiOperation;
import net.sf.jsqlparser.statement.select.Select;
@Api(value="设备模块")
@Controller
@RequestMapping("device")
public class DeviceController extends BaseController{
	Logger log=LoggerFactory.getLogger(SystemDeviceController.class);
	
	  @Autowired
	  private SysOrganizationServiceImpl mSysOrganizationServiceImpl;
	  
	  @Autowired 
	  private SysUserServiceImpl mSysUserServiceImpl;
	  
	  @Autowired
	  private SysDeviceMapper mSysDeviceMapper;
	  
	  @Autowired
	  private SysUserMapper mSysUserMapper;
	  
	  @Autowired
	  private SysUserInOrgMapper mSysUserInOrgMapper;
	  
	/**
	 * 待删除？？查询设备列表，只能查询该部门下的设备，身兼多职，需要考虑，根据权限对设备进行操作，员工只能查看上传自己的文件
	 * @param id
	 * @return
	 */
	@ApiOperation(value="跳转至设备管理模块",httpMethod="GET",produces="application/json")
	 @ResponseBody
	@RequestMapping(value="list",method = RequestMethod.GET)
	public PageInfo device(@RequestParam Long id,
							@RequestParam Integer requestType,
							@RequestParam(value="page",defaultValue="1") Integer page,
							@RequestParam(value="row",defaultValue="15") Integer row
							) {
	
		System.out.println("device type-->"+id+"<--requestType-->"+requestType); 
		PageInfo pageInfo=null;
		if(requestType==0) {			//   查询机构，机构查询完毕后，汇总当前机构中的设备数据信息，汇总个人设备
										//	 查询该机构以及子机构中的人，通过该人，再查询设备
			pageInfo = mSysOrganizationServiceImpl.selectDeviceFromOrg(page, row, id);
		}else if(requestType==1) {		//  查询个人
			SysUser sysUser = mSysUserServiceImpl.selectById(id);
			List<SysDeviceAndCallDto> listUserAndDevice = mSysUserServiceImpl.listUserAndDevice(sysUser.getId()); 
			
		     pageInfo=new PageInfo(listUserAndDevice.size(), listUserAndDevice);
		}
		System.out.println("pageInfo-->"+pageInfo.toString());
		return pageInfo;
	} 
	
	@ApiOperation(value="查看该组织所有设备",httpMethod="GET",produces="application/json")
	@RequestMapping(value="device")
	@ResponseBody
	public PageInfo getAllDevice2(
								@RequestParam(value="userName",defaultValue="",required=false) String userName,
								HttpServletRequest request,
								@RequestParam(value="searchType",required=false,defaultValue="0") Integer searchType,
								@RequestParam(value="password",defaultValue="",required=false) String password) {
		  SysUser sysUser = mSysUserMapper.selectUserByLoginName(userName);
		  SysUser userDb = mSysUserMapper.selectById(sysUser.getId());
			if (userDb.getLoginName().equals(userName)) {	// 用户已绑定
				 Boolean checkPassWord = checkPassWord(userDb,password);
				 if(!checkPassWord) {
					 return new PageInfo(ResponseCode.password_incorrect.getCode(), ResponseCode.password_incorrect.getMsg());
				 }
			}
			SysUserInOrg userInOrg = mSysUserInOrgMapper.selectByUserId(sysUser.getId());
			
		PageInfo pageInfo = mSysOrganizationServiceImpl.selectDeviceFromOrg(0, 0, userInOrg.getSysOrgId());
		List<SysDeviceAndCallDto> listUserAndDevice=(List<SysDeviceAndCallDto>)pageInfo.getRows();
		List<SysDeviceAndCallDto> listSearchSysDevice = searchSysDevice(listUserAndDevice, null,searchType,request);
		return new PageInfo(listSearchSysDevice.size(), listSearchSysDevice);
	}

	
	/**
	 * 加载所有设备，
	 *@param orgId	机构id
	 *@param personalId	个人id
	 *@param page	页码
	 *@param row	加载条数
	 *@param searchType 0:查找所有，1:查询在线，2：查询离线
	 * @return  allDevice.jsp,根据不同参数加载不同数据
	 */
	@ApiOperation(value="查看该组织所有设备",httpMethod="GET",produces="text/html")
	@RequestMapping(value="allDevice")
	public String getAllDevice(@RequestParam(value="orgId",defaultValue="-1") String orgId,
								@RequestParam(value="personalId",defaultValue="-1")String personalId,
								@RequestParam(value="page",defaultValue="1")Integer page,
								@RequestParam(value="row",defaultValue="6")Integer row,
								@RequestParam(value="isLoadAll",required=false,defaultValue="0") Integer isLoadAll,
								@RequestParam(value="searchContent",required=false,defaultValue="") String searchContent,
								@RequestParam(value="searchType",required=false,defaultValue="0") Integer searchType,
								@RequestParam(value="userId",defaultValue="0",required=true)Long userId,
								HttpServletRequest request) {   
		System.out.println("getAllDevice-->"+orgId);
		if("searchContent".equals(searchContent)||StringUtils.isEmpty(searchContent)) {
			request.setAttribute("searchType", searchType);
		}else {
			request.setAttribute("searchType", 3);
		}
		if(!mobileHasPermission(userId, PermissionCode.DEVICE_SELECT.pName)) {
			request.setAttribute("perMsg", PermissionCode.DEVICE_SELECT.pMsg);
			return "system/noPermiss";
		}
		// 判断有无查询权限，setAttribute;
		if(!"-1".equals(orgId)&&"-1".equals(personalId)) {					//查询机构
			request.setAttribute("orgId", orgId); 
			request.setAttribute("requestType", 0);   
			if(isLoadAll!=null&&isLoadAll==1) {								//加载顶级机构	
				request.setAttribute("isLoadAll",1);
			} 
			PageInfo pageInfo = mSysOrganizationServiceImpl.selectDeviceFromOrg(page, row, Long.parseLong(orgId));
			List<SysDeviceAndCallDto> listUserAndDevice=(List<SysDeviceAndCallDto>)pageInfo.getRows();
			List<SysDeviceAndCallDto> listSearchSysDevice = searchSysDevice(listUserAndDevice, searchContent,searchType,request); 
			request.setAttribute("listDevice", listSearchSysDevice);
		}else if("-1".equals(orgId)&&!"-1".equals(personalId)) {			//查询个人		
			request.setAttribute("orgId", personalId);
			request.setAttribute("requestType", 1);
			
			SysUser sysUser = mSysUserServiceImpl.selectById(Long.parseLong(personalId));
			List<SysDeviceAndCallDto> listUserAndDevice = mSysUserServiceImpl.listUserAndDevice(sysUser.getId());
			request.setAttribute("listDevice", listUserAndDevice);
		}
		 
		
//		boolean hasP = PermissionUtil.hasDataPermission(null, "user:manage");
//		if(hasP) {
//			request.setAttribute("hasP",1);
//		}
		List<String> listUserPerCode = selectPerCodeByUserId(userId);
		request.setAttribute("listUserPerCode", listUserPerCode);
		return "home/allDevice";
	}
	
	/**
	 * 搜索过滤相关数据
	 * @param list
	 * @param searchContent
	 * @param searchType  搜索类型 0,搜索全部，1，在线设备，2，离线设备
	 * @param request
	 * @return
	 */
	private List<SysDeviceAndCallDto> searchSysDevice(List<SysDeviceAndCallDto> list,
													String searchContent,
													Integer searchType,
													HttpServletRequest request){
		String promptMsg="";
		if(StringUtils.isEmpty(searchContent)) {
			promptMsg="未查到设备";
			return list;
		}
		searchContent=searchContent.toLowerCase();
		List<SysDeviceAndCallDto> listFilter=new ArrayList<>();
		for(SysDeviceAndCallDto dto:list) {
			SysDevice sysDevice = dto.getSysDevice();
			String deviceSerial = sysDevice.getDeviceSerial().toLowerCase();
			
			switch (searchType) {
				case 0:{
					SysUser sysUser = dto.getSysUser();
					String zhName = sysUser.getZhName().toLowerCase();
					
					if(deviceSerial.contains(searchContent)||zhName.contains(searchContent)) {
						listFilter.add(dto);
					}
				}
					break;
				case 1:{
					Date deviceTime = sysDevice.getDeviceTime();
					if(deviceTime!=null) {
						long time = deviceTime.getTime();
						boolean isOnLine=System.currentTimeMillis()-time<(5*60*1000)?true:false;
						if(isOnLine) {
							listFilter.add(dto);
						}
					}
				}
					break;
				case 2:
					Date deviceTime = sysDevice.getDeviceTime();
					if(deviceTime!=null) {
						long time = deviceTime.getTime();
						boolean isOnLine=System.currentTimeMillis()-time<(5*60*1000)?true:false;
						if(!isOnLine) {
							listFilter.add(dto);
						}
					}else {
						listFilter.add(dto);
					}
					break;
			
			}
		
		}
		if(listFilter.size()>0) { 
			if(searchType==0) {
				request.setAttribute("searchContent", searchContent);
			}
			return listFilter;
		}else {
			promptMsg="未查到设备";
		}
		if(!StringUtils.isEmpty(promptMsg)) {
			System.out.println("集合数据-->"+listFilter.toString());
			request.setAttribute("promptMsg", promptMsg);
		}
		return listFilter;
	}

	
	
	@ApiOperation(value="设备心跳",httpMethod="GET",produces="text/json")
	@ResponseBody
	@RequestMapping(value="heart",method=RequestMethod.GET)
	public Result deviceHeart(@RequestParam(value="deviceSerial",defaultValue="") String deviceSerial,
			@RequestParam(value="args",required=false,defaultValue="")String args,
			@RequestParam(value="sign",required=false,defaultValue="") String sign,
								HttpServletRequest request) {
		try {
			Map<String, String> decodParam = decodParam(args,sign);
			if(decodParam.size()>0) {
				deviceSerial=decodParam.get("deviceSerial");
				deviceSerial=deviceSerial==null?"":deviceSerial;
				l.info("deviceSerial--->"+deviceSerial);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Result.instance(ResponseCode.decode_err.getCode(), ResponseCode.decode_err.getMsg());
		}
		
		if(StringUtils.isEmpty(deviceSerial)) {
			return Result.instance(ResponseCode.missing_parameter.getCode(), ResponseCode.missing_parameter.getMsg());
		}
		String ip = request.getRemoteAddr();
		Long result = mSysDeviceMapper.updateDeviceTimeById(deviceSerial,ip);
		if(result>0) {
			return Result.success();
		}else {
			return Result.instance(ResponseCode.device_not_exist.getCode(),ResponseCode.device_not_exist.getMsg());
		}
	}
	
	@ResponseBody
	@RequestMapping(value="encode",method=RequestMethod.POST)
	@SecurityParameter(inDecode=true)
	public Result testEncodeParam(/*@RequestBody SysDevice sysDevice*/
			@RequestParam(value="deviceSerial",required=false,defaultValue="")String deviceSerial,
			@RequestParam(value="requestData",required=false,defaultValue="")String requestData,
			@RequestParam(value="sign",required=false,defaultValue="") String sign) {
		
//		log.info("deviceSerial"+sysDevice.getDeviceSerial());
		try {
			Map<String, String> decodParam = decodParam(requestData,sign);
			if(decodParam.size()>0) {
				deviceSerial=decodParam.get("deviceSerial");
				deviceSerial=deviceSerial==null?"":deviceSerial;
				log.info("deviceSerial--->"+deviceSerial);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Result.instance(ResponseCode.decode_err.getCode(), ResponseCode.decode_err.getMsg());
		}
	
		log.info("deviceSerial"+deviceSerial);
		return Result.success(); 
	}
	
	
	
	
	
	
}
