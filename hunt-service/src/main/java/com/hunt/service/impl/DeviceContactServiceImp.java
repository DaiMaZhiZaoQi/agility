package com.hunt.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.transaction.Transaction;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.joda.time.Years;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.TransformerUtils;

import com.hunt.dao.SysContactMapper;
import com.hunt.dao.SysContactUserMapper;
import com.hunt.dao.SysOrganizationMapper;
import com.hunt.dao.SysPermissionMapper;
import com.hunt.dao.SysUserMapper;
import com.hunt.dao.SysUserPermissionMapper;
import com.hunt.model.dto.PerFeatureDto;
import com.hunt.model.dto.SysContactUserDto;
import com.hunt.model.dto.SysUserOrgDto;
import com.hunt.model.entity.SysContact;
import com.hunt.model.entity.SysContactUser;
import com.hunt.model.entity.SysOrganization;
import com.hunt.model.entity.SysPermission;
import com.hunt.model.entity.SysPermissionGroup;
import com.hunt.model.entity.SysUser;
import com.hunt.model.entity.SysUserPermission;
import com.hunt.service.DeviceContactService;
import com.hunt.service.SysOrganizationService;
import com.hunt.service.SysPermissionService;
import com.hunt.service.SysUserService;
import com.hunt.service.SystemService;
import com.hunt.util.UtReadCsv;
import com.hunt.util.AESCipher;
import com.hunt.util.DateUtil;
import com.hunt.util.PermissionUtil;
import com.hunt.util.ResponseCode;
import com.hunt.util.Result;
import com.hunt.util.StringUtil;

@Service
@Transactional
public class DeviceContactServiceImp implements DeviceContactService{
	
	@Autowired
	SysContactMapper mSysContactMapper;
	@Autowired
	SysContactUserMapper mSysContactUserMapper;
	@Autowired
	SysUserService mSysUserService;
	@Autowired
	SysOrganizationMapper mSysOrganizationMapper;
	
	@Autowired
	SysPermissionService mSysPermissionService;
	@Autowired
	SysPermissionMapper mSysPermissionMapper;
	@Autowired
	SysUserPermissionMapper mSysUserPermissionMapper;
	@Autowired
	SysUserMapper mSysUserMapper;
	@Autowired
	SysOrganizationService mSysOrganizationService;
	
	@Autowired
	SystemService mSysteService;
//    @Autowired
//    private SysPermissionService sysPermissionService;
    
