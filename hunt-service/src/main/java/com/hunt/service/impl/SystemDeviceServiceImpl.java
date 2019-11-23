package com.hunt.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import com.github.pagehelper.PageHelper;
import com.hunt.dao.SysDeviceMapper;
import com.hunt.dao.SysDeviceRoleOrgMapper;
import com.hunt.dao.SysOrganizationMapper;
import com.hunt.dao.SysUserMapper;
import com.hunt.dao.SysUserRoleOrganizationMapper;
import com.hunt.model.dto.LoginInfo;
import com.hunt.model.dto.PageInfo;
import com.hunt.model.dto.SysDeviceDto;
import com.hunt.model.entity.SysDevice;
import com.hunt.model.entity.SysDeviceRoleOrg;
import com.hunt.model.entity.SysOrganization;
import com.hunt.model.entity.SysUser;
import com.hunt.model.entity.SysUserRoleOrganization;
import com.hunt.service.SystemDeviceService;
import com.hunt.util.ResponseCode;
import com.hunt.util.Result;
import com.hunt.util.StringUtil;

@Service
@Transactional
public class SystemDeviceServiceImpl implements SystemDeviceService {
	
	@Autowired
	private SysDeviceMapper mSysDeviceMapper;
	@Autowired
	private SysUserRoleOrganizationMapper mSysRoleOrgMapper;
	@Autowired
	private SysUserMapper mSysUserMapper;
	@Autowired
	private SysDeviceRoleOrgMapper mSysDeviceRoleOrgMapper;
	@Autowired
	private SysOrganizationMapper mSysOrganizationMapper;
	
	private Logger log=LoggerFactory.getLogger(SystemDeviceServiceImpl.class);
	
	@Override
	public boolean isExistDevice(String deviceSerial) {
		return mSysDeviceMapper.isExistDevice(deviceSerial);
	}
	
	
	
	 
	 
	@Override
	public Long insertDevice(SysDevice sysDevice, Long sysUserId, Long sysOrgId) {
		log.info("insert-"+sysDevice.toEntityString()+"-sysUserId-->"+sysUserId+"-sysUserIdRoleOrgid-->"+sysOrgId);
		SysDevice sysDevExit = mSysDeviceMapper.selectByDeviceSerial(sysDevice.getDeviceSerial());
		if(sysDevExit==null) {			//  存在该设备
			mSysDeviceMapper.insert(sysDevice);
		}else {
			sysDevice.setId(sysDevExit.getId());
			mSysDeviceMapper.update(sysDevice);
		}
		Long createBy = sysDevice.getCreateBy(); 
		Long id = sysDevice.getId(); 
		if(sysUserId!=null&&sysUserId>0) {
			if(sysOrgId!=null&&sysOrgId!=0) {		//  机构id
					SysDeviceRoleOrg sysDeviceRoleOrg = new SysDeviceRoleOrg();
					sysDeviceRoleOrg.setSysDeviceId(id);
					sysDeviceRoleOrg.setCreateBy(createBy); 
					sysDeviceRoleOrg.setSysUserId(sysUserId);
					sysDeviceRoleOrg.setSysOrgId(sysOrgId);
//					sysDeviceRoleOrg.setSysRoleOrgId(sysUserIdRoleOrgid[i]);
					insertDeviceRoleOrg(sysDeviceRoleOrg);
			}else {
				SysDeviceRoleOrg sysDeviceRoleOrg = new SysDeviceRoleOrg();
				sysDeviceRoleOrg.setSysDeviceId(id);
				sysDeviceRoleOrg.setCreateBy(createBy); 
				sysDeviceRoleOrg.setSysUserId(sysUserId);
				insertDeviceRoleOrg(sysDeviceRoleOrg);
			}
		}
		return id;
	}

	
	/**
	 * 查询使用该设备的用户数量
	 * @param deviceId
	 * @return
	 */
	private boolean isValidUse(Long deviceId) {
		Set<Long> setUserId= mSysDeviceRoleOrgMapper.selectUserIdByDeviceId(deviceId);
		return setUserId.size()<1;
	}
	
