package com.hunt.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.junit.validator.PublicClassValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.hunt.dao.SysDeviceMapper;
import com.hunt.dao.SysDeviceRoleOrgMapper;
import com.hunt.dao.SysOrganizationMapper;
import com.hunt.dao.SysUserInOrgMapper;
import com.hunt.dao.SysUserMapper;
import com.hunt.model.dto.PageDto;
import com.hunt.model.dto.PageInfo;
import com.hunt.model.dto.SysContactDto;
import com.hunt.model.dto.SysContactUserDto;
import com.hunt.model.dto.SysUserGroupDto;
import com.hunt.model.dto.SysUserOrgDto;
import com.hunt.model.dto.UpFileResultDto;
import com.hunt.model.entity.SysContact;
import com.hunt.model.entity.SysContactUser;
import com.hunt.model.entity.SysDevice;
import com.hunt.model.entity.SysDeviceRoleOrg;
import com.hunt.model.entity.SysOrganization;
import com.hunt.model.entity.SysUser;
import com.hunt.model.entity.SysUserInOrg;
import com.hunt.properties.PropertiesUtil;
import com.hunt.service.DeviceContactService;
import com.hunt.service.DeviceManageService;
import com.hunt.service.SystemService;
import com.hunt.util.AESCipher;
import com.hunt.util.DateUtil;
import com.hunt.util.PermissionCode;
import com.hunt.util.PermissionUtil;
import com.hunt.util.ResponseCode;
import com.hunt.util.Result;
import com.hunt.util.StringUtil;
import com.hunt.util.UtReadCsv;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.jsqlparser.statement.select.Skip;

@Controller
@Api("通讯录管理")
@RequestMapping("contact")
public class DeviceContactControler extends BaseController{
	 Logger log = LoggerFactory.getLogger(BaseController.class);
	@Autowired
	private SysUserMapper mSysUserMapper;
	
	@Autowired
	private SysUserInOrgMapper mSysUerInOrgMapper;
	@Autowired
	private SysOrganizationMapper mSysOrganizationMapper;
	
	@Autowired
	private SystemService mSystemService;
	
	@Autowired
	private DeviceContactService mDeviceContactService;
	
	@Autowired
	private DeviceManageService mDeviceManageService;
	@Autowired
	private SysDeviceMapper mSysDeviceMapper;
	@Autowired
	private SysDeviceRoleOrgMapper mSysDeviceRoleOrgMapper;
	