	@Override
	public Result insertContact(SysContact sysContact,Long sysUserId) {	  
		String absolutePath = sysContact.getAbsolutePath();
//		String contactCode = UtReadCsv.isCorrectFileVersion(absolutePath);
		String[] strArr = UtReadCsv.isCorrectFileVersion(absolutePath);
		if(strArr.length<2)return new Result(ResponseCode.missing_parameter.getCode(),"文件不完整");
		String strOrgName=strArr[0];
		boolean strCorrect = StringUtil.isStrCorrect(strOrgName);
		if(!strCorrect)return new Result(ResponseCode.encode_fail.getCode(),ResponseCode.encode_fail.getMsg());
		String contactCode=strArr[1];
		sysContact.setOrgName(strOrgName);
		sysContact.setContactCode(contactCode);
		if(StringUtils.isEmpty(contactCode)) {
			if(sysContact.getId()==null) {
				File file = new File(absolutePath);
				file.delete();											//  删除刚上传的文件
			}
			return new Result(ResponseCode.missing_parameter.getCode(),"文件缺少版本号,请重新选择文件");
		}
		SysContact sysContact2 = mSysContactMapper.selectContactByOrgName(strOrgName);
		String contactCode2 ="0";
		Long sysContactId2=0l;
		if(sysContact2!=null) {
			System.out.println("sysContact2"+sysContact2.toEntityString());
			contactCode2= sysContact2.getContactCode();
			sysContactId2=sysContact2.getId();
			List<SysContactUser> listSysContact = mSysContactUserMapper.selectByUserId(sysUserId);
			boolean isHavePer=false;
			for(SysContactUser sysCC:listSysContact) {
				Long sysContactId = sysCC.getSysContactId();
				if(sysContactId==sysContactId2) {
					isHavePer=true;
				}
			}
			if(!isHavePer) {
				File file = new File(absolutePath);
				file.delete();
				return new Result(ResponseCode.no_permission.getCode(),ResponseCode.no_permission.getMsg());
			}
		
			Integer status = sysContact2.getStatus();
			sysContact.setStatus(status);
		}
		
		if(sysContact.getId()==null) {
			if(Integer.parseInt(contactCode)<=Integer.parseInt(contactCode2)) {
				File file = new File(absolutePath);
				file.delete();
				return new Result(ResponseCode.name_already_exist.getCode(),"版本号错误,请重新选择文件");
			}else {
					if(sysContactId2>0) {				//  存在通讯录
//						String absolutePath2 = sysContact2.getAbsolutePath();
//						File file = new File(absolutePath2);
//						file.delete();
//						sysContact.setId(sysContactId2);
//						mSysContactMapper.update(sysContact);
						
						sysContact2.setAbsolutePath(sysContact.getAbsolutePath());
						sysContact2.setStatus(2);
						sysContact2.setContactCode(contactCode);
						sysContact2.setOriFileName(sysContact.getOriFileName());
						sysContact2.setContactName(sysContact.getContactName());
						Long updateBy = sysContact.getUpdateBy();
						sysContact2.setUpdateBy(updateBy); 
						mSysContactMapper.insert(sysContact2);
						Long id = sysContact2.getId();
						sysContact.setId(id); 
						return new Result(ResponseCode.success.getCode(),sysContact.getId()+"");
					}else {
						SysUser sysUser = mSysUserMapper.selectById(sysUserId);
						String loginName = sysUser.getLoginName();
						sysContact.setAuthName(loginName);
						mSysContactMapper.insert(sysContact);
						SysContactUser sysContactUser = new SysContactUser();
						sysContactUser.setSysContactId(sysContact.getId());
						SysOrganization sysOrganization = mSysOrganizationMapper.selectIdByUserId(sysUserId);
						sysContactUser.setSysOrgId(sysOrganization.getId());
						sysContactUser.setSysUserId(sysUserId);
						sysContactUser.setIsAuth(1);
						mSysContactUserMapper.insert(sysContactUser);
					}
			}
			
		}else {
			mSysContactMapper.update(sysContact);
			
		}
		
		
		return new Result(ResponseCode.success.getCode(),sysContact.getId()+"");
		
		
	}
	
