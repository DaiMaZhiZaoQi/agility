package com.hunt.service.impl;

import com.github.pagehelper.PageHelper;
import com.hunt.dao.*;
import com.hunt.model.entity.*;
import com.hunt.service.SysOrganizationService;
import com.hunt.service.SysUserService;
import com.hunt.service.SystemService;
import com.hunt.util.BeanHelpUtils;
import com.hunt.util.ListUtil;
import com.hunt.util.PermissionUtil;
import com.hunt.util.Result;
import com.hunt.util.StringUtil;
import com.hunt.util.UtReadCsv;
import com.sun.xml.internal.bind.v2.TODO;
import com.sun.xml.internal.ws.org.objectweb.asm.Label;
import com.hunt.model.dto.LoginInfo;
import com.hunt.model.dto.PageInfo;
import com.hunt.model.dto.PerFeatureDto;
import com.hunt.model.dto.SysCallLogDeviceRecoDto;
import com.hunt.model.dto.SysDeviceAndCallDto;
import com.hunt.model.dto.SysDeviceCallLogAndRecordDto;
import com.hunt.model.dto.SysUserDto;

import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: ouyangan
 * @Date : 2016/10/7
 */
@Service
@Transactional
public class SysUserServiceImpl implements SysUserService {
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserRoleOrganizationMapper sysUserRoleOrganizationMapper;
    @Autowired
    private SysRoleOrganizationMapper mSysRoleOrganizationMapper;
    @Autowired
    private SysOrganizationMapper mSysOrganizationMapper;
    @Autowired
    private SysUserPermissionMapper sysUserPermissionMapper;
    
    @Autowired
    private SysUserRoleMapper mSysUserRoleMapper;
    @Autowired
    private SysUserInOrgMapper mSysUserInOrgMapper;
    @Autowired
    private SysUserOrganizationMapper mSysUserOrgnizationMapper;
    
    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    @Autowired
    private SysLoginStatusMapper sysLoginStatusMapper;
    @Autowired
    private SysPermissionGroupMapper sysPermissionGroupMapper;
    @Autowired
    private SysDeviceRoleOrgMapper mSysDeviceRoleOrgMapper;
    @Autowired
    private SysDeviceMapper mSysDeviceMapper;
    @Autowired
    private SysDeviceTotalMapper mSysDeviceTotalMapper;
    @Autowired
    private SysDeviceCallLogMapper mSysDeviceCallLogMapper;
    @Autowired
    private SysDeviceRecordMapper mSysDeviceRecordMapper;
    @Autowired
    private SystemService mSystemService;
    @Autowired
    private SysOrganizationService mSysOrganizationService;
    
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
//    @Autowired
//    private InitSuper mInitSupper;
    
    @Override
	public Result init(String loginName,String passsword) {
    	if(!"super".equals(loginName)&&!"111111".equals(passsword)) {
    		return null;
    	}
    	SysUser sysUser = selectByLoginName(loginName);
    	if(sysUser==null) {
//    		InitSuper mInitSupper=new InitSuper();
//    		mInitSupper.initSysUser();
    	}
    	
    	
    	return Result.success();
    	
	}
    
    
    
    

	@Override
    public long insertUser(SysUser user, String jobIds, String permissionIds) {
        sysUserMapper.insert(user);
        String[] jobIdArray = jobIds.split(",");
        for (String jobid : jobIdArray) {			//  
            SysUserRoleOrganization userRoleOrganization = new SysUserRoleOrganization();
            userRoleOrganization.setSysUserId(user.getId());
            userRoleOrganization.setSysRoleOrganizationId(Long.valueOf(jobid));
            userRoleOrganization.setIsFinal(1);
            sysUserRoleOrganizationMapper.insert(userRoleOrganization);
        }
        if (StringUtils.hasText(permissionIds)) {
            String[] permissionIdArray = permissionIds.split(",");
            for (String permissionId : permissionIdArray) {
                SysUserPermission userPermission = new SysUserPermission();
                userPermission.setSysUserId(user.getId());
                userPermission.setSysPermissionId(Long.valueOf(permissionId));
                userPermission.setIsFinal(1);
                sysUserPermissionMapper.insert(userPermission);
            }
        }
        return user.getId();
    }
	
	
	
    

    
    
    
    @Override
	public long insertUserOrgRole(SysUser user, String roleIds, SysOrganization sysOrganization,List<SysOrganization> listSysOrg) {
    	 sysUserMapper.insert(user);
         String[] roleArrays = roleIds.split(",");
         for (String roleId : roleArrays) {			
        	 	SysUserRole sysUserRole = new SysUserRole();
        	 	sysUserRole.setSysUserId(user.getId());
        	 	sysUserRole.setSysRoleId(Long.valueOf(roleId));
        	 	mSysUserRoleMapper.insert(sysUserRole); 
         }
         if(sysOrganization!=null) {
        	 SysUserInOrg sysUserInOrg = new SysUserInOrg();
        	 sysUserInOrg.setSysOrgId(sysOrganization.getId());
        	 sysUserInOrg.setSysUserId(user.getId());
        	 sysUserInOrg.setSysOrgCode(sysOrganization.getOrgCode());
        	 mSysUserInOrgMapper.insert(sysUserInOrg);
         }
         
         
        	 if(listSysOrg.size()>0) {
//        		 List<Long> listOrgId=new ArrayList<>();   //  所持有的机构权限id
        		 for(SysOrganization sysOrg:listSysOrg) {   
        			 SysUserOrganization sysUserOrg = new SysUserOrganization();
        			 sysUserOrg.setSysUserId(user.getId());
        			 sysUserOrg.setSysOrgId(sysOrg.getId());
        			 sysUserOrg.setSysOrgCode(sysOrg.getOrgCode()); 
//        			 if(!listOrgId.contains(sysOrg.getId())) {
//        				 listOrgId.add(sysOrg.getId());
//        			 }
        			 mSysUserOrgnizationMapper.insert(sysUserOrg);
        		 }
//        		 if(!listOrgId.contains(sysOrganization.getId())) {   // 所在机构权限是默认持有
//        				SysUserOrganization sysUserOrg = new SysUserOrganization();
//        				sysUserOrg.setSysUserId(user.getId());
//        				sysUserOrg.setSysOrgId(sysOrganization.getId());
//        				sysUserOrg.setSysOrgCode(sysOrganization.getOrgCode());
//        				 mSysUserOrgnizationMapper.insert(sysUserOrg);
//        		 }
        	 }else {			//  添加用户默认的数据权限
				SysUserOrganization sysUserOrg = new SysUserOrganization();
				sysUserOrg.setSysUserId(user.getId());
				sysUserOrg.setSysOrgId(sysOrganization.getId());
				sysUserOrg.setSysOrgCode(sysOrganization.getOrgCode());
				 mSysUserOrgnizationMapper.insert(sysUserOrg);
			}
         
         
       
         
         // TODO 考虑刷新权限   详见之前新增用户时的权限刷新规则
         
         // 通过用户id查找权限,有该权限则跳过,没有该权限则删除
//         if (StringUtils.hasText(permissionIds)) {
//             String[] permissionIdArray = permissionIds.split(",");
//             for (String permissionId : permissionIdArray) {
//                 SysUserPermission userPermission = new SysUserPermission();
//                 userPermission.setSysUserId(user.getId());
//                 userPermission.setSysPermissionId(Long.valueOf(permissionId));
//                 userPermission.setIsFinal(1);
//                 sysUserPermissionMapper.insert(userPermission);
//             }
//         }
         return user.getId();
	}





