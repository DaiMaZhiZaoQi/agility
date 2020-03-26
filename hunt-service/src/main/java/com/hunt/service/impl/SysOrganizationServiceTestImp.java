package com.hunt.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hunt.dao.SysIpForbiddenMapper;
import com.hunt.dao.SysOrganizationMapper;
import com.hunt.model.entity.SysIpForbidden;
import com.hunt.model.entity.SysOrganization;
import com.hunt.service.SysOrganizationServiceTest;

/**
 * 事务测试 类注解	@Transations
 * 方法级别	@Transation 
 * 
 * @author williambai
 *
 */

@Service

public class SysOrganizationServiceTestImp implements SysOrganizationServiceTest{

	@Autowired
	SysIpForbiddenMapper mSysIpMapper;
	
	@Transactional
	@Override
	public Long updateSysOrg(SysIpForbidden sysIpForbidden) {
		mSysIpMapper.update(sysIpForbidden);
		int i=1/0;
		return null;
	}



}
