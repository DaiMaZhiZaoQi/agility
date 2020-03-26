package com.hunt.controller;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.javassist.expr.NewArray;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.aspectj.weaver.tools.Trace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.annotation.SessionScope;

import com.hunt.dao.SysDeviceMapper;
import com.hunt.dao.SysDeviceRoleOrgMapper;
import com.hunt.dao.SysUserInOrgMapper;
import com.hunt.dao.SysUserMapper;
import com.hunt.model.dto.LoginInfo;
import com.hunt.model.dto.PageInfo;
import com.hunt.model.entity.SysDevice;
import com.hunt.model.entity.SysDeviceRoleOrg;
import com.hunt.model.entity.SysOrganization;
import com.hunt.model.entity.SysUser;
import com.hunt.model.entity.SysUserInOrg;
import com.hunt.model.entity.SysUserRoleOrganization;
import com.hunt.service.SysUserService;
import com.hunt.service.SystemDeviceService;
import com.hunt.util.PermissionCode;
import com.hunt.util.PermissionUtil;
import com.hunt.util.ResponseCode;
import com.hunt.util.Result;
import com.hunt.util.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.jsqlparser.statement.update.Update;

@Api(value="设备列表")
@Controller
@RequestMapping("systemdevice")
public class SystemDeviceController extends BaseController{
	Logger log=LoggerFactory.getLogger(SystemDeviceController.class);
	@Autowired
	private SystemDeviceService mSysDeviceService;
	@Autowired
	private SysUserMapper mSysUserMapper;
	@Autowired
	private SysUserInOrgMapper mSysUserInOrgMapper;
	@Autowired 
	private SysUserService mSysUserService;
	@Autowired
	private SysDeviceMapper mSysDeviceMapper;
	@Autowired
	private SysDeviceRoleOrgMapper mSysDeviceRoleOrgMapper;
	