	@Autowired
	private PropertiesUtil mPropertiesUtil;
	
	
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
						@RequestParam(value="contactId",required=false,defaultValue="") Long contactId,
						HttpServletRequest request)  {
		if(!mobileHasPermission(userId, PermissionCode.CONTACT_INSERT.pName)) {
			return Result.instance(PermissionCode.CONTACT_INSERT.pCode, PermissionCode.CONTACT_INSERT.pMsg);
		}
		if(mf==null||mf.length==0) {  
			return new Result(ResponseCode.missing_parameter.getCode(),"缺少文件");
		}
		System.out.println("文本内容-->"+Arrays.toString(mf)); 
		for(int i=0;i<mf.length;i++) {
//			for(MultipartFile mpt:mf) {
				File absoFile=null;
				String fileName="";
				SysContact sysContact=null;
				SysUserInOrg sysUserInOrg = mSysUerInOrgMapper.selectByUserId(userId);
				SysOrganization sysOrganization = mSysOrganizationMapper.selectById(sysUserInOrg.getSysOrgId());
				if(contactId!=null&&contactId>0) {
					 sysContact= mDeviceContactService.selectContactNoStatus(contactId);
					String absolutePath = sysContact.getAbsolutePath();
					fileName=sysContact.getOriFileName();
					absoFile=new File(absolutePath);
					
				}else {
					if(i>0)return new Result(ResponseCode.missing_parameter.getCode(),"缺少文件");
//					SysUser sysUser = mSysUserMapper.selectById(userId);
				
//					String fileTop=sysUser.getLoginName()+"_"+userId; 
					String fileTop=sysOrganization.getName()+"_"+sysOrganization.getId();
					fileName= mf[0].getOriginalFilename();
					String fileType=fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase().trim();
					if(!fileType.equals("csv"))return new Result(ResponseCode.missing_parameter.getCode(),"不支持的格式,请上传csv文件");
					absoFile = createFile(fileTop, request, fileType,mPropertiesUtil.getCsvFilePath());
				}
			
				//  获取文件保存的真实路径
				try { 
					if(absoFile==null)return Result.error(ResponseCode.file_config_fail.getMsg());
					mf[0].transferTo(absoFile);
					String enCodeType= UtReadCsv.getFileEncode2(absoFile.getAbsolutePath());
					BufferedReader read =null;
					BufferedWriter writer=null;
					FileOutputStream fOut=null;
					FileInputStream fRead=null;
					File newFile=null;
					try {
						if(!"utf-8".equals(enCodeType)) {
							System.out.println("编码格式-->"+enCodeType);
							read= new BufferedReader(new InputStreamReader(new FileInputStream(absoFile),enCodeType));
							String str="";
							
							String parent = absoFile.getParent();
							
							newFile=new File(parent,"TEMP"+System.currentTimeMillis()+".csv");
							writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFile),"UTF-8"));
							
							while((str=read.readLine())!=null) { 
//								System.out.println("读物文件--->str"+str);
								writer.write(str+"\n");
							}
							writer.flush();
							String name = absoFile.getName(); 
							absoFile.delete();
							File fileResult = new File(parent,name);
							fOut= new FileOutputStream(fileResult);
							fRead = new FileInputStream(newFile);
							int len=0;
							byte[] bs=new byte[1024*8];
							while((len=fRead.read(bs))!=-1) {
								fOut.write(bs,0,bs.length);
							}
							fOut.flush();
							fRead.close();
							
							absoFile=fileResult;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}finally {
						if(read!=null) {
							read.close();
						}
						if(writer!=null) {
							writer.close();
						}
						if(fOut!=null) {
							fOut.close();
						}
						if(fRead!=null) {
							fRead.close();
						}
						if(newFile!=null) {
							newFile.delete();
							if(newFile.exists()) {
								System.out.println("文件-->"+newFile.getAbsolutePath());
							}
						}
					}
				
					//  存入数据库，并加入缓存
					if(sysContact==null) { 
						sysContact = new SysContact(null,"",absoFile.getName(),fileName,"",absoFile.getAbsolutePath(),userId,userId);
					}
					
					Result result = mDeviceContactService.insertContact(sysContact,userId,sysOrganization);
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
			if(!mobileHasPermission(userId, PermissionCode.CONTACT_INSERT.pName)) {
				return Result.instance(PermissionCode.CONTACT_INSERT.pCode, PermissionCode.CONTACT_INSERT.pMsg);
			}
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
			SysUserInOrg sysUserInOrg = mSysUerInOrgMapper.selectByUserId(userId);
			SysOrganization sysOrganization = mSysOrganizationMapper.selectById(sysUserInOrg.getSysOrgId());
//			SysUser sysUser = mSysUserMapper.selectById(userId);
			String fileTop=sysOrganization.getName()+"_"+sysOrganization.getId();
			String fileName= files[0].getOriginalFilename();
			String fileType=fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase().trim();
			if(!fileType.equals("csv"))return new Result(ResponseCode.missing_parameter.getCode(),"不支持的格式,请上传csv文件");
			File absoFile = createFile(fileTop, request, fileType,mPropertiesUtil.getCsvFilePath());
			try {
				if(absoFile==null)return Result.error(ResponseCode.file_config_fail.getMsg());
				files[0].transferTo(absoFile);
				String contactName=absoFile.getName();
				String filePath=absoFile.getAbsolutePath();
				SysContact sysContact = new SysContact(0l, "",contactName, fileName,synCode,filePath, userId, userId);
				return mDeviceContactService.mInsertContact(sysContact, userId,sysOrganization);
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
		if(!mobileHasPermission(userId, PermissionCode.CONTACT_INSERT.pName)) {
			return Result.instance(PermissionCode.CONTACT_INSERT.pCode, PermissionCode.CONTACT_INSERT.pMsg);
		}
//			SysUser sysUser = mSysUserMapper.selectById(userId);
			SysContactUserDto sysContactUserDto = new SysContactUserDto();
			sysContactUserDto.setSysContact(new SysContact(contactId,"","","",contactSychPassword,"",userId,userId));
			SysUserInOrg sysUserInOrg = mSysUerInOrgMapper.selectByUserId(userId);
			SysContactUser sysContactUser = new SysContactUser(contactId,sysUserInOrg.getSysOrgId());
			sysContactUser.setSysUserId(userId);
			sysContactUserDto.setSysContactUser(sysContactUser);
//			mSystemService.clearAuthorizationInfoCacheByUserId(userId);
			return mDeviceContactService.uploadContact(sysContactUserDto);
	}
	
	@ApiOperation(value="删除通讯录文件",httpMethod="GET",produces="application/json")
	@ResponseBody
	@RequestMapping(value="deleteContact")
	public Result deleteContact(@RequestParam(value="userId",defaultValue="0") Long userId,
								@RequestParam(value="contactId",defaultValue="0") Long contactId) {
		if(!mobileHasPermission(userId, PermissionCode.CONTACT_DELETE.pName)) {
			return Result.instance(PermissionCode.CONTACT_DELETE.pCode, PermissionCode.CONTACT_DELETE.pMsg);
		}
		Result result=mDeviceContactService.updateContact(contactId, userId);
//		mSystemService.clearAuthorizationInfoCacheByUserId(userId);
		return result;
	}
	
	@RequestMapping("download")
	@ResponseBody
	public Result downLoad(@RequestParam("contactId") Long contactId,
						 @RequestParam(value="deviceSerial",required=false,defaultValue="0") String deviceSerial,
						 @RequestParam(value="userId",required=false,defaultValue="0") Long userId,
						 @RequestParam(value="password",required=false,defaultValue="") String password,
						 @RequestParam(value="passwordOk",required=false,defaultValue="0") String passwordOk,
							@RequestParam(value="args",required=false,defaultValue="")String args,
							@RequestParam(value="sign",required=false,defaultValue="") String sign,
						HttpServletRequest request,
						HttpServletResponse resp) {
		try {
			Map<String, String> decodParam = decodParam(args,sign);
			if(decodParam.size()>0) {
				deviceSerial=decodParam.get("deviceSerial");
				deviceSerial=deviceSerial==null?"":deviceSerial;
				password=decodParam.get("password");
				password=password==null?"":password;
				log.info("deviceSerial--->"+password);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Result.instance(ResponseCode.decode_err.getCode(), ResponseCode.decode_err.getMsg());
		}
		//  判断文件下载权限
		if(!"0".equals(deviceSerial)&&"0".equals(userId+"")) {		//   移动端
			//  移动端下载
			SysDevice sysDevice= mSysDeviceMapper.selectByDeviceSerial(deviceSerial);
			if(sysDevice!=null) {						//  存在该设备
				List<SysDeviceRoleOrg> selectByDeviceId = mSysDeviceRoleOrgMapper.selectByDeviceId(sysDevice.getId());
				if(selectByDeviceId!=null&&selectByDeviceId.size()>0) {		//  存在该设备已绑定
					SysDeviceRoleOrg sysDeviceRoleOrg = selectByDeviceId.get(0);
					Long sysUserId = sysDeviceRoleOrg.getSysUserId();
					if(!mobileHasPermission(sysUserId, PermissionCode.CONTACT_SELECT.pName)) {
						return Result.instance(PermissionCode.CONTACT_SELECT.pCode, PermissionCode.CONTACT_SELECT.pMsg);
					}
					passwordOk="1";
					updateHeart(request,deviceSerial);
				}
			}else {
				return Result.instance(ResponseCode.device_not_exist.getCode(), ResponseCode.device_not_exist.getMsg());
			}
		}else if("0".equals(deviceSerial)&&!"0".equals(userId+"")){			//  web端
			//  pc端下载
			if(!mobileHasPermission(userId, PermissionCode.CONTACT_SELECT.pName)) {
				return Result.instance(PermissionCode.CONTACT_SELECT.pCode, PermissionCode.CONTACT_SELECT.pMsg);
			}
		}else {
			return Result.instance(ResponseCode.missing_parameter.getCode(), "缺少参数，设备序列号deviceSerial");
		}
		
		SysContact sysContact = mDeviceContactService.selectContact(contactId);
		if(sysContact==null)return Result.instance(ResponseCode.file_not_exist.getCode(), ResponseCode.file_not_exist.getMsg());
		String absolutePath = sysContact.getAbsolutePath();
		File file = new File(absolutePath);
		boolean passDownLoad=false;
		if(!file.exists())return Result.instance(ResponseCode.file_not_exist.getCode(), ResponseCode.file_not_exist.getMsg());
		if(!StringUtils.isEmpty(password)) {  // 密码验证
			String contactSychPassword = sysContact.getContactSychPassword();
			if(!password.equals(contactSychPassword)) {
				return Result.instance(ResponseCode.password_incorrect.getCode(), ResponseCode.password_incorrect.getMsg());
			}
			if("0".equals(passwordOk)) {
				return Result.success();
			}
			file = AESCipher.deFile(absolutePath, contactSychPassword);
			passDownLoad=true;
		}
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
			resp.setHeader("Content-Disposition", "attachment;fileName="+sysContact.getOriFileName());
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
			if(passDownLoad) {
				file.delete();
			}
		}
		return Result.success();
		
	}
	
	
	
	@ApiOperation(value="移动端查询所有通讯录",httpMethod="GET",produces="application/json")
	@RequestMapping(value="list",method= {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public PageInfo list(@RequestParam(value="userId",defaultValue="0") Long userId,
						@RequestParam(value="deviceSerial",defaultValue="") String deviceSerial,
						@RequestParam(value="pageDto",defaultValue="",required=false) String pageDtoJson,
						@RequestParam(value="args",required=false,defaultValue="")String args,
						@RequestParam(value="sign",required=false,defaultValue="") String sign,
						HttpServletRequest request
						) {
		try {
			Map<String, String> decodParam = decodParam(args,sign);
			if(decodParam.size()>0) {
				deviceSerial=decodParam.get("deviceSerial");
				deviceSerial=deviceSerial==null?"":deviceSerial;
				log.info("deviceSerial--->"+deviceSerial);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new PageInfo(ResponseCode.decode_err.getCode(), ResponseCode.decode_err.getMsg());
		}
		
		int visiteTyep=1;
		if(!StringUtils.isEmpty(deviceSerial)) {
			SysDeviceRoleOrg sysDeviceRoleOrg = mDeviceManageService.selectBySerial(deviceSerial);
			if(sysDeviceRoleOrg==null) {
				PageInfo pageInfo=new PageInfo(ResponseCode.device_not_bind.getCode(), new ArrayList<>());
				pageInfo.setOtherRows("设备没有绑定到用户,或者设备已删除");
				return pageInfo;
			}
			userId=sysDeviceRoleOrg.getSysUserId();
			visiteTyep=0;
		}else if("0".equals(userId+"")){
			return new PageInfo(0, ResponseCode.missing_parameter.getMsg()+",deviceSerial");
		}
		if(!mobileHasPermission(userId, PermissionCode.CALLLOG_SELECT.pName)) {
			return new PageInfo(PermissionCode.CALLLOG_SELECT.pCode,Result.instance(PermissionCode.CALLLOG_SELECT.pCode, PermissionCode.CALLLOG_SELECT.pMsg));
		}
		
		if(visiteTyep==1) {
			PageDto pageDto = JsonUtils.readValue(pageDtoJson, PageDto.class);
			Integer total=0;
			if(pageDto!=null) {
				if(pageDto.getPage()==1) {	// 首次加载
					 total= mDeviceContactService.selectTotal(userId);
				}
			}
			List<SysContactDto> listSysContactDto = mDeviceContactService.selectByOrgId2(userId, pageDto);
			return new PageInfo(total==0?listSysContactDto.size():total, listSysContactDto);
			
		}
		
		if(visiteTyep==0) {
			updateHeart(request,deviceSerial);
		}
		
//		List<SysContact> listContact = mDeviceContactService.selectByUserId(userId,visiteTyep);
		List<SysContactDto> listContact = mDeviceContactService.selectByOrgId(userId);
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
	
	

//	@ResponseBody
//	@ApiOperation(value="授权通讯录",produces="application/json",httpMethod="GET")
//	@RequestMapping(value="auth",method=RequestMethod.POST)
//	public Result auth(
////			@RequestBody List<SysUserOrgDto> sysUserOrgDto,
//			@RequestParam(value="sysUserOrgDto")String sysUserOrgDto,
//						@RequestParam(value="contactId",defaultValue="0") Long contactId) {
//		if(contactId==0)return Result.instance(ResponseCode.missing_parameter.getCode(), "请选择要授权的文件");
//		List<SysUserOrgDto> listSysUserOrgDto=Arrays.asList(JsonUtils.readValue(sysUserOrgDto, SysUserOrgDto[].class));
//		
//		return  mDeviceContactService.auth(listSysUserOrgDto,contactId);
//	}
	
	@ResponseBody
	@ApiOperation(value="授权通讯录",produces="application/json",httpMethod="POST")
	@RequestMapping(value="auth",method=RequestMethod.POST)
	public Result authContact(
			  @RequestParam(value="contactId",required=true) Long contactId,
			  @RequestParam(value="sysUserId",required=true) Long sysUserId,
//			  @RequestParam(value="listSysOrgPerStr",required=true,defaultValue="")String listSysOrgPerStr,
			  @RequestParam(value="listSysUserInOrg",required=false,defaultValue="") String listSysUserInOrgStr,
			  @RequestParam(value="listSysUserInOrgAll",required=false,defaultValue="")String listSysUserInOrgAllStr
			) {
			  
			if(StringUtils.isEmpty(listSysUserInOrgStr)) {
				return Result.instance(ResponseCode.missing_parameter.getCode(), ResponseCode.missing_parameter.getMsg());
			}
			if(!mobileHasPermission(sysUserId, PermissionCode.CONTACT_DELETE.pName)) {
				return Result.instance(PermissionCode.CONTACT_DELETE.pCode, PermissionCode.CONTACT_DELETE.pMsg);
			}
//			   SysUserInOrg sysUserInOrg = mSysUerInOrgMapper.selectByUserId(sysUserId);
			   List<SysUserGroupDto> listUserGroupDto=Arrays.asList(JsonUtils.readValue(listSysUserInOrgStr, SysUserGroupDto[].class));
			   List<SysUserGroupDto> newList=new ArrayList<>(listUserGroupDto);
			   System.out.println("newlist"+newList.toString()); 
			   
			   List<SysUserGroupDto> listUserGroupDtoAll=Arrays.asList(JsonUtils.readValue(listSysUserInOrgAllStr, SysUserGroupDto[].class));
			   List<SysUserGroupDto> newListAll=new ArrayList<>(listUserGroupDtoAll);
			   mDeviceContactService.authUserInOrg(contactId,newList,newListAll);
			   return Result.success();
		  
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
