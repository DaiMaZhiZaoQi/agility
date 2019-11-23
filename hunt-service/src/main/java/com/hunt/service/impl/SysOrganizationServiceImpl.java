package com.hunt.service.impl;

import com.google.gson.Gson;
import com.hunt.dao.SysDeviceTotalMapper;
import com.hunt.dao.SysOrganizationMapper;
import com.hunt.dao.SysRoleMapper;
import com.hunt.dao.SysRoleOrganizationMapper;
import com.hunt.model.dto.PageInfo;
import com.hunt.model.dto.SysCallLogDeviceRecoDto;
import com.hunt.model.dto.SysCallLogTotal;
import com.hunt.model.dto.SysDeviceAndCallDto;
import com.hunt.model.dto.SysDeviceCallLogAndRecordDto;
import com.hunt.model.dto.SysOrgACallDto;
import com.hunt.model.dto.SysOrganizationTree;
import com.hunt.model.dto.SysUserDto;
import com.hunt.model.entity.SysDeviceTotal;
import com.hunt.model.entity.SysOrganization;
import com.hunt.model.entity.SysUser;
import com.hunt.service.SysOrganizationService;
import com.hunt.service.SysRoleOrganizationService;
import com.hunt.service.SysUserService;
import com.hunt.util.PermissionUtil;
import com.hunt.util.StringUtil;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: ouyangan
 * @Date : 2016/10/10
 */
@Service
@Transactional
public class SysOrganizationServiceImpl implements SysOrganizationService {

    @Autowired
    private SysOrganizationMapper sysOrganizationMapper;

    @Autowired
    private SysRoleOrganizationMapper mSysRoleOrganizationMapper;
    
    @Autowired
    private SysDeviceTotalMapper mSysDeviceTotalMapper;
    
    @Autowired
    private SysRoleOrganizationService mSysRoleOrganizationServiceImpl;
    @Autowired
    private SysUserService mSysUserService;
    
    /**查询类型 0，查询机构，1，查询个人然后转换成机构*/
    private int queryType;
    
    public void setQueryType(int queryType) {
		this.queryType = queryType;
	}
    
    @Override
    public Long insertOrganization(SysOrganization sysOrganization) {
        Long i = sysOrganizationMapper.insert(sysOrganization);
        return i;
    }

    @Override
    public int deleteOrganization(long id) {
        SysOrganization sysOrganization = sysOrganizationMapper.selectById(id);
        sysOrganization.setStatus(2);
        sysOrganizationMapper.update(sysOrganization);
        return 1;
    }

    @Override
    public void updateOrganization(SysOrganization organization) {
        sysOrganizationMapper.update(organization);
    }
    
    
    @Override
    public PageInfo selectPage(int page, int row, long id) {
        SysOrganizationTree sysOrganizationTree = selectSysOrganizationTree(id);
        List<SysOrganizationTree> list = new ArrayList<>();
        list.add(sysOrganizationTree);
        PageInfo pageInfo = new PageInfo(sysOrganizationMapper.selectCounts(), list);
        return pageInfo;
    }
    
    
   
    @Override
	public PageInfo selectPageNoTree(int page, int row, long id) {
    	
    	List<SysOrganizationTree> list = new ArrayList<>();
    	list = selectSysOrganizationList(id,list);
        PageInfo pageInfo = new PageInfo(list.size(), list);
        return pageInfo;
	}
    
    
    
    @Override
	public PageInfo selectOrgFromOrgId(Integer page, Integer row, Long id) { 
    	List<SysOrganization> listOrg=new ArrayList<>();
    	List<SysOrganization> listOrganization = selectSysOrgList(id,listOrg,false);
    	PageInfo pageInfo=new PageInfo(listOrganization.size(), listOrganization);
		return pageInfo;
	}