	@Override
	public long insertUserPermission(SysUser sysUser) {
    	 
    	SysPermissionGroup sysPermissionGroup = createPermissionGroup(sysUser);
    	
    	sysPermissionGroupMapper.insert(sysPermissionGroup);
    	Long userId = sysUser.getId();
    	Long permiGroupId = sysPermissionGroup.getId(); 
    	
//    	long  permiGroupId= sysPermissionGroupMapper.selectIdByGroupName("设备状态");		//  用户设备权限默认权限组
    	List<SysPermission> listPermission = createDefaultPermission(userId,permiGroupId); 
    	List<String> listPermissionCode=getPermissionCode(listPermission); 
    	System.out.println("listPermission-->"+listPermission.size()+"--->"+listPermission.toString());
    	long insertSuccess=0;
    	if(permiGroupId!=0&&listPermission.size()>0&&listPermissionCode.size()>0) {
    		sysPermissionMapper.inertDefaultPermission(listPermission);
     		List<SysPermission> listSysPermission = sysPermissionMapper.selectIdByCode(listPermissionCode);
    		System.out.println("insertUserPermission-->"+listSysPermission.toString());
    		for(SysPermission sysPermission:listSysPermission) {
    			System.out.println("添加成功-->"+sysPermission.toString());
    			Long idLong=sysPermission.getId();
    			if(idLong!=null&&idLong>0) {
    				SysUserPermission userPermission = new SysUserPermission();
    				userPermission.setSysUserId(userId);
    				userPermission.setSysPermissionId(idLong);
    				userPermission.setIsFinal(1);
    				Long insertId = sysUserPermissionMapper.insert(userPermission);
    				insertSuccess= userPermission.getId();
//    				System.out.println("insertId-->"+insertId+"--id-->"+id);
    				
    			}else {
    				//  TODO 添加失败手动回滚事务
    				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    			}
    		}
    	}
		return insertSuccess;
	}



	@Override
	public Long insertObjPermission(SysPermissionGroup sysPermissionGroup,Long userId, PerFeatureDto preFeature) {
		String name = sysPermissionGroup.getName();
		boolean existGroupName = sysPermissionGroupMapper.isExistGroupName(name);
		if(existGroupName)return 2l;
    	sysPermissionGroupMapper.insert(sysPermissionGroup);
    	Long permiGroupId = sysPermissionGroup.getId(); 
    	List<SysPermission> listPermission = PermissionUtil.createDefPermission(preFeature, permiGroupId); 
    	List<String> listPermissionCode=getPermissionCode(listPermission); 
    	System.out.println("listPermission-->"+listPermission.size()+"--->"+listPermission.toString());
    	long insertSuccess=0;
    	if(listPermissionCode.size()>0) {
    		sysPermissionMapper.inertDefaultPermission(listPermission);
     		List<SysPermission> listSysPermission = sysPermissionMapper.selectIdByCode(listPermissionCode);
    		System.out.println("insertUserPermission-->"+listSysPermission.toString());
    		for(SysPermission sysPermission:listSysPermission) {
    			System.out.println("添加成功-->"+sysPermission.toString());
    			Long idLong=sysPermission.getId();
    			if(idLong!=null&&idLong>0) {
    				SysUserPermission userPermission = new SysUserPermission();
    				userPermission.setSysUserId(userId);
    				userPermission.setSysPermissionId(idLong);
    				userPermission.setIsFinal(1);
    				Long insertId = sysUserPermissionMapper.insert(userPermission);
    				insertSuccess= userPermission.getId();
    			}else {
    				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    			}
    		}
    	}
		return insertSuccess;
	}





	@Override
	public int insertUserPermissionSingle(long userId) {
       	String perList="device:"+userId+":list";
       	long  permiGroupId= sysPermissionGroupMapper.selectIdByGroupName("设备状态");
    	SysPermission sysPerList = new SysPermission();
    	sysPerList.setName("查询设备");
    	sysPerList.setDescription("查看该用户的所有状态");
    	sysPerList.setCode(perList);
    	sysPerList.setSysPermissionGroupId(permiGroupId);
    	sysPermissionMapper.inertDefaultPermissionSingle(sysPerList);
    	System.out.println("sysperList-->"+sysPerList.toString());
    	
    	
    	
		return 0;
	}
	