	@Override
	public Long updateDevice(Long sysOrgId,SysDevice sysDevice, Long sysUserId) {
		log.info("insert-"+sysDevice.toEntityString()+"-sysUserId-->"+sysUserId+"-sysUserIdRoleOrgid-->"+sysOrgId+"-->");
		try {
			mSysDeviceMapper.update(sysDevice);
			
		} catch (Exception e) {
			return -1l;
		}
		
		Long createBy = sysDevice.getCreateBy(); 
		Long updateBy = sysDevice.getUpdateBy(); 
		Long devId = sysDevice.getId(); 
		boolean validUse = isValidUse(devId);
		if(validUse) {		//	设备不存在，
			SysDeviceRoleOrg sysDeviceRoleOrg = new SysDeviceRoleOrg();
			sysDeviceRoleOrg.setSysOrgId(sysOrgId);
			sysDeviceRoleOrg.setSysDeviceId(devId);
			sysDeviceRoleOrg.setCreateBy(createBy); 
			sysDeviceRoleOrg.setSysUserId(sysUserId);
			Long result=mSysDeviceRoleOrgMapper.insert(sysDeviceRoleOrg);
			System.out.println("mSysDeviceRoleOrgMapper result--->"+result);
			return result;
		}else {
			SysDeviceRoleOrg lSysDeviceRoleOrg = new SysDeviceRoleOrg();
			lSysDeviceRoleOrg.setSysOrgId(sysOrgId);
			lSysDeviceRoleOrg.setUpdateBy(updateBy); 
			lSysDeviceRoleOrg.setSysUserId(sysUserId);
			lSysDeviceRoleOrg.setSysDeviceId(devId);
		   Long result=updateDeviceRoleOrg(lSysDeviceRoleOrg);
		   System.out.println("result-->"+result);
		   return result;
		}
		
		/*if(sysDeviceRoleOrgId!=null&&sysDeviceRoleOrgId.length>0) {			//	sys_device_role_ord存在该条记录
			if(sysDeviceRoleOrgId.length<sysRoleOrgId.length) {				// 	修改并新增 sys_device_role_org
				//   判断哪个sysRoleOrgId需要新增
				for(int i=0;i<sysRoleOrgId.length;i++) {
					Long lSysRoleOrgId=sysRoleOrgId[i];
					if(i>=sysDeviceRoleOrgId.length) {								//   开始新增
						SysDeviceRoleOrg sysDeviceRoleOrg = new SysDeviceRoleOrg();
						sysDeviceRoleOrg.setSysRoleOrgId(lSysRoleOrgId);
						sysDeviceRoleOrg.setSysDeviceId(id);
						sysDeviceRoleOrg.setCreateBy(createBy); 
						sysDeviceRoleOrg.setSysUserId(sysUserId);
						insertDeviceRoleOrg(sysDeviceRoleOrg);
					}else {															//	修改原来的
						SysDeviceRoleOrg LSysDeviceRoleOrg = new SysDeviceRoleOrg();
						LSysDeviceRoleOrg.setSysRoleOrgId(lSysRoleOrgId);
						LSysDeviceRoleOrg.setId(sysDeviceRoleOrgId[i]);
						LSysDeviceRoleOrg.setUpdateBy(updateBy); 
						LSysDeviceRoleOrg.setSysUserId(sysUserId);
						updateDeviceRoleOrg(LSysDeviceRoleOrg);
					}
				}
			}else if(sysDeviceRoleOrgId.length==sysRoleOrgId.length){		// 	修改原来的
				for(int i=0;i<sysRoleOrgId.length;i++) {
					Long lSysRoleOrgId=sysRoleOrgId[i];
					SysDeviceRoleOrg LSysDeviceRoleOrg = new SysDeviceRoleOrg();
					LSysDeviceRoleOrg.setSysRoleOrgId(lSysRoleOrgId);
					LSysDeviceRoleOrg.setId(sysDeviceRoleOrgId[i]);
					LSysDeviceRoleOrg.setUpdateBy(updateBy); 
					LSysDeviceRoleOrg.setSysUserId(sysUserId);
					updateDeviceRoleOrg(LSysDeviceRoleOrg);
				}
			}else if(sysDeviceRoleOrgId.length>sysRoleOrgId.length) {		
				for(int i=0;i<sysDeviceRoleOrgId.length;i++) {
					if(i>=sysRoleOrgId.length) {							//	解绑
						SysDeviceRoleOrg LSysDeviceRoleOrg = new SysDeviceRoleOrg();
						LSysDeviceRoleOrg.setSysRoleOrgId(0l);
						LSysDeviceRoleOrg.setId(sysDeviceRoleOrgId[i]);
						LSysDeviceRoleOrg.setUpdateBy(updateBy); 
						LSysDeviceRoleOrg.setSysUserId(0l);
						LSysDeviceRoleOrg.setSysDeviceId(0L);
						updateDeviceRoleOrg(LSysDeviceRoleOrg);
					}else {													
						Long lSysRoleOrgId=sysRoleOrgId[i];
						SysDeviceRoleOrg LSysDeviceRoleOrg = new SysDeviceRoleOrg();
						LSysDeviceRoleOrg.setSysRoleOrgId(lSysRoleOrgId);
						LSysDeviceRoleOrg.setId(sysDeviceRoleOrgId[i]);
						LSysDeviceRoleOrg.setUpdateBy(updateBy); 
						LSysDeviceRoleOrg.setSysUserId(sysUserId);
						updateDeviceRoleOrg(LSysDeviceRoleOrg);
					}
					
				}
			}
			
		}else {								//	在sys_device_role_org没有这条记录  添加记录
			if(sysRoleOrgId!=null&&sysRoleOrgId.length>0) {
				for(int i=0;i<sysRoleOrgId.length;i++) {
					Long lSysRoleOrgId=sysRoleOrgId[i];
					SysDeviceRoleOrg sysDeviceRoleOrg = new SysDeviceRoleOrg();
					sysDeviceRoleOrg.setSysRoleOrgId(lSysRoleOrgId);
					sysDeviceRoleOrg.setSysDeviceId(id);
					sysDeviceRoleOrg.setCreateBy(createBy); 
					sysDeviceRoleOrg.setSysUserId(sysUserId);
					insertDeviceRoleOrg(sysDeviceRoleOrg);
				}
			}else {
				SysDeviceRoleOrg sysDeviceRoleOrg = new SysDeviceRoleOrg();
				sysDeviceRoleOrg.setSysDeviceId(id);
				sysDeviceRoleOrg.setCreateBy(createBy); 
				sysDeviceRoleOrg.setSysUserId(sysUserId);
				insertDeviceRoleOrg(sysDeviceRoleOrg);
			}
		}*/
//		return id;
	}
	