	@Override
	public Result mInsertContact(SysContact sysContact, Long userId) {
		//是否有版本号
		String absolutePath = sysContact.getAbsolutePath();
		String contactSychPassword = sysContact.getContactSychPassword(); 
		String[] fileStr = UtReadCsv.isCorrectFileVersion(absolutePath);
		if(fileStr.length<2)return new Result(ResponseCode.missing_parameter.getCode(),"文件不完整");
		String strOrgName=fileStr[0];
		boolean strCorrect = StringUtil.isStrCorrect(strOrgName);
		if(!strCorrect) {
			deleteFile(absolutePath);
			return new Result(ResponseCode.encode_fail.getCode(),ResponseCode.encode_fail.getMsg());// new Result(ResponseCode.missing_parameter.getCode(), "编码格式错误，支持UTF-8");
		}
		//是否有版本名称
		String contactCode=fileStr[1];
		sysContact.setOrgName(strOrgName);
		sysContact.setContactCode(contactCode);
		if(StringUtils.isEmpty(contactCode)) {
			if(sysContact.getId()==null) {
				deleteFile(absolutePath);										//  删除刚上传的文件
			}
			return new Result(ResponseCode.missing_file_version.getCode(),ResponseCode.missing_file_version.getMsg());
		}
		//验证同步密码
		SysContact dbSysContact = mSysContactMapper.selectContactByOrgName(strOrgName);
		String currTime = DateUtil.getCurrTime(); 
		if(dbSysContact==null) {
			//  加密通讯录
			File file = new File(absolutePath);
			if(file.exists()) {
				String tagFN=strOrgName+"_"+contactCode+"_"+currTime+".csv";
				File escFile = AESCipher.escFile(absolutePath, tagFN, contactSychPassword);
				sysContact.setAbsolutePath(escFile.getAbsolutePath()); 
			}
			// 添加通讯录
			SysUser sysUser = mSysUserMapper.selectById(userId);
			String loginName = sysUser.getLoginName();
			sysContact.setAuthName(loginName);
			sysContact.setOrgName(strOrgName);
			sysContact.setContactCode(contactCode); 
			sysContact.setStatus(1);
			
			mSysContactMapper.insert(sysContact);
			
			SysContactUser sysContactUser = new SysContactUser();
			sysContactUser.setSysContactId(sysContact.getId());
			SysOrganization sysOrganization = mSysOrganizationMapper.selectIdByUserId(userId);
			sysContactUser.setSysOrgId(sysOrganization.getId());
			sysContactUser.setSysUserId(userId);
			sysContactUser.setIsAuth(1); 
			sysContactUser.setStatus(1);
			mSysContactUserMapper.insert(sysContactUser);
			// 更新权限
			insertPermission(sysContact.getId(),userId);
			mSysteService.clearAuthorizationInfoCacheByUserId(userId);
			return Result.success(sysContact);
		}else {
			// 修改通讯录
			String dbSynPassWord = dbSysContact.getContactSychPassword();
			String contactCode2 = dbSysContact.getContactCode();
			
			if (Integer.parseInt(contactCode)<=Integer.parseInt(contactCode2)) {
				deleteFile(absolutePath);
				return new Result(ResponseCode.file_exist_code.getCode(),ResponseCode.file_exist_code.getMsg());
			}
			if(!dbSynPassWord.equals(contactSychPassword)) {
				deleteFile(absolutePath);
				return new Result(ResponseCode.file_exist_err.getCode(),ResponseCode.file_exist_err.getMsg());
			} 
			
			// 加密通讯录
			File file = new File(absolutePath);
			if(file.exists()) {
				String tagFN=strOrgName+"_"+contactCode+"_"+currTime+".csv";
				File escFile = AESCipher.escFile(absolutePath, tagFN, contactSychPassword);
				dbSysContact.setContactName(tagFN); 
				String absolutePath2 = dbSysContact.getAbsolutePath();
				dbSysContact.setAbsolutePath(escFile.getAbsolutePath()); 
				File file2 = new File(absolutePath2);
				if(file2.exists()) {
					file2.delete();
				}
			}else {
				return new Result(ResponseCode.file_not_exist.getCode(),ResponseCode.file_not_exist.getMsg());
			}
			
			// 修改通讯录
//			String contactName = sysContact.getContactName();
			String oriFileName = sysContact.getOriFileName();
			Long updateBy = sysContact.getUpdateBy();
			
			dbSysContact.setOrgName(strOrgName);
			dbSysContact.setContactCode(contactCode);
			
			dbSysContact.setOriFileName(oriFileName);
//			dbSysContact.setAbsolutePath(absolutePath);
			dbSysContact.setUpdateBy(updateBy); 
			if(mobileHasPermission(userId,"contact:"+dbSysContact.getId()+":"+"list")) {
				mSysContactMapper.update(dbSysContact); 
				sysContact.setId(dbSysContact.getId());
				mSysteService.clearAuthorizationInfoCacheByUserId(userId);
				return Result.success(dbSysContact);
			}
			return Result.instance(ResponseCode.no_permission.getCode(), ResponseCode.no_permission.getMsg());
		} 
	}
	
	/**
	 * 
	 * @param filePath
	 */
	private void deleteFile(String filePath) {
		File file=new File(filePath);
			file.delete();
	}
	