	/**
	 * 每一个用户都有默认的机构权限  organization:list insert sys_role_permission
	 * 
	 */
	private void insertOrgPermission() {
		//  TODO  insert OrgPermission
	}
	
		/**
		 * 获得权限码
		 * @param listPermission
		 * @return
		 */
	   private List<String> getPermissionCode(List<SysPermission> listPermission) {
		   List<String> listPermissionCode=new ArrayList<>();
			for(SysPermission permission:listPermission) {
				listPermissionCode.add(permission.getCode());
			}
			return listPermissionCode;
		}
	   
	   /**
	    * 创建用户权限组，每一个用户就是一个权限组
	    * @param user
	    * @return
	    */
	   private SysPermissionGroup createPermissionGroup(SysUser user) {
		   String loginName = user.getLoginName();
		   Long userId = user.getId();
		   SysPermissionGroup sysPermissionGroup = new SysPermissionGroup();
		   String permissionGrName="#"+userId+""+loginName+"的设备状态";
	        sysPermissionGroup.setName(permissionGrName);
	        sysPermissionGroup.setDescription(permissionGrName);
	        sysPermissionGroup.setIsFinal(2);
	        return sysPermissionGroup;
	   }
	   
	private List<SysPermission> createDefaultPermission(long userId,long sysPermissionGroupId) {
    	String perList="device:"+userId+":list";
    	String perUpdate="device:"+userId+":update";
    	String perDelete="device:"+userId+":delete";
    	
    	List<SysPermission> listSysPermission=new ArrayList<>();
    	SysPermission sysPerList = new SysPermission();
    	sysPerList.setName("查询设备");
    	sysPerList.setDescription("查看该用户的所有状态");
    	sysPerList.setCode(perList);
    	sysPerList.setSysPermissionGroupId(sysPermissionGroupId);
    	
    	SysPermission sysPerUpdate=new SysPermission();
    	sysPerUpdate.setName("修改设备");
    	sysPerUpdate.setDescription("修改该设备数据");
    	sysPerUpdate.setCode(perUpdate);
    	sysPerUpdate.setSysPermissionGroupId(sysPermissionGroupId);
    	
    	SysPermission sysDelete=new SysPermission();
    	sysDelete.setName("删除设备");
    	sysDelete.setDescription("删除该设备数据");
    	sysDelete.setCode(perDelete);
    	sysDelete.setSysPermissionGroupId(sysPermissionGroupId);
    	
    	listSysPermission.add(sysPerList);
    	listSysPermission.add(sysPerUpdate);
    	listSysPermission.add(sysDelete);
    	
    	
    
    
    	return listSysPermission;
    	
    }



	@Override
    public boolean isExistLoginName(String loginName) {
        return sysUserMapper.selectByLoginName(loginName);
    }

    @Override
    public SysUser selectById(long id) {
        return sysUserMapper.selectById(id);
    }
    
    
    
    
    
	@Override
	public List<SysDeviceAndCallDto> listUserAndDevice(Long id) {    
			List<SysDeviceAndCallDto> listDeviceAndCallDto=new ArrayList<>();
			//   查询用户   SysUser
			SysUser sysUser = sysUserMapper.selectById(id);
			Set<SysRoleOrganization> set=new HashSet<>();
			Set<SysOrganization> setOrg=new HashSet<>();
			//	 根据用户id查询SysDeviceRoleOrg
			List<SysDeviceRoleOrg> listSysDeviceRoleOrg = mSysDeviceRoleOrgMapper.selectByUserId(id);// 一个用户只绑定一个设备
			for(SysDeviceRoleOrg sysDeviceRoleOrg:listSysDeviceRoleOrg) {
				SysDeviceAndCallDto dto=new SysDeviceAndCallDto();
				dto.setSysUser(sysUser);
				Long sysDeviceId = sysDeviceRoleOrg.getSysDeviceId();
				System.out.println("listUserAndDevice-->"+sysDeviceId);
				//   查询设备  SysDevice
				SysDevice sysDevice = mSysDeviceMapper.selectById(sysDeviceId);
				dto.setSysDevice(sysDevice);
				Long sysOrgId = sysDeviceRoleOrg.getSysOrgId(); 
//				Long sysRoleOrgId = sysDeviceRoleOrg.getSysRoleOrgId();
				//	 查询设备人员所处的角色  查询设备人员所处的结构
//				selectRoleOrg(sysOrgId,set,setOrg);
				selectUpRoleOrg(sysOrgId,setOrg);
				
				
				// 	  查询设备统计	sysDeviceTotal
				List<SysDeviceTotal> sysDeviceTotal = mSysDeviceTotalMapper.selectByDeviceId(sysDeviceId);
				   
				dto.setSysDeviceTotal(BeanHelpUtils.calcu(sysDeviceTotal));
				// 	 查询通话记录	listDeviceCallLog
//				List<SysDeviceCallLogAndRecordDto> listDto=new ArrayList<>();
//				List<SysDeviceCallLog> listDeviceCallLog = mSysDeviceCallLogMapper.selectTotalByDeviceId(sysDeviceId);
//				System.out.println("通话记录list.size()-->"+listDeviceCallLog.toString());
//				for(SysDeviceCallLog callLog:listDeviceCallLog) {
//					SysDeviceCallLogAndRecordDto sysDeviceCallLogAndRecordDto = new SysDeviceCallLogAndRecordDto();
//					BeanUtils.copyProperties(callLog, sysDeviceCallLogAndRecordDto);
//					Long callId = callLog.getId(); 
//					//	查询设备的录音	listDeviceRecord
//					SysDeviceRecord sysDeviceRecord= mSysDeviceRecordMapper.selectByCallLogId(callId);
//					 sysDeviceCallLogAndRecordDto.setSysDeviceRecord(sysDeviceRecord); 
//					 listDto.add(sysDeviceCallLogAndRecordDto);
//				}
				dto.setSetSysRoleOrganization(set);
				dto.setSetSysOrganization(setOrg);
//				dto.setListDeviceCallLogAndRecord(listDto); 
				listDeviceAndCallDto.add(dto);
			}
		return listDeviceAndCallDto;
	}
	
	
	
	
	