	@Override
	public Result autoBind(String deviceSerial, String deviceName, String userName, String password) {
		// 基于权限，有该权限才能绑定该设备   错误  单个设备是不存在权限这个概念
		// 查询该用户是否存在
		SysUser sysUser = mSysUserMapper.selectUserByLoginName(userName);
		if(sysUser==null) {
			return Result.instance(ResponseCode.unknown_account.getCode(), ResponseCode.unknown_account.getMsg());
		}
		Long sysUserId= sysUser.getId(); 
		String dbUserPassword = sysUser.getPassword();
		String passwordSalt = sysUser.getPasswordSalt();
		String createPassword = StringUtil.createPassword(password, passwordSalt, 2);
		if(!createPassword.equals(dbUserPassword)){
			return Result.instance(ResponseCode.password_incorrect.getCode(),ResponseCode.password_incorrect.getMsg());
		}
		// 查询该设备是否已经绑定，如果被其他绑定，则自动解绑其他，绑定自己
		SysDevice sysDevice = mSysDeviceMapper.selectBindByDeviceSerial(deviceSerial);
		
		SysOrganization sysOrganization = mSysOrganizationMapper.selectIdByUserId(sysUserId);
		Long sysOrgId = sysOrganization.getId();
		if(sysDevice==null) {
			SysDevice sysDeviceN = new SysDevice();
			sysDeviceN.setDeviceName(deviceName);
			
			sysDeviceN.setCreateBy(sysUserId);
			sysDeviceN.setUpdateBy(sysUserId);
			sysDeviceN.setDeviceSerial(deviceSerial);
			sysDeviceN.setStatus(1);
			
			
			Long insertDevice = insertDevice(sysDeviceN,sysUserId,sysOrgId);
			if(insertDevice>0) {
				return Result.instance(ResponseCode.success.getCode(), "绑定成功");
			}
		}
		Long sysDeviceId = sysDevice.getId();
		
		
		List<SysDeviceRoleOrg> listSysDevRog= mSysDeviceRoleOrgMapper.selectByDeviceId(sysDeviceId);
		Long resultId=0l;
		if(listSysDevRog.size()>0) {
			// 已绑定，解除绑定
		 
			SysDeviceRoleOrg sysDeviceRoleOrg = listSysDevRog.get(0); 
			sysDeviceRoleOrg.setSysOrgId(sysOrgId);
			sysDeviceRoleOrg.setSysUserId(sysUserId);
			sysDeviceRoleOrg.setUpdateBy(sysUserId); 
			resultId = mSysDeviceRoleOrgMapper.update(sysDeviceRoleOrg);
		}else {
			// 新增绑定
			SysDeviceRoleOrg sysDeviceRoleOrg = new SysDeviceRoleOrg();
			sysDeviceRoleOrg.setSysOrgId(sysOrgId);
			sysDeviceRoleOrg.setSysDeviceId(sysDeviceId);
			sysDeviceRoleOrg.setSysUserId(sysUserId);
			sysDeviceRoleOrg.setCreateBy(sysUserId);
			sysDeviceRoleOrg.setUpdateBy(sysUserId);
			resultId = mSysDeviceRoleOrgMapper.insert(sysDeviceRoleOrg);
		}
		if(!StringUtils.isEmpty(deviceName)) {
			SysDevice sysDevice2 = new SysDevice(); 
			sysDevice2.setDeviceName(deviceName);
			sysDevice2.setId(sysDevice.getId());
			mSysDeviceMapper.update(sysDevice2); 
		}
		// 绑定设备 成功
		if(resultId>0) {
			return Result.instance(ResponseCode.success.getCode(), "绑定成功");
		}
		return Result.error("参数错误");
	}





