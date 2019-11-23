package com.hunt.service;


import java.util.List;

import org.omg.CORBA.LongHolder;

import com.hunt.model.dto.SysContactUserDto;
import com.hunt.model.dto.SysUserOrgDto;
import com.hunt.model.entity.SysContact;
import com.hunt.util.Result;

/**
 * 企业通讯录服务，上传文件时，文件存在云端，然后点确定，激活文件，html中选择文件，file标签不能获取文件名
 * @author williambai
 *
 */

public interface DeviceContactService {

	/**
	 * 添加通讯录
	 * @param sysContact
	 * @return
	 */
	Result insertContact(SysContact sysContact,Long userId);
	
	/**
	 * 移动端上传文件添加记录 
	 * @param sysContact
	 * @param userId
	 * @return
	 */
	Result mInsertContact(SysContact sysContact,Long userId);
	
	SysContact selectContact(Long contactId);
	/**
	 * 已授权
	 * @param contactId
	 * @return
	 */
	List<SysUserOrgDto> selectUserOrg(Long contactId);
	/**
	 * 未授权
	 * @param sysContactId 通讯录id
	 * @param orgId 机构id
	 * @param disUp 是否加载上级机构 0 不加载 1,加载
	 * @return
	 */
	List<SysUserOrgDto> selectUnAuth(Long sysContactId,Long orgId,Integer disUp);
	
	/**
	 * 通过用户id查询通讯录
	 * @param userId
	 * @param visitType 0 移动端，1 浏览器端
	 * @return
	 */
	List<SysContact> selectByUserId(Long userId,int visitType);
	
	/**
	 * 上传通话记录
	 * @param sysContactUserDto  包含两个对象  SysContact sysContactUser
	 * @return
	 */
	Result uploadContact(SysContactUserDto sysContactUserDto);
	
	/**
	 * 修改通讯录文件状态
	 * @param contactId
	 * @param userId
	 * @return
	 */
	Result updateContact(Long contactId,Long userId);

	/**
	 * 企业通讯录授权
	 * @param listSysUserOrgDto
	 * @param contactId
	 * @return
	 */
	Result auth(List<SysUserOrgDto> listSysUserOrgDto, Long contactId);

	/**
	 * 企业通讯录一键授权给机构
	 * @param listSysUserOrgDto
	 * @param contactId
	 * @return
	 */
	Result authOrg(List<SysUserOrgDto> listSysUserOrgDto, Long contactId);
	
	/**
	 * 解除授权
	 * @param listSysUserOrgDto
	 * @param contactId
	 * @return
	 */
	Result noAuth(List<SysUserOrgDto> listSysUserOrgDto, Long contactId);

	/**
	 * 解除该机构下的所有用户的授权
	 * @param listSysUserOrgDto
	 * @param contactId
	 * @return
	 */
	Result unAuthOrg(List<SysUserOrgDto> listSysUserOrgDto, Long contactId);

	 
	
}
