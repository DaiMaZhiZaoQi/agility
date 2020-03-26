package com.hunt.service;

import org.springframework.transaction.annotation.Transactional;

import com.hunt.model.entity.SysIpForbidden;
import com.hunt.model.entity.SysOrganization;

/**
 * SysOrganization事务测试Service
 * @author williambai
 *
 */
public interface SysOrganizationServiceTest {
//	@Transactional
	Long updateSysOrg(SysIpForbidden sysIpForbidden);
	
}
