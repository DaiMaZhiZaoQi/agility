package com.hunt.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonObject;
import com.hunt.dao.SysDeviceCallLogMapper;
import com.hunt.dao.SysDeviceMapper;
import com.hunt.dao.SysDeviceRoleOrgMapper;
import com.hunt.dao.SysOrganizationMapper;
import com.hunt.dao.SysUserInOrgMapper;
import com.hunt.model.dto.LoginInfo;
import com.hunt.model.entity.SysDevice;
import com.hunt.model.entity.SysDeviceCallLog;
import com.hunt.model.entity.SysDeviceRecord;
import com.hunt.model.entity.SysDeviceRoleOrg;
import com.hunt.model.entity.SysOrganization;
import com.hunt.model.entity.SysUserInOrg;
import com.hunt.properties.PropertiesUtil;
import com.hunt.service.DeviceCallLogService;
import com.hunt.service.DeviceRecordService;
import com.hunt.util.AmrToMP3Utils;
import com.hunt.util.DateUtil;
import com.hunt.util.PermissionCode;
import com.hunt.util.ResponseCode;
import com.hunt.util.Result;
import com.hunt.util.StringUtil;

import io.swagger.annotations.Api;

@Api(value="上传通话录音")
@Controller
@RequestMapping("deviceRecord")
public class DeviceRecordController extends BaseController{

	Logger log=LoggerFactory.getLogger(SystemDeviceController.class);
	
	@Autowired
	private DeviceRecordService mDeviceRecordService;
	@Autowired
	private PropertiesUtil mPropUtil;
	@Autowired
	private SysDeviceMapper mSysDeviceMapper;
	@Autowired
	private SysDeviceRoleOrgMapper mSysDeviceRoleOrgMapper;
	@Autowired
	private SysDeviceCallLogMapper mSysDeviceCallLogMapper;
	
	@Autowired
	private SysUserInOrgMapper mSysUserInOrgMapper;
	@Autowired
	private SysOrganizationMapper mSysOrganizationMapper;
	
	@Autowired
	private DeviceCallLogService mDeviceCallLogService;
	