	@Override
	public List<SysCallLogDeviceRecoDto> listCallDevByUserId(Long userId,String sort,String order,Integer page,Integer row,String sType,Long beginTime,Long endTime,String sContent,Integer callIsHaveRecord) {
//		List<SysCallLogDeviceRecoDto> listCallLog = mSysDeviceTotalMapper.selectCallLogByUser(userId,sort,order,page,row);
//		String callDate = StringUtil.getSRange(sTimeType);
		String callNumber="";
		String deviceSerial="";
		String callName="";
		switch (sType) {
			case "0":		// 查询号码
				callNumber=sContent;
				break;
			case "1":
				callName=sContent;
				break;
			case "2":		// 查询设备号
				deviceSerial=sContent;
				break; 
		}
		List<SysCallLogDeviceRecoDto> listCallLog = mSysDeviceTotalMapper.selectSearCallLogByUser(userId,sort,order,page,row,callNumber,deviceSerial,callName,beginTime,endTime, callIsHaveRecord);
//		Set<Long> setRoleOrgId=new HashSet<>();
		for(SysCallLogDeviceRecoDto dto:listCallLog) {
			Long orgId = dto.getOrgId();
			SysOrganization sysOrg = mSysOrganizationMapper.selectById(orgId);
//			SysCallLogDeviceRecoDto orgName = mSysDeviceTotalMapper.selectRogIdByRoleOrgId(devRolgId);
			if(sysOrg!=null) {
				dto.setOrgName(sysOrg.getName());
				dto.setOrgId(sysOrg.getId());
			}
		}
		
		return listCallLog;
	}





	@Override
	public List<SysDeviceAndCallDto> listUserCallRecord(Long id) {
		List<SysDeviceAndCallDto> listDeviceAndCallDto=new ArrayList<>();
//		SysUser sysUser = sysUserMapper.selectById(id);											//  查询用户   SysUser
		Set<SysRoleOrganization> set=new HashSet<>();											//  以人为单位或机构未单位，查询这个人之下的通话记录
		Set<SysOrganization> setOrg=new HashSet<>();
		SysUser sysUser = sysUserMapper.selectById(id);
		System.out.println("listUserCallRecord-->"+id);
		List<SysDeviceRoleOrg> listSysDeviceRoleOrg = mSysDeviceRoleOrgMapper.selectByUserId(id);// 一个用户只绑定一个设备
		if(listSysDeviceRoleOrg.size()<0)return listDeviceAndCallDto;
		SysDeviceAndCallDto dto=new SysDeviceAndCallDto();
		
//		List<SysDeviceCallLogAndRecordDto> listDto=new ArrayList<>();
		for(SysDeviceRoleOrg sysDeviceRoleOrg:listSysDeviceRoleOrg) {
			//  填充该人所在的  研发部/普通员工/CCCCCCG
			dto.setSysUser(sysUser);
			selectRoleOrg(sysDeviceRoleOrg.getSysOrgId(),set,setOrg);
			Long sysDeviceId = sysDeviceRoleOrg.getSysDeviceId();
			List<SysDeviceCallLog> listDeviceCallLog = mSysDeviceCallLogMapper.selectTotalByDeviceId(sysDeviceId);
//			List<SysDeviceCallLog> listDeviceCallLog = mSysDeviceCallLogMapper.selectByDeviceId(sysDeviceId,"");
//			for(SysDeviceCallLog callLog:listDeviceCallLog) {
//				SysDeviceCallLogAndRecordDto sysDeviceCallLogAndRecordDto = new SysDeviceCallLogAndRecordDto();
//				BeanUtils.copyProperties(callLog, sysDeviceCallLogAndRecordDto);
//				Long callId = callLog.getId(); 
//				//	查询设备的录音	listDeviceRecord
//				SysDeviceRecord sysDeviceRecord= mSysDeviceRecordMapper.selectByCallLogId(callId);
//				 sysDeviceCallLogAndRecordDto.setSysDeviceRecord(sysDeviceRecord); 
//				 listDto.add(sysDeviceCallLogAndRecordDto);
//			}			
			 
		}      
		dto.setSetSysRoleOrganization(set);
		dto.setSetSysOrganization(setOrg);
//		dto.setListDeviceCallLogAndRecord(listDto); 
		listDeviceAndCallDto.add(dto);
		
		return listDeviceAndCallDto;
	}

	/**
	 * 2019/08/22
	 */
	@Override
	public List<SysDeviceCallLogAndRecordDto> listCallLogRecord(Long roleOrgId,String sort,String order,Integer page,Integer rows) {
		List<SysDeviceCallLog> listDeviceCallLog = mSysDeviceCallLogMapper.selectByRoleOrgId(roleOrgId,sort,order,page,rows);
		List<SysDeviceCallLogAndRecordDto> listDto=new ArrayList<>();
		for(SysDeviceCallLog callLog:listDeviceCallLog) {
			SysDeviceCallLogAndRecordDto sysDeviceCallLogAndRecordDto = new SysDeviceCallLogAndRecordDto();
			BeanUtils.copyProperties(callLog, sysDeviceCallLogAndRecordDto);
			Long callId = callLog.getId(); 
			//	查询设备的录音	listDeviceRecord
			SysDeviceRecord sysDeviceRecord= mSysDeviceRecordMapper.selectByCallLogId(callId);
			 sysDeviceCallLogAndRecordDto.setSysDeviceRecord(sysDeviceRecord); 
			 listDto.add(sysDeviceCallLogAndRecordDto);
		}	
		return listDto;
	}