	@Override
	public Result unBindDevice(Long sysOrgId, SysDevice sysDevice, Long sysUserId) {
		log.info("insert-"+sysDevice.toEntityString()+"-sysUserId-->"+sysUserId+"-sysUserIdRoleOrgid-->"+sysOrgId+"-->");
		mSysDeviceMapper.update(sysDevice);
		
//		Long createBy = sysDevice.getCreateBy(); 
		Long updateBy = sysDevice.getUpdateBy(); 
		Long devId = sysDevice.getId(); 
		boolean validUse = isValidUse(devId);
		if(validUse) {		//	设备不存在，
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Result.instance(ResponseCode.data_not_exist.getCode(),ResponseCode.data_not_exist.getMsg());
		}else {
			SysDeviceRoleOrg lSysDeviceRoleOrg = new SysDeviceRoleOrg();
			lSysDeviceRoleOrg.setSysOrgId(sysOrgId);
			lSysDeviceRoleOrg.setUpdateBy(updateBy); 
			lSysDeviceRoleOrg.setSysUserId(sysUserId);
			lSysDeviceRoleOrg.setSysDeviceId(devId);
			lSysDeviceRoleOrg.setStatus(2);
		   Long result=updateDeviceRoleOrg(lSysDeviceRoleOrg);
		   System.out.println("result-->"+result);
		   if(result>0)return Result.success(lSysDeviceRoleOrg);
		}
		return Result.error(ResponseCode.data_not_exist.getMsg());
	}





