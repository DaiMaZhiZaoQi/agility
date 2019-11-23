package com.hunt.util;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import com.hunt.model.dto.PerFeatureDto;
import com.hunt.model.entity.SysPermission;

public class PermissionUtil {

	
	
	
	/**
	 * 是否有该数据权限
	 * @param targetUserId  目标用户id
	 * @param permission  	权限码   (device:targetUserId:list) 该用户的设备信息展示，(device:targetUserId:updateXXX) 修改XXX
	 * TODO hasDataPermission 查询有权限的用户的 
	 * @return
	 */
	public static boolean hasDataPermission(Long targetUserId,String permission) {
		//  权限在添加用户的初期会默认插入几条权限，   list, update。当前用户默认就有该权限
		// 查询该权限对应的权限id
		// 查询当前用户的所拥有的权限id，所拥有的的权限id，应该在一登录的时候就保存了。工具方法直接获取
		Subject subject = SecurityUtils.getSubject();
		boolean permitted = subject.isPermitted(permission);
		System.out.println("hasDataPermission-->"+permitted);
		return permitted;
	}
	

	
	/**
	 * 比如需要控制 通讯录表权限  默认权限 即 contact:contactId:list 查找
	 * @param id 需要权限控制的表的id  
	 * @param permissionGroupId	创建的权限所属的权限组id
	 * @return
	 */
	public static List<SysPermission> createDefPermission(PerFeatureDto preFeature,Long sysPermissionGroupId){
		String feature = preFeature.getFeature();
		Long featureId = preFeature.getFeatureId(); 
		String perList=feature+":"+featureId+":"+"list";
		String perUpdate=feature+":"+featureId+":"+"update";
		String perDelete=feature+":"+featureId+":"+"delete";
		
//		String perList="device:"+userId+":list";
//    	String perUpdate="device:"+userId+":update";
//    	String perDelete="device:"+userId+":delete";
    	
    	List<SysPermission> listSysPermission=new ArrayList<>();
    	SysPermission sysPerList = new SysPermission();
    	sysPerList.setName("查询企业通讯录");
    	sysPerList.setDescription("查看用户的企业通讯录");
    	sysPerList.setCode(perList);
    	sysPerList.setSysPermissionGroupId(sysPermissionGroupId);
    	
    	SysPermission sysPerUpdate=new SysPermission();
    	sysPerUpdate.setName("升级企业通讯录");
    	sysPerUpdate.setDescription("升级企业通讯录");
    	sysPerUpdate.setCode(perUpdate);
    	sysPerUpdate.setSysPermissionGroupId(sysPermissionGroupId);
    	
    	SysPermission sysDelete=new SysPermission();
    	sysDelete.setName("删除企业通讯录");
    	sysDelete.setDescription("删除企业通讯录");
    	sysDelete.setCode(perDelete);
    	sysDelete.setSysPermissionGroupId(sysPermissionGroupId);
    	
    	listSysPermission.add(sysPerList);
    	listSysPermission.add(sysPerUpdate);
    	listSysPermission.add(sysDelete);
    	return listSysPermission;
		
	}
	
}