	/**
	 * 查找角色机构表
	 * @param sysOrgid  机构id
	 * @param set
	 * @return
	 */
	private void selectRoleOrg(Long sysOrgid,Set<SysRoleOrganization> set,Set<SysOrganization> setOrg){
		if(sysOrgid!=0) {
//			SysRoleOrganization sysRoleOrganization = mSysRoleOrganizationMapper.selectById(id);
//			set.add(sysRoleOrganization);
//			Long sysOrganizationId = sysRoleOrganization.getSysOrganizationId();
			SysOrganization sysOrganization= mSysOrganizationMapper.selectById(sysOrgid);
			setOrg.add(sysOrganization);
		}
	}
	
	/**点击的当前机构id**/
	private Long curOrgId=0l;
	
	@Override
	public void setCurOrgId(Long curOrgId) {
		this.curOrgId=curOrgId;
	}





	/**
	 * 查询该用户所处的机构
	 * @param sysOrgid
	 * @param setOrg
	 */
	private void selectUpRoleOrg(Long sysOrgid,Set<SysOrganization> setOrg) {
		if(sysOrgid!=0) {
			SysOrganization sysOrganization = mSysOrganizationMapper.selectById(sysOrgid);
			Long parentId = sysOrganization.getParentId();
			Long id = sysOrganization.getId();
			if(parentId==curOrgId) {
				setOrg.add(sysOrganization);
				return;
			}
			if(id==curOrgId) {
				sysOrganization.setName("本部");
				setOrg.add(sysOrganization);
				return;
			}else {
				selectUpRoleOrg(parentId, setOrg);
				
			} 
		}
	}



	/**
	 * 用户转变成机构类型
	 * @param sysUser
	 * @return
	 */
	public SysOrganization sysUserConvertSysOrg(SysUser sysUser) {
		SysOrganization sysOrganization=new SysOrganization();
		String loginName=sysUser.getLoginName();
		long id=sysUser.getId();
		sysOrganization.setName(loginName);
		sysOrganization.setId(id);
		sysOrganization.setOrgType(1);
		return sysOrganization;
	}
	

    @Override
    public void updateUser(SysUser user, String jobIds, String permissionIds) {
        sysUserMapper.update(user);
//        sysUserPermissionMapper.deleteByUserId(user.getId());
//        sysUserRoleOrganizationMapper.deleteUserId(user.getId());	//  删除用户角色
        Long userId = user.getId();
        List<Long> listPd = sysUserPermissionMapper.selectPdByUserId(userId);
        if (StringUtils.hasText(permissionIds)) {
            String[] permissionIdArray = permissionIds.split(","); 
            List<String> asList = Arrays.asList(permissionIdArray);
            List<Long> listPerId= ListUtil.getUpdate(listPd, asList);
            for(Long pId:listPerId) {
            	sysUserPermissionMapper.deleteByUserIdPid(userId, pId);
            }   //  db [1,2,3,4,5]	asList[1,2,6,7,8]
            List<Long> listAsInsert = ListUtil.getInsert(listPd, asList);
            for (Long permissionId : listAsInsert) {
                SysUserPermission userPermission = new SysUserPermission();
                userPermission.setSysUserId(userId);
                userPermission.setSysPermissionId(Long.valueOf(permissionId));
                userPermission.setIsFinal(1);
                sysUserPermissionMapper.insert(userPermission);
            }
        }else {					// 删除所有
             for(Long pId:listPd) {
             	sysUserPermissionMapper.deleteByUserIdPid(userId, pId);
             }
        }
        List<Long> listSysRoleOrgId= sysUserRoleOrganizationMapper.selectRoleOrgIdByUserId(userId);
        String[] jobIdArray = jobIds.split(",");
        List<String> asList = Arrays.asList(jobIdArray); 
        List<Long> listUpdate = ListUtil.getUpdate(listSysRoleOrgId,asList);
        for(Long roleOrgId:listUpdate) {
        	sysUserRoleOrganizationMapper.deleteByUserRoleOrgId(userId, roleOrgId);
        }
         
        List<Long> listJobId = ListUtil.getInsert(listSysRoleOrgId, asList);
        for(Long jobid:listJobId) {
        	 SysUserRoleOrganization userRoleOrganization = new SysUserRoleOrganization();
             userRoleOrganization.setSysUserId(userId);
             userRoleOrganization.setSysRoleOrganizationId(Long.valueOf(jobid));
             userRoleOrganization.setIsFinal(1);
             sysUserRoleOrganizationMapper.insert(userRoleOrganization);
        }
    }
    