	/**
	 * 
	 * @param sysContactId
	 * @param sysUserId
	 */
	private void insertPermission(Long sysContactId,Long sysUserId) {
		// insert sys_permission_group	权限组格式  contactName_ContactId_企业通讯录
		 SysPermissionGroup sysPermissionGroup = new SysPermissionGroup();
		 SysContact sContact = mSysContactMapper.selectById(sysContactId);
		 
		 String oriFileName = sContact.getOriFileName(); 
		 String permissionGroupName=oriFileName+"_"+sysContactId+"_"+"的企业通讯录";
		 
	        sysPermissionGroup.setName(permissionGroupName);
	        sysPermissionGroup.setDescription(permissionGroupName);
	        sysPermissionGroup.setIsFinal(2);
	        
	        PerFeatureDto perFeatureDto = new PerFeatureDto();
	        perFeatureDto.setFeature("contact");
	        perFeatureDto.setFeatureId(sysContactId);
	       mSysUserService.insertObjPermission(sysPermissionGroup, sysUserId, perFeatureDto);
	}



	@Override
	public SysContact selectContact(Long contactId) {
	  return mSysContactMapper.selectById(contactId);
		
	}
	
	
	


	@Override
	public List<SysUserOrgDto> selectUserOrg(Long contactId) {
		return mSysContactUserMapper.selectUserOrgAuthByContactId(contactId);
	}

	

	@Override
	public List<SysUserOrgDto> selectUnAuth(Long sysContactId,Long orgId,Integer disUp) {
		List<SysOrganization> list=new ArrayList<SysOrganization>();
		List<SysOrganization> listSysOrg=null;
		if(disUp==0) {
			listSysOrg= mSysOrganizationService.selectSysOrgList(orgId,list,false);
		}else {
			listSysOrg= mSysOrganizationService.selectUpSysOrgList(orgId, list);
		}
   		List<SysUserOrgDto> listSysUserOrg = getSysUserOrg(listSysOrg,sysContactId);
		return listSysUserOrg;
	}

	
	public List<SysUserOrgDto> getSysUserOrg(List<SysOrganization> listOrg,Long contactId){
   		List<SysUserOrgDto> list = mSysContactUserMapper.selectUserOrgAuthByContactId(contactId);
   		List<Long> listUserId=new ArrayList<>();
   		for(SysUserOrgDto dto:list) {
   			Long userId = dto.getSysUserId();
   			listUserId.add(userId);
   		}
   		List<SysUserOrgDto> listUserOrgDto=new ArrayList<>();
   		for(SysOrganization sysOrg:listOrg) {
   			Long id = sysOrg.getId();
   			
   			List<SysUser> listUser = mSysUserMapper.selectUserByOrgId(id);
   			for(SysUser sysUser:listUser) {
   				Long sysUserId = sysUser.getId();
   				if(!listUserId.contains(sysUserId)) {
   					SysUserOrgDto sysUserOrgDto = new SysUserOrgDto();
   					
   					BeanUtils.copyProperties(sysUser, sysUserOrgDto);
   					sysUserOrgDto.setSysUserId(sysUserId);
   					sysUserOrgDto.setIsAuth(0);
   					sysUserOrgDto.setOrgId(id);
   					sysUserOrgDto.setOrgName(sysOrg.getName());
   					listUserOrgDto.add(sysUserOrgDto);
   				}
   			}
   			
   		}
   		return listUserOrgDto;
   	}

	@Override
	public List<SysContact> selectByUserId(Long userId,int visitType) {
		List<Long> listContactId = mSysContactUserMapper.selectContactIdByUser(userId);
		List<SysContact> listContact=new ArrayList<SysContact>();
		if(visitType==0) {
			for(Long contactId:listContactId) {    
				System.out.println("contactId2-->"+contactId);
				if(mobileHasPermission(userId,"contact:"+contactId+":"+"list")) {
					SysContact sysContact = mSysContactMapper.selectActivyById(contactId);   
					if(sysContact!=null) {
						String absolutePath = sysContact.getAbsolutePath();
						System.out.println("absolutePath-->"+absolutePath);
						if(StringUtils.isEmpty(absolutePath))continue;
						File file = new File(absolutePath);
						if(!file.exists())continue;
						System.out.println("sysContact modile-->"+sysContact.toEntityString()); 
						listContact.add(sysContact);
					}
				}
			}
		}else {   
//			System.out.println("session--》"+session.toString());
			for(Long contactId:listContactId) {
				System.out.println("contactId2-->"+contactId);
				if(hasDataPermission("contact:"+contactId+":"+"list")) {
					SysContact sysContact = mSysContactMapper.selectActivyById(contactId);
					if(sysContact!=null) {
						String absolutePath = sysContact.getAbsolutePath();
						System.out.println("absolutePath-->"+absolutePath);
						if(StringUtils.isEmpty(absolutePath))continue;
						File file = new File(absolutePath);
						if(!file.exists())continue;
						System.out.println("sysContact-->"+sysContact.toEntityString()); 
						listContact.add(sysContact);
					}
				}
			}
		}
		return listContact;
	}
	
