package com.hunt.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hunt.dao.SysDeviceMapper;
import com.hunt.dao.SysDeviceRoleOrgMapper;
import com.hunt.model.dto.PageDto;
import com.hunt.model.dto.PageInfo;
import com.hunt.model.dto.SysDeviceOrgDto;
import com.hunt.model.entity.SysDevice;
import com.hunt.model.entity.SysDeviceRoleOrg;
import com.hunt.model.entity.SysOrganization;
import com.hunt.service.DeviceManageService;
import com.hunt.service.SysOrganizationService;
import com.hunt.util.StringUtil;

/**
 * 话机管理
 * @author williambai
 *
 */
@Service
@Transactional
public class DeviceManageServiceImp implements DeviceManageService{

	@Autowired
	private SysDeviceMapper mSysDeviceMapper;
	@Autowired
	private SysOrganizationService mSysOrganizationService;
	@Autowired
	private SysDeviceRoleOrgMapper mSysDeviceRogMapper;
	
	@Override
	public PageInfo selectUnRegisDevice(PageDto pageDto) {
		List<SysDevice> listSysDevice = mSysDeviceMapper.selectUnRegisDevice(pageDto);
		return new PageInfo(listSysDevice.size(), listSysDevice);
	}

	@Override
	public Long selectUnRegisCount() {
		return mSysDeviceMapper.selectUnRegisCount();
	}

	@Override
	public Long selectRegisCount(Long orgId) {
		List<SysOrganization> listOrg=new ArrayList<SysOrganization>();
		List<SysOrganization> listTotal = mSysOrganizationService.selectSysOrgList(orgId,listOrg,false);
		Set<Long> setOrgId=new HashSet<>();
    	for(SysOrganization sysOrganization:listTotal) {
    		setOrgId.add(sysOrganization.getId());
    	}
    	return mSysDeviceMapper.selectRegisCount(setOrgId);
	}

	@Override
	public PageInfo selectRegisDevice(PageDto pageDto,Long orgId) {
		List<SysOrganization> listOrg=new ArrayList<SysOrganization>();
		List<SysOrganization> listTotal = mSysOrganizationService.selectSysOrgList(orgId,listOrg,false);
		Set<Long> setOrgId=new HashSet<>();
    	for(SysOrganization sysOrganization:listTotal) {
    		setOrgId.add(sysOrganization.getId());
    	}
    	List<SysDeviceOrgDto> listDeviceOrg = mSysDeviceMapper.selectDevRegis(setOrgId, pageDto);
		return new PageInfo(listDeviceOrg.size(), listDeviceOrg);
	}

	@Override
	public SysDeviceRoleOrg selectBySerial(String deviceSerial) {
		SysDevice sysDevice = mSysDeviceMapper.selectBindByDeviceSerial(deviceSerial);
		if(sysDevice!=null) {
			List<SysDeviceRoleOrg> listDevRog = mSysDeviceRogMapper.selectOnByDeviceId(sysDevice.getId());
			if(listDevRog.size()>0) {
				return listDevRog.get(0);
			}
		}
		
		return null;
	}
	
	
	
	
	
	
	

}