	/**
     * 通过机构id
     * @param page
     * @param row
     * @param id 机构id
     * @return
     */
    @Override
    public PageInfo selectDeviceFromOrg(int page,int row,Long id) {
    	
    	List<SysDeviceAndCallDto> listDeviceAndCall=new ArrayList<>();
    	//	查询机构列表，父机构及其子机构  递归查询
    	List<SysOrganization> listOrg=new ArrayList<>();
    	List<SysOrganization> listOrganization = selectSysOrgList(id,listOrg,false);
    	//	通过机构id查询sys_role_organization  
    	for(SysOrganization sysOrganization:listOrganization) {
    		Long orgId = sysOrganization.getId();
    		 List<SysUser> listSysUser = mSysRoleOrganizationServiceImpl.selectSysUserByOrganization(orgId);		//  可做搜索
    		 if(listSysUser!=null) {
    			 for(SysUser sysUser:listSysUser) {
    				 String zhName = sysUser.getZhName();
    				 mSysUserService.setCurOrgId(id);
    				 List<SysDeviceAndCallDto> listUserAndDevice = mSysUserService.listUserAndDevice(sysUser.getId());
    				 listDeviceAndCall.addAll(listUserAndDevice);
    			 }
    		 }
    		
    	}
    	//	通过sys_role_org_id查询sys_user_role_organization   
    	
    	
    	PageInfo pageInfo=new PageInfo(listDeviceAndCall.size(), listDeviceAndCall);
    	return pageInfo;
    }
    
    
    
    
    
   
    @Override
	public PageInfo selectDeviceCallRecordFromOrg(int page, int row, Long id) {
    	
    	List<SysDeviceAndCallDto> listDeviceAndCall=new ArrayList<>();
    	//	查询机构列表，父机构及其子机构  递归查询
    	List<SysOrganization> listOrg=new ArrayList<>();
    	List<SysOrganization> listOrganization = selectSysOrgList(id,listOrg,false);
    	//	通过机构id查询sys_role_organization  
    	for(SysOrganization sysOrganization:listOrganization) {
    		Long orgId = sysOrganization.getId();
    		 List<SysUser> listSysUser = mSysRoleOrganizationServiceImpl.selectSysUserByOrganization(orgId);//查询该机构下的用户
    		 if(listSysUser!=null) {
    			 for(SysUser sysUser:listSysUser) {
    				 String zhName = sysUser.getZhName(); 
    				 
    				 List<SysDeviceAndCallDto> listUserAndDevice = mSysUserService.listUserCallRecord(sysUser.getId());
    				 listDeviceAndCall.addAll(listUserAndDevice);
    			 }
    		 }
//    		 List<Long> listRoleOrgId = mSysRoleOrganizationMapper.selectIdByOrgId(orgId);//查询该机构下的用户
//    		 for(Long roleOrgId:listRoleOrgId) {
//    			List<SysOrgACallDto> listSysOrg= mSysUserService.listCallLogRecord(roleOrgId,sort,order,page,rows);
//    		 }
    	}
    	PageInfo pageInfo=new PageInfo(listDeviceAndCall.size(), listDeviceAndCall);
    	return pageInfo;
	}
    