	@ApiOperation(value="设备列表静态页",httpMethod="GET",produces="text/html")
	@RequestMapping(value="device",method=RequestMethod.GET)
	public String systemDevice() {
		return "system/device";
	}
	
	
	/**
	 * 添加设备,第一版添加绑定设备，只绑定一个用户。
	 * 有用户id和角色机构id，则添加到 sys_device_role_org表中
	 * 需判断userId 是否存在
	 * @param deviceName
	 * @param deviceSerial
	 * @param description
	 * @param userId
	 * @return
	 */
	@ApiOperation(value="添加设备",httpMethod="POST",produces="application/json",response=Result.class)
	@ResponseBody
//	@RequiresPermissions("device:insert") 
	@RequestMapping(value="insert",method={RequestMethod.POST,RequestMethod.GET})
	public Result insert(@RequestParam(required=true,defaultValue="") String deviceName,
						@RequestParam(required=true,defaultValue="")  String deviceSerial,
						@RequestParam(required=false,defaultValue="我的话机")  String description,
						@RequestParam(required=false,defaultValue="") String userName,
						@RequestParam(required=false,defaultValue="") String password,
						@RequestParam(value="args",required=false,defaultValue="")String args,
						@RequestParam(value="sign",required=false,defaultValue="") String sign,
						HttpServletRequest request
						) {
		try {
			Map<String, String> decodParam = decodParam(args,sign);
			if(decodParam.size()>0) {
				deviceName=decodParam.get("deviceName");
				deviceName=deviceName==null?"":deviceName;
				deviceSerial=decodParam.get("deviceSerial");
				deviceSerial=deviceSerial==null?"":deviceSerial;
				description=decodParam.get("description");
				description=description==null?"我的话机":description;
				userName=decodParam.get("userName");
				userName=userName==null?"":userName;
				password=decodParam.get("password");
				password=password==null?"":password;
				
				log.info("deviceSerial--->"+deviceSerial);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Result.instance(ResponseCode.decode_err.getCode(), ResponseCode.decode_err.getMsg());
		}
	
		String ip = request.getRemoteAddr();
		SysDevice sysDevice= mSysDeviceMapper.selectByDeviceSerial(deviceSerial);
		if(sysDevice!=null) {						//  存在该设备
			List<SysDeviceRoleOrg> selectByDeviceId = mSysDeviceRoleOrgMapper.selectByDeviceId(sysDevice.getId());
			if(selectByDeviceId!=null&&selectByDeviceId.size()>0) {		//  存在该设备已绑定
				SysDeviceRoleOrg sysDeviceRoleOrg = selectByDeviceId.get(0);
				Long sysUserId = sysDeviceRoleOrg.getSysUserId();
				SysUser userDb = mSysUserMapper.selectById(sysUserId);
				if (userDb.getLoginName().equals(userName)) {	// 用户已绑定
					 Boolean checkPassWord = checkPassWord(userDb,password);
					 if(!checkPassWord) {
						 return Result.instance(ResponseCode.password_incorrect.getCode(), ResponseCode.password_incorrect.getMsg());
					 }else {
						 updateHeart(request,deviceSerial);
						 return Result.instance(ResponseCode.success.getCode(), "连接成功");
					}
				}else {				//  用户已绑定，解绑原来的绑定
				
				}
			}
			
			//  验证用户名密码，判断用户添加设备权限  重新绑定用户
			SysUser user = mSysUserMapper.selectUserByLoginName(userName);
			if(user==null) {
				return Result.instance(ResponseCode.name_no_exist.getCode(), ResponseCode.name_no_exist.getMsg());
			}else {
				SysUserInOrg sysUserInOrg = mSysUserInOrgMapper.selectByUserId(user.getId());
				 if(sysUserInOrg==null) {
					 return Result.instance(ResponseCode.user_no_in_org.getCode(), ResponseCode.user_no_in_org.getMsg());
				 }
				 Boolean checkPassWord = checkPassWord(user,password);
				 if(!checkPassWord) {
					 return Result.instance(ResponseCode.password_incorrect.getCode(), ResponseCode.password_incorrect.getMsg());
				 }else {
					if(!mobileHasPermission(user.getId(), PermissionCode.DEVICE_INSERT.pName)) {
						return Result.instance(PermissionCode.DEVICE_INSERT.pCode, PermissionCode.DEVICE_INSERT.pMsg);
					}else {
						sysUserInOrg = mSysUserInOrgMapper.selectByUserId(user.getId());
						sysDevice.setSysOrgId(sysUserInOrg.getSysOrgId());
						sysDevice.setSysOrgCode(sysUserInOrg.getSysOrgCode());
						
						sysDevice.setDeviceMsg(ip);
						Long updateDevice = mSysDeviceService.updateDevice(sysUserInOrg.getSysOrgId(),sysDevice,user.getId());
						if(updateDevice>0) {
							return Result.instance(ResponseCode.success.getCode(), "连接成功");
						}
					}
				}
				
			}
		}else {				//  不存在该设备
			//  验证用户名密码，判断用户添加设备权限
			SysUser user = mSysUserMapper.selectUserByLoginName(userName);
			if(user==null) {
				return Result.instance(ResponseCode.name_no_exist.getCode(), ResponseCode.name_no_exist.getMsg());
			}else {
				SysUserInOrg sysUserInOrg = mSysUserInOrgMapper.selectByUserId(user.getId());
				 if(sysUserInOrg==null) {
					 return Result.instance(ResponseCode.user_no_in_org.getCode(), ResponseCode.user_no_in_org.getMsg());
				 }
				 Boolean checkPassWord = checkPassWord(user, password);
				 if(!checkPassWord) {
					 return Result.instance(ResponseCode.password_incorrect.getCode(), ResponseCode.password_incorrect.getMsg());
				 }else {
					 if(!mobileHasPermission(user.getId(), PermissionCode.DEVICE_INSERT.pName)) {
							return Result.instance(PermissionCode.DEVICE_INSERT.pCode, PermissionCode.DEVICE_INSERT.pMsg);
						}
						SysDevice sysDeviceN = new SysDevice();
						sysDeviceN.setDeviceName(deviceName);
				        sysDeviceN.setCreateBy(user.getId());
				        sysDeviceN.setCreateBy(user.getId());
				        sysDeviceN.setDeviceSerial(deviceSerial);
				        sysDeviceN.setDescription(description);
						sysDeviceN.setStatus(1);
						sysDeviceN.setDeviceMsg(ip);
						Long deviceId = mSysDeviceService.insertDevice(sysDeviceN,user.getId(),sysUserInOrg);
						if(deviceId<=0) {
							Result result = new Result(30003,"序列号已存在");
							return result;
						}
				}
			
			}
		}
		 return Result.instance(ResponseCode.success.getCode(), "连接成功");  //  TODO 返回接口版本
	}
	

	
	
	/**
	 * 修改设备绑定
	 * @param deviceName		设备名称
	 * @param deviceSerial		设备序列号
	 * @param description		设备描述
	 * @param sysDeviceRoleOrgId 	sys_device_role_org	表的主id 身兼多职
	 * @param sysRoleOrgId		角色机构id		身兼多职
	 * @param userId			待修改的用户id
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value="修改设备",httpMethod="POST",produces="application/json",response=Result.class)
	@RequestMapping(value="update",method=RequestMethod.POST)
	public Result update(@RequestParam(value="deviceName") String deviceName,
			@RequestParam(value="deviceSerial",required=false,defaultValue="")   String deviceSerial,
			@RequestParam(required=false)   Long deviceId,
			@RequestParam(required=false)  String description,
			@RequestParam(required=false)	Long sysOrgId,
			@RequestParam(value="userName",required=false,defaultValue="") String userName, 
			@RequestParam(value="passWord",required=false) String password,
			@RequestParam(required=false)  Long userId) {
		
		log.info("update-->"+sysOrgId);
		if(StringUtils.isEmpty(deviceSerial)) {
			return Result.instance(ResponseCode.missing_parameter.getCode(), "缺少机器序列号");
		}
		if(deviceId==null||deviceId==0) {
			return Result.error(ResponseCode.data_not_exist.getMsg());
		} 
		
		SysUserInOrg sysUserInOrg=null;
		SysUser user=null;
		if(!StringUtils.isEmpty(userName)) {	//  自动绑定
					//  验证用户名密码，判断用户添加设备权限
					user = mSysUserMapper.selectUserByLoginName(userName);
					if(user==null) {
						return Result.instance(ResponseCode.name_no_exist.getCode(), ResponseCode.name_no_exist.getMsg());
					}else {
						userId=user.getId();
						 sysUserInOrg = mSysUserInOrgMapper.selectByUserId(userId);
						 if(sysUserInOrg==null) {
							 return Result.instance(ResponseCode.user_no_in_org.getCode(), ResponseCode.user_no_in_org.getMsg());
						 }
						String passwordSalt = user.getPasswordSalt();
						String userDbPassWord = user.getPassword();
						String createPassword = StringUtil.createPassword(password, passwordSalt, 2);
						if(!createPassword.equals(userDbPassWord)) {	// 密码不正确
							return Result.instance(ResponseCode.password_incorrect.getCode(), ResponseCode.password_incorrect.getMsg());
						} else {										//查询用户设备添加权限
							if(!mobileHasPermission(user.getId(), PermissionCode.DEVICE_INSERT.pName)) {
								return Result.instance(PermissionCode.DEVICE_INSERT.pCode, PermissionCode.DEVICE_INSERT.pMsg);
							}
						}
					}
			
			return mSysDeviceService.autoBind(deviceSerial, deviceName, user,sysUserInOrg);
		}
		SysDevice sysDevice = new SysDevice();
		sysDevice.setId(deviceId);
		sysDevice.setDeviceName(deviceName);
        Subject subject = SecurityUtils.getSubject();
        LoginInfo loginInfo=(LoginInfo)subject.getSession().getAttribute("loginInfo");
        Long createId = loginInfo.getId(); 
        sysDevice.setCreateBy(createId);
		sysDevice.setUpdateBy(createId);
		sysDevice.setDeviceSerial(deviceSerial);
		sysDevice.setDescription(description);
		sysDevice.setStatus(1);
		boolean hasDataPermission = PermissionUtil.hasDataPermission(userId, "device:"+userId+":update");
		if(!hasDataPermission)return Result.instance(ResponseCode.no_permission.getCode(), ResponseCode.no_permission.getMsg());
		Long deviceUpdateId = mSysDeviceService.updateDevice(sysOrgId,sysDevice,userId);
		if(deviceUpdateId==null) {
			Result result=new Result(ResponseCode.data_not_exist.getCode(),"设备id未绑定");
			return result;
		}
		if(deviceUpdateId<=0) {
			Result result = new Result(30003,"序列号已存在");
			
			return result;
		}
		return Result.success(deviceUpdateId);
	}
	
	/**
	 * 
	 * @param deviceName
	 * @param deviceSerial
	 * @param deviceId
	 * @param description
	 * @param sysOrgId
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value="修改设备",httpMethod="POST",produces="application/json",response=Result.class)
	@RequestMapping(value="unBindDevice",method=RequestMethod.POST)
	public Result unBindDevice(@RequestParam(value="deviceName",required=false) String deviceName,
			@RequestParam(required=false)   String deviceSerial,
			@RequestParam(value="deviceId",required=true,defaultValue="0")   Long deviceId,
			@RequestParam(required=false)  String description,
			@RequestParam(value="sysOrgId",required=true)	Long sysOrgId,
			@RequestParam(value="userId",required=true)  Long userId) {
		log.info("update-->"+sysOrgId);
		if(deviceId==0) {
			return Result.error(ResponseCode.data_not_exist.getMsg());
		} 
		SysDevice sysDevice = new SysDevice();
		sysDevice.setId(deviceId);
//		sysDevice.setDeviceName(deviceName);
        Subject subject = SecurityUtils.getSubject();
        LoginInfo loginInfo=(LoginInfo)subject.getSession().getAttribute("loginInfo");
        Long createId = loginInfo.getId(); 
//        sysDevice.setCreateBy(createId);
		sysDevice.setUpdateBy(createId);
//		sysDevice.setDeviceSerial(deviceSerial);
		sysDevice.setDescription(description);
		sysDevice.setStatus(0);
		boolean hasDataPermission = PermissionUtil.hasDataPermission(userId, "device:"+userId+":update");
		if(!hasDataPermission)return Result.instance(ResponseCode.no_permission.getCode(), ResponseCode.no_permission.getMsg());
		return mSysDeviceService.unBindDevice(sysOrgId,sysDevice,userId);
		
	}
	
	/**
	 * 浏览设备
	 * @param page	页数
	 * @param rows	每页显示的行数
	 * @param sort	排序字段，默认使用deviceName字段排序
	 * @param order	排序方式正序，还是倒序
	 * @param deviceName 设备名称  搜索时用到的字段
	 * @param deviceSerial	设备序列号	搜索时的字段
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions(value="device:list")
	@RequestMapping(value="list",method=RequestMethod.POST)
	public PageInfo list(@RequestParam(defaultValue="1") int page, 
						@RequestParam(defaultValue="15") int rows,
						@RequestParam(defaultValue="deviceName") String sort,
						@RequestParam(defaultValue="asc") String order,
						@RequestParam(defaultValue="",required=false) String deviceName,
						@RequestParam(defaultValue="",required=false) String deviceSerial
						) {
//		Subject subject = SecurityUtils.getSubject();
//		LoginInfo loginInfo = (LoginInfo)subject.getSession().getAttribute("loginInfo");
//		List<SysUserRoleOrganization> jobs = loginInfo.getJobs();
//		List<Long> listRoleOrgId=new ArrayList<>();
//		for(SysUserRoleOrganization userRoleOrg:jobs) {
//			Long sysRoleOrganizationId = userRoleOrg.getSysRoleOrganizationId();
//			//  TODO  查询对应职能下的设备
//		}s
//		System.out.println("loginInfo-->"+loginInfo.toString());
//		SessionScope.get("loginInfo").jobs 
		//  数据总条数
		//	数据总页数
		
		
		PageInfo pageInfo= mSysDeviceService.selectPage(page, rows,StringUtil.camelToUnderline(sort),order,deviceName,deviceSerial);
		return pageInfo;
		
		
	}
	
	
	
	
}