	/**
	 * 天剑sys_device_role_org
	 * @param sysDevice
	 * @param sysUserId
	 * @param sysUserIdRoleOrgid
	 * @return
	 */
/*	public Long updateRoleOrgId(SysDevice sysDevice, Long sysUserId, Long sysUserIdRoleOrgid) {
		log.info("updateRoleOrgId-"+sysDevice.toEntityString()+"-sysUserId-->"+sysUserId+"-sysUserIdRoleOrgid-->"+sysUserIdRoleOrgid);
		Long createBy = sysDevice.getCreateBy(); 
		Long id = sysDevice.getId(); 
		SysDeviceRoleOrg sysDeviceRoleOrg = new SysDeviceRoleOrg();
		sysDeviceRoleOrg.setSysDeviceId(id);
		sysDeviceRoleOrg.setCreateBy(createBy); 
		sysDeviceRoleOrg.setSysUserId(sysUserId);
		sysDeviceRoleOrg.setSysRoleOrgId(sysUserIdRoleOrgid);
		insertDeviceRoleOrg(sysDeviceRoleOrg);
		return id;
	}
*/
	
	
	



	@Override
	public Long insertDeviceRoleOrg(SysDeviceRoleOrg sysDeviceRoleOrg) {
		
		boolean isValid= isValidUse(sysDeviceRoleOrg.getSysDeviceId());
		if(isValid) {
			 mSysDeviceRoleOrgMapper.insert(sysDeviceRoleOrg);
		}
		
		return sysDeviceRoleOrg.getId();
		
	}
	
	
	@Override
	public Long updateDeviceRoleOrg(SysDeviceRoleOrg sysDeviceRoleOrg) {
		  
		return mSysDeviceRoleOrgMapper.update(sysDeviceRoleOrg);
	}





	/**
	 * 查询设备
	 */
	@Override
	public PageInfo selectPage(int page, int rows,String sort,String order,String deviceName,String deviceSerial) {
		log.info("page--"+page+"--rows-->"+rows+"--sort-->"+sort+"--order-->"+order+"--deviceName-->"+deviceName+"--deviceSerial-->"+deviceSerial);
		int counts=mSysDeviceMapper.selectCounts();
		 PageHelper.startPage(page, rows);
		 List<SysDevice> listDevice= mSysDeviceMapper.selectAll(sort,order,deviceName,deviceSerial);
		 List<SysDeviceDto> listSysDeviceDto=new ArrayList<>();
		 for(SysDevice sysDevice:listDevice) {
			 Long id = sysDevice.getId(); 				//  设备id
			 List<SysDeviceRoleOrg> sysDeviceRoleOrg = mSysDeviceRoleOrgMapper.selectByDeviceId(id);
			 SysDeviceDto sysDeviceDto = new SysDeviceDto();
			 BeanUtils.copyProperties(sysDevice, sysDeviceDto);
			 sysDeviceDto.setSysDeviceRoleOrg(sysDeviceRoleOrg);
			 listSysDeviceDto.add(sysDeviceDto);
		 }
		 System.out.println("selectPage-->"+listDevice.toString());
		//2019/07/07     先查询设备    包含了数据权限，只允许查询获取到查询权限的设备每做一个功能都要考虑到权限的赋予问题
		//  查询对应职能下的设备
//		mSysDeviceRoleOrgMapper.selectDeviceIdByRoleOrgId();
		//  查询所有的用户，
		 
		
		
		return new PageInfo(counts, listSysDeviceDto);
	}









	




	
}