	/**
	 * 是否有该数据权限
	 * @param targetUserId  目标用户id
	 * @param permission  	权限码   (contact:contactId:list) 该用户的设备信息展示，(contact:contactId:updateXXX) 修改XXX
	 * @return
	 */
	public boolean hasDataPermission(String permission) {
		Subject subject = SecurityUtils.getSubject(); 
//		PrincipalCollection previousPrincipals = subject.getPreviousPrincipals();
//		String string = previousPrincipals.toString();
		boolean permitted = subject.isPermitted(permission);
//		System.out.println("hasDataPermission-->"+permitted+"toString-->"+subject.toString());
		return permitted;
	}
	
	/**
	 * 移动端判断用户是否有权限
	 * @param userId
	 * @param permission
	 * @return
	 */
	public boolean mobileHasPermission(Long userId,String permission) {
		SysPermission sysPermission = mSysPermissionMapper.selectByCode(permission);
		Long id = sysPermission.getId();
		List<Long> lisSysUP = mSysUserPermissionMapper.selectPerIdByUserId(userId);
		return lisSysUP.contains(id);
		
	}  
	


	@Override
	public Result uploadContact(SysContactUserDto sysContactUserDto) {
		// insert sys_contact
		SysContact sysContact = sysContactUserDto.getSysContact();
		Long id = sysContact.getId();
		// TODO 查询密码是否匹配，不匹配
		String contactSychPassword = sysContact.getContactSychPassword();
//		String orgName = sysContact.getOrgName();
		SysContact tempSysContact = mSysContactMapper.selectById(id);
		if(tempSysContact==null)return Result.instance(ResponseCode.data_not_exist.getCode(), "通讯录不存在请上传");
		SysContact dbSysContact = mSysContactMapper.selectContactByOrgName(tempSysContact.getOrgName());
		if(dbSysContact!=null) {
			String dbSychPassWord = dbSysContact.getContactSychPassword();
			if(!StringUtils.isEmpty(dbSychPassWord)&&!contactSychPassword.equals(dbSychPassWord)) {
				return Result.instance(ResponseCode.data_not_exist.getCode(), "同步密码错误,请联系通讯录作者"); 
			}
		}
		String orgName = tempSysContact.getOrgName();
		String contactCode = tempSysContact.getContactCode(); 
		String currTime = DateUtil.getCurrTime(); 
		SysContactUser sysContactUser = sysContactUserDto.getSysContactUser();
		if(dbSysContact==null) {
			//加密文件
			String absolutePath = tempSysContact.getAbsolutePath(); 
		
			
			String tagFN=orgName+"_"+contactCode+"_"+currTime+".csv";
			File escFile = AESCipher.escFile(absolutePath, tagFN, contactSychPassword);
			sysContact.setAbsolutePath(escFile.getAbsolutePath()); 
			//修改数据库
			Long sysUserId = sysContactUser.getSysUserId(); 
			Long sysContactId = sysContact.getId();
			sysContactUser.setSysContactId(sysContactId);
			sysContactUser.setStatus(1);
			sysContactUser.setSysUserId(sysUserId);
			sysContact.setStatus(1); 
			
			sysContact.setContactName(tagFN);
			Integer update = mSysContactMapper.update(sysContact);
			if(update<=0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return new Result(ResponseCode.missing_parameter.getCode(),"请重新上传");
			}
			 
			// insert sys_contact_user
			mSysContactUserMapper.update(sysContactUser);
			// insert sys_permission_group	权限组格式  contactName_ContactId_企业通讯录
			 SysPermissionGroup sysPermissionGroup = new SysPermissionGroup();
			 SysContact sContact = mSysContactMapper.selectById(sysContactId);
			 
			 String oriFileName = sContact.getOriFileName(); 
			 String permissionGroupName=oriFileName+"_"+sysContactId+"_"+"的企业通讯录";
			 
		        sysPermissionGroup.setName(permissionGroupName);
		        sysPermissionGroup.setDescription(permissionGroupName);
		        sysPermissionGroup.setIsFinal(2);
		        
		        PerFeatureDto perFeatureDto = new PerFeatureDto();
		        perFeatureDto.setFeature("contact");
		        perFeatureDto.setFeatureId(sysContactId);
		        Long permissionId = mSysUserService.insertObjPermission(sysPermissionGroup, sysUserId, perFeatureDto);
		        if(permissionId==null||permissionId<=0) {
		        	TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		        	return new Result(ResponseCode.code_already_exist.getCode(),"请勿重复提交，参数错误 rollBack");
		        }else if(permissionId==2) {
		        	return new Result(ResponseCode.success.getCode(),"上传成功");
		        }
		}else {
			Long dbSysContactId = dbSysContact.getId();
			String absolutePath = tempSysContact.getAbsolutePath();
			File file = new File(absolutePath);
			if(file.exists()) {
				String dbSysAboPath = dbSysContact.getAbsolutePath();
				File file2 = new File(dbSysAboPath);
				//加密文件
				
				String tagFN=orgName+"_"+contactCode+"_"+currTime+".csv";
				File escFile = AESCipher.escFile(absolutePath, tagFN, contactSychPassword);
				tempSysContact.setContactName(tagFN);
				tempSysContact.setAbsolutePath(escFile.getAbsolutePath()); 
				if(file2.exists()) {
					file2.delete();
				}
			} else {
				return Result.instance(ResponseCode.data_not_exist.getCode(), "通讯录不存在请上传");
			}
			tempSysContact.setId(dbSysContactId); 
			tempSysContact.setStatus(1);
			Integer update = mSysContactMapper.update(tempSysContact);
			if(update<=0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return new Result(ResponseCode.missing_parameter.getCode(),"请重新上传");
			}else {
				mSysContactMapper.deleteById(id);
			}
			return new Result(ResponseCode.success.getCode(),"上传成功");
//			sysContactUser.setStatus(1);
//			sysContactUser.setSysContactId(dbSysContactId);
			
		}
		return Result.success(sysContactUserDto);
	}
	
	
	

	
	