    @Override
	public Result updateUserByOrgRole(SysUser user, String roleIds, String orgIds,String permiOrgIds) {
    	  sysUserMapper.update(user);
        Long userId = user.getId();
        List<Long> listRoleId = mSysUserRoleMapper.selectRoleIdByUserId(userId);
        // 用户角色表处理
        if (StringUtils.hasText(roleIds)) {
            String[] roleIdArray = roleIds.split(","); 
            List<String> asList = Arrays.asList(roleIdArray);
            List<Long> listPerId= ListUtil.getUpdate(listRoleId, asList);
            for(Long roleId:listPerId) {
            	mSysUserRoleMapper.updateByUserIdRoleId(userId,roleId);
            }
            List<Long> listAsInsert = ListUtil.getInsert(listRoleId, asList);
            for (Long roleId : listAsInsert) {
            	SysUserRole sysUserRole = new SysUserRole();
        	 	sysUserRole.setSysUserId(userId);
        	 	sysUserRole.setSysRoleId(roleId);
        	 	mSysUserRoleMapper.insert(sysUserRole); 
            }
        }else {					// 删除不具有的用户角色
             for(Long roleId:listRoleId) {
            	 mSysUserRoleMapper.updateByUserIdRoleId(userId,roleId);
             }
        }
        
        // 用户机构表处理
        List<Long> listSysUserOrgId= mSysUserOrgnizationMapper.selectOrgIdByUserId(userId);
        String[] orgIdsArray = orgIds.split(",");
        String[] permiOrgArray = permiOrgIds.split(",");
        List<String> asList = Arrays.asList(orgIdsArray); 
        List<Long> listUpdate = ListUtil.getUpdate(listSysUserOrgId,asList);
        for(Long roleOrgId:listUpdate) {
        	mSysUserOrgnizationMapper.updateByUserIdOrgId(userId, roleOrgId);
        }
         
        List<Long> listOrgId = ListUtil.getInsert(listSysUserOrgId, asList);
        for(Long orgId:listOrgId) { 
        	if(permiOrgArray.length>0) {
        		for(String permiOrgId:permiOrgArray) {
            		SysUserOrganization sysUserOrg = new SysUserOrganization();
            		sysUserOrg.setSysUserId(userId);
            		sysUserOrg.setSysOrgId(Long.valueOf(orgId));
            		sysUserOrg.setSysPermissionOrgId(Long.valueOf(permiOrgId));
            		mSysUserOrgnizationMapper.insert(sysUserOrg);
            	}
        	}else {
        		SysUserOrganization sysUserOrg = new SysUserOrganization();
        		sysUserOrg.setSysUserId(userId);
        		sysUserOrg.setSysOrgId(Long.valueOf(orgId));
        		mSysUserOrgnizationMapper.insert(sysUserOrg);
			}
        	
        }
		return Result.success();
	}
    
    
    @Override
    public Result updateUserDetail(SysUser user, String roleIds,SysOrganization sysOrganization,List<SysOrganization> listSysOrg) {
    	sysUserMapper.update(user);
    	Long userId = user.getId();
    	List<Long> listRoleId = mSysUserRoleMapper.selectRoleIdByUserId(userId);
    	// 用户角色表处理
    	if (StringUtils.hasText(roleIds)) {
    		String[] roleIdArray = roleIds.split(","); 
    		List<String> asList = Arrays.asList(roleIdArray);
    		List<Long> listPerId= ListUtil.getUpdate(listRoleId, asList);
    		for(Long roleId:listPerId) {
    			mSysUserRoleMapper.updateByUserIdRoleId(userId,roleId);
    		}
    		List<Long> listAsInsert = ListUtil.getInsert(listRoleId, asList);
    		for (Long roleId : listAsInsert) {
    			SysUserRole sysUserRole = new SysUserRole();
    			sysUserRole.setSysUserId(userId);
    			sysUserRole.setSysRoleId(roleId);
    			mSysUserRoleMapper.insert(sysUserRole); 
    		}
    	}
    	
    	// 修改用户所在的机构   sys_user_in_org
    	if(sysOrganization!=null) {
    		SysUserInOrg sysUserInOrg = mSysUserInOrgMapper.selectByUserId(userId);
    		
    		sysUserInOrg.setSysOrgCode(sysOrganization.getOrgCode());
    		sysUserInOrg.setSysOrgId(sysOrganization.getId());
    		mSysUserInOrgMapper.update(sysUserInOrg);
    	}
    	// 修改用户机构表权限 sys_user_organization  数据库 1,2,3,4    前端数据1,2,3,4,5,6,7
    	
    	List<Long> listSysUserOrgId= mSysUserOrgnizationMapper.selectOrgIdByUserId(userId);
    	List<Long> listOrgIdss=new ArrayList<>();
    	for(SysOrganization sysOrg:listSysOrg) {
    		Long lSysOrgId=sysOrg.getId();
    		if(!listOrgIdss.contains(lSysOrgId)) {
    			listOrgIdss.add(lSysOrgId);
    		}
    	}
    	List<Long> listUpdate = ListUtil.getUpdate(listSysUserOrgId,listOrgIdss);
    	System.out.println("listUpdate--->"+listSysUserOrgId.toString()+"--->"+listSysUserOrgId.toString()+"--->"+listOrgIdss.toString());
    	for(Long sysOrgId:listUpdate) {
    		mSysUserOrgnizationMapper.updateByUserIdOrgId(userId, sysOrgId);
    	}
    	List<Long> listOrgId = ListUtil.getInsert(listSysUserOrgId, listOrgIdss);
    	for(Long orgId:listOrgId) { 
    		for(SysOrganization sysOrg:listSysOrg) {
    			if(sysOrg.getId()==orgId) {
    				SysUserOrganization sysUserOrg = new SysUserOrganization();
    				sysUserOrg.setSysUserId(userId);
    				sysUserOrg.setSysOrgCode(sysOrg.getOrgCode());
    				sysUserOrg.setSysOrgId(orgId);
    				mSysUserOrgnizationMapper.insert(sysUserOrg);		
    			}
    		}
    		
    	}
    	return Result.success();
    }


	@Override
	public Result updateSuperUser(SysUser user, String roleIds) {
	  	sysUserMapper.update(user);
    	Long userId = user.getId();
    	List<Long> listRoleId = mSysUserRoleMapper.selectRoleIdByUserId(userId);
    	// 用户角色表处理
    	if (StringUtils.hasText(roleIds)) {
    		String[] roleIdArray = roleIds.split(","); 
    		List<String> asList = Arrays.asList(roleIdArray);
    		List<Long> listPerId= ListUtil.getUpdate(listRoleId, asList);
    		for(Long roleId:listPerId) {
    			mSysUserRoleMapper.updateByUserIdRoleId(userId,roleId);
    		}
    		List<Long> listAsInsert = ListUtil.getInsert(listRoleId, asList);
    		for (Long roleId : listAsInsert) {
    			SysUserRole sysUserRole = new SysUserRole();
    			sysUserRole.setSysUserId(userId);
    			sysUserRole.setSysRoleId(roleId);
    			mSysUserRoleMapper.insert(sysUserRole); 
    		}
    	}
    	return Result.success();
	}





