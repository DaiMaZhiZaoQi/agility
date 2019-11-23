package com.hunt.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;
import org.junit.validator.PublicClassValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hunt.dao.SysUserMapper;
import com.hunt.model.dto.PageInfo;
import com.hunt.model.dto.SysContactUserDto;
import com.hunt.model.dto.SysUserOrgDto;
import com.hunt.model.dto.UpFileResultDto;
import com.hunt.model.entity.SysContact;
import com.hunt.model.entity.SysContactUser;
import com.hunt.model.entity.SysDeviceRoleOrg;
import com.hunt.model.entity.SysUser;
import com.hunt.service.DeviceContactService;
import com.hunt.service.DeviceManageService;
import com.hunt.service.SystemService;
import com.hunt.util.DateUtil;
import com.hunt.util.PermissionUtil;
import com.hunt.util.ResponseCode;
import com.hunt.util.Result;
import com.hunt.util.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@Api("通讯录管理")
@RequestMapping("contact")
public class DeviceContactControler extends BaseController{

	@Autowired
	private SysUserMapper mSysUserMapper;
	@Autowired
	private SystemService mSystemService;
	
	@Autowired
	private DeviceContactService mDeviceContactService;
	
	@Autowired
	private DeviceManageService mDeviceManageService;
	
	
	@ApiOperation(value="通讯录管理",produces="text/html",httpMethod="GET")
	@RequestMapping(value="contact")
	public String contact() {
		return "home/contactManage";
	}
	
	/**
	 * 上传文件，上传成功则返回文件id,及文件名 ,
	 * @return
	 */
	@ApiOperation(value="pc端上传企业通讯录",produces="application/json",httpMethod="POST")
	@RequestMapping(value="upFile",method=RequestMethod.POST)
	@ResponseBody
	public Result upFile(@RequestParam("file") MultipartFile[] mf,
						@RequestParam("userId") Long userId,
//						@RequestParam(value="sysFileName",required=false,defaultValue="") String sysFileName,//  修改该文件时的该文件名 
						@RequestParam(value="contactId",required=false,defaultValue="") Long contactId,
						HttpServletRequest request)  {
		if(mf==null||mf.length==0) {
			return new Result(ResponseCode.missing_parameter.getCode(),"缺少文件");
		}
		System.out.println("文本内容-->"+Arrays.toString(mf)); 
		for(int i=0;i<mf.length;i++) {
//			for(MultipartFile mpt:mf) {
				File absoFile=null;
				String fileName="";
				SysContact sysContact=null;
				if(contactId!=null&&contactId>0) {
					 sysContact= mDeviceContactService.selectContact(contactId);
					String absolutePath = sysContact.getAbsolutePath();
					fileName=sysContact.getOriFileName();
					absoFile=new File(absolutePath);
					
				}else {
					if(i>0)return new Result(ResponseCode.missing_parameter.getCode(),"缺少文件");
					SysUser sysUser = mSysUserMapper.selectById(userId);
					String fileTop=sysUser.getLoginName()+"_"+userId;
					fileName= mf[0].getOriginalFilename();
					String fileType=fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase().trim();
					if(!fileType.equals("csv"))return new Result(ResponseCode.missing_parameter.getCode(),"不支持的格式,请联系管理员");
					absoFile = createFile(fileTop, request, fileType);
				}
			
				//  获取文件保存的真实路径
				try { 
					mf[0].transferTo(absoFile);
					//  存入数据库，并加入缓存
					if(sysContact==null) { 
						sysContact = new SysContact(null,"",absoFile.getName(),fileName,"",absoFile.getAbsolutePath(),userId,userId);
					}
					
					Result result = mDeviceContactService.insertContact(sysContact,userId);
					if(result.getCode()==ResponseCode.success.getCode()) {
						/*mDeviceContactService.selectContact(contactId);*/
						UpFileResultDto upFileResultDto = new UpFileResultDto();
						Integer status = sysContact.getStatus();
						upFileResultDto.setStatus(status);
						upFileResultDto.setId(sysContact.getId());
						upFileResultDto.setSysFileName(absoFile.getName());
						upFileResultDto.setFileName(fileName);
						String fileUrl=request.getContextPath()+"/contact/download?contactId="+sysContact.getId();
						upFileResultDto.setFileUrl(fileUrl);
						return Result.success(upFileResultDto);
					}else {
						return result;
					}
				} catch (Exception e) {
					
					e.printStackTrace();
					return new Result(ResponseCode.error.getCode(),"文件上传失败");
				}
		}
		return Result.error("上传失败");
	}
	