	@Override
	public Result updateContact(Long contactId, Long userId) {
		String permission="contact:"+contactId+":delete";
		boolean hasPermission = PermissionUtil.hasDataPermission(userId, permission); 
		if(!hasPermission) return new Result(ResponseCode.missing_parameter.getCode(), "您没有该权限");
		SysContact sysContact = new SysContact();
		sysContact.setStatus(3);
		sysContact.setId(contactId);
		
		mSysContactMapper.update(sysContact);			//  修改通讯录 伪删除
		
		SysContactUser sysContactUser = new SysContactUser();
		sysContactUser.setSysContactId(contactId);
		sysContactUser.setStatus(3);
		sysContactUser.setSysUserId(userId);
		mSysContactUserMapper.update(sysContactUser);	//	修改sys_contact_user 伪删除
		
		SysContact sysContact2 = mSysContactMapper.selectById(contactId);
		String oriFileName = sysContact2.getOriFileName();
		String permissionGroupName=oriFileName+"_"+contactId;
		boolean upDateResult = mSysPermissionService.updatePermission(permissionGroupName);
		if(upDateResult) {
			return Result.instance(ResponseCode.success.getCode(), "操作成功");
		}else {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Result.instance(ResponseCode.error.getCode(), "操作失败");
		}
	}
	
	

	