	public static void main(String[] args) {
		List<Long> listL=new ArrayList<>();
		for(Long ll:listL) {
			System.out.println("ll-->"+ll);
		}
		listL.add(1l);
		listL.add(3l);
		listL.add(4l);
		listL.add(8l);
		List<String> listS=new ArrayList<>();
		listS.add("1");
		listS.add("3");
		listS.add("5");
		listS.add("7");
		List<Long> update = ListUtil.getUpdate(listL, listS);
		List<Long> insert = ListUtil.getInsert(listL, listS); 
		System.out.println("update-->"+update+"insert-->"+insert); 
	}
   
	/**
	 * 查询该用户下有哪些机构，该机构下有哪些用户
	 * @param userId
	 * @return
	 */
	private Set<Long> selectUserId(Long userId) {
		List<Long> listOrgId = mSysUserOrgnizationMapper.selectOrgIdByUserId(userId);
		 return mSysUserInOrgMapper.selectUserIdByListOrgId(listOrgId);
	
	}
	
	
	
	

    @Override
	public PageInfo selectPage(int page, int rows, String sort, String order, String loginName, String zhName,
			String email, String phone, String address, long userId) {
    	Set<Long> selectUserId = selectUserId(userId);
    	
    	 PageHelper.startPage(page, rows);
         List<SysUser> sysUsers = sysUserMapper.selectAllUserId(sort, order, loginName, zhName, email, phone, address,selectUserId);
         System.out.println("selectPage--sysUsers-->"+sysUsers.toString()); 
         
         List<SysUserDto> sysUserDtos = new ArrayList<>();
         for (SysUser user : sysUsers) {
         	System.out.println("selectPage--sysUsers-->"+user.getZhName());
             SysUserDto userDto = new SysUserDto();
             BeanUtils.copyProperties(user, userDto);
             userDto.setPassword("");
             userDto.setPasswordSalt("");
             //  查询用户所处的角色
             List<SysUserRole> listSysUserRole = mSysUserRoleMapper.selectByUserId(user.getId());
             // 查询用户所在的机构
             SysUserInOrg sysUserInOrg = mSysUserInOrgMapper.selectByUserId(user.getId());
             // 查询用户的机构权限
             List<SysUserOrganization> listUserOrg = mSysUserOrgnizationMapper.selectByUserId(user.getId());
             
             userDto.setmSysUserInOrg(sysUserInOrg);
             userDto.setListUserRole(listSysUserRole); 
             userDto.setListUserOrg(listUserOrg);
             
             sysUserDtos.add(userDto);
         }
         PageInfo pageInfo = new PageInfo(sysUsers.size(), sysUserDtos);
         return pageInfo;
    	
    	
	}





	@Override
    public PageInfo selectPage(int page, int rows, String sort, String order, String loginName, String zhName, String email, String phone, String address) {
        log.info("page = [" + page + "], rows = [" + rows + "], sort = [" + sort + "], order = [" + order + "], loginName = [" + loginName + "], zhName = [" + zhName + "], email = [" + email + "], phone = [" + phone + "], address = [" + address + "]");
        int counts = sysUserMapper.selectCounts();
        PageHelper.startPage(page, rows);
        List<SysUser> sysUsers = sysUserMapper.selectAll(sort, order, loginName, zhName, email, phone, address);
        System.out.println("selectPage--sysUsers-->"+sysUsers.toString()); 
        
        List<SysUserDto> sysUserDtos = new ArrayList<>();
        for (SysUser user : sysUsers) {
        	System.out.println("selectPage--sysUsers-->"+user.getZhName());
            SysUserDto userDto = new SysUserDto();
            BeanUtils.copyProperties(user, userDto);
            userDto.setPassword("");
            userDto.setPasswordSalt("");
            //  查询用户所处的角色
            List<SysUserRole> listSysUserRole = mSysUserRoleMapper.selectByUserId(user.getId());
            // 查询用户所在的机构
            SysUserInOrg sysUserInOrg = mSysUserInOrgMapper.selectByUserId(user.getId());
            // 查询用户的机构权限
            List<SysUserOrganization> listUserOrg = mSysUserOrgnizationMapper.selectByUserId(user.getId());
            
//            List<SysUserPermission> userPermissions = sysUserPermissionMapper.selectByUserId(user.getId());
//            List<SysPermission> permissions = new ArrayList<>();
//            for (SysUserPermission userPermission : userPermissions) {
//                SysPermission sysPermission = sysPermissionMapper.selectById(userPermission.getSysPermissionId());
//                if(sysPermission!=null) {
//                	permissions.add(sysPermission);
//                }
//            }
            
            
//            List<SysUserRoleOrganization> userRoleOrganizations = sysUserRoleOrganizationMapper.selectByUserId(user.getId());
            //   TODO  最好是精细化权限管理，一个用户对另一个用户有管理权限，则加载该用户   
//            userDto.setPermissions(permissions);
            userDto.setmSysUserInOrg(sysUserInOrg);
            userDto.setListUserRole(listSysUserRole); 
            userDto.setListUserOrg(listUserOrg);
            
//            userDto.setUserRoleOrganizations(userRoleOrganizations);
            sysUserDtos.add(userDto);
        }
        PageInfo pageInfo = new PageInfo(counts, sysUserDtos);
        return pageInfo;
    }

    
    
    
    
    @Override
	public PageInfo selectUserByOrgId(Long sysOrgId) {
    	//  查询当前机构下的人
		List<Long> listOrgId=new ArrayList<>();
		mSysOrganizationService.selectSysOrgIdList(sysOrgId,listOrgId,false);
    	List<SysUserDto2> listSysUser = mSysUserInOrgMapper.selectUserByListOrgId(listOrgId);
		return new PageInfo(listSysUser.size(), listSysUser); 
	}
    
    
    
    