	private Logger logger= LoggerFactory.getLogger(DeviceRecordController.class);
	@ResponseBody 
	@RequestMapping(value="insert",method=RequestMethod.POST)
	public Result insert(
			@RequestParam(value="sysDeviceCallLog",required=false,defaultValue="")String sysDeviceCallLogJson,
			/*@RequestParam(value="record",required=false) String recordStr,*/ 
			@RequestParam(value="file",required=false)MultipartFile[] uploadFile,   
			@RequestParam(value="deviceSerial",required=false,defaultValue="")String deviceSerial,  
			@RequestParam(value="args",required=false,defaultValue="")String args,
			@RequestParam(value="sign",required=false,defaultValue="") String sign,
			HttpServletRequest request) throws IllegalStateException, IOException {
		System.out.println("------>"+Arrays.toString(uploadFile)+"-->"+sysDeviceCallLogJson+"-sysDeviceRecord-->"+deviceSerial);  
		
		try {
			Map<String, String> decodParam = decodParam(args,sign);
			if(decodParam.size()>0) {
				sysDeviceCallLogJson=decodParam.get("sysDeviceCallLogJson");
				sysDeviceCallLogJson=sysDeviceCallLogJson==null?"":sysDeviceCallLogJson;
				deviceSerial=decodParam.get("deviceSerial");
				deviceSerial=deviceSerial==null?"":deviceSerial;
				log.info("deviceSerial--->"+deviceSerial);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Result.instance(ResponseCode.decode_err.getCode(), ResponseCode.decode_err.getMsg());
		}
		
		if(StringUtils.isEmpty(deviceSerial)) {
			return Result.instance(ResponseCode.missing_parameter.getCode(), "缺少机器序列号");
		}
		SysDevice sysDevice = mSysDeviceMapper.selectByDeviceSerial(deviceSerial);
		if(sysDevice==null) {
			return Result.instance(ResponseCode.missing_parameter.getCode(), "序列号错误,该设备不存在");
		}else {
			// 用户id 通话记录添加权限判断
			List<SysDeviceRoleOrg> listSysDeviceRoleOrg = mSysDeviceRoleOrgMapper.selectOnByDeviceId(sysDevice.getId());
			if(listSysDeviceRoleOrg!=null) {
				SysDeviceRoleOrg sysDeviceRoleOrg = listSysDeviceRoleOrg.get(0);
				Long sysUserId = sysDeviceRoleOrg.getSysUserId();
				if(!mobileHasPermission(sysUserId, PermissionCode.CALLLOG_INSERT.pName)) {
					return Result.instance(PermissionCode.CALLLOG_INSERT.pCode, PermissionCode.CALLLOG_INSERT.pMsg);
				}
				return	insertCallLogAndRecord(sysDeviceCallLogJson, uploadFile, deviceSerial, request, sysDevice,sysDeviceRoleOrg);
			}else {
				return new Result(ResponseCode.data_not_exist.getCode(),ResponseCode.data_not_exist.getMsg());
			}
			
		}
	}
	
	@Transactional
	private Result insertCallLogAndRecord(String sysDeviceCallLogJson, MultipartFile[] uploadFile,
			String deviceSerial, HttpServletRequest request, SysDevice sysDevice,SysDeviceRoleOrg sysDeviceRoleOrg) throws IOException {
		Long id = sysDevice.getId();   
		Long callLogId=null;
		//  添加设备通话记录 
		Result result=null;
		SysDeviceCallLog sysDeviceCallLog=null;
		if(!"".equals(sysDeviceCallLogJson)) { 	
			sysDeviceCallLog=JsonUtils.readValue(sysDeviceCallLogJson, SysDeviceCallLog.class);
			if(sysDeviceCallLog==null) {
				System.out.println("sysDeviceCallLog null");
				return Result.instance(ResponseCode.missing_parameter.getCode(), "缺少参数或字段名错误");
			}
			if(sysDeviceCallLog.getCallDate()==null){
				return Result.instance(ResponseCode.missing_parameter.getCode(), "缺少参数通话时间");
			}
			sysDeviceCallLog.setDeviceId(id);
			

			SysDeviceCallLog sysDeviceCallLogExist = mSysDeviceCallLogMapper.selectByCallDate(id,sysDeviceCallLog.getCallDate());
			if(sysDeviceCallLogExist!=null) {		//  不允许重新上传
				return Result.success();
//				sysDeviceCallLog.setId(sysDeviceCallLogExist.getId());
//				sysDeviceCallLog.setOrgId(sysDeviceCallLogExist.getOrgId());
//	        	callLogId=Long.parseLong(sysDeviceCallLogExist.getId()+"");
//	        	Long updateAllCallLog = mDeviceCallLogService.updateAllCallLog(sysDeviceCallLog);
//	        	if(updateAllCallLog>0) {
//	        		result=Result.instance(ResponseCode.success.getCode(), "重传成功");
//	        	}
			}else {
				Long sysUserId = sysDeviceRoleOrg.getSysUserId();
				SysUserInOrg sysUserInOrg = mSysUserInOrgMapper.selectByUserId(sysUserId);
				SysOrganization sysOrganization = mSysOrganizationMapper.selectById(sysUserInOrg.getSysOrgId());
				
				sysDeviceCallLog.setOrgId(sysOrganization.getId());
				sysDeviceCallLog.setOrgName(sysOrganization.getName());
				sysDeviceCallLog.setOrgCode(sysOrganization.getOrgCode());
				sysDeviceCallLog.setUserId(sysUserId);
				sysDeviceCallLog.setDevSerial(sysDevice.getDeviceSerial());
				result= mDeviceCallLogService.insert(sysDeviceCallLog);
				if(result.getCode()==ResponseCode.success.getCode()) {
		        	callLogId=sysDeviceCallLog.getId();
				}else {
					return result;
				}
			}
	     
		}
		
	if((!"".equals(uploadFile))&&uploadFile!=null&&uploadFile.length>0&&sysDeviceCallLog!=null) { 
		Integer callhasRecord = sysDeviceCallLog.getCallHasRecord();
		SysDeviceRecord sysDeviceRecord=null;
		if(callhasRecord==1) {
			sysDeviceRecord=new SysDeviceRecord();
			sysDeviceRecord.setCallLogId(callLogId); 
			if(uploadFile.length<=0) {
				return Result.instance(ResponseCode.data_not_exist.getCode(), "缺少录音文件");
			}
		}else {
			return Result.error("参数错误，callhasRecord应该为1");
		}
		
		System.out.println("callLogId-->"+callLogId);
		if(callLogId==null||callLogId==0) {
			return Result.instance(ResponseCode.missing_parameter.getCode(), "通话id不存在，请提交通话记录，获取通话id");
		}
		SysDeviceRecord sysDeviceRecordDao = mDeviceRecordService.selectDeviceRecordByCallLogId(callLogId);
		boolean isExistAudio=false;
		if(sysDeviceRecordDao!=null) {								//  该条通话记录的通话录音已存在，覆盖原有的通话录音。
			isExistAudio=true;
				String recoAbsoluteFilePath = sysDeviceRecordDao.getRecoAbsoluteFilePath();
				File file = new File(recoAbsoluteFilePath);
				if(file.exists()) {									//  删除文件
					file.delete();
					mDeviceRecordService.updateDeviceTotalRecordByDeviceId(sysDeviceRecordDao.getDeviceId(), sysDeviceRecordDao);
				}
		}
		
		sysDeviceRecord.setDeviceId(id);
			
			for(MultipartFile mf:uploadFile) {
				
				String string = mf.toString();
				System.out.println("------>"+mf.toString());
				String filename = mf.getOriginalFilename();
				String fileType = filename.substring(filename.lastIndexOf(".")+1).toLowerCase().trim();// 文件类型
					if(!mf.isEmpty()) {
						String path=request.getServletContext().getRealPath("/");
						long minTime=System.currentTimeMillis();
						String yearAndMonthToday = DateUtil.createFilePath(minTime); 
						String relaPath=deviceSerial+yearAndMonthToday;
						String realPathParent=null;
						if(!StringUtils.isEmpty(mPropUtil.getRecordFilePath())){
							realPathParent=mPropUtil.getRecordFilePath()+File.separator+relaPath;
						}else {
							realPathParent= new File(path).getParent()+File.separator+relaPath;
						}
						
						File file=new File(realPathParent);
						if(!file.exists()){
							boolean mkdirs = file.mkdirs();
							if(!mkdirs)return Result.error(ResponseCode.file_config_fail.getMsg());
						}
						String currTime = DateUtil.getTodayTime(minTime); 		//  时间毫秒数当成文件名，保持文件唯一，重复上传不会被覆盖
						String sdFileName=currTime+"."+fileType;
						//创建目标文件
						File toFile=new File(realPathParent,sdFileName);
						System.out.println("--->"+toFile.getAbsolutePath());
						//将上传文件写到目标文件中
						if("amr".equals(fileType)) {
							InputStream inputStream = mf.getInputStream();
							String amrToMP3FilePath = AmrToMP3Utils.amrToMP3(inputStream, toFile.getName(),realPathParent);
							sysDeviceRecord.setRecoAbsoluteFilePath(amrToMP3FilePath);
						}else {
							mf.transferTo(toFile);
							sysDeviceRecord.setRecoAbsoluteFilePath(toFile.getAbsolutePath());
						}
						sysDeviceRecord.setRecoRealFileName(filename);
						//获取文件的相对路径并将其添加到用户对象中
						String contentType = mf.getContentType();
						System.out.println("文件类型-->"+contentType);
						long size = mf.getSize();
						sysDeviceRecord.setRecoPhoneSize(size); 
						sysDeviceRecord.setRecoFilePath(relaPath);
						Long callRecordMs = sysDeviceCallLog.getCallRecordMs();
						sysDeviceRecord.setRecoAudioLength(callRecordMs);;
						sysDeviceRecord.setCallLogId(callLogId);
					}
			}
			System.out.println("insert audio-->"+sysDeviceRecord.toEntityString());
			if(isExistAudio) {						// 已存在该录音
				mDeviceRecordService.update(sysDeviceRecord,sysDeviceCallLog.getOrgId());
			}else {									//	没有该录音
				mDeviceRecordService.insert(sysDeviceRecord,sysDeviceCallLog.getOrgId());
			}
			String recoFilePath = sysDeviceRecord.getRecoFilePath();
			if(!StringUtils.isEmpty(recoFilePath)) {
				 result = new Result();
				result.setCode(10000);
				result.setMsg("上传成功"+recoFilePath); 
				FileResponse fileResponse = new FileResponse();
				
				fileResponse.setFilePath(request.getContextPath()+"/deviceRecord/audio?callLogId="+callLogId);
				fileResponse.setFileSize(sysDeviceRecord.getRecoPhoneSize());
				return Result.success(fileResponse);
			}
		}
			updateHeart(request,deviceSerial);
			return result;
	}
	
	class FileResponse{
		private String filePath;
		private Long fileSize;
		public String getFilePath() {
			return filePath;
		}
		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}
		public Long getFileSize() {
			return fileSize;
		}
		public void setFileSize(Long fileSize) {
			this.fileSize = fileSize;
		}
	
		
	}

	
	@RequestMapping("audio")
	@ResponseBody
	public void audioPlay(@RequestParam(value="callLogId",required=false,defaultValue="0")Long callLogId,
										HttpServletRequest request,
										HttpServletResponse response) {
		if(callLogId<=0) {
			return;
		}
		SysDeviceRecord sysDeviceRecord = mDeviceRecordService.selectDeviceRecordByCallLogId(callLogId);
		if(sysDeviceRecord==null)return;
		String recoAbsoluteFilePath = sysDeviceRecord.getRecoAbsoluteFilePath();
		if(StringUtils.isEmpty(recoAbsoluteFilePath))return;
		File file = new File(recoAbsoluteFilePath);
		FileInputStream fileInputStream=null;
		ServletOutputStream outputStream=null;
		try {
			fileInputStream= new FileInputStream(file); 				//    FileNotFoundException
			outputStream = response.getOutputStream();
			String mimeType = request.getServletContext().getMimeType(file.getName());
			response.setHeader("Content-Disposition", "attachment;filename="+file.getName());
			response.setHeader("Accept-Ranges", "bytes");
			response.setContentType(mimeType); 
			response.setContentLength(Integer.parseInt(file.length()+""));
			response.setHeader("Content-Range", "bytes 0-"+(Integer.parseInt(file.length()+"")/2)+"/"+(Integer.parseInt(file.length()+"")/2));
//			outputStream.setWriteListener(new WriteListener() {
//				
//				@Override
//				public void onWritePossible() throws IOException {
//					// TODO Auto-generated method stub
//					System.out.println("onWritePossible---");
//				}
//				
//				@Override
//				public void onError(Throwable t) {
//					System.out.println("onError---");
//					try {
//						if(fileInputStream!=null) {
//							fileInputStream.close();
//						}
//						if(outputStream!=null) {
//							outputStream.close();
//						}
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//					
//					
//				}
//			});
			
			
			byte[] bs=null;
			while(fileInputStream.available()>0) {
				if(fileInputStream.available()>10240) {
					bs=new byte[10240];
				}else {
					bs=new byte[fileInputStream.available()];
				}
				fileInputStream.read(bs,0,bs.length);
				outputStream.write(bs,0,bs.length);
			}
		
			
		}catch (Exception e) {
			logger.error("audioPlay--->"+e.getMessage());
		}finally {
			if(fileInputStream!=null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					logger.error("audioPlay--->"+e.getMessage());
				}
			}
			
			if(outputStream!=null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					logger.error("audioPlay--->"+e.getMessage());
				}
			}
			
		}
	}
	
	

	
	
	
}