	@Override
	public Result auth(List<SysUserOrgDto> listSysUserOrgDto, Long contactId) {
		for(SysUserOrgDto sysUserOrgDto:listSysUserOrgDto) {
			Integer isAuth = sysUserOrgDto.getIsAuth();
			Long orgId = sysUserOrgDto.getOrgId();
			Long sysUserId = sysUserOrgDto.getSysUserId();
			
			SysContactUser sysContactUser = new SysContactUser();
			sysContactUser.setIsAuth(isAuth);
			sysContactUser.setStatus(1);
			sysContactUser.setSysOrgId(orgId);
			sysContactUser.setSysUserId(sysUserId);
			sysContactUser.setSysContactId(contactId);
			Long sysContactResult=0l; 
			SysContactUser dbSysConU = mSysContactUserMapper.selectUnAuth(sysContactUser);
			if(dbSysConU==null) {
				sysContactResult=mSysContactUserMapper.insert(sysContactUser);     //  修改状态通讯录授权状态 而不是插入一条记录 
			}else {
				//  更新sysContactUser
				sysContactUser.setId(dbSysConU.getId());
				sysContactResult=mSysContactUserMapper.update(sysContactUser);
			}
			
			
			// 赋 select 权限
			String code="contact:"+contactId+":list";
			SysPermission sysPermission = mSysPermissionMapper.selectByCode(code);
			if(sysPermission==null) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return Result.instance(ResponseCode.data_not_exist.getCode(), "没有权限");
			}
			SysUserPermission sysUserPermission = new SysUserPermission();
			sysUserPermission.setSysPermissionId(sysPermission.getId());
			sysUserPermission.setSysUserId(sysUserId); 
			SysUserPermission dbSysUsP = mSysUserPermissionMapper.select(sysUserPermission);
			if(dbSysUsP==null) {
				sysUserPermission.setCreateBy(sysUserId);
				Long insert = mSysUserPermissionMapper.insert(sysUserPermission);
			}else {
				sysUserPermission.setId(dbSysUsP.getId()); 
				mSysUserPermissionMapper.updateDelete(sysUserPermission, 1); 
			}
			mSysteService.clearAuthorizationInfoCacheByUserId(sysUserId);
		}
		return Result.success();
	}

	
	
	
	
	

	@Override
	public Result authOrg(List<SysUserOrgDto> listSysUserOrgDto, Long contactId) {
		for(SysUserOrgDto sysUserOrgDto:listSysUserOrgDto) {
			Long orgId = sysUserOrgDto.getOrgId();
			List<SysUser> listUser = mSysUserMapper.selectUserByOrgId(orgId);
			
			for(SysUser sysUser:listUser) {
				SysContactUser sysContactUser = new SysContactUser();
				Long sysUserId = sysUser.getId();
				sysContactUser.setIsAuth(0);
				sysContactUser.setStatus(1);
				sysContactUser.setSysOrgId(orgId);
				sysContactUser.setSysUserId(sysUserId);
				sysContactUser.setSysContactId(contactId);
				
				SysContactUser sysSelect = new SysContactUser();
				sysSelect.setSysContactId(contactId);
				sysSelect.setSysUserId(sysUserId);
				sysSelect.setStatus(1);
				SysContactUser sysContact = mSysContactUserMapper.select(sysSelect);
				if(sysContact!=null) {	//  已经授权了不在授权
					continue;
				}
				
				SysContactUser dbSysConU = mSysContactUserMapper.selectUnAuth(sysContactUser);
				if(dbSysConU==null) {
					sysSelect.setSysOrgId(orgId); 
					mSysContactUserMapper.insert(sysContactUser);
				}else {					//  存在
										//  更新sysContactUser
					sysContactUser.setId(dbSysConU.getId());
					mSysContactUserMapper.update(sysContactUser);
				}
				
				
				// 赋 select 权限
				String code="contact:"+contactId+":list";
				SysPermission sysPermission = mSysPermissionMapper.selectByCode(code);
				if(sysPermission==null) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return Result.instance(ResponseCode.data_not_exist.getCode(), "没有权限");
				}
				SysUserPermission sysUserPermission = new SysUserPermission();
				sysUserPermission.setSysPermissionId(sysPermission.getId());
				sysUserPermission.setSysUserId(sysUserId); 
				
				SysUserPermission dbSysUsP = mSysUserPermissionMapper.select(sysUserPermission);
				if(dbSysUsP==null) {
					sysUserPermission.setCreateBy(sysUserId);
					Long insert = mSysUserPermissionMapper.insert(sysUserPermission);
				}else {
					sysUserPermission.setId(dbSysUsP.getId()); 
					mSysUserPermissionMapper.updateDelete(sysUserPermission, 1); 
				}
				mSysteService.clearAuthorizationInfoCacheByUserId(sysUserId);
//				if(insert<=0) {
//					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//					return Result.instance(ResponseCode.data_not_exist.getCode(), "没有权限");
//				}
			}
			
			
			
		}
		return Result.success();
	}

	
	

	@Override
	public Result noAuth(List<SysUserOrgDto> listSysUserOrgDto, Long contactId) {
		for(SysUserOrgDto userOrgDto:listSysUserOrgDto) {
			Integer isAuth = userOrgDto.getIsAuth();
			Long orgId = userOrgDto.getOrgId();
			Long sysUserId = userOrgDto.getSysUserId();
			
			
			SysContactUser sysSelect = new SysContactUser();
			sysSelect.setSysContactId(contactId);
			sysSelect.setSysUserId(sysUserId);
			mSysContactUserMapper.updateStatusByUserIdContactId(sysSelect,3);
			
			String code="contact:"+contactId+":list";
			SysPermission sysPermission = mSysPermissionMapper.selectByCode(code);
			SysUserPermission sysUserPermission = new SysUserPermission();
			sysUserPermission.setSysUserId(sysUserId);
			sysUserPermission.setSysPermissionId(sysPermission.getId());
			mSysUserPermissionMapper.updateDelete(sysUserPermission,2);
			mSysteService.clearAuthorizationInfoCacheByUserId(sysUserId);
//			SysContactUser sysContact = mSysContactUserMapper.select(sysSelect);
		}
		return Result.success();
	}
	
	


	@Override
	public Result unAuthOrg(List<SysUserOrgDto> listSysUserOrgDto, Long contactId) {
		for(SysUserOrgDto sysUserOrgDto:listSysUserOrgDto) {
			Long orgId = sysUserOrgDto.getOrgId();
			List<SysUser> listUser = mSysUserMapper.selectUserByOrgId(orgId);
			
			for(SysUser sysUser:listUser) {
//				SysContactUser sysContactUser = new SysContactUser();
				Long sysUserId = sysUser.getId();
//				sysContactUser.setIsAuth(0);
//				sysContactUser.setStatus(1);
//				sysContactUser.setSysOrgId(orgId);
//				sysContactUser.setSysUserId(sysUserId);
//				sysContactUser.setSysContactId(contactId);
				
//				
				// 解除授权
				SysContactUser sysCon = new SysContactUser();
				sysCon.setSysContactId(contactId);
				sysCon.setSysUserId(sysUserId);
//				sysCon.setStatus(3);
				mSysContactUserMapper.updateStatusByUserIdContactId(sysCon,3);
				
				String code="contact:"+contactId+":list";
				SysPermission sysPermission = mSysPermissionMapper.selectByCode(code);
				if(sysPermission==null)continue;
				SysUserPermission sysUserPermission = new SysUserPermission();
				sysUserPermission.setSysUserId(sysUserId);
				sysUserPermission.setSysPermissionId(sysPermission.getId());
				mSysUserPermissionMapper.updateDelete(sysUserPermission,2);
				mSysteService.clearAuthorizationInfoCacheByUserId(sysUserId);
			}
		}
		return Result.success();
	}


	/**
	 * 判断该用户是否存在该版本的企业通讯录
	 * @param userId
	 * @param filePath
	 * @return false:不存在该文件	true:存在该文件
	 */
	private boolean isExitVersion(Long userId,String contactCode) { 
		List<Long> listContactId = mSysContactUserMapper.selectContactIdByUser(userId);
		if(listContactId.size()<=0) {  
			return false;     
		} 
		String selectCCD = mSysContactMapper.selectByIds(listContactId, contactCode);
		return contactCode.equals(selectCCD);
	}
	

}