    @Override
    public PageInfo selectDeviceCallbyOrg(Long id,String sort,String order,Integer page,Integer rows) {
    	//	查询机构列表，父机构及其子机构  递归查询
    	List<SysOrganization> listOrg=new ArrayList<>();
    	List<SysOrganization> listOrganization = selectSysOrgList(id,listOrg,false);
    	//	通过机构id查询sys_role_organization  
    	List<SysOrgACallDto> listSysOrg=new ArrayList<>();
    	for(SysOrganization sysOrganization:listOrganization) {
    		Long orgId = sysOrganization.getId();
    		List<Long> listRoleOrgId = mSysRoleOrganizationMapper.selectIdByOrgId(orgId);//查询该机构下的用户
    		SysOrgACallDto sysOrgACallDto=new SysOrgACallDto(); 
    		List<SysDeviceTotal> listTotal=new ArrayList<>();
    		List<SysDeviceCallLogAndRecordDto> listDto=new ArrayList<>();
    		Set<Long> setDeviceId=new HashSet<>();
    		for(Long roleOrgId:listRoleOrgId) {
    			
    			List<SysDeviceCallLogAndRecordDto> listCallLogRecord = mSysUserService.listCallLogRecord(roleOrgId,sort,order,page,rows);
    			
    			listDto.addAll(listCallLogRecord);
    			for(SysDeviceCallLogAndRecordDto dto:listCallLogRecord) {
    				Long deviceId = dto.getDeviceId();
    				setDeviceId.add(deviceId);
    				
    			}
    			for(Long deviceId:setDeviceId) {
    				List<SysDeviceTotal> total= mSysDeviceTotalMapper.selectByDeviceId(deviceId);
    				listTotal.addAll(total);
    			}
    		}
    		SysDeviceTotal calcu = calcu(listTotal);
    		sysOrgACallDto.setListDeviceTotal(calcu);
    		sysOrgACallDto.setmOrganization(sysOrganization);
    		sysOrgACallDto.setList(listDto);
    		listSysOrg.add(sysOrgACallDto);
    	}
    	PageInfo pageInfo=new PageInfo(listSysOrg.size(), listSysOrg);
    	return pageInfo;
    }
    
//    @Override
  	public PageInfo selectAllCallLogByOrgOld(Long id, String sort, String order, Integer page, Integer rows,String sType,Long beginTime,Long endTime,String sContent,Integer callIsHaveRecord) {
    	List<SysOrganization> listOrg=new ArrayList<>();
    	List<SysOrganization> listOrganization = selectSysOrgList(id,listOrg,false);	
    	Set<Long> setOrgId=new HashSet<>();
    	
//    	String dayMonth=StringUtil.getSRange(sTimeType);
    	
    	for(SysOrganization sysOrganization:listOrganization) {
    		setOrgId.add(sysOrganization.getId());
    	}
    
    	List<SysCallLogDeviceRecoDto> listCallAndReco=null;
    	switch (sType) {
		case "0":			//  查询号码
			listCallAndReco=mSysDeviceTotalMapper.selectSearCallNumCallLogByOrg(setOrgId, sort, order, page, rows,sContent,beginTime,endTime,callIsHaveRecord);
			break; 
		case "1":			//	查询联系人
			listCallAndReco=mSysDeviceTotalMapper.selectSearCallNameCallLogByOrg(setOrgId, sort, order, page, rows,sContent,beginTime,endTime,callIsHaveRecord);
			break;     
		case "2":			//	查询序列号
			listCallAndReco=mSysDeviceTotalMapper.selectSearDevserCallLogByOrg(setOrgId, sort, order, page, rows,sContent,beginTime,endTime,callIsHaveRecord);
			break;
		default:			//	默认查询
			listCallAndReco=mSysDeviceTotalMapper.selectSearCallDateCallLogByOrg(setOrgId, sort, order, page, rows, beginTime,endTime,callIsHaveRecord);
			break;
		}
    	PageInfo pageInfo=new PageInfo(listCallAndReco.size(), listCallAndReco);
    	
  		return pageInfo;
  	}
    
    
    @Override
    public PageInfo selectAllCallLogByOrg(Long orgId, String sort, String order, Integer page, Integer rows,String sType,Long beginTime,Long endTime,String sContent,Integer callIsHaveRecord) {
    	System.out.println("selectAllCallLogByOrg-->"+orgId+"-sort->"+sort+"-order->"+order+"-page->"+page+"-rows->"+rows+"-sType->"+sType+"-beginTime->"+beginTime+"-endTime->"+endTime+"-sContent->"+sContent+"-callIsHaveRecord->"+callIsHaveRecord);
    	//selectAllCallLogByOrg-->1-sort->call_date-order->DESC-page->1-rows->15-sType->3-beginTime->1574006400722-endTime->1573985525722-sContent->null-callIsHaveRecord->0
    	//selectAllCallLogByOrg-->1-sort->call_date-order->DESC-page->1-rows->15-sType->3-beginTime->1573401600000-endTime->1573985583000-sContent->-callIsHaveRecord->0
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
    	
    	 List<SysUser> listSysUser = mSysUserService.selectSysUserByOrgId(orgId);
    	 List<SysCallLogDeviceRecoDto> list=new ArrayList<>();
    	 Set<Long> setUserId=new HashSet<>();
    	for(SysUser sysUser:listSysUser) {
    		Long userId = sysUser.getId();
    		boolean has = PermissionUtil.hasDataPermission(userId, "device:"+userId+":list");
    		if(has) {
    			setUserId.add(userId);
    			/*List<SysCallLogDeviceRecoDto> listCallLog = mSysDeviceTotalMapper.selectSearCallLogByUser(userId,sort,order,page,rows,callNumber,deviceSerial,callName,beginTime,endTime, callIsHaveRecord);
    			for(SysCallLogDeviceRecoDto dto:listCallLog) {
    				Long sysOrgId = dto.getOrgId();
    				SysOrganization sysOrg = sysOrganizationMapper.selectById(sysOrgId);
    				if(sysOrg!=null) {
    					dto.setOrgName(sysOrg.getName());
    					dto.setOrgId(sysOrg.getId());
    				}
    			}
    			list.addAll(listCallLog);*/
    		}
    	}
    	if(setUserId.size()<=0) {
    		return new PageInfo(list.size(),list);
    	}
    	List<SysCallLogDeviceRecoDto> listCallLog = mSysDeviceTotalMapper.selectSearCallLogByUserSet(setUserId,sort,order,page,rows,callNumber,deviceSerial,callName,beginTime,endTime, callIsHaveRecord);
		for(SysCallLogDeviceRecoDto dto:listCallLog) {
			Long sysOrgId = dto.getOrgId();
			SysOrganization sysOrg = sysOrganizationMapper.selectById(sysOrgId);
			if(sysOrg!=null) {
				dto.setOrgName(sysOrg.getName());
				dto.setOrgId(sysOrg.getId());
			}
		}
		list.addAll(listCallLog);
    	
//    	List<SysCallLogDeviceRecoDto> listCallAndReco=null;
//    	switch (sType) {
//    	case "0":			//  查询号码
//    		listCallAndReco=mSysDeviceTotalMapper.selectSearCallNumCallLogByOrg(setOrgId, sort, order, page, rows,sContent,beginTime,endTime,callIsHaveRecord);
//    		break; 
//    	case "1":			//	查询联系人
//    		listCallAndReco=mSysDeviceTotalMapper.selectSearCallNameCallLogByOrg(setOrgId, sort, order, page, rows,sContent,beginTime,endTime,callIsHaveRecord);
//    		break;     
//    	case "2":			//	查询序列号
//    		listCallAndReco=mSysDeviceTotalMapper.selectSearDevserCallLogByOrg(setOrgId, sort, order, page, rows,sContent,beginTime,endTime,callIsHaveRecord);
//    		break;
//    	default:			//	默认查询
//    		listCallAndReco=mSysDeviceTotalMapper.selectSearCallDateCallLogByOrg(setOrgId, sort, order, page, rows, beginTime,endTime,callIsHaveRecord);
//    		break;
//    	}
    	PageInfo pageInfo=new PageInfo(list.size(), list);
    	
    	return pageInfo;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
  

	private SysDeviceTotal calcu(List<SysDeviceTotal> list) {
    	Long callLogCount=0l;		//  通话记录
    	Long callRecordCount=0l;	//  通话录音数量
    	Long recoFileSize=0l;		//  录音文件大小
    	Long callAlAcceptCount=0l;	//  已接电话
    	Long callNoAccept=0l;		// 未接电话
    	Long callRefuAcc=0l;		//  拒接
    	Long callCallOut=0l;		//  呼出
    	SysDeviceTotal sysDeviceTotal = new SysDeviceTotal();
    	for(SysDeviceTotal to:list) {
    		 callLogCount += to.getCallLogCount();
    		 callRecordCount+=to.getCallRecordCount();
    		 recoFileSize+=to.getRecoFileSize();
    		 callAlAcceptCount+=to.getCallAlreadyAcceptCount();
    		 callNoAccept+=to.getCallNoAccept();
    		 callRefuAcc+=to.getCallRefuseAccept();
    		 callCallOut+=to.getCallCallOut();
    	}
    	sysDeviceTotal.setCallLogCount(callLogCount);
    	sysDeviceTotal.setCallRecordCount(callRecordCount);
    	sysDeviceTotal.setRecoFileSize(recoFileSize);
    	sysDeviceTotal.setCallAlreadyAcceptCount(callAlAcceptCount);
    	sysDeviceTotal.setCallNoAccept(callNoAccept);
    	sysDeviceTotal.setCallRefuseAccept(callRefuAcc);
    	sysDeviceTotal.setCallCallOut(callCallOut);
    	
    	return sysDeviceTotal;
    }

	@Override
    public List<SysOrganization> selectSysOrgList(Long id ,List<SysOrganization> list,boolean loadChild) {  //  可优化递归查询
    	if(!loadChild) {
    		SysOrganization sysOrganization = sysOrganizationMapper.selectById(id);
    		list.add(sysOrganization);
    	}
    	List<SysOrganization> listSysOrgChildren= sysOrganizationMapper.selectChildren(id);
    	for(SysOrganization org:listSysOrgChildren) {
    		Long id2 = org.getId();
    		list.add(org);
    		selectSysOrgList(id2, list,true);
    	}
    	return list;
    }
    
	
	@Override
	public List<SysOrganization> selectUpSysOrgList(Long orgId, List<SysOrganization> list) {
		SysOrganization sysOrganization = sysOrganizationMapper.selectById(orgId);
		if(sysOrganization==null) {
			return list;
		}
		Long parentId = sysOrganization.getParentId();
		list.add(sysOrganization);
		selectUpSysOrgList(parentId, list);
		return list;
	}

	@Override
	public List<SysOrganizationTree> selectSysOrganizationList(long id,List<SysOrganizationTree> list) {
    	SysOrganizationTree tree = new SysOrganizationTree();
    	SysOrganization organization = sysOrganizationMapper.selectById(id);	// 机构列表
        BeanUtils.copyProperties(organization, tree);
       
    	list.add(tree);
    	 List<SysOrganizationTree> treeList = selectChildrenTreeList(id);
    	 list.addAll(treeList);
    	 for(int i=0;i<treeList.size();i++) {
    		 SysOrganizationTree sysOrganizationTree = treeList.get(i);
    		 int orgType = sysOrganizationTree.getOrgType();
    		 if(orgType==0) {
    			 selectSysOrganizationList(sysOrganizationTree.getId(),list);
//    			 tree.getChildren().set(i, selectSysOrganizationList(sysOrganizationTree.getId(),list));
    		 }
    	 }
		return list;
	}
	
	@Override
    public SysOrganizationTree selectSysOrganizationTree(long id) {
        SysOrganizationTree tree = new SysOrganizationTree();
        SysOrganization sysOrganization = sysOrganizationMapper.selectById(id);		//  找到顶级大机构
        BeanUtils.copyProperties(sysOrganization, tree);
        List<SysOrganizationTree> treeList = selectChildrenTreeList(id);			//  查询子集合
        tree.setChildren(treeList);
//        String json = new Gson().toJson(treeList);
//        System.out.println("selectSysOrg-->"+json);
        for (int i = 0; i < treeList.size(); i++) {
        	SysOrganizationTree sysOrganizationTree = treeList.get(i);
        	int orgType = sysOrganizationTree.getOrgType();
        	if(orgType==0) {
        		tree.getChildren().set(i, selectSysOrganizationTree(sysOrganizationTree.getId()));
        	}
        }
        return tree;
    }

    @Override
    public List<SysOrganizationTree> selectChildrenTreeList(long id) {
    	List<SysOrganization> childrenContainer=new ArrayList<>();
        List<SysOrganization> childrenList = sysOrganizationMapper.selectChildren(id);   //  把该机构下的员工查询出来
        if(queryType==1) {
        	mSysRoleOrganizationServiceImpl.setQueryType(1);
        	PageInfo pageInfo = mSysRoleOrganizationServiceImpl.selectOrganizationUnderPerson(id);
        	if(pageInfo!=null) {  
	        	Object rows = pageInfo.getRows();
	        	if(rows instanceof List) {
	        		List<SysOrganization> listOrg = (List<SysOrganization>)rows; 
//	        		System.out.println("pageInf org tree list-->"+pageInfo.toString()+"-->"+listOrg.toString());
	        		childrenContainer.addAll(listOrg);
	        	} 
        	}
        }
        childrenContainer.addAll(childrenList);
        List<SysOrganizationTree> childrenTreeList = new ArrayList<>();
        for (SysOrganization s : childrenContainer) {
            SysOrganizationTree sysOrganizationTree = new SysOrganizationTree();
            BeanUtils.copyProperties(s, sysOrganizationTree);			//   实体复制
            childrenTreeList.add(sysOrganizationTree);
        }
        return childrenTreeList;
    }

    @Override
    public boolean isExistFullName(String fullName) {
        return sysOrganizationMapper.isExistFullName(fullName);
    }

    @Override
    public SysOrganization selectOrganization(long id) {
        SysOrganization sysOrganization = sysOrganizationMapper.selectById(id);
        return sysOrganization;
    }

    @Override
    public boolean isExistFullNameExcludeId(long id, String fullName) {

        return sysOrganizationMapper.isExistFullNameExcludeId(id, fullName);
    }

	@Override
	public String selectOrgCodeByParentId(long parentId) {
		return sysOrganizationMapper.selectOrgCodeByParentId(parentId);
	}

	@Override
	public PageInfo selectPageC(long id) {
		
		return new PageInfo(10, 10);
	}
    
}