	@ApiOperation(value="移动端上传企业通讯录",produces="application/json",httpMethod="POST")
	@ResponseBody
	@RequestMapping(value="mUpContact",method=RequestMethod.POST)
	public Result mobileUpFile(@RequestParam(value="deviceSerial",defaultValue="") String deviceSerial,
								@RequestParam(value="file",defaultValue="")MultipartFile[] files,
								@RequestParam(value="synCode",defaultValue="")String synCode,
								@RequestParam(value="synCode2",defaultValue="")String synCode2,
								HttpServletRequest request) {
		if(!StringUtils.isEmpty(deviceSerial)) {
			SysDeviceRoleOrg sysDeviceRoleOrg = mDeviceManageService.selectBySerial(deviceSerial);
			Long userId=sysDeviceRoleOrg.getSysUserId();
			if(files.length<=0) {
				return new Result(ResponseCode.missing_parameter.getCode(),"缺少文件");
			}
			if(StringUtils.isEmpty(synCode)||synCode.length()<4) {
				return new Result(ResponseCode.file_sych_err.getCode(),ResponseCode.file_sych_err.getMsg());
			}else {
				if(!StringUtils.isEmpty(synCode2)) {
					if(!StringUtil.isMatcher(synCode, synCode2)) {
						return new Result(ResponseCode.file_sych_err.getCode(),ResponseCode.file_sych_err.getMsg());
					}
				}else {
					if(!StringUtil.isNoChCorrect(synCode)) {
						return new Result(ResponseCode.file_sych_err.getCode(),ResponseCode.file_sych_err.getMsg());
					}
				} 
			}
			SysUser sysUser = mSysUserMapper.selectById(userId);
			String fileTop=sysUser.getLoginName()+"_"+userId;
			String fileName= files[0].getOriginalFilename();
			String fileType=fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase().trim();
			if(!fileType.equals("csv"))return new Result(ResponseCode.missing_parameter.getCode(),"不支持的格式,请联系管理员");
			File absoFile = createFile(fileTop, request, fileType);
			try {
				files[0].transferTo(absoFile);
				String contactName=absoFile.getName();
				String filePath=absoFile.getAbsolutePath();
				SysContact sysContact = new SysContact(0l, "",contactName, fileName,synCode,filePath, userId, userId);
				return mDeviceContactService.mInsertContact(sysContact, userId);
//				return Result.success(sysContact);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			return new Result(ResponseCode.missing_parameter.getCode(),"缺少序列号");
		}
		return Result.error();
	}
	
	  
	
	/**
	 * 上传企业通讯录时，同时修改 sys_contact,sys_permission,上传通讯录后，新增权限   权限码格式为 contact:contact:update。contact:contact:delete
	 *  上传成功并确认保存
	 * @param userId
	 * @param orgId
	 * @param contactCode			通讯录版本              版本号发生变化时，客户端访问会更新通讯录
	 * @param contactSychPassword	通讯录同步密码	 默认没有同步密码，设置了同步密码时，同步时需要验证同步密码
	 * @param mf
	 * @return
	 */
	@ApiOperation(value="确认上传企业通讯录",produces="text/json",httpMethod="POST")
	@RequestMapping(value="upContact",method=RequestMethod.POST)  
	@ResponseBody
	public Result upContact(@RequestParam(value="userId",defaultValue="") Long userId,
							@RequestParam(value="contactSychPassword",defaultValue="") String contactSychPassword,
							@RequestParam(value="contactId",defaultValue="") Long contactId,
							HttpServletRequest request) {
		if(userId==null||userId<=0) {
			return new Result(ResponseCode.missing_parameter.getCode(),"缺少用户id");
		}
		 
		if(contactId==null||contactId<=0) {
			return new Result(ResponseCode.missing_parameter.getCode(),"请先上传文件");
		}
//			SysUser sysUser = mSysUserMapper.selectById(userId);
			SysContactUserDto sysContactUserDto = new SysContactUserDto();
			sysContactUserDto.setSysContact(new SysContact(contactId,"","","",contactSychPassword,"",userId,userId));
			sysContactUserDto.setSysContactUser(new SysContactUser(null,userId,null));
			mSystemService.clearAuthorizationInfoCacheByUserId(userId);
			return mDeviceContactService.uploadContact(sysContactUserDto);
	}
	
	@ApiOperation(value="删除通讯录文件",httpMethod="GET",produces="application/json")
	@ResponseBody
	@RequestMapping(value="deleteContact")
	public Result deleteContact(@RequestParam(value="userId",defaultValue="0") Long userId,
								@RequestParam(value="contactId",defaultValue="0") Long contactId) {
		String permName="contact:"+contactId+":delete";
		boolean hasDataPermission = PermissionUtil.hasDataPermission(userId, permName);
		if(!hasDataPermission)return Result.instance(ResponseCode.can_not_edit.getCode(), "您没有删除权限，请联系作者");
		Result result=mDeviceContactService.updateContact(contactId, userId);
		mSystemService.clearAuthorizationInfoCacheByUserId(userId);
		return result;
	}
	
	@RequestMapping("download")
	@ResponseBody
	public void downLoad(@RequestParam("contactId") Long contactId,
						HttpServletRequest request,
						HttpServletResponse resp) {
		//  判断文件下载权限
		SysContact sysContact = mDeviceContactService.selectContact(contactId);
		if(sysContact==null) return ;
		String absolutePath = sysContact.getAbsolutePath();
		File file = new File(absolutePath);
		if(!file.exists())return;
		FileInputStream read=null;
		ServletOutputStream write=null;
		try {
			 read= new FileInputStream(file);
			 write= resp.getOutputStream();
			String mimeType = request.getServletContext().getMimeType(file.getName());
			resp.setContentType(mimeType);
			resp.setContentLength(Integer.parseInt(file.length()+""));

			String filename = file.getName();
			final String userAgent = request.getHeader("USER-AGENT");
			if (userAgent.contains("MSIE")
					|| userAgent.contains("Trident")
					|| userAgent.contains("Edge")) {//IE浏览器
				filename = URLEncoder.encode(filename, "UTF-8");
			} else if (userAgent.contains("Mozilla")) {//google,火狐浏览器
				filename = new String(filename.getBytes(), "ISO8859-1");
			} else {
				filename = URLEncoder.encode(filename, "UTF-8");//其他浏览器
			}
			resp.setHeader("Content-Disposition", "attachment;fileName="+filename);
			byte[] bs=null;
			while(read.available()>0) {
				System.out.println("readlength-->"+read.available());
				if(read.available()>10240) {
					bs=new byte[10240];
				}else {
					bs=new byte[read.available()];
				}
				read.read(bs,0,bs.length);
				write.write(bs,0,bs.length);
				
			}
//			
//			int len=-1;
//			byte[] bs=new byte[8*1024];
//			while((len=read.read(bs))!=-1) {
//				System.out.println("readlength-->"+read.available());
//				write.write(bs,0,bs.length); 
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(read!=null) {
				try {
					read.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(write!=null) {
				try {
					write.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	
	@ApiOperation(value="移动端查询所有通讯录",httpMethod="GET",produces="application/json")
	@RequestMapping(value="list",method=RequestMethod.GET)
	@ResponseBody
	public PageInfo list(@RequestParam(value="userId",defaultValue="0") Long userId,
						@RequestParam(value="deviceSerial",defaultValue="") String deviceSerial) {
		int visiteTyep=1;
		if(!StringUtils.isEmpty(deviceSerial)) {
			SysDeviceRoleOrg sysDeviceRoleOrg = mDeviceManageService.selectBySerial(deviceSerial);
			userId=sysDeviceRoleOrg.getSysUserId();
			visiteTyep=0;
		}
		List<SysContact> listContact = mDeviceContactService.selectByUserId(userId,visiteTyep);
		return new PageInfo(listContact.size(),listContact);
	}
	
	
	
	@ApiOperation(value="已授权的用户",httpMethod="GET",produces="application/json")
	@RequestMapping(value="listAuth",method=RequestMethod.GET)
	@ResponseBody
	public PageInfo listAuth(@RequestParam(value="sysContactId") Long sysContactId) {
		List<SysUserOrgDto> listUserOrgDto = mDeviceContactService.selectUserOrg(sysContactId);
		return new PageInfo(listUserOrgDto.size(), listUserOrgDto);
		
	}
	 
	@ApiOperation(value="查询未授权通讯录的用户",httpMethod="GET",produces="application/json")
	@ResponseBody
	@RequestMapping(value="listUnAuth",method=RequestMethod.GET)
	public PageInfo listUnAuth(@RequestParam(value="sysContactId") Long sysContactId,
							  @RequestParam(value="sysOrgId")Long sysOrgId,
							  @RequestParam(value="disUp",defaultValue="0")Integer disUp) {
		List<SysUserOrgDto> list = mDeviceContactService.selectUnAuth(sysContactId,sysOrgId,disUp);
		return new PageInfo(list.size(), list);
	}
	
	/**
	 * 创建一个 类似 webApp\loginName_userId\YYYYMM\dd\HHmmssSS.fileType 的文件夹
	 * @param obj
	 * @param request
	 * @param mpt
	 * @return
	 */
	private File createFile(String obj, HttpServletRequest request, String fileType) {
		//  创建文件目录及路劲(创建文件目录及路径)
		String path=request.getServletContext().getRealPath("/");	//  http 项目目录的根路径
		Long currTime=System.currentTimeMillis();
		String createFilePath = DateUtil.createFilePath(currTime);
		String realPath=obj+createFilePath;
		// 文件夹的结构 登录名称_userId/    
		String realPathParent=new File(path).getParent()+File.separator+realPath;
		File file = new File(realPathParent);
		if(!file.exists()) {
			file.mkdirs();
		}
		String timeFileName = DateUtil.getUniqueTime(currTime);
		String sdFileName=timeFileName+"."+fileType;
		File absoFile = new File(realPathParent,sdFileName);
		System.out.println("文件路径为-->"+absoFile.getAbsolutePath());
		return absoFile;
	}
	

	@ResponseBody
	@ApiOperation(value="授权通讯录",produces="application/json",httpMethod="GET")
	@RequestMapping(value="auth",method=RequestMethod.POST)
	public Result auth(
//			@RequestBody List<SysUserOrgDto> sysUserOrgDto,
			@RequestParam(value="sysUserOrgDto")String sysUserOrgDto,
						@RequestParam(value="contactId",defaultValue="0") Long contactId) {
		if(contactId==0)return Result.instance(ResponseCode.missing_parameter.getCode(), "请选择要授权的文件");
		List<SysUserOrgDto> listSysUserOrgDto=Arrays.asList(JsonUtils.readValue(sysUserOrgDto, SysUserOrgDto[].class));
		
		return  mDeviceContactService.auth(listSysUserOrgDto,contactId);
	}
	
	@ApiOperation(value="解除授权通讯录",produces="application/json",httpMethod="GET")
	@RequestMapping(value="unAuth",method=RequestMethod.POST)
	@ResponseBody
	public Result unAuth(@RequestParam(value="sysUserOrgDto")String sysUserOrgDto,
			@RequestParam(value="contactId",defaultValue="0") Long contactId) {
		if(contactId==0)return Result.instance(ResponseCode.missing_parameter.getCode(), "请选择要授权的文件");
		String permName="contact:"+contactId+":update";
		boolean hasDataPermission = PermissionUtil.hasDataPermission(null, permName);
		if(!hasDataPermission)return Result.instance(ResponseCode.can_not_edit.getCode(), "您没有该权限，请联系作者");
		List<SysUserOrgDto> listSysUserOrgDto=Arrays.asList(JsonUtils.readValue(sysUserOrgDto, SysUserOrgDto[].class));
		return mDeviceContactService.noAuth(listSysUserOrgDto,contactId);
		
	}
	
	@ResponseBody
	@RequestMapping(value="authOrg",method=RequestMethod.POST)
	@ApiOperation(value="通讯录一键授权给机构",produces="application/json",httpMethod="GET")
	public Result authOrg(@RequestParam(value="sysUserOrgDto")String sysUserOrgDto,
						 @RequestParam(value="contactId",defaultValue="0")Long contactId) {
		if(contactId==0)return Result.instance(ResponseCode.missing_parameter.getCode(), "请选择要授权的文件");
		
		List<SysUserOrgDto> listSysUserOrgDto=Arrays.asList(JsonUtils.readValue(sysUserOrgDto, SysUserOrgDto[].class));
		return mDeviceContactService.authOrg(listSysUserOrgDto,contactId);
		
	}
	
	@ApiOperation(value="解除通讯录一键授权通讯录",produces="application/json",httpMethod="GET")
	@RequestMapping(value="unAuthOrg",method=RequestMethod.POST)
	@ResponseBody
	public Result unAuthOrg(@RequestParam(value="sysUserOrgDto")String sysUserOrgDto,
			 @RequestParam(value="contactId",defaultValue="0")Long contactId) {
		if(contactId==0)return Result.instance(ResponseCode.missing_parameter.getCode(), "请选择要授权的文件");
		String permName="contact:"+contactId+":update";
		boolean hasDataPermission = PermissionUtil.hasDataPermission(null, permName);
		if(!hasDataPermission)return Result.instance(ResponseCode.can_not_edit.getCode(), "您没有该权限，请联系作者");
		List<SysUserOrgDto> listSysUserOrgDto=Arrays.asList(JsonUtils.readValue(sysUserOrgDto, SysUserOrgDto[].class));
		return mDeviceContactService.unAuthOrg(listSysUserOrgDto,contactId);
	}
	
	
	
	
	
/*	@ResponseBody
	@ApiOperation(value="解除授权",produces="application/json",httpMethod="GET")
	@RequestMapping(value="unAuth",method=RequestMethod.POST)
	public Result unAuth(@RequestParam(value="contactId",defaultValue="0")Long contactId,
						@RequestParam(value="sysUserId",defaultValue="0")Long sysUserId) {
		System.out.println("unAuth-->"+contactId+"-->"+sysUserId);
		return Result.success();
	}
	*/
	
	
}
