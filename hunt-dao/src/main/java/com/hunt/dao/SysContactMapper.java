package com.hunt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hunt.model.entity.SysContact;

public interface SysContactMapper {
  
	
	Long insert(SysContact sysContact);
	
	SysContact selectById(@Param("id") Long id);
	
	/**
	 * 查询已激活的通讯录
	 * @param id
	 * @return
	 */
	SysContact selectActivyById(@Param("id") Long id);
	
	/**
	 * 查询这个id集合之下满足contactCode的通讯录
	 * @param list
	 * @param contactCode
	 * @return
	 */
	String selectByIds(@Param("list")List<Long> list,@Param("contactCode") String contactCode);
	
	/**
	 * 修改
	 * @param sysContact
	 * @return
	 */
	Integer update(SysContact sysContact);
	
	/**
	 * 查询指定机构
	 * @param orgName
	 * @return
	 */
	SysContact selectContactByOrgName(@Param("orgName")String orgName);
	
	/**
	 * 根据id删除，更新后删除数据
	 * @param sysContactId
	 * @return
	 */
	Integer deleteById(@Param("sysContactId") Long sysContactId);
}