    @Override
	public List<SysUser> selectSysUserByOrgId(Long sysOrgId) {
    	SysOrganization organization = mSysOrganizationMapper.selectById(sysOrgId);
    	List<SysOrganization> lisSysOrg=new ArrayList<>();
    	lisSysOrg.add(organization);
    	List<SysOrganization> listOrg = getChildOrg(sysOrgId,lisSysOrg);
    	List<SysUser> listUser= new ArrayList<>();
    	for(SysOrganization org:listOrg) {
    		Long orgId = org.getId();
    		List<Long> listRoleOrgId = mSysRoleOrganizationMapper.selectIdByOrgId(orgId);
    		for(Long roleOrgId:listRoleOrgId) {
    			List<SysUserRoleOrganization> listSysUsRog = sysUserRoleOrganizationMapper.selectByRoleOrgId(roleOrgId);
        		
        		for(SysUserRoleOrganization sysUserRoOrg:listSysUsRog) {
        			SysUserDto sysUserDto = new SysUserDto();
        			
        			Long uId = sysUserRoOrg.getSysUserId();
        			SysUser sysUser = sysUserMapper.selectById(uId); 
        			listUser.add(sysUser);
        			/*BeanUtils.copyProperties(sysUser, sysUserDto);
        			List<SysUserRoleOrganization> listUserRoOrg = sysUserRoleOrganizationMapper.selectByUserId(sysUser.getId());
        			sysUserDto.setUserRoleOrganizations(listUserRoOrg);
        			sysUserDtos.add(sysUserDto);*/
        		}
    		}
    	}
		return listUser;
	}
    
    
    





	@Override
	public List<SysUser> selectSysUserByOrganization(Long id) {
		// TODO Auto-generated method stub
		return null;
	}





	/**
     * 
     * @param id sys_role_organization 的 id
     * @return
     */
    private List<SysRoleOrganization> getChildSysRoleOrg(Long id,List<SysRoleOrganization> list){
    	List<SysRoleOrganization> listSysRoleOrg = mSysRoleOrganizationMapper.selectByPId(id);
    	if(listSysRoleOrg.size()>0) {
    		list.addAll(listSysRoleOrg);
    	}
    	for(SysRoleOrganization sysRog:listSysRoleOrg) {
    		return getChildSysRoleOrg(sysRog.getId(),list);
    	}
    	return list;
    }
    
    private List<SysOrganization> getChildOrg(Long id,List<SysOrganization> list){
    	List<SysOrganization> listOrg = mSysOrganizationMapper.selectChildren(id);
    	if(listOrg.size()>0) {
    		list.addAll(listOrg);
    	}
    	
    	for(SysOrganization sysOrg:listOrg) {
    		return getChildOrg(sysOrg.getId(), list);
    	}
    	
    	return list;
    }




	@Override
    public void updateUser(SysUser user) {
        sysUserMapper.update(user);
    }

    @Override
    public SysUser selectByLoginName(String loginName) {
        return sysUserMapper.selectUserByLoginName(loginName);
    }

    @Override
    public LoginInfo login(SysUser user, Serializable id, int platform) {
        log.debug("sessionId is:{}", id.toString());
        LoginInfo loginInfo = new LoginInfo();
        BeanUtils.copyProperties(user, loginInfo);
        List<SysUserPermission> userPermissions = sysUserPermissionMapper.selectByUserId(user.getId());
        List<SysPermission> permissions = new ArrayList<>();
        for (SysUserPermission userPermission : userPermissions) {
            SysPermission sysPermission = sysPermissionMapper.selectById(userPermission.getSysPermissionId());
            permissions.add(sysPermission);
        }
        loginInfo.setPermissions(permissions);
        List<SysUserRoleOrganization> userRoleOrganizations = sysUserRoleOrganizationMapper.selectByUserId(user.getId());
        loginInfo.setJobs(userRoleOrganizations);

        SysLoginStatus newLoginStatus = new SysLoginStatus();  
        newLoginStatus.setSysUserId(user.getId());
        newLoginStatus.setSysUserZhName(user.getZhName());
        newLoginStatus.setSysUserLoginName(user.getLoginName());
        newLoginStatus.setSessionId(id.toString());
        newLoginStatus.setSessionExpires(new DateTime().plusDays(30).toDate());
        newLoginStatus.setPlatform(platform);

        SysLoginStatus oldLoginStatus = sysLoginStatusMapper.selectByUserIdAndPlatform(user.getId(), platform);
        if (oldLoginStatus != null) {
            if (!oldLoginStatus.getSessionId().equals(id.toString())) {
                redisTemplate.opsForValue().getOperations().delete(oldLoginStatus.getSessionId());
            }
            oldLoginStatus.setStatus(2);
            sysLoginStatusMapper.update(oldLoginStatus);
            newLoginStatus.setLastLoginTime(oldLoginStatus.getCreateTime());
        }
        sysLoginStatusMapper.insert(newLoginStatus);
        return loginInfo;
    }


    @Override
    public boolean isExistLoginNameExcludeId(long id, String loginName) {
        return sysUserMapper.isExistLoginNameExcludeId(id, loginName);
    }

    @Override
    public void deleteById(SysUser user) {
    	Long id = user.getId();
    	String loginName = user.getLoginName();
    	
        sysUserMapper.deleteById(id); 
        sysUserPermissionMapper.deleteByUserId(id);
        sysUserRoleOrganizationMapper.deleteUserId(id);
        sysPermissionMapper.deletPermissionByUserId(id);
        // TODO deleteById 删除用户时，该用户权限组应该也做删除
        sysPermissionGroupMapper.updatePermissionGroup(user);
    }


}
